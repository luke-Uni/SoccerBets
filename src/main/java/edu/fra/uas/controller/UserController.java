package edu.fra.uas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
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
import edu.fra.uas.Team.TeamService;
import edu.fra.uas.User.User;
import edu.fra.uas.User.UserService;
import edu.fra.uas.UserDTO.UserDTO;
import edu.fra.uas.security.SecurityService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	TeamService teamService;

	@Autowired
	EventService eventService;

	@Autowired
	UserService userService;

	@Autowired
	BetService betService;

	@Autowired
	SecurityService securityService;

	// Create an user
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addUser(@RequestBody User user) {

		if (userService.userExists(user)) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user.getUsername() + " already Exists!");
		}

		userService.addUser(user);
		securityService.addToken(user.getUsername());

		return ResponseEntity.status(HttpStatus.CREATED).body(securityService.getToken(user.getUsername())
				+ "\nThis is your Authorization Token.\n" + "Please add this to the header for future requests!");

	}

	// Get all User
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendAllUser(@RequestHeader String token) {

		if (securityService.checkToken(token)) {

			ArrayList<UserDTO> allUserDTO = new ArrayList<>();
			UserDTO userDTO = null;
			for (int i = 0; i < userService.getUsers().size(); i++) {
				userDTO = new UserDTO(userService.getUsers().get(i));
				Link selfLink = linkTo(UserController.class).slash(userService.getUsers().get(i).getUsername())
						.withSelfRel();
				userDTO.add(selfLink);
				allUserDTO.add(userDTO);
			}

			return ResponseEntity.status(HttpStatus.OK).body(allUserDTO);
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

	}

	// Get an specific User
	@RequestMapping(value = "/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendAnUser(@PathVariable String userName, @RequestHeader String token) {

		if (securityService.checkToken(userName, token)) {
			UserDTO userDTO = new UserDTO(userService.getUser(userName));
			Link selfLink = linkTo(UserController.class).slash(userName).withSelfRel();
			userDTO.add(selfLink);

			return ResponseEntity.status(HttpStatus.OK).body(userDTO);
		}

		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);

	}

	// Delete a specific User
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAUser(@PathVariable String username, @RequestHeader String token) {

		if (securityService.checkToken(username, token)) {
			if (userService.deleteUser(username)) {
				return ResponseEntity.status(HttpStatus.OK).body(username + " has been deleted!");
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(username + " does not Exists!");

		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);

	}
}
