package nl.hu.v1wac.firstapp.model;

public class Country {
	private String code;
	private String iso3;
	private String name;
	private String capital;
	private String continent;
	private String region;
	private int surface;
	private int population;
	private String government;
	private double latitude;
	private double longitude;
	
	public Country() {
		
	}
	
	public Country(String code, String iso3, String nm, String cap, String ct, String reg, int sur, int pop, String gov, double lat, double lng) {
		this.code = code; 
		this.iso3 = iso3;
		this.name = nm;
		this.capital = cap;
		this.continent = ct;
		this.region = reg;
		this.surface = sur;
		this.population = pop;
		this.government = gov;
		this.latitude = lat;
		this.longitude = lng;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getIso3() {
		return iso3;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCapital() {
		return capital;
	}
	
	public String getContinent() {
		return continent;
	}
	
	public String getRegion() {
		return region;
	}
	
	public int getSurface() {
		return surface;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public String getGovernment() {
		return government;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setCapital(String capital) {
		this.capital= capital;
	}
	
	public void setSurface(int surface) {
		this.surface = surface;
	}
	
	public void setPopulation(int population) {
		this.population = population;
	}
}
