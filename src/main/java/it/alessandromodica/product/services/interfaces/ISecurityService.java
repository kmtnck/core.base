package it.alessandromodica.product.services.interfaces;

import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOPayloadAuth;
import it.alessandromodica.product.model.bo.BOSecurity;
import it.alessandromodica.product.model.bo.BOSignGoogle;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.bo.BOVerifica;

public interface ISecurityService {

	public void setUtenteCorrente(BOUtente utenteCorrente);
	
	public boolean checkAccessoUtente() throws ServiceException;
	
	public BOSecurity generaScarabocchio();
	
	public BOSignGoogle outhsignout(String email, String nickname) throws ServiceException;
	
	public BOSignGoogle outhsignin(BOPayloadAuth payload) throws ServiceException;
	
	public BOSecurity checkIntegrity(String versione, boolean isbeta) throws ServiceException;
	
	public BOVerifica verificaUtenza() throws ServiceException;

}
