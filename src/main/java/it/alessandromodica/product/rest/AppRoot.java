package it.alessandromodica.product.rest;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.alessandromodica.product.persistence.interfaces.IRepositoryCommands;
import it.alessandromodica.product.persistence.interfaces.IRepositoryQueries;
import it.alessandromodica.product.views.AppViewListener;

//@Component
@ApplicationPath("/services")
public class AppRoot extends Application {

	public static IRepositoryQueries reader;

	public static IRepositoryCommands writer;
	
	public AppRoot()
	{
		reader = AppViewListener.context.getBean(IRepositoryQueries.class);
		writer = AppViewListener.context.getBean(IRepositoryCommands.class);
	}
}