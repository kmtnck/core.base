package it.alessandromodica.product.context.interfaces;

import it.alessandromodica.product.common.InputRequest;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.model.bo.BOAppPayload;
import it.alessandromodica.product.model.bo.BOUtente;

public interface IConsultazione {

	public void setInfo(BOUtente utente, InputRequest inputData);
	
	public BOAppPayload getUtentiConnessi() throws BusinessException;
	
}
