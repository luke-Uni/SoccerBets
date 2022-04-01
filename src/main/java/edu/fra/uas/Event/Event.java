package edu.fra.uas.Event;

import java.util.ArrayList;

import edu.fra.uas.Bet.Bet;
import edu.fra.uas.Team.Team;

public class Event {

	private String eventName;

	private Team teamA;

	private Team teamB;

	private ArrayList<Bet> bets;

	private Team winner;

	public Event() {
	};

	public Event(Team teamA, Team teamB) {

		this.eventName = teamA.getName() + " vs. " + teamB.getName();
		this.teamA = teamA;
		this.teamB = teamB;
		this.winner = null;
		bets = new ArrayList<>();
	}

	public void addBetToEvent(Bet bet) {
		bets.add(bet);
	}

	public ArrayList<Bet> getBets() {
		return bets;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Team getTeamA() {
		return teamA;
	}

	public void setTeamA(Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamB() {
		return teamB;
	}

	public void setTeamB(Team teamB) {
		this.teamB = teamB;
	}

	public Team getWinner() {
		return winner;
	}

	public void setWinner(Team winner) {
		this.winner = winner;
	}

	@Override
	public String toString() {
		return "Event: " + eventName;
	}

}
