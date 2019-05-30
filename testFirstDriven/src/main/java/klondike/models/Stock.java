package klondike.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Stock {

	private Stack<Card> cards;

	public Stock() {
		this.cards = new Stack<Card>();
		for (Suit suit : Suit.values()) {
			for (Number number : Number.values()) {
				this.cards.add(new Card(suit, number));
			}
		}
		Collections.shuffle(this.cards);
	}

	public List<Card> pop(int quantity) {
		assert 0 < quantity && quantity <= this.cards.size();
		List<Card> cardsToReturn = new ArrayList<Card>(this.cards.subList(0, quantity));
		this.cards.removeAll(cardsToReturn);
		return cardsToReturn;
	}

	public boolean empty() {
		return this.cards.empty();
	}

	public Card peek() {
		return this.cards.peek();
	}

	public void push(Card card) {
		this.cards.push(card);
	}

	public Card pop() {
		return this.cards.pop();
	}

}
