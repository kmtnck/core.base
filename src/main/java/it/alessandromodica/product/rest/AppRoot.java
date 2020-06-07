package it.alessandromodica.product.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.alessandromodica.product.services.interfaces.IMainService;
import it.alessandromodica.product.views.AppViewListener;

@ApplicationPath("/services")
public class AppRoot extends Application {

	public static IMainService mainservice;

	public AppRoot() {
		mainservice = AppViewListener.context.getBean(IMainService.class);
	}
}