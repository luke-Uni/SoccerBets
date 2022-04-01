package edu.fra.uas.Event;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fra.uas.Bet.Bet;
import edu.fra.uas.Bet.BetRepository;
import edu.fra.uas.Team.Team;
import edu.fra.uas.Team.TeamRepository;
import edu.fra.uas.User.UserRepository;

@Service
public class EventService {

	@Autowired
	EventRepository eventRep;

	@Autowired
	TeamRepository teamRep;

	@Autowired
	BetRepository betRep;

	@Autowired
	UserRepository userRep;

	// Create the Event
	public void addEvent(String a, String b) {

		Event event = new Event(getTeambyName(a), getTeambyName(b));
		eventRep.allEvents.add(event);
		System.out.println(eventRep.allEvents + " one got added!");

	}

	// delete a Event
	public void deleteEvent(String teamA, String teamB) {

		String eventName = teamA + " vs. " + teamB;

		for (int i = 0; i < eventRep.allEvents.size(); i++) {

			System.out.println(eventRep.allEvents.get(i).getEventName() + ":" + eventName);
			if (eventRep.allEvents.get(i).getEventName().equals(eventName)) {
				eventRep.allEvents.remove(i);
				System.out.println("Event has been deleted!");
			}

		}

	}

	// Get all Events from Repository
	public ArrayList<Event> getEvents() {
		return eventRep.allEvents;
	}

	// get an specific Event
	public Event getEvent(String teamA, String teamB) {

		String eventName = teamA + " vs. " + teamB;

		for (int i = 0; i < eventRep.allEvents.size(); i++) {

			if (eventRep.allEvents.get(i).getEventName().equals(eventName)) {
				System.out.println("Event has been found!");
				return eventRep.allEvents.get(i);
			}

		}

		return null;
	}

	// Check if Event already exists
	public boolean eventExists(String nameA, String nameB) {

		String eventName = nameA + " vs. " + nameB;

		for (int i = 0; i < eventRep.allEvents.size(); i++) {

			if (eventRep.allEvents.get(i).getEventName().equals(eventName)) {
				System.out.println("Event already Exists!");
				return true;
			}

		}

		return false;
	}

	public Team getTeambyName(String teamName) {

		for (int i = 0; i < teamRep.allTeams.size(); i++) {

			if (teamRep.allTeams.get(i).getName().equals(teamName)) {
				System.out.println("Team exists");
				return teamRep.allTeams.get(i);
			}

		}

		System.out.println("Team not found!");

		return null;
	}

	public boolean teamToEventExists(String a, String b) {

		if (getTeambyName(a) == null || getTeambyName(b) == null) {
			return false;
		}

		return true;

	}

	public Event findEventByName(String EventName) {

		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			if (eventRep.allEvents.get(i).getEventName().equals(EventName)) {
				return eventRep.allEvents.get(i);
			}
		}

		System.out.println("Event existiert nicht!");
		return null;

	}

	public void startEvent(String eventName) {

		Event event;

		event = findEventByName(eventName);

		Team firstTeam = event.getTeamA();
		int goalsA = 0;
		int shootsA = countShoots(firstTeam);
		goalsA = shootGoals(shootsA);

		Team secondTeam = event.getTeamB();
		int goalsB = 0;
		int shootsB = countShoots(secondTeam);
		goalsB = shootGoals(shootsB);

		Team champ;

		if (goalsA > goalsB) {
			champ = firstTeam;
			System.out.println(firstTeam.getName() + " has won against " + secondTeam.getName() + " FinalScore: "
					+ goalsA + ":" + goalsB);
		} else if (goalsA < goalsB) {
			champ = secondTeam;
			System.out.println(secondTeam.getName() + " has won against " + firstTeam.getName() + " FinalScore: "
					+ goalsB + ":" + goalsA);
		} else {
			champ = null;
			System.out.println(firstTeam.getName() + " played draw against " + secondTeam.getName() + " FinalScore: "
					+ goalsA + ":" + goalsB);
		}

		for (int j = 0; j < eventRep.allEvents.size(); j++) {

			if (eventRep.allEvents.get(j).getEventName().equals(event.getEventName())) {

				eventRep.allEvents.get(j).getBets().removeAll(eventRep.allEvents.get(j).getBets());
				eventRep.allEvents.get(j).setWinner(champ);

			}

		}

		System.out.println("All Bets on " + event.getEventName() + " got Deleted!");

		for (int i = 0; i < betRep.allBets.size(); i++) {

			if (betRep.allBets.get(i).getEventName().equals(event.getEventName())) {

				if (betRep.allBets.get(i).getWinningTeam().equals(champ.getName())) {
					payout(betRep.allBets.get(i).getBetCreator(), betRep.allBets.get(i).getWager());

				}

			}

		}

	}

	public Team getWinner(String eventName) {

		Event event;
		event = findEventByName(eventName);

		return event.getWinner();

	}

	public ArrayList<Bet> getBets(String name) {

		for (int i = 0; i < eventRep.allEvents.size(); i++) {

			if (eventRep.allEvents.get(i).getEventName().equals(name)) {
				return eventRep.allEvents.get(i).getBets();
			}

		}

		return null;

	}

	public void payout(String winnerName, double wager) {

		for (int i = 0; i < userRep.allUser.size(); i++) {

			if (userRep.allUser.get(i).getUsername().equals(winnerName)) {
				userRep.allUser.get(i).setBalance(userRep.allUser.get(i).getBalance() + payoutCalculation(wager));
				System.out.println(userRep.allUser.get(i).getUsername() + " got: " + payoutCalculation(wager)
						+ "current Balance= " + userRep.allUser.get(i).getBalance());
			}

		}

	}

	public double payoutCalculation(double wager) {

		double tax = 0.95;
		double winFactor = 2;
		double profit = 0;

		profit = wager * winFactor * tax;

		return profit;
	}

	public int shootGoals(int shoots) {
		int randomNumber = 0;

		int goals = 0;

		for (int i = 0; i < shoots; i++) {

			randomNumber = ThreadLocalRandom.current().nextInt(1, 3);
			if (randomNumber == 1) {
				goals++;
			}

		}

		return goals;
	}

	public int countShoots(Team team) {

		if (team.getPowerLvl() == 15) {
			System.out.println(team.getName() + " got " + 30);
			return 30;
		} else if (team.getPowerLvl() >= 14) {
			System.out.println(team.getName() + " got " + 28);
			return 28;
		} else if (team.getPowerLvl() >= 12) {
			System.out.println(team.getName() + " got " + 24);
			return 24;
		} else if (team.getPowerLvl() > 10) {
			System.out.println(team.getName() + " got " + 22);
			return 22;
		} else if (team.getPowerLvl() == 10) {
			System.out.println(team.getName() + " got " + 20);
			return 20;
		} else if (team.getPowerLvl() > 8) {
			System.out.println(team.getName() + " got " + 18);
			return 18;
		} else if (team.getPowerLvl() >= 6) {
			System.out.println(team.getName() + " got " + 16);
			return 16;
		} else if (team.getPowerLvl() > 5) {
			System.out.println(team.getName() + " got " + 14);
			return 14;
		} else if (team.getPowerLvl() == 5) {
			System.out.println(team.getName() + " got " + 12);
			return 12;
		} else if (team.getPowerLvl() >= 4) {
			System.out.println(team.getName() + " got " + 10);
			return 10;
		} else if (team.getPowerLvl() >= 3) {
			System.out.println(team.getName() + " got " + 8);
			return 8;
		} else if (team.getPowerLvl() >= 1) {
			System.out.println(team.getName() + " got " + 6);
			return 6;
		} else if (team.getPowerLvl() > 0.5) {
			System.out.println(team.getName() + " got " + 4);
			return 4;
		} else if (team.getPowerLvl() == 0.5) {
			System.out.println(team.getName() + " got " + 2);
			return 2;
		}

		System.out.println("---------------> Shoots could not be Counted!");

		return -1;

	}

}
