package nl.hu.v1wac.firstapp.webservices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.hu.v1wac.firstapp.model.Country;
import nl.hu.v1wac.firstapp.persistence.CountryPostgresDaoImpl;

public class WorldService {
	private CountryPostgresDaoImpl db = new CountryPostgresDaoImpl();

	
	public List<Country> getAllCountries() {
		return db.findAll();
	}
	
	public List<Country> get10LargestPopulations() {
		return db.find10LargestPopulations();
	}

	public List<Country> get10LargestSurfaces() {
		return db.find10LargestSurfaces();
	}
	
	public Country getCountryByCode(String code) {
		return db.findByCode(code);
	}
}
