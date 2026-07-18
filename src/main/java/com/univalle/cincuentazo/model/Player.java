package com.univalle.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se determina el modelo y la lógica de la clase jugador.
 * Almacena los datos del usuario, de los bots y gestiona las cartas de su mano.
 */

public class Player {

    // Variables
    private String name;
    private boolean isBot;
    private List<Card> hand;

    /**
     * Este es el metodo constructor de los objeto de clase Player
     * @param name es el nombre del jugador
     * @param isBot determina si el jugador es un bot/maquina
     */
    public Player(String name, boolean isBot) {
        this.name = name;
        this.isBot = isBot;
        this.hand = new ArrayList<>();
    }

    /**
     * Metodo getter del nombre del jugador
     * @return nombre del jugador
     */
    public String getName() {
        return name;
    }

    /**
     * Metodo booleano que determina si un jugador es bot o no
     * @return Es bot
     */
    public boolean isBot() {
        return isBot;
    }

    /**
     * Metodo getter de la lista de cartas del jugador
     * @return la lista de cartas de la mano
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Metodo para agregar una carta a la mano del jugador
     * @param card es la carta que recibe el jugador
     */
    public void receiveCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    /**
     * Metodo para remover una carta de la mano del jugador al jugar
     * @param card es la carta jugada
     */
    public void playCard(Card card) {
        hand.remove(card);
    }
}