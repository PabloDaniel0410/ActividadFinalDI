module org.example.actividadfinaldi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.actividadfinaldi to javafx.fxml;
    opens org.example.actividadfinaldi.controller to javafx.fxml;
    opens org.example.actividadfinaldi.model to javafx.base;

    exports org.example.actividadfinaldi;
    exports org.example.actividadfinaldi.controller;
    exports org.example.actividadfinaldi.model;
}