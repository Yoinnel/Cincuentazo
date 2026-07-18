package com.univalle.cincuentazo.model;

/**
 * Esta clase se encarga de definir el modelo y los atributos de una carta individual.
 * Determina el palo, número, ruta de imagen y el valor lógico de cada carta en el juego, es decir, su efecto en partida.
 */

public class Card {

    // Variables
    private String suit;
    private int number;

    /**
     * Este es el metodo constructor de los objeto de clase Card
     * @param suit es el palo de la carta (Corazon, Diamante, Espada o trebol)
     * @param number es el numero de la carta
     */
    public Card(String suit, int number) {
        this.suit = suit;
        this.number = number;
    }

    /**
     * Metodo getter del palo de la carta
     * @return el palo de la carta
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Metodo getter del número de la carta
     * @return el número de la carta
     */
    public int getNumber() {
        return number;
    }

    /**
     * Metodo para obtener el nombre del archivo de imagen de la carta
     * @return nombre de la imagen con extension jpg de la forma "palo_carta.jpg"
     */
    public String getImage() {
        return getSuit() + "_" + getNumber() + ".jpg";
    }

    /**
     * Metodo para obtener el valor que suma o resta la carta en la mesa
     * @return el valor numérico de la carta según las reglas
     */
    public int getValue() {
        switch (number) {
            case 1:
                return 1;
            case 9:
                return 0;
            case 11:
            case 12:
            case 13:
                return -10;
            default:
                return number;
        }
    }
}