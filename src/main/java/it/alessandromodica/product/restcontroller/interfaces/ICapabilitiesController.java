package it.alessandromodica.product.restcontroller.interfaces;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.alessandromodica.product.common.exceptions.BusinessException;

@Api(value = "modulo.capabilities", description = "Interfaccia in cui implementare gli endpoint specifica dell'applicazione")
public interface ICapabilitiesController {

	@ApiOperation(value = "Metodo per l'esecuzione di tutti i servizi supportati dall'applicazione", response = Object.class)
	public Object capabilities(@RequestParam Map<String, String> info, @RequestHeader Map<String, String> headers)
			throws BusinessException;

}
