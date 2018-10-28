package org.elsys.netprog.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;

class Game {
	String gameId;
	int turnsCount;
	String secret;
	boolean success;
	
	public Game(String gameId, int turnsCount, String secret, boolean success) {
		this.gameId = gameId;
		this.turnsCount = turnsCount;
		this.secret = secret;
		this.success = success;
	}
}

@Path("/game")
public class GameController {
	public static Map<String, String> uids = new HashMap<String, String>();
	public static List<Game> games = new ArrayList<Game>();
	public static int counter = 0;
	
	@POST
	@Path("/startGame")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response startGame() throws URISyntaxException{
		//TODO: Add your code here
		List<Integer> numbers = new ArrayList<Integer>();
	    for(int i = 0; i < 10; i++) {
	        numbers.add(i);
	    }
	    Collections.shuffle(numbers);
	    
	    String randomValue = "";
	    for(int i = 0; i < 4; i++) {
	        randomValue += numbers.get(i).toString();
	    }
		
	    String uniqueID = UUID.randomUUID().toString();
		
		uids.put(uniqueID, randomValue);

		Game newGame = new Game(uniqueID, 0, randomValue, false);
		games.add(newGame);
		
		return Response.created(new URI("/games")).entity(uniqueID).build();
	}
	
	@PUT
	@Path("/guess/{id}/{guess}")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response guess(@PathParam("id") String gameId, @PathParam("guess") String guess) throws Exception{
		//TODO: Add your code here
		StringBuilder jsonResponse = new StringBuilder();
		int cows = 0;
		int bulls = 0;

		if(uids.containsKey(gameId)) {
			if(guess.length() != 4 && guess.chars().distinct().count() != (int)guess.length()) {
				return Response.status(400).build();
			}
			
			int i;
			for(i = 0; i < games.size(); i++) {
				if(games.get(i).gameId.equals(gameId)) {
					games.get(i).turnsCount++;
					if(games.get(i).secret.equals(guess)) {
						games.get(i).success = true;
					}
					break;
				}
			}
			
			for(int j = 0; j < 4; j++) {
				if(guess.charAt(j) == games.get(i).secret.charAt(j)) {
					bulls++;
				} else if(games.get(i).secret.contains(guess.charAt(j)+"")){
					cows++;
				}
			}
			
			jsonResponse.append("{" + '"' + "gameId" + '"' + ":" + '"' + gameId + '"' + "," 
				+ '"' + "cowsNumber" + '"' + ":" + cows + ","
				+ '"' + "bullsNumber" + '"' + ":" + bulls + ","
				+ '"' + "turnsCount" + '"' + ":" + games.get(i).turnsCount + ","
				+ '"' + "success" + '"' + ":" + games.get(i).success + '"' + "}");

			return Response.status(Status.OK).entity(jsonResponse).build();
		}
		return Response.status(404).build();
	}
	
	@GET
	@Path("/games")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response getGames() {
		//TODO: Add your code here
		StringBuilder jsonResponse = new StringBuilder();
		jsonResponse.append("[");
		for(Game game : games) {
			counter++;
			jsonResponse.append("{" + '"' + "gameId" + '"' + ":" + '"' + game.gameId + '"' + "," 
					+ '"' + "turnsCount" + '"' + ":" + game.turnsCount + ",");
			if(!game.success) {
				jsonResponse.append('"' + "secret" + '"' + ":" + '"' + "****" + '"' + ",");
			} else {
				jsonResponse.append('"' + "secret" + '"' + ":" + '"' + game.secret + '"' + ",");
			}
			jsonResponse.append('"' + "success" + '"' + ":" + game.success + "}");
			if(counter < games.size()) {
				jsonResponse.append(",");
			} else {
				jsonResponse.append("]");
			}
		}
		
		return Response.status(Status.OK).entity(jsonResponse).build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uids == null) ? 0 : uids.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameController other = (GameController) obj;
		if (uids == null) {
			if (other.uids != null)
				return false;
		} else if (!uids.equals(other.uids))
			return false;
		return true;
	}
}
