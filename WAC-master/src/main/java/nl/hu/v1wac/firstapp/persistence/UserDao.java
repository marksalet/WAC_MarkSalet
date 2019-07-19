package nl.hu.v1wac.firstapp.persistence;

import java.sql.PreparedStatement;

public interface UserDao {
	public String findRoleForUser(String name, String pass);
}
