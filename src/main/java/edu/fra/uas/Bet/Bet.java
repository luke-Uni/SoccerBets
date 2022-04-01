package edu.fra.uas.Bet;

public class Bet {

	private static int counter = 0;

	private int id;
	private String eventName;
	private String betCreator;
	private double wager;
	private String winningTeam;

	public Bet(String eventName, String winningTeam, String betCreator, double wager) {

		this.id = ++counter;
		this.eventName = eventName;
		this.winningTeam = winningTeam;
		this.betCreator = betCreator;
		this.wager = wager;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWinningTeam() {
		return winningTeam;
	}

	public void setWinningTeam(String winningTeam) {
		this.winningTeam = winningTeam;
	}

	public String getBetCreator() {
		return betCreator;
	}

	public void setBetCreator(String betCreator) {
		this.betCreator = betCreator;
	}

	public double getWager() {
		return wager;
	}

	public void setWager(double wager) {
		this.wager = wager;
	}

	@Override
	public String toString() {
		return eventName + " Creator: " + betCreator + " Wager:" + wager + " on: " + winningTeam;
	}

}
