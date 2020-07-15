package it.alessandromodica.product.restcontroller.interfaces;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.alessandromodica.product.common.exceptions.BusinessException;


/**
 * Controller per la gestione delle fasi di autenticazione
 * @author kmtnck
 *
 * @param <T>
 */
@Api(value = "modulo.autenticazione", description = "Interfaccia per la gestione delle fasi di autenticazione utente")
public interface IAuthController {

	@ApiOperation(value = "Outhsign in google account", response = Object.class)
	public Object outhsignin(@RequestParam Map<String, String> info) throws BusinessException;

	@ApiOperation(value = "Outhsign out google account", response = Object.class)
	public Object outhsignout(@RequestParam Map<String, String> info) throws BusinessException;

}
