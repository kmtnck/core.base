package it.alessandromodica.product.common.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import it.alessandromodica.product.app.GoToBusiness;
import it.alessandromodica.product.app.MainApplication;
import it.alessandromodica.product.common.exceptions.BusinessException;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = "it.alessandromodica.product")
//@EnableWebMvc
@EnableTransactionManagement
public class AppConfig {

	/**
	 * Metodo per inizializzare i parametri di configurazione del client sso
	 * 
	 * Inizializza il logger Definisce i parametri di configurazione usati
	 * dall'applicazione
	 * 
	 * @throws BusinessException
	 * 
	 */
	public AppConfig() throws BusinessException {
		// InitApp();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://svr1.alessandromodica.com/aless175_gotobusiness?useSSL=true");
		dataSource.setUsername("aless175_kmtnck");
		dataSource.setPassword("Nsl80cdm");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "it.alessandromodica.product.model.po" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	 
	    return transactionManager;
	}
	 
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	 
	Properties additionalProperties() {
	    Properties properties = new Properties();
	    properties.setProperty("hibernate.hbm2ddl.auto", "update");
	    properties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.mysql.MySQL56SpatialDialect");
	    properties.setProperty("hibernate.show_sql", "true");
	    
	    return properties;
	}

	public static void InitApp() throws BusinessException {

		GoToBusiness.TITOLO_APP = "Una nuova app che fa cose";

		MainApplication.InitApp("appjpa-mysql");

	}

}
