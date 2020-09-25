package it.alessandromodica.product.test.application;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.TestSpringConfig;
import it.alessandromodica.product.app.MainApplication;
import it.alessandromodica.product.persistence.interfaces.IRepositoryQueries;
import it.alessandromodica.product.persistence.repo.BaseRepository;
import it.alessandromodica.product.restcontroller.interfaces.IMainController;
import it.alessandromodica.product.services.interfaces.IMainService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
//profilo test deve essere sempre definito in ogni classe junit springboot affinche venga letto application-test.properties e quindi i parametri h2 (importante per non caricare erroneamente il datasource di default che e' oracle)
//si individua lo scope target del test, in questo caso si vuole testare l'intero contesto del'interfaccia imovimentiinternicontroller 
@WebMvcTest(value = IMainController.class)
//si imposta il mockmvc con autenticazione disabilitata
@AutoConfigureMockMvc
//elenco minimo necessario di classe appartenenti alla catena IOC per istanziare correttamente IMovimentiInterniController
@ContextConfiguration(classes = { 
		MainApplication.class, TestSpringConfig.class, BaseRepository.class, IRepositoryQueries.class,RestTemplate.class,IMainService.class
 })
//Annotation che istruisce la classe di test a gestire la sessione db anche nei casi di lista lazy. Permette quindi il recupero delle righe movimento recuperate in LAZY
@Transactional
//@Sql(scripts = "classpath:/basedati_test.sql")
public class TestRestCall {

	/**
	 * MockMvc che interpreta le annotation rest del controller definito su
	 * webmvntest come uri da chiamare in modalita client L'oggetto mock si
	 * autoconfigura con uri valide in base a quanto descritto sulle annotation
	 * requestmapping e predispone chiamate assert rest per verificarne gli stati di
	 * ritorno e i dati forniti
	 */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Oggetto per serializzare oggetti in stringhe json
	 */
	@Autowired
	private ObjectMapper mapper;

	@Test
	public void test()
	{
		
	}
	
	@Test
	public void testController_test() throws Exception {
		String idtest = "5106";

		//Si chiama la uri rest tramite il mock client. E' una simulazione di una chiamata remota
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/entity/test/{id}", idtest)
						.contentType(MediaType.APPLICATION_JSON))
				//si controlla se la chiamata e' andata a buon fine e, se tutto ok, ritorna il mockresult, che viene analizzato dagli assert successivi
				.andExpect(status().isOk()).andReturn();

		assertTrue(result.getResponse().getContentAsString() != null);
		//Conversione del content response recuperato dal mockclient in oggetto . 
		//L'oggetto e' recuperato dalla base dati di test (H2)
		//MovimentoInterno obj = mapper.readValue(result.getResponse().getContentAsString(), MovimentoInterno.class);	
		
		/*assertTrue(obj != null);
		assertEquals(obj.getD2mMovimentiVariTid().longValue(), 7267L);
		assertEquals(obj.getnRow().longValue(), 1);
		assertEquals(obj.getStato().getCodice(), "EL");
		assertEquals(obj.getNumeroAltroMovimento().longValue(), 762);*/
		
		
		
	}
}
