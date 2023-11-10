package Controller;

import Model.DataBase;
import Model.PatientInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewPatientsController {
    @FXML
    private TableColumn<PatientInfo, String> BirthDate;

    @FXML
    private TableColumn<PatientInfo, Integer> age;

    @FXML
    private Button back;

    @FXML
    private TextField searchTxtField;

    @FXML
    private TableColumn<PatientInfo, String> contactNo;

    @FXML
    private TableColumn<PatientInfo, String> gender;

    @FXML
    private TableColumn<PatientInfo, String> name;

    @FXML
    private Button Select;

    @FXML
    private TableView<PatientInfo> table;

    private PatientInfo selectedPatient;
    private PatientTicketingController ptc;
    private final ObservableList<PatientInfo> patientList = FXCollections.observableArrayList();

    public void setReference(PatientTicketingController ptc) {
        this.ptc = ptc;
    }

    public void initialize() {
        loadTable();
    }

    public PatientInfo getSelectedPatient() {
        return selectedPatient;
    }

    private ObservableList<PatientInfo> filterPatients(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return patientList;
        }

        ObservableList<PatientInfo> filteredList = FXCollections.observableArrayList();

        for (PatientInfo patient : patientList) {
            if (patient.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(patient);
            }
        }

        return filteredList;
    }

    public void loadView(ActionEvent event) throws IOException {
        ptc.addBtn.getScene().getWindow().setOpacity(1);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void selectPatient(ActionEvent event) throws IOException {
        PatientInfo selectedPatient = getSelectedPatient();
        if (selectedPatient != null) {
            String patientName = selectedPatient.getName();
            ptc.initData(selectedPatient);

            // Create an alert to display the selected patient's name
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selected Patient");
            alert.setHeaderText(null);
            alert.setContentText("You selected: " + patientName);

            alert.showAndWait(); // Display the alert as a dialog
            loadView(event);

        } else {
            // Handle the case where no patient is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a patient first.");

            alert.showAndWait(); // Display the warning alert
        }
    }

    public void loadTable() {
        DataBase db = new DataBase("PatientInfo");
        ArrayList<String[]> patients = db.getFile();
        for (String[] s : patients) {
            String dateStr = s[3];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            PatientInfo p = new PatientInfo();
            p.setName(s[1]);
            p.setGender(s[5]);
            p.setDate(localDate);
            p.setContactInfo(s[2]);
            patientList.add(p);
        }

        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        age.setCellValueFactory(new PropertyValueFactory<>("Age"));
        gender.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        contactNo.setCellValueFactory(new PropertyValueFactory<>("ContactInfo"));

        table.setItems(patientList);
        searchTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            table.setItems(filterPatients(newValue));
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPatient = newSelection;
            }
        });
    }

}
