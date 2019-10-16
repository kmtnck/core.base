package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the plugin_loggerchat_infoportali database table.
 * 
 */
@Entity
public class PluginDataMain implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=128)
	private String guid;

	@Column(nullable=false, length=20)
	private String coordinate;

	@Column(length=256)
	private String indirizzo;

	@Column(nullable=false)
	private Timestamp lastmodified;

	@Column(nullable=false)
	private float lat;

	@Column(nullable=false)
	private float lon;

	@Column(nullable=false, length=256)
	private String pathimage;

	@Column(nullable=false, length=256)
	private String titolo;

	public PluginDataMain() {
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}


	public String getCoordinate() {
		return this.coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Timestamp getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Timestamp lastmodified) {
		this.lastmodified = lastmodified;
	}

	public float getLat() {
		return this.lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return this.lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public String getPathimage() {
		return this.pathimage;
	}

	public void setPathimage(String pathimage) {
		this.pathimage = pathimage;
	}

	public String getTitolo() {
		return this.titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

}