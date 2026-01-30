package org.example.actividadfinaldi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.actividadfinaldi.util.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Aplicacion principal de alquiler de coches
 */
public class AplicacionP extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            DatabaseConnection.inicializarTablas();
        } catch (SQLException e) {
            System.err.println("Error inicializando BD: " + e.getMessage());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(AplicacionP.class.getResource("PantallaPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Sistema de Alquiler de Coches");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
    }
}