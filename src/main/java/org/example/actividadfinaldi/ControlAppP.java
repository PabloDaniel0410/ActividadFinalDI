package org.example.actividadfinaldi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControlAppP {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
