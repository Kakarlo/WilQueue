package Controller;

import Logic.TriageQueue;
import Model.DataBase;
import Model.PatientInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;

public class PatientRegistrationController {


    @FXML
    private TextField address_textField;

    @FXML
    private TextField allergies_textField;

    @FXML
    private TextField contactNo2_textField;

    @FXML
    private TextField contact_textField;

    @FXML
    private TextArea currMed_textArea;

    @FXML
    private DatePicker date_datePicker;

    @FXML
    private TextField emergencyName_textField;

    @FXML
    private TextField firstName_textField;

    @FXML
    private ComboBox<String> gender_cbo;

    @FXML
    private TextField lastName_textField;

    @FXML
    private TextArea medHistory_textarea;

    @FXML
    private TextField middle_textField;

    @FXML
    private TextField nationality_textField;

    @FXML
    private TextField occupation_textField;

    @FXML
    private Button register_btn;

    @FXML
    private TextField relationship_textField;

    @FXML
    private TextField status_textField;

    @FXML
    public void addPatient() {
        PatientInfo patientInfo = new PatientInfo();
        if (filledUp()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lacking Info");
            alert.setHeaderText(null);
            alert.setContentText("Please Fill-up All Values!");
            alert.showAndWait();
        }
        String name = String.format("%s, %s %s", lastName_textField.getText(), firstName_textField.getText(), middle_textField.getText());
        patientInfo.setPatientID(1); //Needs a table to auto increment
        patientInfo.setName(name);
        patientInfo.setContactInfo(contact_textField.getText());
        patientInfo.setAddress(address_textField.getText());
        patientInfo.setDate(date_datePicker.getValue());
        patientInfo.setGender(gender_cbo.getValue());
        patientInfo.setOccupation(occupation_textField.getText());
        patientInfo.setCivilStatus(status_textField.getText());
        patientInfo.setEmergencyContactName(emergencyName_textField.getText());
        patientInfo.setNationality(nationality_textField.getText());
        patientInfo.setAge(patientInfo.calculateAge());
        patientInfo.setEmergencyContactNumber(contactNo2_textField.getText());
        patientInfo.setRelationship(relationship_textField.getText());
        patientInfo.setAllergies(allergies_textField.getText());
        patientInfo.setCurrMedication(currMed_textArea.getText());
        patientInfo.setMedicalHistory(medHistory_textarea.getText());

        DataBase db = new DataBase("PatientInfo");
        db.addToFile(patientInfo.storeToDB());
        // Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Added");
        alert.setHeaderText(null);
        alert.setContentText("Patient Added");
        alert.showAndWait();
        ptc.initData(patientInfo);

        // Close stage
        register_btn.getScene().getWindow().hide();
        ptc.addBtn.getScene().getWindow().setOpacity(1);
    }

    private PatientTicketingController ptc;

    public void initialize() {
        // Create an ObservableList of gender options
        ObservableList<String> genderOptions = FXCollections.observableArrayList(
                "Male", "Female"
        );
        // Set the gender options to the ComboBox
        if (gender_cbo != null) {
            gender_cbo.setItems(genderOptions);
        }
    }

    public boolean filledUp(){
        return lastName_textField == null || firstName_textField == null || middle_textField == null ||
                contact_textField == null ||address_textField == null || date_datePicker == null ||
                gender_cbo == null || occupation_textField == null || status_textField == null ||
                emergencyName_textField == null || nationality_textField == null || contactNo2_textField == null ||
                relationship_textField == null || allergies_textField == null || currMed_textArea == null ||
                medHistory_textarea == null;
    }

    public void setReference(PatientTicketingController ptc){
        this.ptc = ptc;
    }

    public void exit(ActionEvent event) throws IOException {
        // Loads the Previous window back
        ptc.addBtn.getScene().getWindow().setOpacity(1);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }


}
