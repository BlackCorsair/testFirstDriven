package klondike.models;

public class Interval {
	
	private int min;
	private int max;
	
	public Interval(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public boolean isIncluded(double value) {
		return this.min <= value && value <= this.max;
	}
}
