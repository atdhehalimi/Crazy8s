import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class Deck {

    //Inicializimi i global i array te letrave
    ArrayList<Cards> cards;

    //Krijon nje grumull specifik letrash
    public Deck (int n) {
        cards = new ArrayList<Cards>(n);
    }

    //Krijon nje grumull prej 52 letrash
    public Deck () {
        cards = new ArrayList<Cards>(52);
        int index = 0;
        for (int suit = 0; suit <= 3; suit++) {
            for (int rank = 1; rank <= 13; rank++) {
                cards.add(new Cards(suit, rank));
                index++;
            }
        }
    }

    //Kjo metode tregon nje deck
    public static void printDeck (Deck deck) {
        for (int i = 0; i < deck.cards.size(); i++) {
            Cards.printCard(deck.cards.get(i));
        }
    }

    //Kjo metode  toString krijon nje deck te string 
    public static String deckToString (Deck deck) {
        String deckString = "";
        for (int i = 0; i < deck.cards.size(); i++) {
            deckString += Cards.cardToString(deck.cards.get(i)) + "\n";
        }
        return deckString;
    }

    //Nderron dy letra brenda nje deck
    public static void switchCards (Deck deck, int card1, int card2){
        Cards cardA = new Cards(deck.cards.get(card1).suit, deck.cards.get(card1).rank);
        Cards cardB = new Cards(deck.cards.get(card2).suit, deck.cards.get(card2).rank);

        deck.cards.get(card1).suit = cardB.suit;
        deck.cards.get(card1).rank = cardB.rank;

        deck.cards.get(card2).suit = cardA.suit;
        deck.cards.get(card2).rank = cardA.rank;

    }

    //Perzien letrat ne menyre te rastesishme
    public static void shuffle (Deck deck) {
        for (int i = 0; i < deck.cards.size(); i++) {
            int random = (int)(Math.random()*deck.cards.size());
            switchCards(deck, i, random);
        }
    }

    //Metode qe krijon nje nengrup letrash
    public static Deck subdeck (Deck deck, int low, int high) {
        Deck sub = new Deck(high - low + 1);

        for (int i = 0; i < (high - low + 1); i++) {
            sub.cards.add(deck.cards.get(low + i));
        }

        return sub;
    }

    //Per testimin e metodave perdoret main
    public static void main (String [] args) {
        Deck deck = new Deck();
        shuffle(deck);
        Deck hand1 = subdeck(deck, 0, 7);
        printDeck(hand1);
    }



}
