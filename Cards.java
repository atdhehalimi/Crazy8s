import javax.swing.*;



public class Cards {

    //Inicializimi
    int suit, rank;

    
    public Cards () {
        this.suit = 0;
        this.rank = 0;
    }

    //Konstruktori i llojit
    public Cards (int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    //Metoda shfaq nje leter
    public static void printCard (Cards c) {
        String[] suits = { "Lule", "Diamante", "Zemra", "Gjethe" };
        String[] ranks = { "narf", "Pika", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "Xhandar", "Cika", "Mbreti" };
        System.out.println (ranks[c.rank] + " of " + suits[c.suit]);
    }

    //Kjo metode  toString krijon nje string letrash
    public static String cardToString (Cards c) {
        String cardString;
        String[] suits = {"Lule", "Diamante", "Zemra", "Gjethe"};
        String[] ranks = {"narf", "Pika", "2", "3", "4", "5", "6",
                "7", "8", "9", "10", "Xhandar", "Cika", "Mbreti"};
        return cardString = ranks[c.rank] + " of " + suits[c.suit];
    }

    //Metoda krahason dy letra 
    public static boolean sameCard (Cards c1, Cards c2) {
        return (c1.suit == c2.suit && c1.rank == c2.rank);
    }

    //Metoda kragason dy letra hollesisht
    public int compareCards (Cards c1, Cards c2){

        if (c1.suit > c2.suit) return 1;
        if (c1.suit < c2.suit) return -1;

        if (c1.rank > c2.rank) return 1;
        if (c1.rank < c2.rank) return -1;

        return 0;

    }

    //Per testimin e metodave perdoret main
    public static void main (String [] args){
        Cards card1 = new Cards (1, 11);
        Cards card2 = new Cards (1, 12);
        if (sameCard (card1, card2)) {
            System.out.println ("Karta 1 dhe Karta 2 jane te njejta .");
        }
    }

}
