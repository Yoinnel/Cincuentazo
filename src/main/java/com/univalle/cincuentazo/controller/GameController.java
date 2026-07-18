package com.univalle.cincuentazo.controller;

import com.univalle.cincuentazo.model.Card;
import com.univalle.cincuentazo.model.Deck;
import com.univalle.cincuentazo.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Esta clase es el controlador principal del juego Cincuentazo.
 * Aquí se manejan todos los eventos que ocurren durante las partidas.
 * Se encarga de coordinar la logica del mazo, los turnos del jugador humano, las decisiones de los bots y la actualizacion de la interfaz gráfica.
 */

public class GameController {

    // Variables FXML
    @FXML private ImageView playerCard1;
    @FXML private ImageView playerCard2;
    @FXML private ImageView playerCard3;
    @FXML private ImageView playerCard4;
    @FXML private HBox botTop;
    @FXML private VBox botLeft;
    @FXML private VBox botRight;
    @FXML private ImageView centralCard;
    @FXML private Label Amount;

    // Variables
    private List<ImageView> playerImageViews;
    private Deck mazo;
    private Player jugadorHumano;
    private List<Player> listaDeBots;
    private Card cartaCentral; // La carta en la mesa
    private int sumaMesa = 0;
    private final List<Card> pozoDescartes = new ArrayList<>();
    private boolean debeRobar = false;
    private boolean clicsPermitidos = true;
    private Thread hiloBot;

    /**
     * Metodo de inicialización automática de JavaFX para verificar los componentes visuales.
     * Agrupa las cartas del jugador en una lista para facilitar su manejo lógico.
     */
    @FXML
    public void initialize() {

        if (playerCard1 == null || playerCard2 == null || playerCard3 == null || playerCard4 == null) {
            System.err.println("ERROR: Uno o más ImageViews del FXML son NULL. Verificar los fx:id en Scene Builder.");
        }
        playerImageViews = Arrays.asList(playerCard1, playerCard2, playerCard3, playerCard4);
    }

    /**
     * Metodo para configurar e inicializar todos los elementos de una nueva partida.
     * Crea el mazo, los jugadores, activa los contenedores visuales según los bots elegidos y reparte las cartas iniciales.
     * @param HowManyBots la cantidad de bots elegidos para jugar en la partida.
     * @see PlayersSelectorController en el metodo onStartGameButtonClick.
     */
    public void configurarPartida(int HowManyBots) {

        pozoDescartes.clear();
        this.clicsPermitidos = true;
        this.debeRobar = false;

        mazo = new Deck();
        jugadorHumano = new Player("Tú", false);
        listaDeBots = new ArrayList<>();

        botTop.setVisible(false);
        botLeft.setVisible(false);
        botRight.setVisible(false);

        if (HowManyBots >= 1) {
            listaDeBots.add(new Player("Bot Superior", true));
            botTop.setVisible(true);
        }
        if (HowManyBots >= 2) {
            listaDeBots.add(new Player("Bot Izquierdo", true));
            botLeft.setVisible(true);
        }
        if (HowManyBots == 3) {
            listaDeBots.add(new Player("Bot Derecho", true));
            botRight.setVisible(true);
        }

        for (int i = 0; i < 4; i++) {
            jugadorHumano.receiveCard(mazo.drawCard());
            for (Player bot : listaDeBots) {
                bot.receiveCard(mazo.drawCard()); // Solo se ejecuta para los bots reales en la lista
            }
        }

        cartaCentral = mazo.drawCard();
        sumaMesa = cartaCentral.getValue();

        System.out.println("Partida iniciada con " + listaDeBots.size() + " bots.");
        actualizarPantalla();
    }

    /**
     * Metodo para refrescar y actualizar todos los componentes de la interfaz gráfica.
     * Muestra las imagenes de las cartas del jugador, la carta central y el valor acumulado de la mesa.
     */
    private void actualizarPantalla() {

        List<Card> manoHumano = jugadorHumano.getHand();
        for (int i = 0; i < playerImageViews.size(); i++) {
            ImageView imageView = playerImageViews.get(i);
            if (i < manoHumano.size()) {
                Card carta = manoHumano.get(i);
                cargarImagen(imageView, "/com/univalle/cincuentazo/fxml/images/poker_deck/" + carta.getImage());
            }
            else {
                imageView.setImage(null); // Si el jugador tiene menos cartas
            }
        }

        if (cartaCentral != null) {
            cargarImagen(centralCard, "/com/univalle/cincuentazo/fxml/images/poker_deck/" + cartaCentral.getImage());
        }
        if (Amount != null) {
            Amount.setText("SUMA: " + String.valueOf(sumaMesa));
        }

        System.out.println("Cartas restantes en el mazo: " + mazo.getSize());
    }

    /**
     * Metodo para cargar los archivos de imagen en un contenedor ImageView.
     * Busca el archivo en los recursos del proyecto y maneja las excepciones en caso de que no se encuentre.
     * @param imageView el contenedor donde se mostrara la imagen.
     * @param ruta la ubicación de la imagen en los recursos.
     */
    private void cargarImagen(ImageView imageView, String ruta) {

        if (imageView == null) return;

        try {
            java.io.InputStream resource = getClass().getResourceAsStream(ruta);
            if (resource != null) {
                Image img = new Image(resource);
                imageView.setImage(img);
                imageView.setVisible(true);
            }
            else {
                System.err.println("Archivo no encontrado: " + ruta);
            }
        }
        catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + ruta + " -> " + e.getMessage());
        }
    }

    /**
     * Metodo que maneja el evento de clic sobre las cartas de la mano del jugador humano.
     * Verifica que las restricciones de robo y turnos estén en orden antes de identificar la carta seleccionada y procesar su jugada.
     * @param event el evento de mouse al hacer clic sobre la carta.
     */
    @FXML
    private void onCardClick(javafx.scene.input.MouseEvent event) {

        if (debeRobar) {
            System.out.println("Debes robar una carta del mazo antes de realizar otra jugada.");
            return;
        }
        if (!clicsPermitidos) {
            System.out.println("No puedes jugar en este momento.");
            return;
        }

        ImageView cartaClickeada = (ImageView) event.getSource();
        int indiceCarta = playerImageViews.indexOf(cartaClickeada);
        List<Card> mano = jugadorHumano.getHand();

        if (indiceCarta >= 0 && indiceCarta < mano.size()) {
            Card cartaSeleccionada = mano.get(indiceCarta);
            System.out.println("Hiciste clic en: " + cartaSeleccionada.getSuit() + " " + cartaSeleccionada.getNumber());
            procesarJugadaJugador(cartaSeleccionada, indiceCarta);
        }
    }

    /**
     * Metodo para validar y ejecutar la jugada realizada por el jugador humano.
     * Verifica que el valor acumulado no exceda el límite del juego, actualiza la carta central,
     * remueve la carta de la mano y bloquea los clics hasta que el jugador tome una carta del mazo central.
     * @param carta la carta seleccionada que se va a jugar en la mesa.
     * @param indiceMano la posicion de la carta en la mano del jugador.
     */
    private void procesarJugadaJugador(Card carta, int indiceMano) {

        int valorCarta = calcularEfectoCarta(carta);
        int sumaPotencial = sumaMesa + valorCarta;

        if (sumaMesa >= 0 && sumaPotencial < 0) {
            sumaPotencial = 0;
        }
        if (sumaPotencial > 50) {
            System.out.println("Esta carta (" + carta.getNumber() + ") sumaría " + sumaPotencial + " a la mesa. Supera los 50, intenta con otra.");
            return;
        }

        sumaMesa = sumaPotencial;

        if (cartaCentral != null) {
            pozoDescartes.add(cartaCentral);
        }

        cartaCentral = carta;
        jugadorHumano.getHand().remove(indiceMano);

        actualizarPantalla();

        debeRobar = true;
        clicsPermitidos = false;
        System.out.println("Juegas tu carta. Toca el mazo para robar y pasar el turno...");
    }

    /**
     * Metodo para calcular cuánto suma una carta según el estado actual de la mesa.
     * Aplica las reglas especiales del juego Cincuentazo para el 9, las figuras (J, Q, K) y el comportamiento del As.
     * @param carta la carta a la que se le calculará el efecto en la suma de la mesa.
     * @return el valor entero que se debe sumar o restar a la suma de la mesa.
     */
    private int calcularEfectoCarta(Card carta) {

        int numero = carta.getNumber();

        if (numero == 1) {
            if (sumaMesa + 10 <= 50) {
                System.out.println("El As se juega como +10");
                return 10;
            }
            else {
                System.out.println("El As se juega como +1");
                return 1;
            }
        }
        if (numero == 9) {
            return 0;
        }
        if (numero >= 11 && numero <= 13) {
            if (sumaMesa <= 0) {
                return 0;
            }
            return -10;
        }

        return carta.getValue();
    }

    /**
     * Metodo para reabastecer el mazo central cuando este se queda sin cartas.
     * Toma todas las cartas acumuladas en la lista de descartes, las transfiere de nuevo al mazo y las baraja.
     */
    private void reciclarDescartes() {

        System.out.println("Se agotaron las cartas del mazo centra! Mezclando cartas anteriores");

        if (pozoDescartes.isEmpty()) {
            System.out.println("Error! Las cartas no están siendo enviadas a la lista de descartes");
            return;
        }

        for (Card carta : pozoDescartes) {
            mazo.addCard(carta);
        }

        pozoDescartes.clear();
        mazo.shuffle();

        System.out.println("¡Mazo mezclado! Cartas disponibles para robar: " + mazo.getSize());
    }

    /**
     * Metodo que maneja el evento de clic sobre el mazo de robo de la mesa.
     * Permite al jugador humano robar una carta, gestiona uso de las cartas descartadas de si el mazo está vacío
     * y bloquea las interacciones para ceder el turno a los bots/jugadores máquina.
     * @param event el evento de mouse al hacer clic sobre el mazo
     */
    @FXML
    private void onDeckPoolClick(javafx.scene.input.MouseEvent event) {

        if (!debeRobar) {
            System.out.println("No puedes robar cartas en este momento.");
            return;
        }

        System.out.println("Has tomado una carta.");

        if (mazo.getSize() == 0) {
            reciclarDescartes();
        }
        if (mazo.getSize() > 0) {
            Card nuevaCarta = mazo.drawCard();
            jugadorHumano.receiveCard(nuevaCarta);
            System.out.println("Tomaste una carta. Quedan " + mazo.getSize() + " cartas en el mazo");
        }

        debeRobar = false;
        clicsPermitidos = false;

        actualizarPantalla();
        avanzarSiguienteTurno(0);
    }

    /**
     * Metodo que maneja el evento de clic en el boton de terminar partida para salir al menu principal.
     * Detiene la ejecución de los hilos de los bots en caso de estar activos
     * y realiza la transición de pantallas manteniendo las dimensiones actuales de la ventana.
     * @param event el evento de acción del botón.
     */
    @FXML
    private void onEndGameButtonClick(javafx.event.ActionEvent event) {

        try {
            if (hiloBot != null && hiloBot.isAlive()) {
                hiloBot.interrupt();
            }

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

    /**
     * Metodo para procesar la jugada de un bot en un hilo secundario.
     * Simula tiempos de espera reales para elegir y tomar cartas, modifica temporalmente
     * el estado visual de sus cartas para mejorar la Experiencia de Usuario (UX) y coordina el flujo
     * de la interfaz gráfica usando la cola de ejecución de JavaFX.
     * @param bot el jugador máquina/bot que realizará la jugada.
     * @param indiceBot la posición actual del bot dentro de la lista de bots.
     */
    private void ejecutarTurnoBot(Player bot, int indiceBot) {

        System.out.println(bot.getName() + " está pensando su jugada...");

        hiloBot = new Thread(() -> {
            try {
                long tiempoPensar = (long) ((2.0 + new Random().nextDouble() * 2.0) * 1000);
                Thread.sleep(tiempoPensar);

                Card cartaElegida = evaluarCartasBot(bot);

                if (cartaElegida != null) {
                    int valorEfecto = calcularEfectoCarta(cartaElegida);
                    int sumaAntesDeJugar = sumaMesa;

                    sumaMesa += valorEfecto;

                    if (sumaAntesDeJugar >= 0 && sumaMesa < 0) {
                        sumaMesa = 0;
                    }

                    if (cartaCentral != null) pozoDescartes.add(cartaCentral);
                    cartaCentral = cartaElegida;
                    bot.getHand().remove(cartaElegida);

                    javafx.scene.layout.Pane contenedorBot = null;

                    switch (bot.getName()) {
                        case "Bot Superior":
                            contenedorBot = botTop;
                            break;
                        case "Bot Izquierdo":
                            contenedorBot = botLeft;
                            break;
                        case "Bot Derecho":
                            contenedorBot = botRight;
                            break;
                    }

                    final int indiceOcultar;
                    final javafx.scene.layout.Pane contenedorFinal = contenedorBot;

                    if (contenedorFinal != null && !contenedorFinal.getChildren().isEmpty()) {
                        int cantidadCartas = contenedorFinal.getChildren().size();
                        indiceOcultar = (int) (Math.random() * cantidadCartas);

                        javafx.application.Platform.runLater(() -> {
                            contenedorFinal.getChildren().get(indiceOcultar).setVisible(false);
                        });
                    }
                    else {
                        indiceOcultar = -1;
                    }

                    javafx.application.Platform.runLater(this::actualizarPantalla);

                    long tiempoRobar = (long) ((1.0 + new Random().nextDouble() * 3.0) * 1000);
                    Thread.sleep(tiempoRobar);

                    if (mazo.getSize() == 0) reciclarDescartes();
                    if (mazo.getSize() > 0) {
                        Card nuevaCarta = mazo.drawCard();
                        bot.receiveCard(nuevaCarta);
                    }

                    javafx.application.Platform.runLater(() -> {
                        actualizarPantalla();

                        if (contenedorFinal != null && indiceOcultar != -1) {
                            contenedorFinal.getChildren().get(indiceOcultar).setVisible(true);
                        }

                        avanzarSiguienteTurno(indiceBot + 1);
                    });

                }
                else {
                    System.out.println(bot.getName() + " no tiene cartas válidas, descarta su mano y pierde.");

                    pozoDescartes.addAll(bot.getHand());
                    bot.getHand().clear();

                    javafx.application.Platform.runLater(() -> {
                        switch (bot.getName()) {
                            case "Bot Superior": botTop.setVisible(false); break;
                            case "Bot Izquierdo": botLeft.setVisible(false); break;
                            case "Bot Derecho": botRight.setVisible(false); break;
                        }

                        actualizarPantalla();

                        long botsActivos = listaDeBots.stream().filter(b -> !b.getHand().isEmpty()).count();

                        if (botsActivos == 0) {
                            mostrarAlertaGanador();
                        }
                        else {
                            avanzarSiguienteTurno(indiceBot + 1);
                        }
                    });
                }
            }
            catch (InterruptedException ex) {
                System.err.println("El hilo del bot fue interrumpido: " + ex.getMessage());
            }
        });
        hiloBot.setDaemon(true);
        hiloBot.start();
    }

    /**
     * Metodo para evaluar la mano de un bot y seleccionar la mejor estrategia disponible.
     * Analiza cada carta según un sistema de prioridad que busca ejecutar movimientos agresivos,
     * descartando de manera estricta cualquier opción que supere el límite permitido en la mesa.
     * @param bot la instancia del jugador máquina que se encuentra en su turno.
     * @return la carta seleccionada o null si ninguna jugada es válida.
     */
    private Card evaluarCartasBot(Player bot) {

        Card mejorCarta = null;
        int mayorPrioridad = Integer.MIN_VALUE;

        for (Card carta : bot.getHand()) {
            int numero = carta.getNumber();
            int valorEfecto = obtenerValorSimulado(carta);

            if (sumaMesa + valorEfecto > 50) {
                continue;
            }

            int prioridadCard = 0;

            if (numero == 10) {
                prioridadCard = 100;
            }
            else if (numero >= 1 && numero <= 8) {
                prioridadCard = numero * 10;
            }
            else if (numero == 9) {
                prioridadCard = 5;
            }
            else if (numero >= 11 && numero <= 13) {
                prioridadCard = numero - 10;
            }

            if (prioridadCard > mayorPrioridad) {
                mayorPrioridad = prioridadCard;
                mejorCarta = carta;
            }
        }

        return mejorCarta;
    }

    /**
     * Metodo para simular el valor de una carta antes de ser jugada en la mesa
     * Permite al jugador máquina saber el valor en la suma de la mesa de cada carta de su mano,
     * replicando las mismas reglas de cálculo de suma de la mesa del juego
     * @param carta la carta cuyo valor será simulado
     * @return el valor entero simulado que la carta sumaría a la mesa
     */
    private int obtenerValorSimulado(Card carta) {

        int numero = carta.getNumber();
        if (numero == 9) return 0;
        if (numero >= 11 && numero <= 13) return -10;
        if (numero == 1) {
            return (sumaMesa + 10 <= 50) ? 10 : 1;
        }

        return carta.getValue();
    }

    /**
     * Metodo para gestionar la transición de los turnos entre los jugadores máquina y el humano
     * Recorre la lista buscando el siguiente bot que aún tenga cartas en su mano para cederle el control y,
     * en caso de que todos hayan jugado, válida si el jugador humano tiene movimientos válidos para devolverle el turno.
     * @param siguienteIndice la posición de la lista de bots desde la cual se iniciará la búsqueda del proximo turno.
     */
    private void avanzarSiguienteTurno(int siguienteIndice) {

        Player proximoBot = null;
        int indiceEncontrado = -1;

        for (int i = siguienteIndice; i < listaDeBots.size(); i++) {
            Player bot = listaDeBots.get(i);
            if (!bot.getHand().isEmpty()) {
                proximoBot = bot;
                indiceEncontrado = i;
                break;
            }
        }

        if (proximoBot != null) {
            ejecutarTurnoBot(proximoBot, indiceEncontrado);
        }
        else {
            System.out.println("🤖 El último bot activo ha completado su turno y robo. Evaluando al humano...");

            if (!tieneCartasValidasHumano()) {
                System.out.println("💀 El jugador humano no tiene jugadas válidas disponibles.");
                mostrarAlertaPerdedor();
                return;
            }

            clicsPermitidos = true;
            debeRobar = false;
            System.out.println("Es tu turno");
        }
    }

    /**
     * Metodo para mostrar una ventana emergente de victoria cuando el jugador humano gana la partida.
     * Bloquea las interacciones de la interfaz y ofrece opciones para reiniciar el juego
     * con la misma configuración actual o regresar al menu principal.
     */
    private void mostrarAlertaGanador() {

        clicsPermitidos = false;

        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alerta.setTitle("¡Victoria!");
        alerta.setHeaderText("¡Felicidades! Has ganado la partida.");
        alerta.setContentText("¿Qué quieres hacer ahora?");

        javafx.scene.control.ButtonType botonVolverAJugar = new javafx.scene.control.ButtonType("Volver a jugar");
        javafx.scene.control.ButtonType botonMenu = new javafx.scene.control.ButtonType("Ir al menú");

        alerta.getButtonTypes().setAll(botonVolverAJugar, botonMenu);

        java.util.Optional<javafx.scene.control.ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonVolverAJugar) {
            configurarPartida(listaDeBots.size());
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/cincuentazo/fxml/fxml/menu-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) Amount.getScene().getWindow();

                Scene nuevaEscena = new Scene(root);
                stage.setScene(nuevaEscena);
                stage.show();
            }
            catch (IOException e) {
                System.err.println("Error al regresar al menú desde la victoria: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo para verificar si el jugador humano tiene al menos una jugada válida disponible en su mano.
     * Simula el comportamiento del As, las figuras y los valores nominales de cada carta respecto a la mesa
     * para determinar si el jugador puede continuar o ha quedado bloqueado.
     * @return true si el jugador tiene una carta jugable; false si ninguna carta de su mano evita superar los 50 puntos.
     */
    private boolean tieneCartasValidasHumano() {

        for (Card carta : jugadorHumano.getHand()) {
            int valorEfecto;

            if (carta.getNumber() == 1) {
                valorEfecto = (sumaMesa + 10 <= 50) ? 10 : 1;
            }
            else if (carta.getNumber() == 9) {
                valorEfecto = 0;
            }
            else if (carta.getNumber() >= 11 && carta.getNumber() <= 13) {
                valorEfecto = -10;
            }
            else {
                valorEfecto = carta.getValue();
            }
            if (sumaMesa + valorEfecto <= 50) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo para mostrar una ventana emergente de derrota cuando el jugador humano se queda sin movimientos válidos
     * Bloquea las interacciones de la interfaz y ofrece opciones para reiniciar el juego
     * con la misma cantidad de bots o regresar al menu principal
     */
    private void mostrarAlertaPerdedor() {

        clicsPermitidos = false;

        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alerta.setTitle("¡Derrota!");
        alerta.setHeaderText("No tienes cartas válidas para jugar. ¡Has perdido!  :(");
        alerta.setContentText("¿Qué deseas hacer?");

        javafx.scene.control.ButtonType botonVolverAJugar = new javafx.scene.control.ButtonType("Volver a jugar");
        javafx.scene.control.ButtonType botonMenu = new javafx.scene.control.ButtonType("Ir al menú");

        alerta.getButtonTypes().setAll(botonVolverAJugar, botonMenu);

        java.util.Optional<javafx.scene.control.ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonVolverAJugar) {
            configurarPartida(listaDeBots.size());
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/univalle/cincuentazo/fxml/fxml/menu-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) Amount.getScene().getWindow();
                Scene nuevaEscena = new Scene(root);
                stage.setScene(nuevaEscena);
                stage.show();
            }
            catch (IOException e) {
                System.err.println("Error al regresar al menú desde la derrota: " + e.getMessage());
            }
        }
    }
}