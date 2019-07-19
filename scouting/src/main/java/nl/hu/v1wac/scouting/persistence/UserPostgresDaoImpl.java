package nl.hu.v1wac.scouting.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class UserPostgresDaoImpl extends PostgresBaseDao implements UserDao {
		
	public String findRoleForUser(String email, String password) {
		String result = null;
		Connection con = super.getConnection();		
		try {		
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM \"Gebruiker\" WHERE \"Email\" = '" + email + "' AND \"Wachtwoord\" = '" + password + "'");
			ResultSet dbResultSet = pstmt.executeQuery();
			System.out.println(pstmt);

			if(dbResultSet != null) {
				System.out.println(1);
				while(dbResultSet.next()) {
					System.out.println(2);
					result = dbResultSet.getString("Role");
				}
			}
			
			System.out.println(result);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public int findBoatForUser(String email){
		int result = 0;
		Connection con = super.getConnection();		
		try {		
			PreparedStatement pstmt = con.prepareStatement("SELECT \"Boot_Nummer\" FROM \"Gebruiker\" WHERE \"Email\" = '" + email + "'");
			ResultSet dbResultSet = pstmt.executeQuery();
			System.out.println(pstmt);

			if(dbResultSet != null) {
				System.out.println(1);
				while(dbResultSet.next()) {
					System.out.println(2);
					result = dbResultSet.getInt("Boot_Nummer");
				}
			}
			
			System.out.println(result);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean register(String Voornaam, String Tussenvoegsel, String Achternaam, int Telefoon, String Email, String Wachtwoord) {
		try (Connection con = super.getConnection()) {
			String q = "INSERT INTO \"Gebruiker\"(\"Voornaam\", \"Tussenvoegsel\", \"Achternaam\", \"Telefoonnummer\", \"Email\", \"Wachtwoord\", \"Role\") "
					+ "VALUES ('" + Voornaam + "', '" + Tussenvoegsel + "', '" + Achternaam + "'," + Telefoon + ",'" + Email + "','" + Wachtwoord + "', 'Afwachten');";
			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
	public String getAllUsers() {
		String result = null;
		Connection con = super.getConnection();		
		try {		
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM \"Gebruiker\" ORDER BY \"ID\";");
			ResultSet dbResultSet = pstmt.executeQuery();
			System.out.println(pstmt);
			JsonArrayBuilder jab = Json.createArrayBuilder();
			
			while (dbResultSet.next()) {
				JsonObjectBuilder job = Json.createObjectBuilder();
				job.add("ID", dbResultSet.getInt("ID"));
				job.add("Voornaam", dbResultSet.getString("Voornaam"));
				job.add("Tussenvoegsel", dbResultSet.getString("Tussenvoegsel"));
				job.add("Achternaam", dbResultSet.getString("Achternaam"));
				job.add("Telefoon", dbResultSet.getInt("Telefoonnummer"));
				job.add("Email", dbResultSet.getString("Email"));
				job.add("Boot_Nummer", dbResultSet.getInt("Boot_Nummer"));
				job.add("Role", dbResultSet.getString("Role"));
				
				jab.add(job);
	        }
			
			JsonArray array = jab.build();	
			return array.toString();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public boolean updateRole(String Role, int ID) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("UPDATE \"Gebruiker\" SET \"Role\" = '" + Role + "' where \"ID\" = " + ID + "");
			int dbResultSet = pstmt.executeUpdate();
			System.out.println(dbResultSet + " Succesfull Updated.");
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
		
	}
	public boolean delete(int Nummer) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM \"Gebruiker\" where \"ID\" = " + Nummer + "");
			int dbResultSet = pstmt.executeUpdate();
			System.out.println(dbResultSet + " Succesfull deleted.");
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
		
	}
}
