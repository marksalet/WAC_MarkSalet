package nl.hu.v1wac.scouting.webservices;

import nl.hu.v1wac.scouting.persistence.BoatDao;
import nl.hu.v1wac.scouting.persistence.BoatPostgresDaoImpl;
import nl.hu.v1wac.scouting.persistence.UserPostgresDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.annotation.security.RolesAllowed;



@Path("/userlist")
public class UserList {
	
	@GET
	@Produces("application/json")
	public String getUsers() {
		UserPostgresDaoImpl dao = new UserPostgresDaoImpl();
		String list = dao.getAllUsers();
		return list;
	}
	@DELETE
	@RolesAllowed("Leiding")
	@Path("{code}")
	public Response delete(@PathParam("code") int code) {
		UserPostgresDaoImpl db = new UserPostgresDaoImpl();
		db.delete(code);
		return Response.ok().build();
	}
	@PUT
	@RolesAllowed("Leiding")
	@Path("{nummer}")
	public Response update(@PathParam("nummer") int Nummer, @FormParam("naam") String Naam, @FormParam("lengte") int Lengte, @FormParam("breedte") int Breedte, @FormParam("hoogte") int Hoogte, @FormParam("diepgang") int Diepgang, @FormParam("onderhoud") boolean Onderhoud) {
		System.out.println("------------------");
		System.out.println(Naam);
		BoatPostgresDaoImpl db = new BoatPostgresDaoImpl();
		boolean result = db.update(Nummer, Naam, Lengte, Breedte, Hoogte, Diepgang, Onderhoud);
		if (!result) {
			return Response.status(404).build();
		}
		
		return Response.ok().build();
	}
	@POST
	@Path("{role}/{code}")
	@RolesAllowed("Leiding")
	public Response save(@PathParam("role") String Role, @PathParam("code") int Code) {
		UserPostgresDaoImpl db = new UserPostgresDaoImpl();

		boolean resp = db.updateRole(Role, Code);
		if (!resp) {
			return Response.status(402).build();
		}
		
		return Response.ok().build();
	}
	@POST
	@RolesAllowed("Leiding")
	public Response save(@FormParam("addNummer") int Nummer, @FormParam("addNaam") String Naam, @FormParam("addLengte") int Lengte, @FormParam("addBreedte") int Breedte, @FormParam("addHoogte") int Hoogte, @FormParam("addDiepgang") int Diepgang, @FormParam("addOnderhoud") boolean Onderhoud) {
		BoatPostgresDaoImpl db = new BoatPostgresDaoImpl();

		boolean resp = db.save(Nummer, Naam, Lengte, Breedte, Hoogte, Diepgang, Onderhoud);
		if (!resp) {
			return Response.status(402).build();
		}
		
		return Response.ok().build();
	}
}
