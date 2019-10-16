package it.alessandromodica.product.model.bo;

import java.util.Date;

/**
 * Bean comune ereditato da tutti gli altri bo.
 * 
 * Qui viene rappresentato il generico istante data
 * @author Alessandro
 *
 */
public abstract class BOCommon {

	private Date data;
	private Date ora;
	private Date istante;
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Date getOra() {
		return ora;
	}
	public void setOra(Date ora) {
		this.ora = ora;
	}
	public Date getIstante() {
		return istante;
	}
	public void setIstante(Date istante) {
		this.istante = istante;
	}

}
