package Controller;

import Model.PatientInfo;
import Model.TriageTicket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Arrays;

public class PatientTicketingController {
     @FXML
    public Button addBtn;

    @FXML
    private TextField allergiesTxtField;

    @FXML
    private TextField complaintTxtField;

    @FXML
    private TextField conditionTxtField;

    @FXML
    private TextField currentMedTxtField;

    @FXML
    private TextField dateTxtfield;

    @FXML
    private TextField genderTxtField;

    @FXML
    private TextField nameTxtBox;

    @FXML
    private Button newBtn;

    @FXML
    private TextField noTxtField;

    @FXML
    private ComboBox<String> prioLevelCbo;

    @FXML
    private Button searchBtn;

    private PatientInfo patient;
    private TriageQueueController tqc;
    private double x,y;

    public void initialize(){
        ObservableList<String> priorityOptions = FXCollections.observableArrayList(
                "Resuscitation", "Emergency", "Urgent", "Semi-urgent", "Non-urgent"
        );
        prioLevelCbo.setItems(priorityOptions);
    }

    public void setReference(TriageQueueController tqc){
        this.tqc = tqc;
    }

    public void initData(PatientInfo patient) {
        this.patient = patient;
        if (patient != null) {
            nameTxtBox.setText(patient.getName());
            dateTxtfield.setText(patient.getDate()+"");
            genderTxtField.setText(patient.getGender());
            noTxtField.setText(patient.getContactInfo());
        }
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/triageForm-view.fxml"));
        Parent root = loader.load();
        TriageFormController triageFC = loader.getController();
        triageFC.setReference(this);
        Stage newStage = new Stage(); // Create a new stage for the new scene

        root.setOnMousePressed(event1 -> {
            x = event1.getSceneX();
            y = event1.getSceneY();
        });

        root.setOnMouseDragged(event1 -> {
            newStage.setX(event1.getScreenX() - x);
            newStage.setY(event1.getScreenY() - y);
        });

        root.setOnMouseReleased(event1 -> newStage.setOpacity(1));

        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.centerOnScreen();
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setTitle("Triage Queue");
        newStage.show();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setOpacity(0);

    }
    public void goToSearch(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ViewPatients-view.fxml"));
        Parent root = loader.load();
        ViewPatientsController viewPC = loader.getController();
        viewPC.setReference(this);
        Stage newStage = new Stage(); // Create a new stage for the new scene

        root.setOnMousePressed(event1 -> {
            x = event1.getSceneX();
            y = event1.getSceneY();
        });

        root.setOnMouseDragged(event1 -> {
            newStage.setX(event1.getScreenX() - x);
            newStage.setY(event1.getScreenY() - y);
        });

        root.setOnMouseReleased(event1 -> newStage.setOpacity(1));

        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.centerOnScreen();
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setTitle("Triage Queue");
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setOpacity(0);

    }
    public void goToQueue(ActionEvent event) {
        // Loads the TriageQueue window back
        tqc.loadTables();
        tqc.treatBtn.getScene().getWindow().setOpacity(1);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void addToQueue(){
        TriageTicket ticket = new TriageTicket(patient, complaintTxtField.getText(),
                conditionTxtField.getText(),prioLevelCbo.getValue(),currentMedTxtField.getText(), tqc.getCount());
        tqc.triageQueue.upHeap(ticket);
        tqc.increaseCounter(ticket.getPriorityLevel());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Added");
        alert.setHeaderText(null);
        alert.setContentText(tqc.triageQueue.displayHeap()+"\n"+ Arrays.toString(tqc.counter)+"\n"+tqc.triageQueue.peek());
        alert.showAndWait();
        clearFields();
    }

    public void clearFields(){
        allergiesTxtField.clear();
        complaintTxtField.clear();
        conditionTxtField.clear();
        currentMedTxtField.clear();
        nameTxtBox.clear();
        dateTxtfield.clear();
        genderTxtField.clear();
        noTxtField.clear();

    }

}
