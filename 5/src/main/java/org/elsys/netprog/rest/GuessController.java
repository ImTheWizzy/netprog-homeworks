package org.elsys.netprog.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

@Path("/hash")
public class GuessController {
	String hash = "";
	boolean hashGuessed = true;
	
	@GET
	@Path("")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response getHashAndLength() throws NoSuchAlgorithmException {
		//TODO: Add your code here
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		Random random = new Random();
		byte[] digest = null;
		int length = 1;
		
		if(hashGuessed) {
			length = random.nextInt(9) + 1;
			byte[] byteArr = new byte[length];
			random.nextBytes(byteArr);
			digest = messageDigest.digest(byteArr);
			hashGuessed = false;
		}

		hash = Base64.getEncoder().encodeToString(digest);
			
		StringBuilder jsonResponse = new StringBuilder();
		jsonResponse.append("{" + '"' + "hash" + '"' + ":" + '"' + hash + '"' + "," 
				+ '"' + "length" + '"' + ":" + length + "}");
		
		return Response.status(Status.OK).entity(jsonResponse).build();
	}
	
	@POST
	@Path("")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response guessHash(String jsonInput) throws URISyntaxException {
		//TODO: Add your code here
		String inputHash = ""; // this should parse the hash from jsonInput
		if(inputHash.equals(hash)) {
			hashGuessed = true;
			return Response.status(200).build();
		} else {
			return Response.status(406).build();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + (hashGuessed ? 1231 : 1237);
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
		GuessController other = (GuessController) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (hashGuessed != other.hashGuessed)
			return false;
		return true;
	}
}
