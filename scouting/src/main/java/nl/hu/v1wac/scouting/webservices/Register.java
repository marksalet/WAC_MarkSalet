package nl.hu.v1wac.scouting.webservices;

import nl.hu.v1wac.scouting.persistence.UserPostgresDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.annotation.security.RolesAllowed;



@Path("/register")
public class Register {
	
	@POST
	public Response register(@FormParam("voornaam") String Voornaam, @FormParam("tussenvoegsel") String Tussenvoegsel, @FormParam("achternaam") String Achternaam, @FormParam("telefoon") int Telefoon, @FormParam("email") String Email, @FormParam("wachtwoord") String Wachtwoord) {
		UserPostgresDaoImpl db = new UserPostgresDaoImpl();

		boolean resp = db.register(Voornaam, Tussenvoegsel, Achternaam, Telefoon, Email, Wachtwoord);
		if (!resp) {
			return Response.status(402).build();
		}
		
		return Response.ok().build();
	}
}
