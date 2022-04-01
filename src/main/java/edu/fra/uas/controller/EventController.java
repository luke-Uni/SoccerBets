package edu.fra.uas.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.fra.uas.Bet.Bet;
import edu.fra.uas.Bet.BetService;
import edu.fra.uas.Event.EventService;
import edu.fra.uas.Team.Team;
import edu.fra.uas.Team.TeamService;
import edu.fra.uas.User.UserService;
import edu.fra.uas.security.SecurityService;

@RestController
@RequestMapping(value = "/events")
public class EventController {

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

	// Create an Event
	@RequestMapping(value = "/{teamA}/{teamB}", method = RequestMethod.POST)
	public ResponseEntity<?> addAnEvent(@PathVariable String teamA, @PathVariable String teamB,
			@RequestHeader String token) {

		if (!(secureService.checkToken(token))) {
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
		if (!eventService.teamToEventExists(teamA, teamB)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Atleast one of the Teams does not Exists");
		}

		if (eventService.eventExists(teamA, teamB)) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event already Exists!");

		}

		eventService.addEvent(teamA, teamB);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	// Delete an Event
	@RequestMapping(value = "/{teamA}/{teamB}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAnEvent(@PathVariable String teamA, @PathVariable String teamB,
			@RequestHeader String token) {

		if (secureService.checkToken(token)) {

			if (eventService.eventExists(teamA, teamB)) {
				eventService.deleteEvent(teamA, teamB);
				return ResponseEntity.status(HttpStatus.OK).body("Event has been deleted!");

			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event does not Exists!");
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Get all Events
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendAllEvents() {

		if (eventService.getEvents().size() == 0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There are no events!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(eventService.getEvents());

		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event does not
		// Exists!");
	}

	// Get an specific Event
	@RequestMapping(value = "/{teamA}/{teamB}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendAspecificEvent(@PathVariable String teamA, @PathVariable String teamB) {

		if (eventService.eventExists(teamA, teamB)) {

			return ResponseEntity.status(HttpStatus.OK).body(eventService.getEvent(teamA, teamB));
		}
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

	}

	// Simulate the event and send back the winner
	@RequestMapping(value = "/{eventName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> startTheEvent(@PathVariable String eventName, @RequestHeader String token) {

		if (secureService.checkToken(token)) {
			Team winner;

			eventService.startEvent(eventName);

			winner = eventService.getWinner(eventName);

			return ResponseEntity.status(HttpStatus.OK).body(winner);

		}

		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(value = "/{eventName}/winner", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getWinner(@PathVariable String eventName, @RequestHeader String token) {

		return ResponseEntity.status(HttpStatus.OK).body(eventService.getWinner(eventName));

	}

	// Get all Bets from an Event
	@RequestMapping(value = "/{eventName}/bets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBetsFromEvent(@PathVariable String eventName, @RequestHeader String token) {

		if (secureService.checkToken(token)) {
			ArrayList<Bet> betList = new ArrayList<>();

			betList = eventService.getBets(eventName);

			return ResponseEntity.status(HttpStatus.OK).body(betList);
		}

		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

}
