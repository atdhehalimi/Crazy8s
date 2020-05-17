import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;



public class Game extends JFrame implements ActionListener{


     //Inicializimi i deck
    Deck deck;
    Deck playPile;
    Deck pickUpPile;

    //Inicializimi i deck
    ArrayList<Deck> hands = new ArrayList<Deck>();
    ArrayList<ArrayList<JLabel>> handsCards = new ArrayList<ArrayList<JLabel>>();

    //Inicializimi i  global containers
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    JScrollPane comPane;
    JScrollPane userPane;
    JPanel comPanel;
    JPanel playPanel;
    JPanel userPanel;

    //Inicializimi i komponenteve global
    JLabel playLabel;
    JButton pickUpButton;
    JLabel updates = new JLabel();

    //Inicializimi i komponenteve global
    Cards QoS = new Cards(3, 12);
    int currentSuit, currentRank;
    int turn = 2;
    int numOfPickUp = 0;

//Konstruktori i cili e inicializon lojen
        public Game() {
        super("Crazy 8's!");

        deal(2);

        setLayout(gb);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        gbc.ipady = 225;
        gbc.insets = new Insets(5,5,5,5);

        updates.setText("Rradha jote.");

        createUserPanel();
        playPanel = createPlayingField();
        createComPanel();

        comPane = new JScrollPane(comPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        userPane = new JScrollPane(userPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        gbc.gridx=0;
        gbc.gridy=0;
        super.add(comPane, gbc);

        gbc.ipadx = 200;
        gbc.ipady = 0;

        gbc.gridx=0;
        gbc.gridy=1;
        super.add(playPanel, gbc);

        gbc.ipadx = 200;
        gbc.ipady = 225;

        gbc.gridx=0;
        gbc.gridy=2;
        super.add(userPane, gbc);

        super.setBackground(Color.decode("#590000"));
        super.pack();
        super.setSize(660,765);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);

    }

    //Kjo metode shperndan letrat ne fillim te lojes
    public void deal(int numOfPlayers) {
        deck = new Deck();
        Deck.shuffle(deck);

        int low = 0;
        int high = 7;

        for (int i = 0; i < numOfPlayers; i++) {
            hands.add(Deck.subdeck(deck, low, high));
            handsCards.add(subdeckCards(i, low, high));
            low+=8;
            high+=8;
        }

        playPile = Deck.subdeck(deck, low, low);
        pickUpPile = Deck.subdeck(deck, low + 1, 51);

        currentRank = playPile.cards.get(playPile.cards.size() - 1).rank;
        currentSuit = playPile.cards.get(playPile.cards.size() - 1).suit;
    }

    //Kjo metode ndan nje objekt te deck ne array te llojit te letrave
    public ArrayList<JLabel> subdeckCards (int player, int low, int high) {
        ArrayList<JLabel> sub = new ArrayList<JLabel>();

        if (player == 0) {
            for (int i = 0; i < (high - low + 1); i++) {
                sub.add(cardToLabel(hands.get(player).cards.get(low + i)));
            }
        } else if (player == 1) {
            for (int i = 0; i < (high - low + 1); i++) {
                sub.add(cardToLabel(new Cards()));
            }
        }

        return sub;
    }

   //Kjo metode e perzien deck te letrave kur letrat mbarojne
    public void reshuffle() {
        int numOfPickUp = 0;
        for (int i = 1; i <= 5; i++) {
            if (i == 1) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2) numOfPickUp += 1;
                else if (playPile.cards.get(playPile.cards.size() - i).rank == 12) numOfPickUp += 1;
            } else if (i == 2){
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2) numOfPickUp += 1;
            } else if (i == 3) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 2).rank == 2) numOfPickUp += 1;
            } else if (i == 4) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 2).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 3).rank == 2) numOfPickUp += 1;
            } else if (i == 5) {
                if (playPile.cards.get(playPile.cards.size() - i).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 1).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 2).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 3).rank == 2 && playPile.cards.get(playPile.cards.size() - i + 4).rank == 2) numOfPickUp += 1;
            }
        }
        for (int i = 0; i < playPile.cards.size() - (1 + numOfPickUp); i++) {
            pickUpPile.cards.add(playPile.cards.get(i));
            playPile.cards.remove(i);
        }
        Deck.shuffle(pickUpPile);
    }

    //Kjo metode e perzien deck te letrave kur letrat mbarojne
    public int whoseTurnsNext() {
        if (turn % 2 == 0) return 0;
        else return 1;
    }

    //Metoda perdoret ne mes te levizjeve, lejon lojtarin te vendos letren, gjithashtu inicializon levizjen e lojtarit tjeter 
    public void playCard(int player, int indexOfCard) {
        currentSuit = hands.get(player).cards.get(indexOfCard).suit;
        currentRank = hands.get(player).cards.get(indexOfCard).rank;

        playPile.cards.add(hands.get(player).cards.get(indexOfCard));

        playPanel.remove(playLabel);
        playLabel = cardToLabel(playPile.cards.get(playPile.cards.size() - 1));
        playPanel.add(playLabel);
        playPanel.validate();
        playPanel.repaint();

        hands.get(player).cards.remove(indexOfCard);
        hands.get(player).cards.trimToSize();
        handsCards.get(player).trimToSize();

        playSpecialCard(playPile.cards.get(playPile.cards.size()-1));

        turn += 1;

        checkWin();

        if (whoseTurnsNext() == 1) {
            updateComPanel();
        } else if (whoseTurnsNext() == 0) {
            updateUserPanel();
        }
    }

       //Kjo metode reagon ndaj letrave speciale
    public void playSpecialCard(Cards specialCard) {
        if (specialCard.rank == 2 || Cards.sameCard(specialCard, QoS)) {
            if (specialCard.rank == 2) numOfPickUp += 2;
            else numOfPickUp += 5;

            if (whoseTurnsNext() == 0) {
                updates.setText("Kompjuteri terhoqi " + numOfPickUp + " letra.");
                for (int i = 0; i < numOfPickUp; i++) {
                    pickUp(1);
                }
            } else if (whoseTurnsNext() == 1) {
                JOptionPane.showMessageDialog(null, "Kompjuteri te detyroi t'i terheqesh " + numOfPickUp + " letra!");
                updates.setText("Ti terhoqe " + numOfPickUp + " letra.");
                for (int i = 0; i < numOfPickUp; i++) {
                    pickUp(0);
                }
            }


            return;

        } else if (specialCard.rank == 8) {
            if (whoseTurnsNext() == 0) {
                final Object[] inputs = new Object[]{"Lule", "Diamant", "Zemer", "Gjeth"};
                currentSuit = JOptionPane.showOptionDialog(super.createRootPane(), "Cilin simbol deshironi?", "Zgjedhni simbolin", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, inputs, inputs[3]);
              if (currentSuit == 0) {
                    updates.setText("Simboli tani eshte Lule");
                } else if (currentSuit == 1) {
                    updates.setText("Simboli tani eshte Diamant");
                } else if (currentSuit == 2) {
                    updates.setText("Simboli tani eshte zemer");
                } else if (currentSuit == 3) {
                    updates.setText("Simboli tani eshte Gjethe");
                }

           } else if (whoseTurnsNext() == 1) {
                currentSuit = (int)(Math.random()*4);
                if (currentSuit == 0) {
                    JOptionPane.showMessageDialog(null, "Kompjuteri e ndryshoi ne Lule!");
                    updates.setText("Simboli tani eshte Lule");
                } else if (currentSuit == 1) {
                    JOptionPane.showMessageDialog(null, "Kompjuteri e ndryshoi ne Diamante!");
                    updates.setText("Simboli tani eshte Diamant");
                } else if (currentSuit == 2) {
                    JOptionPane.showMessageDialog(null, "Kompjuteri e ndryshoi ne Zemer!");
                    updates.setText("Simboli tani eshte Zemer");
                } else if (currentSuit == 3) {
                    JOptionPane.showMessageDialog(null, "Kompjuteri e ndryshoi ne Gjethe!");
                    updates.setText("Simboli tani eshte Gjethe");
                }
            }
        } else if (specialCard.rank == 11) {
            if (whoseTurnsNext() == 1) {
                JOptionPane.showMessageDialog(null, "Kompjuteri e kaloi rradhen tende!");
                turn += 1;
            }
            else if (whoseTurnsNext() == 0) {
                updates.setText("Kompjuteri e kalon kete rradhe!");
                turn += 1;
            }
        }

        numOfPickUp = 0;
    }

    //Metoda kontrollon nese ka fitues
    public void checkWin() {
        if (ifWon(hands.get(0))) {
            winner(0);
        } else if (ifWon(hands.get(1))) {
            winner(1);
        }
    }

    //Metoda kthen true nese lojtari nuk ka letra
    public Boolean ifWon(Deck hand) {
        if (hand.cards.size() == 0) return true;
        return false;
    }

    //Metoda shpall fitore
    public void winner(int player) {
        if (player == 0) {
            JOptionPane.showMessageDialog(null, "Fitove!");
            dispose();
            new Game();
        } else if (player == 1) {
            JOptionPane.showMessageDialog(null, "Humbe!");
            dispose();
            new Game();
        }

    }

    //Metoda kontrollon nese levizja eshte e lejueshme
    public Boolean isValidMove(Cards card) {
        if (card.suit == currentSuit || card.rank == currentRank || card.rank == 8) return true;
        return false;
    }

    //Metoda kthen true nese lojtari mund te luaj ndonjeren prej letrave
    public Boolean canPlay(Deck hand) {
        for (int i = 0; i < hand.cards.size(); i++) {
            if (hand.cards.get(i).suit == currentSuit || hand.cards.get(i).rank == currentRank || hand.cards.get(i).rank == 8) return true;
        } return false;
    }

    //Metoda terheq nje leter per lojtarin
    public void pickUp(int player) {
        if (pickUpPile.cards.size() == 0) {
            reshuffle();
        }
        hands.get(player).cards.add(pickUpPile.cards.get(pickUpPile.cards.size() - 1));
        if (player == 0) {
            handsCards.get(player).add(cardToLabel(pickUpPile.cards.get(pickUpPile.cards.size() - 1)));
            userPanel.add(handsCards.get(0).get(handsCards.get(0).size()-1));
            userPanel.validate();
            userPanel.repaint();
        } else if (player == 1) {
            handsCards.get(player).add(cardToLabel(new Cards()));
            comPanel.removeAll();
            for (int i = 0; i < handsCards.get(1).size(); i++) {
                comPanel.add(handsCards.get(1).get(i));
            }
            comPanel.validate();
            comPanel.repaint();
        }

        pickUpPile.cards.remove(pickUpPile.cards.size() - 1);
        pickUpPile.cards.trimToSize();
    }

    //Metoda terheq nje leter per lojtarin
    public JPanel createPlayingField() {
        GridBagConstraints gbcd = new GridBagConstraints();
        JPanel panel = new JPanel(gb);
        pickUpButton = new JButton();
        pickUpButton.setIcon(cardToLabel(new Cards()).getIcon());
        playLabel = cardToLabel(playPile.cards.get(playPile.cards.size() - 1));
        pickUpButton.setBorderPainted(false);
        JPanel menu = createMenu();

        gbcd.fill = GridBagConstraints.HORIZONTAL;
        gbcd.insets = new Insets(10,10,10,10);

        gbcd.gridy = 0;
        gbcd.gridx = 0;
        panel.add(pickUpButton, gbcd);
        gbcd.gridx = 1;
        panel.add(menu, gbcd);
        gbcd.gridx = 2;
        panel.add(playLabel, gbcd);

        pickUpButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!canPlay(hands.get(0))) {
                    pickUp(0);
                    System.out.println("(Lojtari terheq) : tani " + turn);
                    turn += 1;
                    System.out.println("(Lojtari terheq) rradhen tjeter: " + turn);
                    updateComPanel();
                } else JOptionPane.showMessageDialog(null, "Nuk ke nevoje per leter!");

            }
        });

        return panel;
    }

    //Metoda krijon menu-n
    public JPanel createMenu() {
        GridBagConstraints gbcd = new GridBagConstraints();
        JPanel panel = new JPanel(gb);
       JButton newGame = new JButton("Loje e re");
        JButton help = new JButton("Ndihme");


        updates.setHorizontalAlignment(JLabel.CENTER);
        updates.setForeground(Color.WHITE);

        gbcd.fill = GridBagConstraints.HORIZONTAL;
        gbcd.ipadx = 10;
        gbcd.ipady = 10;
        gbcd.insets = new Insets(10,10,10,10);

        gbcd.gridx = 0;
        gbcd.gridy = 0;
        panel.add(newGame, gbcd);
        gbcd.gridx = 0;
        gbcd.gridy = 1;
        panel.add(updates, gbcd);
        gbcd.gridx = 0;
        gbcd.gridy = 2;
        panel.add(help, gbcd);

        newGame.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                new Game();
            }
        });

        help.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null,"Disa keshilla per lojen Crazy 8:\n\n" +
                "1. Luaj letren e cila perkon me numrin apo simnolin e letres ne tabele \n\n" + "2. Loja ka disa letra speciale" +
                " 8-shi ndryshon simbolin,Cika e pushon kundershtarin , 1-shi e ndryshon renditjen dhe 2-shi i shton kundershtarit 2 letra." );
            }
        });

        return panel;
    }

    //Metoda krijon levizjet e lejueshme per kompjuterin
    public Deck comValidMoves(Deck hand) {
        Deck validMoves = new Deck(0);

        for (int i = 0; i < hand.cards.size(); i++) {
            if (hand.cards.get(i).suit == currentSuit || hand.cards.get(i).rank == currentRank || hand.cards.get(i).rank == 8) {
                validMoves.cards.add(hand.cards.get(i));
            }
        }

        validMoves.cards.trimToSize();
        return validMoves;
    }

    //Metoda krjon doren e letrave per kompjuterin
    public void createComPanel() {
        comPanel = new JPanel();
        for (int i = 0; i < handsCards.get(1).size(); i++) {
            comPanel.add(handsCards.get(1).get(i));
        }
    }

    //Kjo metode perditson letrat e kompjuterit dhe inicializon levizjet e tij
    public void updateComPanel() {
        comPanel.removeAll();
        for (int i = 0; i < handsCards.get(1).size(); i++) {
            comPanel.add(handsCards.get(1).get(i));
        }
        if (canPlay(hands.get(1))) {
            Deck compValidMoves = comValidMoves(hands.get(1));
            int indexOfCard = hands.get(1).cards.indexOf(compValidMoves.cards.get((int)(Math.random()*compValidMoves.cards.size())));
            comPanel.remove(handsCards.get(1).get(handsCards.get(1).size() - 1));
            comPanel.validate();
            comPanel.repaint();
            handsCards.get(1).remove(indexOfCard);
            updates.setText("Rradha jote.");
            playCard(1, hands.get(1).cards.indexOf(hands.get(1).cards.get(indexOfCard)));
        } else {
            updates.setText("Kompjuteri terhoqi nje leter.");
            pickUp(1);
            System.out.println("(Kompjuteri terheq letren) tani: " + turn);
            turn += 1;
            System.out.println("(Kompjuteri terheq letren) rradhen tjete: " + turn);
            updateUserPanel();

        }
    }

    //Kjo metode perditson letrat e lojtarit dhe inicializon levizjet e tij
        public void createUserPanel() {
        userPanel = new JPanel();
        for (int i = 0; i < handsCards.get(0).size(); i++) {
            if (isValidMove(hands.get(0).cards.get(i))) {
                final Border border = BorderFactory.createLineBorder(Color.GREEN, 3);
                final int finalI = i;
                userPanel.add(handsCards.get(0).get(i));
                handsCards.get(0).get(i).addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        userPanel.remove(handsCards.get(0).get(finalI));
                        userPanel.validate();
                        userPanel.repaint();
                        handsCards.get(0).remove(finalI);
                        playCard(0, finalI);
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(border);
                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(null);
                    }
                });
            } else {
                userPanel.add(handsCards.get(0).get(i));
            }
        }
    }

    //Kjo metode perditson doren e letrave te lojtarit dhe inicializon levizjet e tij
    public void updateUserPanel() {
        for (int i = 0; i < handsCards.get(0).size(); i++) {
            for (int a = 0; a < handsCards.get(0).get(i).getMouseListeners().length; a++) {
                handsCards.get(0).get(i).removeMouseListener(handsCards.get(0).get(i).getMouseListeners()[a]);
            }
        }
        userPanel.removeAll();
        for (int i = 0; i < handsCards.get(0).size(); i++) {
            if (isValidMove(hands.get(0).cards.get(i))) {
                final Border border = BorderFactory.createLineBorder(Color.GREEN, 3);
                final int finalI = i;
                userPanel.add(handsCards.get(0).get(i));
                handsCards.get(0).get(i).addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        userPanel.remove(handsCards.get(0).get(finalI));
                        userPanel.validate();
                        userPanel.repaint();
                        handsCards.get(0).remove(finalI);
                        playCard(0, finalI);
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(border);
                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {
                        handsCards.get(0).get(finalI).setBorder(null);
                    }
                });
            } else {
                userPanel.add(handsCards.get(0).get(i));
            }
        }
    }

    //Kjo metode i jep simbol letres
    public static JLabel cardToLabel(Cards c) {
        JLabel cardLabel = createCard(c);
        return cardLabel;
    }

    //Kjo metode ndihmese krijon simbol prej letres
    public static JLabel createCard(Cards card) {
        ImageIcon cardIMG = createImageIcon(card, Cards.cardToString(card));
        JLabel cardL = new JLabel(cardIMG);
        return cardL;
    }

    //Metode per krijimin e imazheve te letrave
    protected static ImageIcon createImageIcon(Cards card,
                                               String description) {
        String path = "images/" + card.rank + "-" + card.suit + ".png";
        java.net.URL imgURL = Game.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

  //Kjo metode tregon shfaq duart e te gjithe lojtareve
    public void printHands() {
        for (int i = 0; i < hands.size(); i++) {
            System.out.print("Player " + (i+1) + ":\n");
            Deck.printDeck(hands.get(i));
            System.out.print("\n");
        }
    }

   //Kjo metode shfaq doren e lojtarit
    public void printPlayer(int player) {
        System.out.print("Player " + (player + 1) + ":\n");
        Deck.printDeck(hands.get(player));
    }

    //Kjo metode tregon shfaq duart dhe luan nje grumbull letrash
    public void printGame() {
        printHands();
        System.out.println("PlayPile: " + Cards.cardToString(playPile.cards.get(playPile.cards.size()-1)));
        System.out.print("\n");
    }

    // Metoda Action listener 
    public interface ActionListener {
        public void actionPerformed (ActionEvent e);
    }

    //Metoda actionPerformed
    public void actionPerformed (ActionEvent e) {
    }

    //Nis lojen
    public static void main(String[] args) {
        new Game();
    }

}