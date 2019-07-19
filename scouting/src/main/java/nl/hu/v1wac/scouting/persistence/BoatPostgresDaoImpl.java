package nl.hu.v1wac.scouting.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class BoatPostgresDaoImpl extends PostgresBaseDao implements BoatDao {
		
	public String getAllBoats() {
		String result = null;
		Connection con = super.getConnection();		
		try {		
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM \"Boot\" ORDER BY \"Nummer\";");
			ResultSet dbResultSet = pstmt.executeQuery();
			System.out.println(pstmt);
			JsonArrayBuilder jab = Json.createArrayBuilder();
			
			while (dbResultSet.next()) {
				PreparedStatement pstmt2 = con.prepareStatement("SELECT * FROM \"Onderdelen\" WHERE \"Boot_Nummer\" = " + dbResultSet.getInt("Nummer") + ";");
				ResultSet dbResultSetOnderdelen = pstmt2.executeQuery();
				while (dbResultSetOnderdelen.next()) {
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("Nummer", dbResultSet.getInt("Nummer"));
					System.out.println(dbResultSet.getInt("Nummer"));
					job.add("Naam", dbResultSet.getString("Naam"));
					System.out.println(dbResultSet.getString("Naam"));
					job.add("Lengte", dbResultSet.getDouble("Lengte"));
					job.add("Breedte", dbResultSet.getDouble("Breedte"));
					job.add("Hoogte", dbResultSet.getDouble("Hoogte"));
					job.add("Diepgang", dbResultSet.getDouble("Diepgang"));
					job.add("Onderhoud", dbResultSet.getBoolean("Onderhoud"));	
					job.add("Zwaard", dbResultSetOnderdelen.getBoolean("Zwaard"));	
					job.add("Roer", dbResultSetOnderdelen.getBoolean("Roer"));	
					job.add("Mast", dbResultSetOnderdelen.getBoolean("Mast"));	
					job.add("Giek", dbResultSetOnderdelen.getBoolean("Giek"));	
					job.add("Gaffel", dbResultSetOnderdelen.getBoolean("Gaffel"));	
					job.add("Grootzeil", dbResultSetOnderdelen.getBoolean("Grootzeil"));	
					job.add("Fok", dbResultSetOnderdelen.getBoolean("Fok"));	
					job.add("Vallen", dbResultSetOnderdelen.getBoolean("Vallen"));	
					job.add("Doften", dbResultSetOnderdelen.getBoolean("Doften"));	
					job.add("Vlonders", dbResultSetOnderdelen.getBoolean("Vlonders"));	
					job.add("Wrikriem", dbResultSetOnderdelen.getBoolean("Wrikriem"));	
					job.add("Roeiriem", dbResultSetOnderdelen.getBoolean("Roeiriem"));	
					job.add("Dollen", dbResultSetOnderdelen.getBoolean("Dollen"));
					
					jab.add(job);
				}	
	        }
			
			JsonArray array = jab.build();	
			return array.toString();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String getBoat(int Nummer) {
		String result = null;
		Connection con = super.getConnection();		
		try {		
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM \"Boot\" WHERE \"Nummer\" = " + Nummer + ";");
			ResultSet dbResultSet = pstmt.executeQuery();
			System.out.println(pstmt);
			JsonArrayBuilder jab = Json.createArrayBuilder();
			
			while (dbResultSet.next()) {
				JsonObjectBuilder job = Json.createObjectBuilder();
				job.add("Nummer", dbResultSet.getInt("Nummer"));
				System.out.println(dbResultSet.getInt("Nummer"));
				job.add("Naam", dbResultSet.getString("Naam"));
				System.out.println(dbResultSet.getString("Naam"));
				job.add("Lengte", dbResultSet.getDouble("Lengte"));
				job.add("Breedte", dbResultSet.getDouble("Breedte"));
				job.add("Hoogte", dbResultSet.getDouble("Hoogte"));
				job.add("Diepgang", dbResultSet.getDouble("Diepgang"));
				job.add("Onderhoud", dbResultSet.getBoolean("Onderhoud"));	
				
				jab.add(job);
	        }
			
			JsonArray array = jab.build();	
			return array.toString();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}	
	public boolean save(int Nummer, String Naam, int Lengte, int Breedte, int Hoogte, int Diepgang, boolean Onderhoud) {
		try (Connection con = super.getConnection()) {
			String q = "INSERT INTO \"Boot\"(\"Nummer\", \"Naam\", \"Lengte\", \"Breedte\", \"Hoogte\", \"Diepgang\", \"Onderhoud\") "
					+ "VALUES (" + Nummer + ", '" + Naam + "', " + Lengte + "," + Breedte + "," + Hoogte + "," + Diepgang + "," + Onderhoud + ");";
			PreparedStatement pstmt = con.prepareStatement(q);
			String q2 = "INSERT INTO \"Onderdelen\"(\"Zwaard\", \"Roer\", \"Mast\", \"Giek\", \"Gaffel\", \"Grootzeil\", \"Fok\", \"Vallen\", \"Doften\", \"Vlonders\", \"Wrikriem\", \"Roeiriem\", \"Dollen\", \"Boot_Nummer\") "
					+ "VALUES (true, true, true, true, true, true, true, true, true, true, true, true, true, '" + Nummer + "')";
			PreparedStatement pstmt2 = con.prepareStatement(q2);
			pstmt.executeUpdate();
			pstmt2.executeUpdate();
			System.out.println(pstmt2);
			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
	public boolean update(int Nummer, String Naam, int Lengte, int Breedte, int Hoogte, int Diepgang, boolean Onderhoud) {
		try (Connection con = super.getConnection()) {
			String q = "UPDATE \"Boot\" SET "
					+ "\"Naam\" = '" + Naam + "', "
					+ "\"Lengte\" = " + Lengte + ", "
					+ "\"Breedte\" = " + Breedte + ", "
					+ "\"Hoogte\" = " + Hoogte + ", "
					+ "\"Diepgang\" = " + Diepgang + ", "
					+ "\"Onderhoud\" = '" + Onderhoud + "' "
					+ "WHERE \"Nummer\" = '" + Nummer + "'";
			System.out.println(q);
			PreparedStatement pstmt = con.prepareStatement(q);
			ResultSet dbResultSet = pstmt.executeQuery();
			System.out.println(dbResultSet + " Updated");
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}
	public boolean updateOnderdelen(int Nummer, boolean Zwaard,boolean Roer,boolean Mast,boolean Giek,boolean Gaffel,boolean Grootzeil,boolean Fok,boolean Vallen,boolean Doften,boolean Vlonders,boolean Wrikriem,boolean Roeiriem,boolean Dollen) {
		try (Connection con = super.getConnection()) {
			String q = "UPDATE public.\"Onderdelen\"\r\n" + 
					"	SET \"Zwaard\"=" + Zwaard + ", \"Roer\"=" + Roer + ", \"Mast\"=" + Mast + ", \"Giek\"=" + Giek + ", \"Gaffel\"=" + Gaffel + ", \"Grootzeil\"=" + Grootzeil + ", \"Fok\"=" +Fok + ", \"Vallen\"=" + Vallen + ", \"Doften\"=" + Doften + ", \"Vlonders\"=" + Vlonders + ", \"Wrikriem\"=" + Wrikriem + ", \"Roeiriem\"=" + Roeiriem + ", \"Dollen\"=" + Dollen + 
					"	WHERE \"Boot_Nummer\"=" + Nummer + ";";
			System.out.println(q);
			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.executeQuery();
			System.out.println("Updated");
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}
	public boolean delete(int Nummer) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM \"Onderdelen\" where \"Boot_Nummer\" = " + Nummer + "");
			int dbResultSet = pstmt.executeUpdate();
			PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM \"Boot\" where \"Nummer\" = " + Nummer + "");
			int dbResultSet2 = pstmt2.executeUpdate();
			System.out.println(dbResultSet + dbResultSet2 + " Succesfull deleted.");
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
		
	}
}
