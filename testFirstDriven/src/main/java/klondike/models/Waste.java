package klondike.models;

import java.util.Stack;

public class Waste {
	private Stack<Card> cards;
	
	public Waste() {
		this.cards = new Stack<Card>();
	}
	
	public boolean empty() {
        return this.cards.empty();
    }
    
    public Card peek() {
        return this.cards.peek();
    }
    
    public void push(Card card) {
    	assert ! card.isFacedUp();
        this.cards.push(card);
    }
    
    public Card pop() {
        return this.cards.pop();
    }
}
