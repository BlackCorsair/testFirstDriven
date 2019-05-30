package klondike.models;

import java.util.*;

public class Game {

    public static final int NUMBER_OF_PILES = 7;

    private Error notEligible;

    private Stock stock;

    private Waste waste;

    private Map<Suit, Foundation> foundations;

    private List<Pile> piles;

    public Game() {
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
        this.piles.get(pileIndex).removeTop(1);
        return null;
    }
    
    public Error moveFromPileToPile(int originIndex, int destinationIndex, int numberOfCards) {
        if( (this.notEligible = this.pilesEligibleCheck(originIndex, destinationIndex, numberOfCards)) != null) return notEligible;
        this.piles.get(originIndex).removeTop(numberOfCards);
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

    private Error moveFromWasteToStockEligible() {
        if (!this.stock.empty()) {
            return Error.NO_EMPTY_STOCK;
        }
        if (this.waste.empty()) {
            return Error.EMPTY_WASTE;
        }
        return this.notEligible = null;
    }

    private Error moveFromWasteToFoundationEligible(Suit suit) {
        assert suit != null;
        if (this.waste.empty()) {
            return Error.EMPTY_WASTE;
        }
        if (!this.foundations.get(suit).fitsIn(this.waste.peek())) {
            return Error.NO_FIT_FOUNDATION;
        }
        return this.notEligible = null;
    }

    private Error moveFromWasteToPileEligible(int pileIndex) {
        assert (0 <= pileIndex) && (pileIndex <= Game.NUMBER_OF_PILES);
        if (this.waste.empty()) {
            return Error.EMPTY_WASTE;
        }
        if (!this.piles.get(pileIndex).fitsIn(this.waste.peek())) {
            return Error.NO_FIT_PILE;
        }
        return this.notEligible = null;
    }

    private Error moveFromFoundationToPileEligible(Suit suit, int pileIndex) {
        assert suit != null;
        assert (0 <= pileIndex) && (pileIndex <= Game.NUMBER_OF_PILES);
        if (this.foundations.get(suit).empty()) {
            return Error.EMPTY_FOUNDATION;
        }
        if (!this.piles.get(pileIndex).fitsIn(this.foundations.get(suit).peek())) {
            return Error.NO_FIT_PILE;
        }
        return this.notEligible = null;
    }

    private Error moveFromPileToFoundationEligible(int pileIndex, Suit suit) {
    	assert (0 <= pileIndex) && (pileIndex <= Game.NUMBER_OF_PILES);
        assert suit != null;
        if (this.piles.get(pileIndex).empty()) {
            return Error.EMPTY_PILE;
        }
        if (!this.foundations.get(suit).fitsIn(this.piles.get(pileIndex).peek())) {
            return Error.NO_FIT_FOUNDATION;
        }
        return this.notEligible = null;
    }

    private Error pilesEligibleCheck(int originIndex, int destinationIndex, int numberOfCards) {
    	assert (0 <= originIndex) && (originIndex <= Game.NUMBER_OF_PILES);
        assert (0 <= destinationIndex) && (destinationIndex <= Game.NUMBER_OF_PILES);
        assert (0 <= numberOfCards);
        
        if (originIndex == destinationIndex) {
            return Error.SAME_PILE;
        }
        if (this.piles.get(originIndex).numberOfFaceUpCards() < numberOfCards) {
            return Error.NO_ENOUGH_CARDS_PILE;
        }
        List<Card> cards = this.piles.get(originIndex).peek(numberOfCards);
        if (!this.piles.get(destinationIndex).fitsIn(cards.get(cards.size() - 1))) {
            return Error.NO_FIT_PILE;
        }
        
        return this.notEligible = null;
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
            this.piles.add(new Pile(i + 1, this.stock.takeTop(i + 1)));
        }
    }
}
