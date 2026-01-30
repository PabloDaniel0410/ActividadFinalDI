module org.example.actividadfinaldi {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.actividadfinaldi to javafx.fxml;
    exports org.example.actividadfinaldi;
}