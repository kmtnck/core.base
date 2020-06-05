package it.alessandromodica.product.context.gis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.alessandromodica.product.app.AuthContext;
import it.alessandromodica.product.app.MainContext;
import it.alessandromodica.product.context.interfaces.IGis;
import it.alessandromodica.product.model.bo.BOUtente;

@Controller
public class MainGis extends MainContext implements IGis {

	public void setInfo(BOUtente utente) {
		authContext.setUtenteCorrente(utente);
	}

}
