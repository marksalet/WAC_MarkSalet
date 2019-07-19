package nl.hu.v1wac.firstapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPostgresDaoImpl extends PostgresBaseDao implements UserDao {
		
	public String findRoleForUser(String name, String password) {
		String result = null;
		Connection con = super.getConnection();		
		try {		
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM gebruikers WHERE naam = '" + name + "' AND wachtwoord = '" + password + "'");
			ResultSet dbResultSet = pstmt.executeQuery();
			System.out.println(pstmt);

			if(dbResultSet != null) {
				System.out.println(1);
				while(dbResultSet.next()) {
					System.out.println(2);
					result = dbResultSet.getString("role");
				}
			}
			
			System.out.println(result);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
