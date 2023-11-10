package Controller;

import Model.DataBase;
import Model.PatientInfo;
import javafx.application.Platform;
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

import java.io.IOException;
import java.time.LocalDate;

public class TriageFormController {
    @FXML
    private Button add_btn;

    @FXML
    private Button exit_btn;

    @FXML
    private DatePicker birthDate_datePicker;

    @FXML
    private TextField complaint_txtField;

    @FXML
    private TextField condition_txtField;

    @FXML
    private TextField contact_txtField;

    @FXML
    private TextField firstName_txtField;

    @FXML
    private ComboBox<String> gender_cbo;

    @FXML
    private TextField lastName_txtField;

    @FXML
    private ComboBox<String> level_cbo;

    @FXML
    private TextField middle_txtField;

    @FXML
    private Button next_btn;

    @FXML
    private Button openFull_btn;

    @FXML
    private Button viewQueueBtn;

    @FXML
    protected void addPatient(ActionEvent event) {
        if(lastName_txtField.getText() == null || firstName_txtField.getText() == null || middle_txtField.getText() == null ||
        contact_txtField.getText() == null || birthDate_datePicker.getValue() == null || gender_cbo.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lacking Info");
            alert.setHeaderText(null);
            alert.setContentText("Please Fill-up All Values!");
            alert.showAndWait();
            return;
        }

        String name = String.format("%s, %s %s", lastName_txtField.getText(), firstName_txtField.getText(), middle_txtField.getText());
        LocalDate date = birthDate_datePicker.getValue();
        PatientInfo patient = new PatientInfo();
        patient.setName(name);
        patient.setGender(gender_cbo.getValue());
        patient.setDate(date);
        patient.setContactInfo(contact_txtField.getText());
        DataBase db = new DataBase("PatientInfo");
        db.addToFile(patient.storeToDB());
        ptc.initData(patient);
        clearFields();

        // Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Added");
        alert.setHeaderText(null);
        alert.setContentText("Patient Added");
        alert.showAndWait();
        exit();
    }

    @FXML
    public void goToFullForm(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/patientRegistrationForm-view.fxml"));
        Parent root = loader.load();
        PatientRegistrationController patientRC = loader.getController();
        patientRC.setReference(ptc);

        Stage newStage = new Stage(); // Create a new stage for the new scene

        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.centerOnScreen();
        newStage.setResizable(false);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setTitle("Triage Queue");
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();


        root.setOnMousePressed(event1 -> {
            x = event1.getSceneX();
            y = event1.getSceneY();
        });

        root.setOnMouseDragged(event1 -> {
            newStage.setX(event1.getScreenX() - x);
            newStage.setY(event1.getScreenY() - y);
        });

        root.setOnMouseReleased(event1 -> newStage.setOpacity(1));


    }

    @FXML
    public void exit() {
        ptc.addBtn.getScene().getWindow().setOpacity(1);
        add_btn.getScene().getWindow().hide();
    }

    private double x = 0, y = 0;
    private PatientTicketingController ptc;

    public void setReference(PatientTicketingController ptc){
        this.ptc = ptc;
    }

    public void initialize() {
        ObservableList<String> genderOptions = FXCollections.observableArrayList(
                "Male", "Female"
        );
        gender_cbo.setItems(genderOptions);

    }

    public void clearFields() {
        firstName_txtField.setText("");
        lastName_txtField.setText("");
        middle_txtField.setText("");
        contact_txtField.setText("");
        gender_cbo.setPromptText("Gender");
        birthDate_datePicker.setUserData(LocalDate.now());
    }
}