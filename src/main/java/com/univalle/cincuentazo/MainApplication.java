/**
 * Game "SUDOKU"
 * Desarrollado usando JavaFX, IntelliJ Idea y SceneBuilder
 * Languages used: Java
 * <p>
 * Descripción del juego: Cincuentazo es un juego donde podrás elegir jugar contra 1, 2 o 3 bots. El objetivo del juego
 * es tirar cartas para que esta se sumen a la mesa, algunas con efectos especiales, sin que la sume supere el valor de 50.
 * Tu objetivo es sobrevivir hasta que los demás jugadores no tengan cartas válidas para lanzar.
 *<p>
 * @author Estaban Granada Salamanca
 * @author Yoinnel Gabriel Martinez Brito
 *<p>
 * @version 1.0
 * @since 2026
 */

package com.univalle.cincuentazo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Esta clase se encarga de crear la ventana principal del juego con un tamaño inicial de 1280 x 720 pixeles.

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/univalle/cincuentazo/fxml/fxml/menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cincuentazo");
        stage.setScene(scene);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.show();
    }
}