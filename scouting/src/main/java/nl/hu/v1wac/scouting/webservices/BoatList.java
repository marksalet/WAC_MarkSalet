package nl.hu.v1wac.scouting.webservices;

import nl.hu.v1wac.scouting.persistence.BoatDao;
import nl.hu.v1wac.scouting.persistence.BoatPostgresDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.annotation.security.RolesAllowed;



@Path("/boatlist")
public class BoatList {
	
	@GET
	@Produces("application/json")
	public String getList() {
		BoatDao dao = new BoatPostgresDaoImpl();
		String list = dao.getAllBoats();
		return list;
	}
	@GET
	@Path("{number}")
	@Produces("application/json")
	public String getBoat(@PathParam("number") int number) {
		BoatPostgresDaoImpl dao = new BoatPostgresDaoImpl();
		String boat = dao.getBoat(number);
		return boat;
	}
	@DELETE
	@RolesAllowed("Leiding")
	@Path("{code}")
	public Response delete(@PathParam("code") int code) {
		BoatPostgresDaoImpl db = new BoatPostgresDaoImpl();
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
	@PUT
	@RolesAllowed("Leiding")
	@Path("/onderdelen/{nummer}")
	public Response updateOnderdelen(@PathParam("nummer") int Nummer, @FormParam("Zwaard") boolean Zwaard, @FormParam("Roer") boolean Roer, @FormParam("Mast") boolean Mast, @FormParam("Giek") boolean Giek, @FormParam("Gaffel") boolean Gaffel, @FormParam("Grootzeil") boolean Grootzeil, @FormParam("Fok") boolean Fok, @FormParam("Vallen") boolean Vallen, @FormParam("Doften") boolean Doften, @FormParam("Vlonders") boolean Vlonders, @FormParam("Wrikriem") boolean Wrikriem, @FormParam("Roeiriem") boolean Roeiriem, @FormParam("Dollen") boolean Dollen) {
		System.out.println("------------------");
		System.out.println(Nummer);
		System.out.println(Zwaard);
		BoatPostgresDaoImpl db = new BoatPostgresDaoImpl();
		boolean result = db.updateOnderdelen(Nummer, Zwaard, Roer, Mast, Giek, Gaffel, Grootzeil, Fok, Vallen, Doften, Vlonders, Wrikriem, Roeiriem, Dollen);
		if (!result) {
			return Response.status(404).build();
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
