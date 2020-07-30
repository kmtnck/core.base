package it.alessandromodica.product.restcontroller;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.alessandromodica.product.app.MainApplication;
import it.alessandromodica.product.common.InputData;
import it.alessandromodica.product.common.MagicString;
import it.alessandromodica.product.common.enumerative.AppContext;
import it.alessandromodica.product.common.enumerative.RequestVariable;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.restcontroller.interfaces.IAuthController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController implements IAuthController {

	@Autowired
	private MainApplication mainApp;

	@RequestMapping(value = "/outhsignin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Object outhsignin(@RequestParam Map<String, String> info) throws BusinessException {

		return submitOuthSignGoogle(MainController.getIfContain(info, RequestVariable.email.name()),
				MainController.getIfContain(info, RequestVariable.nickname.name()),
				MainController.getIfContain(info, MagicString.ProtocolloApp.P_PAYLOADAUTH), AppContext.outhsignin);

	}

	@RequestMapping(value = "/outhsignout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Object outhsignout(@RequestParam Map<String, String> info) throws BusinessException {

		return submitOuthSignGoogle(MainController.getIfContain(info, RequestVariable.email.name()),
				MainController.getIfContain(info, RequestVariable.nickname.name()),
				MainController.getIfContain(info, MagicString.ProtocolloApp.P_PAYLOADAUTH), AppContext.outhsignout);

	}

	private Object submitOuthSignGoogle(String email, String nickname, String payload, AppContext context)
			throws BusinessException {

		InputData inputData = new InputData();
		inputData.getMapRequestData().put(RequestVariable.email, email);
		inputData.getMapRequestData().put(RequestVariable.nickname, nickname);

		return mainApp.processSignInOutGoogle(inputData, payload, context);
	}

}
