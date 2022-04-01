package edu.fra.uas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.fra.uas.Bet.BetService;
import edu.fra.uas.Event.EventService;
import edu.fra.uas.Team.Team;
import edu.fra.uas.Team.TeamService;
import edu.fra.uas.User.UserService;
import edu.fra.uas.security.SecurityService;

@RestController
@RequestMapping(value = "/teams")
public class TeamController {
	@Autowired
	TeamService teamService;

	@Autowired
	EventService eventService;

	@Autowired
	UserService userService;

	@Autowired
	BetService betService;

	@Autowired
	SecurityService secureService;

	// Create a team
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addTeam(@RequestBody Team team, @RequestHeader String token) {

		if (!(secureService.checkToken(token))) {
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		} else if (!(teamService.checkValues(team))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(team.getName() + " could not be created!\n" + "Please Check the following Steps:\n"
							+ "1. BasicPower should be minimum 1 and maximum 15!\n"
							+ "2. CurrentForm should be minimum -5, maximum 5 and can not be 0!");
		} else if (!(teamService.teamExists(team.getName()))) {
			teamService.addTeam(team);
			return ResponseEntity.status(HttpStatus.CREATED).body(team);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(team.getName() + " already Exists!");

	}

	// overwrite a team
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> overwriteATeam(@RequestBody Team team, @RequestHeader String token) {

		if (!(secureService.checkToken(token))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token + " is not the correct Token!");
		} else if (!(teamService.checkValues(team))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(team.getName() + " could not be created!\n" + "Please Check the following Steps:\n"
							+ "1. BasicPower should be minimum 1 and maximum 15!\n"
							+ "2. CurrentForm should be minimum -5, maximum 5 and can not be 0!");
		}
		teamService.overwriteTeam(team);

		return ResponseEntity.status(HttpStatus.OK).body(team.getName() + " is updated!");

	}

	// update an attribute of a specific team
	// check Values fehlt
	@RequestMapping(value = "/{teamName}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateATeam(@RequestBody Team team, @PathVariable String teamName,
			@RequestHeader String token) {

		if (secureService.checkToken(token)) {
//			if (!(teamService.checkValues(team))) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//						.body(team.getName() + " could not be created!\n" + "Please Check the following Steps:\n"
//								+ "1. BasicPower should be minimum 1 and maximum 15!\n"
//								+ "2. CurrentForm should be minimum -5, maximum 5 and can not be 0!");
//			}
			teamService.updateTeam(teamName, team);

			return ResponseEntity.status(HttpStatus.OK).body(team.getName() + " is updated!");
		}

		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// delete a team
	@RequestMapping(value = "/{teamName}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteATeam(@PathVariable String teamName, @RequestHeader String token) {

		if (!(secureService.checkToken(token))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token + " is not the correct Token!");
		} else if (teamService.teamExists(teamName)) {
			if (teamService.teamHasAnEvent(teamName)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(teamName + " can not be deleted!\n"
						+ "Team is participating in a Event. Please delete the Event First!");
			}
			teamService.deleteTeam(teamName);
			return ResponseEntity.status(HttpStatus.OK).body(teamName + " has been deleted!");
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" already Exists!");
	}

	// Get all Teams in ascending order
	@RequestMapping(value = "/{sorted}/asc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendAllTeamsAsc(@PathVariable String sorted) {

		if (teamService.getAllTeams(sorted + " asc").isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.status(HttpStatus.OK).body(teamService.getAllTeams(sorted + " asc"));

	}

	// Get all Teams in descending order
	@RequestMapping(value = "/{sorted}/desc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendAllTeamsDesc(@PathVariable String sorted) {

		if (teamService.getAllTeams(sorted + " desc").isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.status(HttpStatus.OK).body(teamService.getAllTeams(sorted + " desc"));

	}
}
