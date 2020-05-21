package it.alessandromodica.product.services.interfaces;

import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.GestioneUtenti;


public interface IMainService {

	public GestioneUtenti getUtente(String nickname) throws ServiceException;
	
	public void logAccesso(String messaggio, BOUtente utentecorrente) throws ServiceException;
}
