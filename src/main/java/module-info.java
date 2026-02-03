module org.example.actividadfinaldi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;

    opens org.example.actividadfinaldi to javafx.fxml;
    opens org.example.actividadfinaldi.controller to javafx.fxml;
    opens org.example.actividadfinaldi.model to javafx.base;


    opens org.example.actividadfinaldi.dao;
    opens org.example.actividadfinaldi.service;
    opens org.example.actividadfinaldi.util;

    exports org.example.actividadfinaldi;
    exports org.example.actividadfinaldi.controller;
    exports org.example.actividadfinaldi.model;
    exports org.example.actividadfinaldi.dao;
    exports org.example.actividadfinaldi.service;
    exports org.example.actividadfinaldi.util;
}