package klondike.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import klondike.models.builders.CardBuilder;
import klondike.models.builders.CompleteGameBuilder;

public class GameTest {

	@Test
	public void testClear() {
		Game game = new Game();
		this.testStockClear(game);
		this.testWasteClear(game);
		this.testFoundationsClear(game);
		this.testPilesClear(game);
	}

	private void testWasteClear(Game game) {
		assertTrue(game.getWaste().empty());
	}

	private void testFoundationsClear(Game game) {
		Map<Suit, Foundation> foundations = game.getFoundations();
		for (Suit suit : Suit.values()) {
			assertTrue(foundations.get(suit).empty());
		}
	}

	private void testPilesClear(Game game) {
		List<Pile> piles = game.getPiles();
		for (int i = 0; i < piles.size(); i++) {
			Pile pile = piles.get(i);
			for (int j = 0; j < i + 1; j++) {
				Card card = pile.peek();
				pile.pop(1);
				assertTrue(card.isFacedUp());
			}
			assertTrue(pile.empty());
		}
	}

	private void testStockClear(Game game) {
		int cardsInPiles = this.cardsInStock(game);
		Stock stock = game.getStock();
		for (int i = 0; i < Number.values().length * Suit.values().length - cardsInPiles; i++) {
			Card card = stock.pop();
			assertFalse(card.isFacedUp());
		}
		assertTrue(stock.empty());
	}

	private int cardsInStock(Game game) {
		int pilesSize = game.getPiles().size();
		int cardsInPiles = 0;
		for (int i = 1; i <= pilesSize; i++) {
			cardsInPiles += i;
		}
		return cardsInPiles;
	}

	@Test
	public void testIsFinished() {
		assertTrue(new CompleteGameBuilder().build().isFinished());
	}

	@Test
	public void testMoveFromStockToWaste() {
		Game game = new Game();
		Card card = game.getStock().peek();
		assertEquals(null, game.moveFromStockToWaste());
		assertNotEquals(card, game.getStock().peek());
		assertEquals(card, game.getWaste().peek());
	}

	@Test
	public void testMoveFromStockToWasteWithError() {
		Game game = new Game();
		this.setEmptyStock(game);
		assertEquals(Error.EMPTY_STOCK, game.moveFromStockToWaste());
	}

	private void setEmptyStock(Game game) {
		Stock stock = game.getStock();
		while (!stock.empty()) {
			stock.pop();
		}
	}

	@Test
	public void testMoveFromWasteToFoundation() {
		Game game = new Game();
		Card card = this.setWaste(game, Number.AS);
		assertEquals(null, game.moveFromWasteToFoundation(card.getSuit()));
		assertEquals(card, game.getFoundations().get(card.getSuit()).peek());
		assertTrue(game.getWaste().empty());
	}

	private Card setWaste(Game game, Number number) {
		Card card = new CardBuilder().number(number).suit(Suit.PIKES).build();
		game.getWaste().push(card);
		return card;
	}

	@Test
	public void testMoveFromWasteToFoundationWithEMPTY_WASTE() {
		Game game = new Game();
		Suit suit = Suit.PIKES;
		assertEquals(Error.EMPTY_WASTE, game.moveFromWasteToFoundation(suit));
		assertTrue(game.getWaste().empty());
		assertTrue(game.getFoundations().get(suit).empty());
	}

	@Test
	public void testMoveFromWasteToFoundationWithNO_FIT_FOUNDATION() {
		Game game = new Game();
		Card card = this.setWaste(game, Number.FIVE);
		assertEquals(Error.NO_FIT_FOUNDATION, game.moveFromWasteToFoundation(card.getSuit()));
		assertEquals(card, game.getWaste().peek());
		assertTrue(game.getFoundations().get(card.getSuit()).empty());
	}

	@Test
	public void testMoveFromWasteToPileWithEmptyPile() {
		Game game = new Game();
		Card card = this.setWaste(game, Number.KING);
		this.setEmptyPile(game, 0);
		assertEquals(null, game.moveFromWasteToPile(0));
		assertEquals(card, game.getPiles().get(0).peek());
		assertTrue(game.getWaste().empty());
	}

	@Test
	public void testMoveFromWasteToPileWithEmptyWaste() {
		Game game = new Game();
		Card card = game.getPiles().get(0).peek();
		assertEquals(Error.EMPTY_WASTE, game.moveFromWasteToPile(0));
		assertEquals(card, game.getPiles().get(0).peek());
		assertTrue(game.getWaste().empty());
	}

	@Test
	public void testMoveFromWasteToPileWithEmptyWithNO_FIT_PILE() {
		Game game = new Game();
		Card card = this.setWaste(game, Number.SEVEN);
		this.setEmptyPile(game, 0);
		assertEquals(Error.NO_FIT_PILE, game.moveFromWasteToPile(0));
		assertTrue(game.getPiles().get(0).empty());
		assertEquals(card, game.getWaste().peek());
	}

	private void setEmptyPile(Game game, int position) {
		game.getPiles().get(position).pop();
	}

	@Test
	public void testMoveFromPileToFoundation() {
		Game game = new Game();
		Suit suit = Suit.HEARTS;
		Card card = this.setPile(game, 0, Number.AS, suit);
		assertEquals(null, game.moveFromPileToFoundation(0,suit ));
		assertEquals(card, game.getFoundations().get(card.getSuit()).peek());
		assertTrue(game.getWaste().empty());
	}

	private Card setPile(Game game, int position, Number number, Suit suit) {
		Pile pile = game.getPiles().get(position);
		while (!pile.empty()) {
			pile.pop();
		}
		int ordinalNumber = Number.values().length - 1;
		int ordinalSuit = 0;
		do {
			pile.pushFacedUp(new CardBuilder().number(Number.values()[ordinalNumber]).suit(Suit.values()[ordinalSuit])
					.facedUp().build());
			ordinalNumber -= 1;
			ordinalSuit = (ordinalSuit + 1) % Suit.values().length;
		} while (ordinalNumber >= number.ordinal());
		return pile.peek();
	}

}
