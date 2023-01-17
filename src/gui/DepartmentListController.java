package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.servicos.DepartamentoServicos;

public class DepartmentListController implements Initializable, DataChangeListener {

	private DepartamentoServicos servico;

	@FXML
	private TableView<Department> tableViewDepartamento;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEdit;
	
	@FXML
	private TableColumn<Department, Department> tableColumnRemove;
	

	@FXML
	private Button btnNovo;
	private ObservableList<Department> obsList;

	@FXML
	public void onBtnNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	public void setDepartamentoServico(DepartamentoServicos servico) {
		this.servico = servico;

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		inicializeNodes();

	}

	private void inicializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());

	}

	public void atualizaTableView() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta null");

		}
		List<Department> list = servico.buscaTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		
	}

	private void createDialogForm(Department obj, String absolutName, Stage parentStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();

			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartamentoServicos());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);

		}
	}

	
	@Override
	public void onDataChanged() {
		atualizaTableView();

	}
	
	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Department, Department>(){
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Department obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;					
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
				
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Department, Department>(){
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Department obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;					
				}
				
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
				
			}
		});
	}
	

	private void removeEntity(Department obj) {
		Optional<ButtonType> result =  Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if(result.get() == ButtonType.OK) {
			
			if(servico == null) {
				throw new IllegalStateException("Service was null");
				
			}
			
			try {
			servico.remove(obj);
			atualizaTableView();
			}catch(DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
				
				
			}
			
		}
		
		
	}

	

}
