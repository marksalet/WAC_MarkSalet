package nl.hu.v1wac.scouting.webservices;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.v1wac.scouting.persistence.UserDao;
import nl.hu.v1wac.scouting.persistence.UserPostgresDaoImpl;

import java.security.Key;
import javax.ws.rs.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/authentication")
public class AuthenticationResource {
	final static public Key key = MacProvider.generateKey();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@FormParam("username") String username, @FormParam("password") String pass) {
		try {
			UserDao dao = new UserPostgresDaoImpl();
			String role = dao.findRoleForUser(username, pass);
			System.out.println(role);
			if (role == null) { System.out.println("NOT FOUND"); throw new IllegalArgumentException("No user found!"); }
			if (role == "Afwachten") { return Response.status(Response.Status.UNAUTHORIZED).build(); }
			String token = createToken("username", role);
			
			SimpleEntry<String, String> JWT = new SimpleEntry<String, String>("JWT", token);
			
			System.out.println(token);
			return Response.ok().entity(JWT).build();
		} catch (JwtException | IllegalArgumentException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("/boat")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response userBoat(@FormParam("username") String username) {
		try {
			UserDao dao = new UserPostgresDaoImpl();
			Integer intBoatNumber = dao.findBoatForUser(username);
			if (intBoatNumber == null) { System.out.println("NOT FOUND"); throw new IllegalArgumentException("No user found!"); }
			
			SimpleEntry<String, Integer> BoatNumber = new SimpleEntry<String, Integer>("BoatNumber", intBoatNumber);
			
			System.out.println(intBoatNumber);
			return Response.ok().entity(BoatNumber).build();
		} catch (JwtException | IllegalArgumentException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	private String createToken(String username, String role) {
		Calendar expiration = Calendar.getInstance();
		expiration.add(Calendar.MINUTE, 30);
	
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(expiration.getTime())
				.claim("Role", role)
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
	}
}
