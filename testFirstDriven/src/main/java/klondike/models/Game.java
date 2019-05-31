package klondike.models;

import java.util.*;

public class Game {

	public static final int NUMBER_OF_PILES = 7;

	private Stock stock;

	private Waste waste;

	private Map<Suit, Foundation> foundations;

	private List<Pile> piles;

	public Game() {
		this.stock = new Stock();
		this.waste = new Waste();
		this.foundationsInitialization();
		this.pilesInitialization();

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
		Error error = this.moveFromWasteToFoundationEligible(suit);
		if (error != null)
			return error;
		this.foundations.get(suit).push(this.waste.pop());
		return null;
	}

	private Error moveFromWasteToFoundationEligible(Suit suit) {
		assert Game.objectIsNotNull(suit);
		if (this.waste.empty()) {
			return Error.EMPTY_WASTE;
		}
		if (!this.foundations.get(suit).fitsIn(this.waste.peek())) {
			return Error.NO_FIT_FOUNDATION;
		}
		return null;
	}

	public Error moveFromWasteToStock() {
		Error error = this.moveFromWasteToStockEligible();
		if (error != null)
			return error;
		while (!this.waste.empty()) {
			this.stock.push(this.waste.pop().flip());
		}
		return null;
	}

	private Error moveFromWasteToStockEligible() {
		if (!this.stock.empty()) {
			return Error.NO_EMPTY_STOCK;
		}
		if (this.waste.empty()) {
			return Error.EMPTY_WASTE;
		}
		return null;
	}

	public Error moveFromWasteToPile(int pileIndex) {
		Error error = this.moveFromWasteToPileEligible(pileIndex);
		if (error != null)
			return error;
		this.piles.get(pileIndex).pushFacedUp(Arrays.asList(this.waste.pop()));
		return null;
	}

	private Error moveFromWasteToPileEligible(int pileIndex) {
		assert Game.indexInsideInterval(pileIndex);
		if (this.waste.empty()) {
			return Error.EMPTY_WASTE;
		}
		if (!this.piles.get(pileIndex).fitsIn(this.waste.peek())) {
			return Error.NO_FIT_PILE;
		}
		return null;
	}

	public Error moveFromFoundationToPile(Suit suit, int pileIndex) {
		Error error = this.moveFromFoundationToPileEligible(suit, pileIndex);
		if (error != null)
			return error;
		this.piles.get(pileIndex).pushFacedUp(Arrays.asList(this.foundations.get(suit).pop()));
		return null;
	}

	private Error moveFromFoundationToPileEligible(Suit suit, int pileIndex) {
		assert Game.objectIsNotNull(suit);
		assert Game.indexInsideInterval(pileIndex);
		if (this.foundations.get(suit).empty()) {
			return Error.EMPTY_FOUNDATION;
		}
		if (!this.piles.get(pileIndex).fitsIn(this.foundations.get(suit).peek())) {
			return Error.NO_FIT_PILE;
		}
		return null;
	}

	public Error moveFromPileToFoundation(int pileIndex, Suit suit) {
		Error error = this.moveFromPileToFoundationEligible(pileIndex, suit);
		if (error != null)
			return error;
		this.foundations.get(suit).push(this.piles.get(pileIndex).peek());
		this.piles.get(pileIndex).pop();
		return null;
	}

	private Error moveFromPileToFoundationEligible(int pileIndex, Suit suit) {
		assert Game.indexInsideInterval(pileIndex);
		assert Game.objectIsNotNull(suit);
		if (this.piles.get(pileIndex).empty()) {
			return Error.EMPTY_PILE;
		}
		if (!this.foundations.get(suit).fitsIn(this.piles.get(pileIndex).peek())) {
			return Error.NO_FIT_FOUNDATION;
		}
		return null;
	}
	
	private static boolean indexInsideInterval (int index) {
		return 0 <= index && index <= Game.NUMBER_OF_PILES;
	}
	
	private static boolean objectIsNotNull(Object obj) {
		if (obj != null)
			return true;
		return false;
	}
	
	private static boolean numberAboveZero(int number) {
		return 0 < number;
	}

	public Error moveFromPileToPile(int originIndex, int destinationIndex, int numberOfCards) {
		Error error = this.pilesEligibleCheck(originIndex, destinationIndex, numberOfCards);
		if (error != null)
			return error;
		this.piles.get(originIndex).pop(numberOfCards);
		this.piles.get(destinationIndex).pushFacedUp(this.piles.get(originIndex).peek(numberOfCards));
		return null;
	}

	private Error pilesEligibleCheck(int originIndex, int destinationIndex, int numberOfCards) {
		assert Game.indexInsideInterval(originIndex);
		assert Game.indexInsideInterval(destinationIndex);
		assert Game.numberAboveZero(numberOfCards);

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

}
