package edu.fra.uas.Bet;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fra.uas.Event.EventRepository;
import edu.fra.uas.Team.TeamRepository;
import edu.fra.uas.User.UserRepository;

@Service
public class BetService {

	@Autowired
	BetRepository betRep;

	@Autowired
	EventRepository eventRep;

	@Autowired
	UserRepository userRep;

	@Autowired
	TeamRepository teamRep;

	public ArrayList<Bet> getBetList() {
		return betRep.allBets;
	}

	// get all events which are related to an event to get the current bets
	public ArrayList<Bet> getAllCurrentBets() {
		ArrayList<Bet> bets = new ArrayList<>();
		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			bets.addAll(eventRep.allEvents.get(i).getBets());
		}
		return bets;

	}

	// get all bets from an User
	public ArrayList<Bet> getBetsFromUser(String username, String state) {

		ArrayList<Bet> userBets = new ArrayList<>();

		// add ALL event to the list
		if (state.equalsIgnoreCase("all")) {

			for (int i = 0; i < betRep.allBets.size(); i++) {
				if (betRep.allBets.get(i).getBetCreator().equals(username)) {
					userBets.add(betRep.allBets.get(i));
				}
			}
			// add all Events, which are CURRENT related to an Event
		} else if (state.equalsIgnoreCase("current")) {
			for (int i = 0; i < eventRep.allEvents.size(); i++) {
				for (int j = 0; j < eventRep.allEvents.get(i).getBets().size(); j++) {

					if (eventRep.allEvents.get(i).getBets().get(j).getBetCreator().equals(username)) {
						userBets.add(eventRep.allEvents.get(i).getBets().get(j));
					}

				}

			}

			// First add ALL Bets and remove all CURRENT Bets, so only the BYGONE Bets are
			// left
		} else if (state.equalsIgnoreCase("bygone")) {
			for (int i = 0; i < betRep.allBets.size(); i++) {
				if (betRep.allBets.get(i).getBetCreator().equals(username)) {
					userBets.add(betRep.allBets.get(i));
				}
			}

			for (int i = 0; i < eventRep.allEvents.size(); i++) {
				for (int j = 0; j < eventRep.allEvents.get(i).getBets().size(); j++) {

					if (eventRep.allEvents.get(i).getBets().get(j).getBetCreator().equals(username)) {
						userBets.remove(eventRep.allEvents.get(i).getBets().get(j));
					}

				}
			}
		}

		return userBets;

	}

	// get all current bets from an User

	// get all Bygone bets from an User

	// delete the bet in the Event and not in the allBets list
	public void retractBet(Bet bet) {
		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			if (eventRep.allEvents.get(i).getEventName().equals(bet.getEventName())) {
				for (int j = 0; j < eventRep.allEvents.get(i).getBets().size(); j++) {
					if (eventRep.allEvents.get(i).getBets().get(j).getBetCreator().equals(bet.getBetCreator())) {
						eventRep.allEvents.get(i).getBets().remove(j);
						System.out.println(bet + " has been deleted!");
					}
				}

			}
		}
	}

	public void refundMoney(Bet bet) {

		for (int i = 0; i < userRep.allUser.size(); i++) {

			if (userRep.allUser.get(i).getUsername().equals(bet.getBetCreator())) {
				userRep.allUser.get(i).setBalance(userRep.allUser.get(i).getBalance() + bet.getWager());
			}
		}

	}

	// Delete old Bet and add the new one to overwrite it in the related Event
	public void overWriteBet(Bet bet) {
		int id = 0;
		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			if (eventRep.allEvents.get(i).getEventName().equals(bet.getEventName())) {
				for (int j = 0; j < eventRep.allEvents.get(i).getBets().size(); j++) {
					if (eventRep.allEvents.get(i).getBets().get(j).getBetCreator().equals(bet.getBetCreator())) {
						id = eventRep.allEvents.get(i).getBets().get(j).getId();
						refundMoney(bet);
						eventRep.allEvents.get(i).getBets().remove(j);
						System.out.println("Bet has been Removed!");
						eventRep.allEvents.get(i).getBets().add(bet);
						eventRep.allEvents.get(i).getBets().get(j).setId(id);
						withdrawMoney(bet.getBetCreator(), bet.getWager());
						System.out.println(bet + " has been Patched!");
					}
				}
			}
		}
	}

	public void makeAbet(Bet bet) {

		// The Bet is saved in the matching Event
		for (int i = 0; i < eventRep.allEvents.size(); i++) {

			if (eventRep.allEvents.get(i).getEventName().equals(bet.getEventName())) {
				eventRep.allEvents.get(i).addBetToEvent(bet);
				System.out.println("Bet got added to Event");
				withdrawMoney(bet.getBetCreator(), bet.getWager());
			}
		}

		// The Bet is added to the list of all Bets
		betRep.allBets.add(bet);

	}

	public void withdrawMoney(String name, double wager) {

		for (int i = 0; i < userRep.allUser.size(); i++) {
			if (userRep.allUser.get(i).getUsername().equals(name)) {
				userRep.allUser.get(i).setBalance(userRep.allUser.get(i).getBalance() - wager);
			}
		}
	}

	public boolean betDataAccepted(Bet bet) {
		if (!(betExistsOnEvent(bet.getBetCreator(), bet.getEventName())) && userExists(bet.getBetCreator())) {
			return true;
		}

		return false;
	}

	// check if the User entered event name exists
	public boolean eventExists(String eventName) {

		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			if (eventRep.allEvents.get(i).getEventName().equalsIgnoreCase(eventName)) {
				return true;
			}
		}

		return false;
	}

	// check if the User entered winner Team exists on the Event
	public boolean winnerExists(String eventName, String winnerTeam) {

		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			if (eventRep.allEvents.get(i).getEventName().equals(eventName)) {
				if (eventRep.allEvents.get(i).getTeamA().getName().equalsIgnoreCase(winnerTeam)
						|| eventRep.allEvents.get(i).getTeamB().getName().equalsIgnoreCase(winnerTeam)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean betExistsOnEvent(String creator, String eventName) {

		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			if (eventRep.allEvents.get(i).getEventName().equals(eventName)) {
				for (int j = 0; j < eventRep.allEvents.get(i).getBets().size(); j++) {
					if (eventRep.allEvents.get(i).getBets().get(j).getBetCreator().equals(creator)) {
						return true;
					}
				}

			}
		}

		return false;
	}

	public boolean userExists(String creator) {

		for (int i = 0; i < userRep.allUser.size(); i++) {
			if (userRep.allUser.get(i).getUsername().equals(creator)) {
				return true;
			}
		}

		return false;
	}

}
