# goToBusiness nome in codice: core.base
 Un modulo che prende cose e ne fa altre per soddisfare la felicita'
 
# Parametri inossidabili
Si considerano inossidabli quelle configurazioni standard presenti in una canonica applicazione e che subiscono pochissime modifiche nel tempo.

In questo progetto le configurazioni sono su xml o annotation, a seconda le specifiche esigenze.

In elenco sono:

a) parametri di compilazione e deploy tramite gli strumenti maven e gradle

b) parametri di logging come può essere log4j o simili

c) configurazione di accesso ad un datastorage : dominio, username/schema, credenziale.

d) Stringhe magiche e parametri definiti a codice.


## a) Parametri di compilazione e deploy 
 Ci sono alcuni parametri strutturali che definiscono l'uri dell'application server (es Tomcat) per il deploy e il nome dell'artefatto finale.
 
Tra le properties presenti sul pom.xml ce ne sono due che definiscono:
 a) il nome dell'artefatto .war 
 b) l'uri dell'indirizzo manager di tomcat
 	
 	<webappname>appdeployata</webappname>
 		
	<uri.deploytomcat>http://%DOMAIN%:8080/manager/text</uri.deploytomcat>
 
 Per deployare il comando da eseguire è il seguente
 
 	mvn tomcat7:redeploy
 
Con gradle si considerano i parametri del tag di configurazione di cargo, il plugin incaricato al deploy remoto su container
 
	 cargo {
	    containerId = 'tomcat7x'
	    port = 8080

	    deployable {
		context = 'appdeployata'
	    }

	    remote {
		hostname = '%DOMAIN%'
		username = '%USERNAME%'
		password = '%PASSWORD%'
	    }
	}

Per deployare il comando è il seguente

	gradle cargoRedeployRemote
 
## b) Parametri di logging con Log4j
La configurazione log4j è la piu semplice e versatile possibile: è definito allo stesso package common nello scope resource.
 Si ha una predisposizione per essere inizializzato da un yaml
 Può essere automaticamente riconosciuto in qualsiasi classpath del package common, in modo da rendere granulare il suo uso in fase di testing o altri casi specifici. Qui di seguito la locazione presente in questo progetto.

	./src/main/resources/it/alessandromodica/product/common/config/log4j.xml


## c) Configurazione della persistence unit tramite persistence.xml
 In java ci sono molteplici approcci per configurare un datastorage, e ancor di piu con l'introduzione delle configurazioni dinamiche yaml kubernets oriented.
 Questa applicazione si basa sulle annotation JPA e Hibernate. La modalità delle transazione è il classico RESOURCE_LOCAL.
La modalità JTA richiede un minimo di adattamento dei repository, ma necessita di un application server JTA compliance. Attualmente l'application server supportato è tomcat ma potrebbe essere incapsulato in un container cordova in un contesto nodejs.
 
I parametri fondamentali da immettere rimangono comunque i seguenti:

	%URI_DB% connessione di accesso al database, in qualunque forma essa sia
	%USERNAME% username/nomeistanza
	%PASSWORD% la password di accesso

L'applicazione è configurata di default per accedere ad un database su file hdbsql di nome storageapp. Il database mappato sul persistence.xml ed è creato a runtime. La base dati iniziale è già usufruibile per fare junit. Si ha libertà di modificare le coordinate di accesso al databse che si preferisce tramite il persistence.xml.

### Autogenerazione con Hibernate Tools

E' possibile tramite Hibernate Tools, autogenerare i bean a partire dalle tabelle applicando le configurazioni basi consigliate da hibernate tools, utilizzando come file di supporto i .properties e il .cfg di hibernate definito all'uopo.
Successivamente integrare i PO generati sul sorgente dell'applicazione definendoli sul persistence.xml. I PO possono essere customizzati a seconda le esigenze.

Il repository riconoscerà la nuova entità una volta che è stata correttamente caricata dalla unità di persistenza definita, nell'esempio la persistence unit si chiama "appjpa"

## d) Vari parametri in static String

Nella classe 
	
	it.alessandromodica.product.app.GoToBusiness
	
è stata definita una variabile statica in cui è valorizzato il nome dell'app sui log. 

	public static final String TITOLO_APP = "goToBusiness";

Non è fondamentale ai fini dell'applicazione, ma è utile se si vuole dare una personalizzazione ai log.

La classe

	it.alessandromodica.product.common.MagicString
	
definisce tutte le costanti stringhe significative utilizzate dall'applicazione.

# Annotation e policy

In questo progetto si è voluto unire il colpo d'occhio dei package con la definizione di annotation su opportuni bean, in modo tale da unire in un contesto specifico le varie definizioni in annotation, e quindi all'interno del codice sorgente e che necessitano di un nuovo deploy in ogni sua modifica.
L'uso delle annotation si limita pertanto nei seguenti casi:

Al package

	it.alessandromodica.product.model.bo.query

Sono raccolti tutti i bean che vengono ritornati da una chiamata sql legacy pura o qualsiasi sia supportata dalle namequery

	@Entity
	@NamedNativeQuery(name = "utentiLoggati", query = "{ call utentiLoggati(:periodo,:nickname)}", resultClass = BOUtentiConnessi.class)

La classe

	it.alessandromodica.product.common.config.ConfigApp

è annotata come 

	@Configurator
	
istruendo Spring a gestirla come classe di configurazione. 
Per ora è definito il 

	@ComponentScan(basePackages="it.alessandromodica.product")

che istruisce il package di ricerca delle annotation IOC (@Component, @Autowired ...)

La classe GoToBusiness definisce in Autowired i repository per l'accesso ai dati, usati dall'infrastruttura per le varie funzionalità

	@Autowired
	protected IRepositoryQueries repoquery;

	@Autowired
	protected IRepositoryCommands repocommands;

	@Autowired
	protected AppRepository reporawsql;

Si può notare che gli oggetti sono interfacce, e i repository sono classificati in base alle azioni di lettura (queries), scrittura (commands) e accesso sql legacy (store procedure in annotation o name query).

# Come avviare l'applicazione da un semplice hello world

Se il clone git è andato a buon fine, maven è stato configurato per accedere ai piu famosi repository presenti nel globo, allora potrebbe essere sufficiente eseguire alla base del progetto il comando 
 
	 mvn clean install

ciè eseguirà una compilazione con il set di junit andati a buon fine.

Se è tutto ok, allora nel target dovrebbe essere presente un artefatto .war con il nome da voi scelto, di default dovrebbe essere

	appdeployata

Per avviare l'applicazione e il suo contesto spring è sufficiente eseguire le seguenti istruzioni

			MainApplication.InitApp("appjpa");	
			context = new AnnotationConfigApplicationContext(ConfigApp.class);

InitApp istanzia il logger e la persistence unit definita in parametro hardcode. Dopo la sua esecuzione il repository è operativo.

Successivamente viene istanziato il contesto applicativo definito dalla configurazione ConfigApp e quindi l'app è avviata.

La flessibilità è tale da poter creare un junit disaccoppiando il contesto di persistenza dal contesto applicativo vero e proprio.

Il goToBusiness è predisposto per eseguire accessi a app esterne con le canoniche sequenze di autenticazione di un google sign-on e sulla classe MainContext è possibile definire la key_id necessaria per accedere ad un applicazione registrata su appengine

%KEYGOOGLEAPI% è da definire una volta seguite le istruzioni su google sign in on 

Nella classe

	it.alessandromodica.product.app.GoToBusiness

è presente la chiamata al decoder indirizzi di google

	URL resolveAddress = new URL(
			"https://maps.googleapis.com/maps/api/geocode/json?key=%KEYGOOGLEAPI%"
			+ coordinate.getLat() + "," + coordinate.getLon());

In questo caso specifico è utilizzato per accedere ai servizi di geo codifica degli indirizzi, si possono implementare altri sistemi , ma in generale quello che uno vuole. Qui era nato dall'esigenza di ottenere indirizzi codificati al volo.

E predisposto per definire nuovi tipi di accessi ad altri provider di autenticazione.

# Sicurezza 
Sul file 

	login.html

nei metodi

	onSignIn(googleUser)
	signOut()

si adatta l'uri di accesso all'endpoint del servizio applicativo impostando il dominio, il nome del progetto definito nel pom e il nome dell'endpoint definito su web.xml che è "handler"
https://%DOMAIN%/${project.webappname}/handler

Impostazioni sistemistiche piu avanzate possono definire l'uri piu idonea a seconda l'ambiente infrastrutturale.

Se tutto è andato bene il login e logout di un account google dovrebbe riflettersi sulle tabelle ad hoc predisposte.

I metodi

	checkIntegrity
	verificaUtenza

presenti nel servizio

	it.alessandromodica.product.services.SecurityService
	
possono poi essere implementati a seconda le esigenze specifiche. Sono presenti due esempi di implementazione.

# Accesso ai dati con gli oggetti repository

In uno dei junit definiti è possibile vedere il modo in cui è possibile eseguire una lettura dati tramite hibernate, utilizzando il comodo oggetto repository implementato in questo progetto, e vero fulcro innovativo di tutta l'applicazione

			BOLikeClause testSearch = new BOLikeClause();
			testSearch.set_nameField("email");
			testSearch.set_value("alessandro.modica@gmail.com");
			BOSearchApp criteria = new BOSearchApp();
			criteria.get_listLikeClause().add(testSearch);

			List<PluginGestioneUtenti> fromDb = repoquery.setEntity(PluginGestioneUtenti.class)
					.search(criteria.getSerialized());

			if (fromDb.isEmpty())
				Assert.assertTrue("I dati non sono stati trovati, ma la query è stata eseguita correttamente", true);
			else {
				for (PluginGestioneUtenti pluginCommonLogaccesso : fromDb) {

					log.info(pluginCommonLogaccesso.getNickname());
				}
				Assert.assertTrue(true);
			}

L'oggetto BOSearchApp rappresenta la maggior parte delle operazioni di lettura che si può fare su un database. L'esempio mostra come eseguire una ricerca in forma like, in cui si chiede il recupero di dati dalla tabella plugingestioneutenti che abbiano la mia email. In pratica si vuole cercare l'utenza del creatore di questa applicazione.
