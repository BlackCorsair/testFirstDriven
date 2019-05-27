package klondike.models;

public class Foundation {
	
	private CardStack stack;

    private Suit suit;

    public Foundation(Suit suit) {
        this.stack = new CardStack();
        this.suit = suit;
    }

    public boolean isComplete() {
        return this.stack.cards.size() == Number.values().length;
    }

    public boolean fitsIn(Card card) {
        assert card != null;
        return card.getSuit() == this.suit &&
                (card.getNumber() == Number.AS ||
                        (!this.empty() && card.isNextTo(this.peek())));
    }

    public Suit getSuit() {
        return this.suit;
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
