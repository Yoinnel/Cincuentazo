package com.univalle.cincuentazo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Esta clase se encarga de gestionar las interacciones del menu principal del juego.
 * Controla el redireccionamiento de las pantallas principales como el selector de jugadores y la guia de juego.
 */

public class MainMenuController {

    /**
     * Metodo que maneja el evento de clic en el boton de jugar para ir al selector de jugadores
     * @param event el evento de acción del botón
     */
    @FXML
    private void onPlayButtonClick(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/cincuentazo/fxml/fxml/players-selector-view.fxml"));
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

    /**
     * Metodo que maneja el evento de clic en el boton de cómo jugar
     * @param event el evento de acción del botón
     */
    @FXML
    private void onHowToPlayButtonClick(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/cincuentazo/fxml/fxml/how-to-play-view.fxml"));
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
            } else {
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