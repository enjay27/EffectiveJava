package rules.item58;

import java.util.*;

public class UseForEach {

    enum Suit { CLUB, DIAMOND, HEART, SPADE }

    enum Rank {ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING,}

    enum Face { ONE, TWO, THREE, FOUR, FIVE, SIX }


    static class Card {
        Suit suit;
        Rank rank;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }

        @Override
        public String toString() {
            return suit.name() + " " + rank.name();
        }
    }

    static Collection<Suit> suits = Arrays.asList(Suit.values());
    static Collection<Rank> ranks = Arrays.asList(Rank.values());

    public static void main(String[] args) {

        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }

        System.out.println("deck = " + deck);

        Collection<Face> faces = EnumSet.allOf(Face.class);
        for (Iterator<Face> i = faces.iterator(); i.hasNext(); ) {
            Face face = i.next();
            for (Iterator<Face> j = faces.iterator(); j.hasNext(); ) {
                System.out.println(face + " " + j.next());
            }
        }

    }


}
