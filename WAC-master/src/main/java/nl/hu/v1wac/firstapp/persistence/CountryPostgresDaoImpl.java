package nl.hu.v1wac.firstapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nl.hu.v1wac.firstapp.model.Country;

public class CountryPostgresDaoImpl extends PostgresBaseDao implements CountryDao{
	public boolean save(Country country) {
		try (Connection con = super.getConnection()) {
			String q = "insert into country(code, name, capital) " +
					"values ('" + country.getCode() + "', '" + country.getName() + "', '" + country.getCapital() + "')";
			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
	
	private List<Country> getCountries(String query) {
		List<Country> results = new ArrayList<Country>();
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet dbResultSet = pstmt.executeQuery();
			
			while (dbResultSet.next()) {
				String code = dbResultSet.getString("code");
				String iso3 = dbResultSet.getString("iso3");
				String nm = dbResultSet.getString("name"); 
				String cap = dbResultSet.getString("capital");
				String ct = dbResultSet.getString("continent");
				String reg = dbResultSet.getString("region");
				int sur = dbResultSet.getInt("surfacearea"); 
				int pop = dbResultSet.getInt("population");
				String gov = dbResultSet.getString("governmentform");
				double lat = dbResultSet.getDouble("latitude");
				double lng = dbResultSet.getDouble("longitude");
				
				Country c = new Country(code, iso3, nm, cap, ct, reg, sur, pop, gov, lat, lng);
				results.add(c);
			}
		} catch (SQLException sqle) { sqle.printStackTrace(); }
		
		return results;
	}
	
	public List<Country> findAll() {
		return this.getCountries("SELECT * FROM country ORDER BY name LIMIT 20");
	}
	
	public Country findByCode(String codeSearch) {
		Country result = null;
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM country WHERE code = '" + codeSearch + "'");
			ResultSet dbResultSet = pstmt.executeQuery();
			
			while (dbResultSet.next()) {
				String code = dbResultSet.getString("code");
				String iso3 = dbResultSet.getString("iso3");
				String nm = dbResultSet.getString("name"); 
				String cap = dbResultSet.getString("capital");
				String ct = dbResultSet.getString("continent");
				String reg = dbResultSet.getString("region");
				int sur = dbResultSet.getInt("surfacearea"); 
				int pop = dbResultSet.getInt("population");
				String gov = dbResultSet.getString("governmentform");
				double lat = dbResultSet.getDouble("latitude");
				double lng = dbResultSet.getDouble("longitude");
				
				result = new Country(code, iso3, nm, cap, ct, reg, sur, pop, gov, lat, lng);
			}
		} catch (SQLException sqle) { sqle.printStackTrace(); }
		
		return result;
	}
	
	public List<Country> find10LargestPopulations() {
		return this.getCountries("SELECT * FROM country ORDER BY population DESC LIMIT 10");
	}
	
	public List<Country> find10LargestSurfaces() {
		return this.getCountries("SELECT * FROM country ORDER BY surfacearea DESC LIMIT 10");
	}
	
	public boolean update(Country country) {
		try (Connection con = super.getConnection()) {
			
			String q = "UPDATE country SET "
					+ "name = '" + country.getName() + "', "
					+ "capital = '" + country.getCapital() + "', "
					+ "surfacearea = " + country.getSurface() + ", "
					+ "population = " + country.getPopulation() + " "
					+ "where code = '" + country.getCode() + "'";
			System.out.println(q);
			PreparedStatement pstmt = con.prepareStatement(q);
			ResultSet dbResultSet = pstmt.executeQuery();		
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		return true;
	}
	
	public boolean delete(Country country) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("delete from country where code = '" + country.getCode() + "'");
			ResultSet dbResultSet = pstmt.executeQuery();
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
		
	}
}

