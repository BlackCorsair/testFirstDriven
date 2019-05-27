package klondike.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stock {

	private CardStack stack;

	public Stock() {
		this.stack = new CardStack();
		for (Suit suit : Suit.values()) {
			for (Number number : Number.values()) {
				this.stack.cards.add(new Card(suit, number));
			}
		}
		Collections.shuffle(this.stack.cards);
	}

	public List<Card> takeTop(int quantity) {
		assert 0 < quantity && quantity <= this.stack.cards.size();
		List<Card> cardsToReturn = new ArrayList<Card>(this.stack.cards.subList(0, quantity));
		this.stack.cards.removeAll(cardsToReturn);
		return cardsToReturn;
	}

	public boolean empty() {
		return this.stack.cards.empty();
	}

	public Card peek() {
		return this.stack.cards.peek();
	}

	public void push(Card card) {
		this.stack.cards.push(card);
	}

	public Card pop() {
		return this.stack.cards.pop();
	}

}
