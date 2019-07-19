package nl.hu.v1wac.scouting.persistence;

public interface UserDao {
	public String findRoleForUser(String email, String pass);
	public int findBoatForUser(String email);
}