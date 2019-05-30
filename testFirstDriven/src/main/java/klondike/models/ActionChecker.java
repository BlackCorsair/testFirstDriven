package klondike.models;

import java.util.List;
import java.util.Map;

public class ActionChecker {

	protected Error notEligible;

	protected Stock stock;

	protected Waste waste;

	protected Map<Suit, Foundation> foundations;

	protected List<Pile> piles;
    
    public ActionChecker()
    {}

	protected Error moveFromWasteToStockEligible() {
        if (!this.stock.empty()) {
            return Error.NO_EMPTY_STOCK;
        }
        if (this.waste.empty()) {
            return Error.EMPTY_WASTE;
        }
        return this.notEligible = null;
    }

	protected Error moveFromWasteToFoundationEligible(Suit suit) {
        assert suit != null;
        if (this.waste.empty()) {
            return Error.EMPTY_WASTE;
        }
        if (!this.foundations.get(suit).fitsIn(this.waste.peek())) {
            return Error.NO_FIT_FOUNDATION;
        }
        return this.notEligible = null;
    }

	protected Error moveFromWasteToPileEligible(int pileIndex) {
        assert (0 <= pileIndex) && (pileIndex <= Game.NUMBER_OF_PILES);
        if (this.waste.empty()) {
            return Error.EMPTY_WASTE;
        }
        if (!this.piles.get(pileIndex).fitsIn(this.waste.peek())) {
            return Error.NO_FIT_PILE;
        }
        return this.notEligible = null;
    }

	protected Error moveFromFoundationToPileEligible(Suit suit, int pileIndex) {
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

	protected Error moveFromPileToFoundationEligible(int pileIndex, Suit suit) {
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

	protected Error pilesEligibleCheck(int originIndex, int destinationIndex, int numberOfCards) {
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
}
