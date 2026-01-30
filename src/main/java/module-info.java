module org.example.actividadfinaldi {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.actividadfinaldi to javafx.fxml;
    exports org.example.actividadfinaldi;
    exports org.example.actividadfinaldi.DAO;
    opens org.example.actividadfinaldi.DAO to javafx.fxml;
}