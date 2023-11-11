package Controller;

import Model.DataBase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LogInController {
    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField passWord;

    @FXML
    private TextField userName;

    @FXML
    void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void passWordKeyPressed(KeyEvent event) throws IOException {
        KeyCode key = event.getCode();
        if (key.equals(KeyCode.ENTER)) {
            login();
        }
    }

    // Variables
    private String username, password;
    private final HashMap<String, String> accounts = new HashMap<>();
    private double x = 0, y = 0;

    // Methods
    public void initialize() {
        DataBase db = new DataBase("Accounts");
        ArrayList<String[]> arr = db.getFile();
        for (String[] s : arr) {
            // Creates a key by combining the username and password
            accounts.put(s[0] + s[1], s[2]);
        }
    }

    public void login() throws IOException {
        String type;
        username = userName.getText();
        // Check if the text field are empty
        if (username.isEmpty()) {
            showAlert("Invalid Login", "Please check your username and password.");
            clear();
            return;
        }
        password = passWord.getText();
        if (password.isEmpty()) {
            showAlert("Invalid Login", "Please check your username and password.");
            clear();
            return;
        }
        // Clear fields
        clear();

        // Check for valid username and password
        if (accounts.containsKey(username + password)) {
            type = accounts.get(username + password);
            // Check what kind of account is used
            if (type.equals("Admin")) {
                // Run Other Program
                // Soon to come
            } else if (type.equals("Triage")) {
                // Run the Triage Program
                loadView();
            } else {
                showAlert("Invalid Account", "Please ask the developer to update your access");
            }
        } else {
            showAlert("Invalid Login", "Please check your username and password.");
        }
    }

    public void loadView() throws IOException {
        // Hide previous window
        LoginBtn.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TriageQueue-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage(); // Create a new stage for the new scene
        Scene scene = new Scene(root);

        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

            stage.setOpacity(.9);
        });

        root.setOnMouseReleased((MouseEvent event) -> stage.setOpacity(1));

        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Triage Queue");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    private void clear() {
        userName.setText("");
        passWord.setText("");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
