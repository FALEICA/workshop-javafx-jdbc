package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.servicos.DepartamentoServicos;

public class DepartmentListController implements Initializable {
	
	private DepartamentoServicos servico;
	
	@FXML
	private TableView<Department> tableViewDepartamento;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btnNovo;
	
	private ObservableList<Department> obsList;
	
	
	@FXML
	public void onBtnNovoAction() {
		System.out.println("onBtnNovoAction");
	}
	
	
	public void setDepartamentoServico(DepartamentoServicos servico) {
		this.servico = servico;
		
	}
	
	
	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		inicializeNodes();
		
	}







	private void inicializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id") );
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage =(Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
		
		
				
		
	}
	
	public void atualizaTableView() {
		if(servico == null) {
			throw new IllegalStateException("Servico esta null");
					
		}
		List<Department> list = servico.buscaTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}
	
	

}
