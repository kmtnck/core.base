package it.alessandromodica.product.context.interfaces;

import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.model.bo.BOAppPayload;
import it.alessandromodica.product.model.bo.BORequestData;
import it.alessandromodica.product.model.bo.BOUtente;

public interface IConsultazione {

	public void setInfo(BOUtente utente, BORequestData inputData);
	
	public BOAppPayload getUtentiConnessi() throws BusinessException;
	
}
