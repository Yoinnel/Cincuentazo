package com.univalle.cincuentazo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Esta clase se encarga de gestionar la selección de la cantidad de jugadores para la partida.
 * Controla la interacción de los botones de selección, calcula el número de bots necesarios y realiza el cambio a la escena del juego.
 */

public class PlayersSelectorController {

    // Variables FXML
    @FXML private ToggleButton TwoPlayersButton;
    @FXML private ToggleButton ThreePlayersButton;
    @FXML private ToggleButton FourPlayersButton;

    // Variables
    private ToggleGroup Players;

    /**
     * Metodo de inicialización automática de JavaFX para configurar los componentes visuales
     */
    @FXML
    public void initialize() {
        Players = new ToggleGroup();
        TwoPlayersButton.setToggleGroup(Players);
        ThreePlayersButton.setToggleGroup(Players);
        FourPlayersButton.setToggleGroup(Players);
    }

    /**
     * Metodo que maneja el evento de clic en el boton de iniciar juego
     * @param event el evento de acción del botón
     * @throws IOException si ocurre un error al cargar la vista del juego
     */
    @FXML
    private void onStartGameButtonClick(ActionEvent event) throws IOException {

        ToggleButton selected = (ToggleButton) Players.getSelectedToggle();

        if (selected == null) {
            System.out.println("Elige la cantidad de jugadores");
            return;
        }

        int HowManyPlayers = 0;
        int HowManyBots = 0;

        if (selected == TwoPlayersButton) {
            HowManyPlayers = 2;
            HowManyBots = 1;
        }
        if (selected == ThreePlayersButton) {
            HowManyPlayers = 3;
            HowManyBots = 2;
        }
        else if (selected == FourPlayersButton) {
            HowManyPlayers = 4;
            HowManyBots = 3;
        }

        if (HowManyBots == 1) {
            System.out.println("Iniciando juego con " + HowManyBots + " Bot.");
        }
        else {
            System.out.println("Iniciando juego con " + HowManyBots + " Bots.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/cincuentazo/fxml/game-view.fxml"));
            Parent root = loader.load();
            GameController gameController = loader.getController();

            javafx.scene.control.Button boton = (javafx.scene.control.Button) event.getSource();
            Stage stage = (Stage) boton.getScene().getWindow();

            double anchoActual = stage.getWidth();
            double altoActual = stage.getHeight();
            boolean estabaMaximizado = stage.isMaximized();
            gameController.configurarPartida(HowManyBots);

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

    /**
     * Metodo que maneja el evento de clic en el boton de regresar al menú principal
     * @param event el evento de acción del botón
     * @throws Exception si ocurre un error al cargar la vista del menú
     */
    @FXML
    private void onPlayersSelectorBackButtonClick(ActionEvent event) throws  Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/cincuentazo/fxml/menu-view.fxml"));
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
