//package nl.hu.v1wac.firstapp.servlets;
//package nl.hu.v1wac.firstapp.model;
package nl.hu.v1wac.firstapp.webservices;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import nl.hu.v1wac.firstapp.model.Country;
import nl.hu.v1wac.firstapp.webservices.WorldService;
import nl.hu.v1wac.firstapp.persistence.CountryDao;
import nl.hu.v1wac.firstapp.persistence.CountryPostgresDaoImpl;
import javax.annotation.security.RolesAllowed;


@Path("/countries")
public class ServiceProvider {
	private static WorldService worldService = new WorldService();

	public static WorldService getWorldService() {
		return worldService;
	}
	
	@GET	
	@Produces("application/json")
	public String getCounties() {
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (Country c : worldService.getAllCountries()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("code", c.getCode());
			job.add("name", c.getName());
			job.add("capital", c.getCapital());
			job.add("surface", c.getSurface());
			job.add("government", c.getGovernment());
			job.add("lat", c.getLatitude());
			
			jab.add(job);
		}
		
		JsonArray array = jab.build();	
		return array.toString();
    }
	
	@GET
	@Path("{code}")
	@Produces("application/json")
	public String getCounty(@PathParam("code") String code) {
		Country c = worldService.getCountryByCode(code);
		
		if (c == null) {
			throw new WebApplicationException("Geen land met deze code!");
		}
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("code", c.getCode());
		job.add("name", c.getName());
		job.add("capital", c.getCapital());
		job.add("surface", c.getSurface());
		job.add("government", c.getGovernment());
		job.add("lat", c.getLatitude());
		
		return job.build().toString();
	}
	
	@GET
	@Path("/largestsurfaces")
	@Produces("application/json")
	public String getCountiesLargesSurface() {
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (Country c : worldService.get10LargestSurfaces()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("code", c.getCode());
			job.add("name", c.getName());
			job.add("capital", c.getCapital());
			job.add("surface", c.getSurface());
			job.add("government", c.getGovernment());
			job.add("lat", c.getLatitude());
			
			jab.add(job);
		}
		
		JsonArray array = jab.build();	
		return array.toString();
	}
	
	@GET
	@Path("/largestpopulations")
	@Produces("application/json")
	public String getCountiesLargesPopulation() {
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (Country c : worldService.get10LargestPopulations()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("code", c.getCode());
			job.add("name", c.getName());
			job.add("capital", c.getCapital());
			job.add("surface", c.getSurface());
			job.add("government", c.getGovernment());
			job.add("lat", c.getLatitude());
			
			jab.add(job);
		}
		
		JsonArray array = jab.build();	
		return array.toString();
	}
	
	@DELETE
	@RolesAllowed("user")
	@Path("{code}")
	public Response delete(@PathParam("code") String code) {
		CountryPostgresDaoImpl db = new CountryPostgresDaoImpl();
		Country c = new Country();
		c.setCode(code);
		db.delete(c);
		return Response.ok().build();
	}
	
	@PUT
	@RolesAllowed("user")
	@Path("{code}")
	public Response update(@PathParam("code") String code, @FormParam("land") String name, @FormParam("hoofdstad") String capital, @FormParam("populatie") int population, @FormParam("surface") int surface) {
		System.out.println("------------------");
		System.out.println(name);
		CountryPostgresDaoImpl db = new CountryPostgresDaoImpl();
		Country country = new Country(); 
		country.setCode(code);
		country.setName(name);
		country.setCapital(capital);
		country.setSurface(surface);
		country.setPopulation(population);

		boolean result = db.update(country);
		if (!result) {
			return Response.status(404).build();
		}
		
		return Response.ok().build();
	}
	
	@POST
	@RolesAllowed("user")
	public Response save(@FormParam("code") String code, @FormParam("land") String name, @FormParam("hoofdstad") String capital, @FormParam("populatie") int population, @FormParam("surface") int surface) {
		CountryDao db = new CountryPostgresDaoImpl();
		Country country = new Country();
		country.setCode(code);
		country.setName(name);
		country.setCapital(capital);
		country.setSurface(surface);
		country.setPopulation(population);
		boolean resp = db.save(country);
		
		if (!resp) {
			return Response.status(402).build();
		}
		
		return Response.ok().build();
	}

}