package it.alessandromodica.product.context.interfaces;

import it.alessandromodica.product.common.OutputData;
import it.alessandromodica.product.common.InputData;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.model.bo.BOUtente;

public interface IConsultazione {

	public void setInfo(BOUtente utente, InputData inputData);
	
	public OutputData getUtentiConnessi() throws BusinessException;
	
}
