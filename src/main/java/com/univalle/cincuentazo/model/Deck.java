package com.univalle.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de crear el mazo principal con las 52 cartas del juego.
 * Controla las acciones de contar, mezclar y repartir las cartas a los jugadores.
 */

public class Deck {

    // Variables
    private final List<Card> cards;

    /**
     * Este es el metodo constructor del objeto de la clase Deck
     */
    public Deck() { // Nota: Cámbiale el nombre a tu clase si prefieres, yo la llamaré Deck
        this.cards = new ArrayList<>();
        CreateDeck();
        shuffle();
    }

    /**
     * Metodo que se encarga de llenar la lista con las 52 cartas del juego
     */
    private void CreateDeck() {
        String[] suits = {"corazon", "diamante", "espada", "trebol"};

        for (String suit : suits) {
            for (int number = 1; number <= 13; number++) {
                cards.add(new Card(suit, number));
            }
        }
    }

    /**
     * Metodo para remover y retornar la última carta del mazo central
     * @return la ultima carta del mazo central
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            // Si el mazo se llega a quedar vacío, podemos controlar eso después
            return null;
        }
        // Removemos y devolvemos la última carta de la lista (así simulamos sacar la de arriba)
        return cards.remove(cards.size() - 1);
    }

    /**
     * Metodo getter para obtener la cantidad de cartas restantes
     * @return la cantidad de cartas en el mazo central
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Metodo para agregar una carta de vuelta al mazo
     * @param card es la carta añadida
     */
    public void addCard(Card card) {
        this.cards.add(card); // (Cambia 'cards' por el nombre de tu lista interna de cartas en Deck)
    }

    /**
     * Metodo para mezclar de forma aleatoria las cartas del mazo
     */
    public void shuffle() {
        java.util.Collections.shuffle(this.cards); // (Cambia 'cards' por tu lista interna)
    }

}