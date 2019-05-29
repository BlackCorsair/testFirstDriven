package klondike.models;

import org.junit.Test;

import klondike.models.builders.CardBuilder;

public class WasteTest {

	protected Waste createWasteStack() {
		return new Waste();
	}
	
	@Test(expected = AssertionError.class)
	public void testPushCardFacedDownResultsInAssertionError() {
		this.createWasteStack().push(new CardBuilder().facedUp().build());
	}
}
