module com.example.wilqueue {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens Controller to javafx.fxml;
    exports Controller;
    opens Logic to javafx.fxml;
    exports Logic;
    opens Model to javafx.fxml;
    exports Model;
}