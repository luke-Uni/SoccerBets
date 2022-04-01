package edu.fra.uas.controller;

import java.util.ArrayList;

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

import edu.fra.uas.Bet.Bet;
import edu.fra.uas.Bet.BetService;
import edu.fra.uas.Event.EventService;
import edu.fra.uas.Team.TeamService;
import edu.fra.uas.User.UserService;
import edu.fra.uas.security.SecurityService;

@RestController
@RequestMapping(value = "/bets")
public class BetController {

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

	// Create a bet
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addBet(@RequestBody Bet bet, @RequestHeader String token) {

		if (secureService.checkToken(bet.getBetCreator(), token)) {
			if (!(betService.eventExists(bet.getEventName()))) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event does not Exist!");
			}
			if (!(betService.winnerExists(bet.getEventName(), bet.getWinningTeam()))) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Select a matching Team!");
			}

			if (betService.betDataAccepted(bet)) {
				betService.makeAbet(bet);
				return ResponseEntity.status(HttpStatus.OK).body("Bet got added");
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Problems that could triggered this Bad_Request \n" + "1. The Betcreator does not Exists.\n"
							+ "2. You already placed your Bet on this Event");

		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Get all Bets
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllBets(@RequestHeader String token) {

		if (secureService.checkToken(token)) {
			ArrayList<Bet> betList = new ArrayList<>();

			betList = betService.getBetList();

			return ResponseEntity.status(HttpStatus.OK).body(betList);
		}

		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Get all current Bets
	@RequestMapping(value = "/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCurrentBets(@RequestHeader String token) {

		if (secureService.checkToken(token)) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(betService.getAllCurrentBets());
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);

	}

	// Get all/current/bygone Bets from a specific User
	@RequestMapping(value = "/{userName}/{state}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllBetsFromAnUser(@RequestHeader String token, @PathVariable String userName,
			@PathVariable String state) {

		if (secureService.checkToken(userName, token)) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(betService.getBetsFromUser(userName, state));
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// retract a specific bet
	@RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retractABet(@RequestBody Bet bet, @RequestHeader String token) {

		if (secureService.checkToken(bet.getBetCreator(), token)) {

			betService.retractBet(bet);

			return ResponseEntity.status(HttpStatus.OK).body(bet);
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Overwrite a specific bet
	@RequestMapping(method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> overWrtiteABet(@RequestBody Bet bet, @RequestHeader String token) {

		if (secureService.checkToken(bet.getBetCreator(), token)) {
			if (!(betService.eventExists(bet.getEventName()))) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event does not Exist!");
			}
			if (!(betService.winnerExists(bet.getEventName(), bet.getWinningTeam()))) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Select a matching Team!");
			}

			betService.overWriteBet(bet);

			return ResponseEntity.status(HttpStatus.OK).body(bet);

		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}
}
