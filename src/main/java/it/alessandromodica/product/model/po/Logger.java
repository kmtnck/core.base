package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the plugin_logger database table.
 * 
 */
@Entity
public class Logger implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idlogger;

	@Column(length=256)
	private String context;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataevento;

	@Column(length=256)
	private String level;

	@Column(length=4000)
	private String messaggio;

	public Logger() {
	}

	public int getIdlogger() {
		return this.idlogger;
	}

	public void setIdlogger(int idlogger) {
		this.idlogger = idlogger;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getDataevento() {
		return this.dataevento;
	}

	public void setDataevento(Date dataevento) {
		this.dataevento = dataevento;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMessaggio() {
		return this.messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

}