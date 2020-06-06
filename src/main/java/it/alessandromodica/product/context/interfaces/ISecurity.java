package it.alessandromodica.product.context.interfaces;

import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.model.bo.BOPayloadAuth;
import it.alessandromodica.product.model.bo.BOSecurity;
import it.alessandromodica.product.model.bo.BOSignGoogle;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.bo.BOVerifica;

public interface ISecurity {

	
	
	public boolean checkAccessoUtente() throws BusinessException;

	public BOSecurity checkIntegrity(String versione, boolean isbeta) throws BusinessException;

	public BOSecurity generaScarabocchio();

	public BOVerifica verificaUtenza() throws BusinessException;

	public BOSignGoogle outhsignin(BOPayloadAuth payload) throws BusinessException;

	public BOSignGoogle outhsignout(String email, String nickname) throws BusinessException;
	
	public BOUtente getUtenteCorrente();
	
	public void setUtenteCorrente(BOUtente utente);
	
}
