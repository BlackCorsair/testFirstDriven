package klondike.models;

public class Waste {
	private CardStack stack;
	
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
