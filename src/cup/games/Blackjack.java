package cup.games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Blackjack {
	
	private Queue<Card> cardQueue;
	
	private ArrayList<Card> dealerCards;
	
	private ArrayList<Card> playerCards;
	
	private GameState gameState;
	
	int bet;
	
	public Blackjack(int bet) {
		this.bet = bet;
		
		gameState = GameState.PLAYING;
		
		cardQueue = getQueue(shuffleDeck(generateDeck()));
		dealerCards = new ArrayList<Card>();
		playerCards = new ArrayList<Card>();
		
		playerCards.add(cardQueue.poll());
		dealerCards.add(cardQueue.poll());
		playerCards.add(cardQueue.poll());
		Card hiddenDealercard = cardQueue.poll();
		hiddenDealercard.setHidden(true);
		dealerCards.add(hiddenDealercard);
		
		
		
		updateGameState();
	}
	
	public ArrayList<Card> getDealerCards(){
		return dealerCards;
	}
	
	public ArrayList<Card> getPlayerCards(){
		return playerCards;
	}
	
	public int getDealerValue() {
		return getCardValue(dealerCards);
	}
	
	public int getPlayerValue() {
		return getCardValue(playerCards);
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public int getBet() {
		return bet;
	}
	
	public void hit() {
		gameState = GameState.PLAYING;
		playerCards.add(cardQueue.poll());
		
		updateGameState();
	}
	
	public void stand() {
		gameState = GameState.PLAYING;
		dealerCards.get(1).setHidden(false);
		
		while(getDealerValue() < 17) {
			dealerCards.add(cardQueue.poll());
		}
		
		gameState = GameState.END;
		
		updateGameState();
	}
	
	private int getCardValue(List<Card> cards) {
		int total = 0;
		int hiddenValue = 0;
		
		int aces = 0;
		for(Card card : cards) {
			int value = card.getValue();
			
			if(value > 10) {
				value = 10;
			}
			if(value == 1) {
				value = 11;
				aces++;
			}
			
			if(card.isHidden()) {
				hiddenValue += value;
				continue;
			}
			

			total += value;
			
		}
		
		if(hiddenValue > 0 && (total + hiddenValue) == 21) {
			total += hiddenValue;
		}
		
		for(int i = 0; i < aces; i++) {
			if(total <= 21) {
				break;
			}
			
			total -= 10;
		}
		
		
		return total;
	}
	
	public boolean isPlaying() {
		if(gameState == GameState.PLAYING) {
			return true;
		}else {
			return false;
		}
	}
	
	public void updateGameState() {
		if(playerCards.size() == 2 && dealerCards.size() == 2 && gameState != GameState.END) {
			if(getDealerValue() == 21) {
				System.out.println("DEALER START BJ");
				dealerCards.get(1).setHidden(false);
				if(getPlayerValue() == 21) {
					gameState = GameState.TIE;
					System.out.println("START TIE");
					dealerCards.get(1).setHidden(false);
				}else {
					gameState = GameState.DEALERWIN;
					System.out.println("DEALER START WIN");
					dealerCards.get(1).setHidden(false);
				}
			}else if(getPlayerValue() == 21 && getDealerValue() != 21) {
				gameState = GameState.INSTANTBLACKJACK;
				System.out.println("PLAYER START BJ WIN");
				dealerCards.get(1).setHidden(false);
			}
			
		}else if(gameState == GameState.PLAYING) {
			if(getPlayerValue() == 21) {
				gameState = GameState.PLAYERWIN;
				dealerCards.get(1).setHidden(false);
			}else if(getPlayerValue() > 21) {
				gameState = GameState.DEALERWIN;
				dealerCards.get(1).setHidden(false);
			}
			
		}else if(gameState == GameState.END) {
			if(getDealerValue() > 21) {
				gameState = GameState.PLAYERWIN;
				dealerCards.get(1).setHidden(false);
			}else if(getDealerValue() > getPlayerValue()) {
				gameState = GameState.DEALERWIN;
				dealerCards.get(1).setHidden(false);
			}else if(getDealerValue() == getPlayerValue()) {
				gameState = GameState.TIE;
				dealerCards.get(1).setHidden(false);
			}else {
				gameState = GameState.PLAYERWIN;
				dealerCards.get(1).setHidden(false);
			}
		}
	}
	
	private Queue<Card> getQueue(ArrayList<Card> deck) {
		Queue<Card> queue = new LinkedList<>();
		for(Card card : deck) {
			queue.add(card);
		}
		
		return queue;
	}
	
	private ArrayList<Card> shuffleDeck(ArrayList<Card> deck){
		Random random = new Random();
		
		for(int i = 0; i < deck.size(); i++) {
			int newIndex = random.nextInt(deck.size());
			Card card = deck.get(i);
			deck.set(i, deck.get(newIndex));
			deck.set(newIndex, card);
		}
		
		return deck;
	}
	
	private ArrayList<Card> generateDeck(){
		ArrayList<Card> deck = new ArrayList<Card>();
		
		Symbol[] symbols = Symbol.values();
		
		for(int i = 1; i <= 13; i++) {
			for(int j = 0; j < symbols.length; j++) {
				deck.add(new Card(i, symbols[j]));
			}
		}
		return deck;
	}
}