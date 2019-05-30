package klondike.models;

import java.util.*;

public class Game extends ActionChecker {

    public static final int NUMBER_OF_PILES = 7;

    public Game() {
    	super();
        this.notEligible = null;
    	this.stock = new Stock();
        this.waste = new Waste();
        this.foundationsInitialization();
        this.pilesInitialization();
        
    }

    public boolean isFinished() {
        for (Suit suit : Suit.values()) {
            if (!this.foundations.get(suit).isComplete()) {
                return false;
            }
        }
        return true;
    }

    public Error moveFromStockToWaste() {
        if (this.stock.empty()) {
            return Error.EMPTY_STOCK;
        }
        if (this.stock.peek().isFacedUp()) {
        	this.waste.push(this.stock.pop().flip());
        } else {
        	this.waste.push(this.stock.pop());
        }
        return null;
    }

    public Error moveFromWasteToFoundation(Suit suit) {
        if ((this.notEligible = this.moveFromWasteToFoundationEligible(suit)) != null) return notEligible;
        this.foundations.get(suit).push(this.waste.pop());
        return null;
    }

    public Error moveFromWasteToStock() {
        if ((this.notEligible = this.moveFromWasteToStockEligible()) != null) return notEligible;
        while (!this.waste.empty()) {
            this.stock.push(this.waste.pop().flip());
        }
        return null;
    }

    public Error moveFromWasteToPile(int pileIndex) {
        if ((this.notEligible = this.moveFromWasteToPileEligible(pileIndex)) != null) return notEligible;
        this.piles.get(pileIndex).push(Arrays.asList(this.waste.pop()));
        return null;
    }

    public Error moveFromFoundationToPile(Suit suit, int pileIndex) {
        if ((this.notEligible = this.moveFromFoundationToPileEligible(suit, pileIndex)) != null) return notEligible;
        this.piles.get(pileIndex).push(Arrays.asList(this.foundations.get(suit).pop()));
        return null;
    }

    public Error moveFromPileToFoundation(int pileIndex, Suit suit) {
        if ((this.notEligible = this.moveFromPileToFoundationEligible(pileIndex, suit)) != null) return notEligible;
        this.foundations.get(suit).push(this.piles.get(pileIndex).peek());
        this.piles.get(pileIndex).pop();
        return null;
    }
    
    public Error moveFromPileToPile(int originIndex, int destinationIndex, int numberOfCards) {
        if( (this.notEligible = this.pilesEligibleCheck(originIndex, destinationIndex, numberOfCards)) != null) return notEligible;
        this.piles.get(originIndex).pop(numberOfCards);
        this.piles.get(destinationIndex).push(this.piles.get(originIndex).peek(numberOfCards));
        return null;
    }

    public Stock getStock() {
        return this.stock;
    }

    public Waste getWaste() {
        return this.waste;
    }

    public Map<Suit, Foundation> getFoundations() {
        return foundations;
    }

    public List<Pile> getPiles() {
        return piles;
    }

    private void foundationsInitialization() {
        this.foundations = new HashMap<Suit, Foundation>();
        for (Suit suit : Suit.values()) {
            this.foundations.put(suit, new Foundation(suit));
        }
    }
    
    private void pilesInitialization() {
        this.piles = new ArrayList<Pile>();
        for (int i = 0; i < Game.NUMBER_OF_PILES; i++) {
            this.piles.add(new Pile(i + 1, this.stock.pop(i + 1)));
        }
    }
}
