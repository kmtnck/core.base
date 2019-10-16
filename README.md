# goToBusiness nome in codice: core.base
 Un modulo che prende cose e ne fa altre per soddisfare la felicita'
 
# Parametri inossidabili
Si considerano inossidabili quelle configurazioni strutturali che sono comuni in qualsiasi applicazione e che richiedono un punto preciso su cui definirle.

In questo progetto le configurazioni sono miste su xml o annotation a seconda le specifiche esigenze, in ogni caso possono essere "traslate" su qualsiasi altra configurazione strutturale dinamica. I parametri inossidabili rimangono invariati per la maggior parte del tempo o in ogni caso subiscono poche modifiche al netto di una prima configurazione.

In elenco sono:

a) parametri di deploy tramite plugin di maven o gradle

b) parametri di logging

c) configurazione di accesso ad un datastorage : dominio, username/schema, credenziale. In generale si considera una generica connection 
string . Nel nostro caso il file di configurazione è persistence.xml

d) Stringhe magiche e parametri definiti a codice.


## a) Parametri di compilazione e deploy con maven o gradle
 Ci sono alcuni parametri strutturali che ne definiscono gli accessi al deploy dell'application server (per ora tomcat) e il nome della web application frontend.
 
 Tra le properties definite sul pom.xml ce ne sono due che definiscono il nome dell'artefatto .war (e relativo uri deployato) e l'uri dell'indirizzo di tomcat o dell'application server cui eseguire il deploy. Questa fase può essere gestita da un continuoos delivery opportuno
 	
 	<webappname>appdeployata</webappname>
 		
	<uri.deploytomcat>http://%DOMAIN%:8080/manager/text</uri.deploytomcat>
 
 Per deployare il comando da eseguire è il seguente
 
 	mvn tomcat7:redeploy
 
 Per la compilazione e deploy in remoto tomcat con gradle i parametri da considerare sono il tag di configurazione di cargo, il plugin incaricato al deploy remoto su container
 
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
 
## b) Log4j
 La configurazione log4j è la piu semplice e versatile possibile, ossia definito allo stesso package common nello scope resource.
 I vantaggi sostanziali sono due: 
 1) predisposizione a essere inizializzato da un yaml
 2) può essere automaticamente riconosciuto in qualsiasi classpath del package common, in modo da rendere granulare il suo uso in fase di testing o altri casi specifici.

	./src/main/resources/it/alessandromodica/product/common/config/log4j.xml


## c) Configurazione delle unità di persistenza su file persistence.xml
 In java ci sono molteplici approcci per configurare un datastorage, e ancor di piu con l'introduzione delle configurazioni dinamiche yaml kubernets oriented.
 Questa applicazione si basa sulle java EE annotation per la persistenza di oggetti ejb con framework Hibernate all'ultima release di cui si scrive questo README. L'approccio delle transazione è il classico RESOURCE_LOCAL.
 Un supporto JTA richiede un minimo adattamento dei repository, ma necessita di un application server JTA compliance. Attualmente l'application server supportato è il canonico tomcat/nodejs/cordova. L'ambiente enterprise è stato predisposto in evoluzioni future.
 
I parametri fondamentali da immettere rimangono comunque i seguenti:

	%URI_DB% connessione di accesso al database, in qualunque forma essa sia
	%USERNAME% username/nomeistanza
	%PASSWORD% la password di accesso

L'applicazione è configurata di default per accedere ad un database su file hdbsql di nome storageapp. Il database mappato sul persistence.xml ed è creato a runtime. La base dati iniziale è già usufruibile per fare junit.

### Autogenerazione con Hibernate Tools

E' possibile tramite Hibernate Tools, autogenerare i bean a partire dalle tabelle applicando le configurazioni basi consigliate da hibernate tools, utilizzando come file di supporto i .properties e il .cfg di hibernate definito all'uopo.
Successivamente integrare i PO generati sul sorgente dell'applicazione definendoli sul persistence.xml. I PO possono essere customizzati a seconda le esigenze.

Il repository riconoscerà la nuova entità una volta che è stata correttamente caricata dalla unità di persistenza definita, nell'esempio la persistence unit si chiama "appjpa"

## d) Vari parametri in static String

Nella classe 
	
	it.alessandromodica.product.app.GoToBusiness
	
è stata definita una variabile statica in cui è valorizzato il nome dell'app sui log. 

	public static final String BRUTALE_VARIABILE_NOME_APP = "[bla bla]";

Non è fondamentale ai fini dell'applicazione, ma è utile se si vuole dare una personalizzazione ai log, ora dice bla bla bla

Esiste la classe

	it.alessandromodica.product.common.MagicString
	
in cui sono definite tutte le costanti stringhe significative utilizzate da un applicativo.

# Annotation e policy

In questo progetto si è voluto unire il colpo d'occhio dei package con la definizione di annotation su opportuni bean, in modo tale da unire in un contesto specifico le varie definizioni in annotation, e quindi all'interno del codice sorgente e che necessitano di un nuovo deploy in ogni sua modifica.
L'uso delle annotation si limita pertanto nei seguenti casi:

Al package

	it.alessandromodica.product.common.bo.query

Sono raccolti tutti i bean che vengono ritornati da una chiamata sql legacy pura o qualsiasi sia supportata dalle namequery

	@Entity
	@NamedNativeQuery(name = "utentiLoggati", query = "{ call utentiLoggati(:periodo,:nickname)}", resultClass = BOUtentiConnessi.class)

La classe

	it.alessandromodica.product.common.config.ConfigApp

è annotata come 

	@Configurator
	
istruendo Spring a gestirla come classe di configurazione. 
Per ora è definito il 

	@ComponentScan

che istruisce il package di ricerca delle annotation IOC (@Component, @Autowired ...)

L'annotation ComponentScan è importante per veicolare lo scanning dei bean Spring IOC sia  nell'istanziarli a startup, sia nell'iniettarli tramite le dipendenze Autowired.

Nota: Possono essere previsti vari configurator, ma di norma ne è sufficiente uno. 

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
