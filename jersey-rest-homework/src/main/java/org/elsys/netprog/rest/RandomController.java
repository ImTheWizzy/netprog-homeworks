package org.elsys.netprog.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

@Path("/random")
public class RandomController {
	@GET
	@Path("/numbers/{nums}/{min}/{max}")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response randomNums(@PathParam("nums") int numCount, @PathParam("min") int minValue, @PathParam("max") int maxValue) throws Exception{
		//TODO: Add your code here
		Random random = new Random();
		
		StringBuilder jsonResponse = new StringBuilder();
		jsonResponse.append("{" + '"' + "num:" + '"' + numCount + "," + '"' + "min:" + '"' 
		+ minValue + "," + '"' + "max:" + '"' + maxValue + "," + '"' + "numbers" + '"' + ":[");
		
		for(int i = 0; i < numCount; i++) {
			int randomNum = random.nextInt(maxValue + 1 - minValue) + minValue;
			jsonResponse.append(randomNum);
			if(i + 1 < numCount) {
				jsonResponse.append(", ");
			} else {
				jsonResponse.append("]}");
			}
		}
		return Response.status(Status.OK).entity(jsonResponse.toString()).build();
	}
	
	@GET
	@Path("/string/{length}/{allowedChars}")
	@Produces(value={MediaType.APPLICATION_JSON})
	public Response randomStrings(@PathParam("length") int stringLength, @PathParam("allowedChars") String allowedChars) throws Exception{
		//TODO: Add your code here
		Random random = new Random();
		
		StringBuilder jsonResponse = new StringBuilder();
		jsonResponse.append("{" + '"' + "length:" + '"' + stringLength + "," + '"' + "allowedChars:" + '"' 
		+ '"' + allowedChars + '"' + "," + '"' + "string" + '"' + ":" + '"');
		
		for(int i = 0; i < stringLength; i++) {
			char randomChar = allowedChars.charAt(random.nextInt(allowedChars.length()));
			jsonResponse.append(randomChar);
		}
		jsonResponse.append('"' + "}");
		return Response.status(Status.OK).entity(jsonResponse.toString()).build();
	}
}
