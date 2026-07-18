package com.univalle.cincuentazo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Esta clase se encarga de gestionar la pantalla de instrucciones del juego.
 * Controla únicamente la acción de regresar al menu principal desde la guía de como jugar.
 */

public class HowToPlayController{

    /**
     * Metodo que maneja el evento de clic en el botón para volver al menú principal
     * @param event el evento de acción del botón
     */
    @FXML
    private void onHowToPlayBackButtonClick(javafx.event.ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/cincuentazo/fxml/fxml/menu-view.fxml"));
            Parent root = loader.load();

            javafx.scene.control.Button boton = (javafx.scene.control.Button) event.getSource();
            Stage stage = (Stage) boton.getScene().getWindow();

            double anchoActual = stage.getWidth();
            double altoActual = stage.getHeight();
            boolean estabaMaximizado = stage.isMaximized();

            Scene nuevaEscena = new Scene(root);
            stage.setScene(nuevaEscena);

            if (estabaMaximizado) {
                stage.setMaximized(true);
            }
            else {
                stage.setWidth(anchoActual);
                stage.setHeight(altoActual);
            }

            stage.show();

        }
        catch (IOException e) {
            System.err.println("Error al cambiar de escena: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}