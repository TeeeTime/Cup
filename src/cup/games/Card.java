package cup.games;

public class Card {
	
	private int value;
	
	private Symbol symbol;
	
	private boolean hidden;
	
	public Card(int value, Symbol symbol) {
		this.value = value;
		this.symbol = symbol;
		this.hidden = false;
	}
	
	public int getValue() {
		return value;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public String toString() {
		if(hidden) {
			return "H";
		}
		
		String output = value +"";
		
		switch(symbol) {
		case CLUBS: output += "C"; break;
		case DIAMONDS: output += "D"; break;
		case HEARTS: output += "H"; break;
		case SPADES: output += "S"; break;
		}
		
		return output;
	}

}

enum Symbol{
	CLUBS,
	DIAMONDS,
	HEARTS,
	SPADES
	
}