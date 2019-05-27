package klondike.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Pile {

	private CardStack stack;

	private final int number;

	private int numberOfFaceUpCards;

	public Pile(int number, List<Card> cards) {
		assert cards.size() > 0;
		this.stack = new CardStack();
		this.number = number;
		this.numberOfFaceUpCards = 0;
		this.stack.cards.addAll(cards);
		this.flipFirstCard();
	}

	public void push(Card card) {
		assert this.fitsIn(card);
		this.stack.push(card);
		this.numberOfFaceUpCards++;
	}

	public Card pop() {
		this.numberOfFaceUpCards--;
		return this.stack.pop();
	}

	private void flipFirstCard() {
		assert !this.stack.cards.empty() && this.numberOfFaceUpCards == 0 && !this.stack.cards.peek().isFacedUp();
		this.stack.cards.peek().flip();
		this.numberOfFaceUpCards++;
	}

	public boolean fitsIn(Card card) {
		assert card != null;
		return (this.stack.cards.empty() && card.getNumber() == Number.KING) || (!this.stack.cards.empty()
				&& this.stack.cards.peek().isNextTo(card) && this.stack.cards.peek().getColor() != card.getColor());
	}

	public List<Card> getTop(int numberOfCards) {
		assert numberOfCards <= this.numberOfFaceUpCards;
		return new ArrayList<Card>(
				this.stack.cards.subList(this.stack.cards.size() - numberOfCards, this.stack.cards.size()));
	}

	public void addToTop(List<Card> cards) {
		assert cards != null;
		this.stack.cards.addAll(cards);
		this.numberOfFaceUpCards += cards.size();
	}

	public void removeTop(int numberOfCards) {
		assert numberOfCards <= this.numberOfFaceUpCards;
		for (int i = 0; i < numberOfCards; i++) {
			this.stack.cards.pop();
			numberOfFaceUpCards--;
		}
		if (this.numberOfFaceUpCards == 0 && !this.stack.cards.empty()) {
			flipFirstCard();
		}
	}

	public int numberOfFaceUpCards() {
		return this.numberOfFaceUpCards;
	}

	public boolean empty() {
		return this.stack.cards.empty();
	}

	public Stack<Card> getCards() {
		return this.stack.cards;
	}

	public int getNumber() {
		return this.number;
	}
	
	public Card peek() {
        return this.stack.cards.peek();
    }
}
