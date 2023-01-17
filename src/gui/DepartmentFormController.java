package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.servicos.DepartamentoServicos;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	private DepartamentoServicos service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
	
	
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label lblErrorName;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	
	
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	
	public void setDepartmentService(DepartamentoServicos service) {
		this.service = service;
		
	}
	
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	
	
	
	
	
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
			
		}
		try {	
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error Saving Object", null, e.getMessage(), AlertType.ERROR);
			
		}
		
	}
	
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}


	private Department getFormData() {
		Department obj = new Department();		
		ValidationException exception = new ValidationException("Validation Error");		
		obj.setId(Utils.tryParseToInt(txtId.getText()));		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "O campo nao pode ser vazio.");
		}
		obj.setName(txtName.getText());		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;		
	}
	
	


	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	
	

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();	
		
	}

	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);		
		
	}
	
	public void updateFormData() {
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
				
		if(fields.contains("name")) {
			lblErrorName.setText(errors.get("name"));
		}
	}
}