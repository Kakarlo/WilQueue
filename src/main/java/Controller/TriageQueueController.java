package Controller;

import Logic.TriageQueue;
import Model.DataBase;
import Model.TriageTicket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class TriageQueueController {
    @FXML
    private TableColumn<TriageTicket, Integer> age;

    @FXML
    private TableColumn<TriageTicket, String> complaint;

    @FXML
    private TableColumn<TriageTicket, String> condition;

    @FXML
    private TableColumn<TriageTicket, String> number;

    @FXML
    private TableColumn<TriageTicket, String> name;

    @FXML
    private TableColumn<TriageTicket, String> gender;

    @FXML
    private TableColumn<TriageTicket, Integer> level;

    @FXML
    private ListView<TriageTicket> listView1;

    @FXML
    private ListView<TriageTicket> listView2;

    @FXML
    private ListView<TriageTicket> listView3;

    @FXML
    private Label nUrgentLbl;

    @FXML
    private Button nextPatientBtn;

    @FXML
    private Button nextPatientBtn2;

    @FXML
    private Button nextPatientBtn3;

    @FXML
    private Label resuscitationLbl;

    @FXML
    private Label sUrgentLbl;

    @FXML
    private Button skipBtn;

    @FXML
    public Button treatBtn;

    @FXML
    private Button exit_btn;

    @FXML
    private Label treatingLbl1;

    @FXML
    private Label treatingLbl2;

    @FXML
    private Label treatingLbl3;

    @FXML
    private Label urgentLbl;

    @FXML
    private Label emergencyLbl;

    @FXML
    void exit(ActionEvent event) {
        saveState();
        exit_btn.getScene().getWindow().hide();
    }

    @FXML
    private TableView<TriageTicket> queueTable;

    @FXML
    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/patientTicketing-view.fxml"));
        Parent root = loader.load();
        PatientTicketingController pt = loader.getController();
        pt.setReference(this);
        Stage newStage = new Stage(); // Create a new stage for the new scene

        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.centerOnScreen();
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setTitle("Triage Queue");
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setOpacity(0);

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

    private double x = 0, y = 0;
    public TriageQueue triageQueue;
    public int count = 0;
    public int[] counter = new int[5];
    private final boolean[] isStationAvailable = new boolean[3];
    private DataBase db;
    private final ObservableList<TriageTicket> patients = FXCollections.observableArrayList();

    public void initialize() {
        loadQueue();
        initializeTables();
        loadTables();
        Arrays.fill(isStationAvailable, true);
    }

    public void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setLabels() {
        resuscitationLbl.setText("Resuscitation: " + counter[0]);
        emergencyLbl.setText("Emergency: " + counter[1]);
        urgentLbl.setText("Urgent: " + counter[2]);
        sUrgentLbl.setText("Semi-urgent: " + counter[3]);
        nUrgentLbl.setText("Non-urgent: " + counter[4]);
        if (patients.isEmpty()){
            treatBtn.setDisable(true);
            skipBtn.setDisable(true);
            count = 0;
        } else {
            treatBtn.setDisable(false);
            skipBtn.setDisable(false);
        }
    }

    public void skipPatient() {
        if (!triageQueue.isEmpty()) {
            TriageTicket patient = triageQueue.poll();
            reduceCounter(patient.getPriorityLevel());
            loadTables();
        }
    }

    public void treatPatient() {
        if (!isStationsAvailable()) {
            alert("No Stations available");
            return;
        }
        if (triageQueue.isEmpty()) {
            alert("No patients");
            return;
        }
        TriageTicket patient = triageQueue.poll();
        sendToStation(patient);
        reduceCounter(patient.getPriorityLevel());
        loadTables();
    }

    public boolean isStationsAvailable() {
        return isStationAvailable[0] || isStationAvailable[1] || isStationAvailable[2];
    }

    public void sendToStation(TriageTicket patient) {
        if (isStationAvailable[0]) {
            nextPatientBtn.setDisable(false);
            treatingLbl1.setText("Station 1");
            listView1.getItems().add(patient);
            isStationAvailable[0] = false;
        } else if (isStationAvailable[1]) {
            nextPatientBtn2.setDisable(false);
            treatingLbl2.setText("Station 2");
            listView2.getItems().add(patient);
            isStationAvailable[1] = false;
        } else if (isStationAvailable[2]) {
            nextPatientBtn3.setDisable(false);
            treatingLbl3.setText("Station 3");
            listView3.getItems().add(patient);
            isStationAvailable[2] = false;
        }
    }

    public void nextPatient1() {
        saveHistory(listView1.getItems().get(0));
        listView1.getItems().remove(0);
        treatingLbl1.setText("Station 1 | Next Patient");
        nextPatientBtn.setDisable(true);
        isStationAvailable[0] = true;
    }

    public void nextPatient2() {
        saveHistory(listView2.getItems().get(0));
        listView2.getItems().remove(0);
        treatingLbl2.setText("Station 2 | Next Patient");
        nextPatientBtn2.setDisable(true);
        isStationAvailable[1] = true;
    }

    public void nextPatient3() {
        saveHistory(listView3.getItems().get(0));
        listView3.getItems().remove(0);
        treatingLbl3.setText("Station 3 | Next Patient");
        nextPatientBtn3.setDisable(true);
        isStationAvailable[2] = true;

    }

    public void saveHistory(TriageTicket t){
        DataBase db = new DataBase("TriageHistory");
        String hold = t.getPatientID() + "#" +
                t.getPriorityLevel() + "#" +
                t.getPatientNumber() + "#" +
                t.getName() + "#" +
                t.getAge() + "#" +
                t.getGender() + "#" +
                t.getNumber() + "#" +
                t.getChiefComplaint() + "#" +
                t.getCurrentCondition() + "#" +
                t.getCounter() + "#" +
                t.getPriorityNumber() + "\n";
        db.addToFile(hold);
    }

    public void reduceCounter(String priorityLevel) {
        switch (priorityLevel) {
            case "Resuscitation" -> counter[0]--;
            case "Emergency" -> counter[1]--;
            case "Urgent" -> counter[2]--;
            case "Semi-urgent" -> counter[3]--;
            case "Non-urgent" -> counter[4]--;
        }
    }

    public void increaseCounter(String priorityLevel) {
        switch (priorityLevel) {
            case "Resuscitation" -> counter[0]++;
            case "Emergency" -> counter[1]++;
            case "Urgent" -> counter[2]++;
            case "Semi-urgent" -> counter[3]++;
            case "Non-urgent" -> counter[4] = counter[4] + 1;
            default -> System.out.println("Hey");
        }
    }

    public int getCount() {
        return ++count;
    }

    public void saveState(){
        DataBase db = new DataBase("TriageQueueState");
        String hold = "";
        // Returns the value back to the queue when unprocessed
        if (!listView1.getItems().isEmpty()) triageQueue.upHeap(listView1.getItems().get(0));
        if (!listView2.getItems().isEmpty()) triageQueue.upHeap(listView2.getItems().get(0));
        if (!listView3.getItems().isEmpty()) triageQueue.upHeap(listView3.getItems().get(0));
        TriageTicket[] copy = triageQueue.heapSort();
        patients.clear();
        for (int i = 0; i < copy.length - 1; i++){
            hold += copy[i].getPatientID() + "#" +
                    copy[i].getPriorityLevel() + "#" +
                    copy[i].getPatientNumber() + "#" +
                    copy[i].getName() + "#" +
                    copy[i].getAge() + "#" +
                    copy[i].getGender() + "#" +
                    copy[i].getNumber() + "#" +
                    copy[i].getChiefComplaint() + "#" +
                    copy[i].getCurrentCondition() + "#" +
                    copy[i].getCounter() + "#" +
                    copy[i].getPriorityNumber() + "\n";
        }
        db.storeToFile(hold);
    }

    public void loadQueue() {
        triageQueue = new TriageQueue();
        db = new DataBase("TriageQueueState");
        if (db.isEmpty()) return;
        treatBtn.setDisable(false);
        skipBtn.setDisable(false);
        ArrayList<String[]> arr = db.getFile();
        for (String[] s : arr) {
            TriageTicket t = new TriageTicket();
            t.setPatientID(Integer.parseInt(s[0]));
            t.setPriorityLevel(s[1]);
            t.setPatientNumber(s[2]);
            t.setName(s[3]);
            t.setAge(Integer.parseInt(s[4]));
            t.setGender(s[5]);
            t.setNumber(s[6]);
            t.setChiefComplaint(s[7]);
            t.setCurrentCondition(s[8]);
            t.setCounter(Integer.parseInt(s[9]));
            t.setPriorityNumber();
            triageQueue.upHeap(t);
            increaseCounter(t.getPriorityLevel());
            count = Math.max(t.getCounter(), count);
        }
        setLabels();
    }

    public void changePriority(TriageTicket ticket){
        ChoiceDialog<String> cd = new ChoiceDialog<>();
        ObservableList<String> priorityOptions = cd.getItems();
        cd.setHeaderText("Priority Level:");
        cd.setTitle("Change");
        priorityOptions.add("Resuscitation");
        priorityOptions.add("Emergency");
        priorityOptions.add("Urgent");
        priorityOptions.add("Semi-urgent");
        priorityOptions.add("Non-urgent");
        cd.showAndWait();
        if (cd.getSelectedItem() == null) return;
        if (!cd.getSelectedItem().equals(ticket.getPriorityLevel())){
            triageQueue.deleteNode(ticket);
            reduceCounter(ticket.getPriorityLevel());
            ticket.setPriorityLevel(cd.getSelectedItem());
            triageQueue.upHeap(ticket);
            increaseCounter(ticket.getPriorityLevel());
            loadTables();
        }
    }

    public void initializeTables() {
        level.setCellValueFactory(new PropertyValueFactory<>("priorityLevel"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        age.setCellValueFactory(new PropertyValueFactory<>("age")); // Use lowercase 'age'
        gender.setCellValueFactory(new PropertyValueFactory<>("gender")); // Use lowercase 'gender'
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        complaint.setCellValueFactory(new PropertyValueFactory<>("chiefComplaint"));
        condition.setCellValueFactory(new PropertyValueFactory<>("currentCondition"));
        queueTable.setRowFactory(tv -> {
            TableRow<TriageTicket> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    TriageTicket rowData = row.getItem();
                    changePriority(rowData);
                }
            });
            return row ;
        });
        queueTable.setItems(patients);

        listView1.setCellFactory(param -> new ListCell<TriageTicket>() {
            @Override
            protected void updateItem(TriageTicket item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getStationData() == null) {
                    setText(null);
                } else {
                    setStyle("-fx-font-size: 18;");
                    setText(item.getStationData());
                }
            }
        });

        listView2.setCellFactory(param -> new ListCell<TriageTicket>() {
            @Override
            protected void updateItem(TriageTicket item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getStationData() == null) {
                    setText(null);
                } else {
                    setStyle("-fx-font-size: 18;");
                    setText(item.getStationData());
                }
            }
        });

        listView3.setCellFactory(param -> new ListCell<TriageTicket>() {
            @Override
            protected void updateItem(TriageTicket item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getStationData() == null) {
                    setText(null);
                } else {
                    setStyle("-fx-font-size: 18;");
                    setText(item.getStationData());
                }
            }
        });
    }

    public void loadTables() {
        // Unsorted
        TriageTicket[] copy = triageQueue.getHeap();
        patients.clear();
        for (int i = 1; i <= triageQueue.getCount(); i++) {
            patients.add(copy[i]);
        }
        setLabels();

        // Sorted
        /*
        TriageTicket[] copy = triageQueue.heapSort();
        patients.clear();
        for (int i = 0; i < copy.length - 1; i++){
            patients.add(copy[i]);
        }
        System.out.println(patients.size());
        setLabels();
         */
    }

}
