package edu.fra.uas.Team;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fra.uas.Event.EventRepository;

@Service
public class TeamService {

	@Autowired
	TeamRepository teamRep;
	@Autowired
	EventRepository eventRep;

	// Add the specific team if it does not already Exists
	public void addTeam(Team team) {

		teamRep.allTeams.add(team);
		System.out.println(team + " is added");

	}

	// check values restriction
	public boolean checkValues(Team team) {

		if (team.getBasicPower() > 0 & team.getBasicPower() <= 16 & team.getCurrentForm() >= -5
				& team.getCurrentForm() <= 5 & team.getCurrentForm() != 0) {
			System.out.println("Values got accepted!");
			return true;
		}

		return false;
	}

	// if the team exists delete it and create after this a new Team
	public void overwriteTeam(Team team) {
		for (int i = 0; i < teamRep.allTeams.size(); i++) {
			if (teamRep.allTeams.get(i).getName().equals(team.getName())) {
				teamRep.allTeams.remove(i);
				System.out.println(team.getName() + " got deleted to overwrite!");

			}
		}
		addTeam(team);
		System.out.println(team.getName() + " is overwritten!");
	}

	// Update the correct attribute
	public boolean updateTeam(String teamName, Team team) {
		for (int i = 0; i < teamRep.allTeams.size(); i++) {
			if (teamRep.allTeams.get(i).getName().equals(teamName)) {
				if (team.getName() != null) {
					System.out.println(teamRep.allTeams.get(i).getName() + " got updated: "
							+ teamRep.allTeams.get(i).getName() + " --> " + team.getName());
					teamRep.allTeams.get(i).setName(team.getName());
				}
				if (team.getBasicPower() > 0) {
					System.out.println(teamRep.allTeams.get(i).getName() + " got updated: "
							+ teamRep.allTeams.get(i).getBasicPower() + " --> " + team.getBasicPower());
					teamRep.allTeams.get(i).setBasicPower(team.getBasicPower());
				}
				if (team.getCurrentForm() != 0) {
					System.out.println(teamRep.allTeams.get(i).getName() + " got updated: "
							+ teamRep.allTeams.get(i).getCurrentForm() + " --> " + team.getCurrentForm());
					teamRep.allTeams.get(i).setCurrentForm(team.getCurrentForm());
				}

				double factor = 0;
				factor = (1 + (teamRep.allTeams.get(i).getCurrentForm() / 10));

				teamRep.allTeams.get(i).setPowerLvl(teamRep.allTeams.get(i).getBasicPower() * factor);

				System.out.println(teamRep.allTeams.get(i));
				return true;

			}
		}

		return false;
	}

	public void deleteTeam(String teamName) {

		for (int i = 0; i < teamRep.allTeams.size(); i++) {
			if (teamRep.allTeams.get(i).getName().equals(teamName)) {
				teamRep.allTeams.remove(teamRep.allTeams.get(i));
				System.out.println(teamName + " got deleted!");
			}
		}

	}

	public ArrayList<Team> getAllTeams(String sorted) {

		ArrayList<Team> allTeamsSorted = teamRep.allTeams;

		String[] sortSplit = sorted.split(" ");

		String sortedBy = sortSplit[0];
		String direction = sortSplit[1];

		if (sortedBy.equalsIgnoreCase("BASICPOWER")) {

			allTeamsSorted.sort(
					(o1, o2) -> Double.toString(o1.getBasicPower()).compareTo(Double.toString(o2.getBasicPower())));
			System.out.println(sortedBy + " sorted");
		} else if (sortedBy.equalsIgnoreCase("CURRENTFORM")) {

			allTeamsSorted.sort(
					(o1, o2) -> Double.toString(o1.getCurrentForm()).compareTo(Double.toString(o2.getCurrentForm())));
			System.out.println(sortedBy + " sorted");
		} else if (sortedBy.equalsIgnoreCase("POWERLEVEL")) {

			allTeamsSorted
					.sort((o1, o2) -> Double.toString(o1.getPowerLvl()).compareTo(Double.toString(o2.getPowerLvl())));
			System.out.println(sortedBy + " sorted !");

		}

		if (direction.equalsIgnoreCase("DESC")) {
			Collections.reverse(allTeamsSorted);
		}

		return allTeamsSorted;

	}

	public boolean teamHasAnEvent(String teamName) {

		for (int i = 0; i < eventRep.allEvents.size(); i++) {
			if (eventRep.allEvents.get(i).getTeamA().getName().equals(teamName)
					| eventRep.allEvents.get(i).getTeamB().getName().equals(teamName)) {
				System.out.println(teamName + " can not be deleted!\n"
						+ "Team participate in a Event. Please delete the Event First!");
				return true;
			}
		}

		return false;
	}

	public boolean teamExists(String teamName) {
		for (int i = 0; i < teamRep.allTeams.size(); i++) {
			if (teamRep.allTeams.get(i).getName().equals(teamName)) {
				return true;
			}
		}

		return false;
	}

}
