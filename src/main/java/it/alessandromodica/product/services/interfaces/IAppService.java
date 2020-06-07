package it.alessandromodica.product.services.interfaces;

import java.util.List;

import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.po.VUtentiLoggatiDettaglio;

public interface IAppService {
	
	public List<VUtentiLoggatiDettaglio> getUtentiConnessi() throws ServiceException;
	
}
