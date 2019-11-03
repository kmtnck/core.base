package it.alessandromodica.product.model.bo;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class BOCoordinate extends BOComponente{

	private String coordinate;
	private Double lat;
	private Double lon;
	private Double lng;
	private String wtkCompliance;

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getWtkCompliance() {
		String wtkCompliance = "POINT (" + getLat() + " " + getLon() + ")";
		this.wtkCompliance = wtkCompliance;
		return wtkCompliance;
	}

	public void setWtkCompliance(String wtkCompliance) {
		this.wtkCompliance = wtkCompliance;
	}

	public Geometry wktToGeometry() {
		WKTReader fromText = new WKTReader();
		this.wtkCompliance = "POINT (" + getLat() + " " + getLon() + ")";		
		Geometry geom = null;
		try {
			geom = fromText.read(wtkCompliance);
		} catch (ParseException e) {
			throw new RuntimeException("Not a WKT string:" + wtkCompliance);
		}
		return geom;
	}

	public static Geometry wktToGeometry(Number lat, Number lon) {
		String wtkCompliance = "POINT (" + lat + " " + lon + ")";
		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try {
			geom = fromText.read(wtkCompliance);
		} catch (ParseException e) {
			throw new RuntimeException("Not a WKT string:" + wtkCompliance);
		}
		return geom;
	}
}
