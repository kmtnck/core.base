package it.alessandromodica.product.services.interfaces;

import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.PluginGestioneUtenti;


public interface IMainService {

	public PluginGestioneUtenti getUtente(String nickname) throws ServiceException;
	
	public void logAccesso(String messaggio, BOUtente utentecorrente) throws ServiceException;
}
