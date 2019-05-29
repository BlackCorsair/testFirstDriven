package klondike.models;

public class Waste {
	private CardStack stack;
	
	public Waste() {
		this.stack = new CardStack();
	}
	
	public boolean empty() {
        return this.stack.cards.empty();
    }
    
    public Card peek() {
        return this.stack.cards.peek();
    }
    
    public void push(Card card) {
    	assert ! card.isFacedUp();
        this.stack.cards.push(card);
    }
    
    public Card pop() {
        return this.stack.cards.pop();
    }
}
