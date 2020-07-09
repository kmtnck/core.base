# goToBusiness nome in codice: core.base
 Un modulo che prende cose e ne fa altre per soddisfare la felicita' .
 
 Una piu' ampia documentazione in stile javadoc e rigorosamente autogenerata la trovate qui https://alessandromodica.com/javadoc/gotobusiness/
(non e' sempre aggiornata, ma si puo autogenerare)
 
# Parametri standard
Si considerano standard quelle configurazioni presenti in una canonica applicazione e che subiscono pochissime modifiche nel tempo.

In questo progetto le configurazioni sono su xml o annotation, a seconda le specifiche esigenze. 

In elenco sono:

a) parametri di compilazione e deploy tramite gli strumenti maven e gradle

b) parametri di logging come puo' essere log4j o simili

c) configurazione di accesso ad un datastorage : dominio, username/schema, credenziale.

d) Stringhe magiche e parametri definiti a codice.


## a) Parametri di compilazione e deploy 

 Ci sono alcuni parametri strutturali che definiscono l'uri dell'application server (es Tomcat) per il deploy e il nome dell'artefatto finale.
 
Tra le properties presenti sul pom.xml ce ne sono due che definiscono:
 a) il nome dell'artefatto .war 
 b) l'uri dell'indirizzo manager di tomcat
 	
 	<webappname>appdeployata</webappname>
	<uri.deploytomcat>http://%DOMAIN%:8080/manager/text</uri.deploytomcat>
 
 Per deployare il comando da eseguire e' il seguente
 
 	mvn tomcat7:redeploy
 
Solo per gradle:
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

Per deployare il comando e' il seguente

	gradle cargoRedeployRemote
 
## b) Parametri di logging con logback.xml
La configurazione log4j e' la piu semplice e versatile possibile.
 
	./src/main/resources/logback.xml


## c) Configurazione dell'accesso ai dati
 In java ci sono molteplici approcci per configurare un datastorage, e ancor di piu con l'introduzione delle configurazioni dinamiche yaml kubernets oriented.
 
 Questa applicazione si basa sulle annotation JPA e Hibernate. La modalita'  delle transazione e' il classico RESOURCE_LOCAL, ma sostanzialmente si puo configurare l'accesso al datasource (o piu datasource) nelle modalita piu congeniali.
 
Tuttavia la modalita' JTA richiede un minimo di adattamento dei repository che in questa release e' in una libreria separata e supporta le annotation springboot. Una libreria ad hoc yarepository con supporto delle jta implementation permettera di avviarlo anche in un contesto EE.

A titolo di esempio si e' scelto come application server il "semplice" Tomcat, ma la scelta dell'AP e' ininfluente ai fini di questo progetto.
 
I parametri fondamentali da immettere rimangono comunque i seguenti:

	%URI_DB% connessione di accesso al database, in qualunque forma essa sia
	%USERNAME% username/nomeistanza
	%PASSWORD% la password di accesso

Questi parametri potrebbero risiedere in un file yaml o recuperati dinamicamente a fronte di opportune chiamate, in questo progetto i parametri sono raccolti nel file datasource.properties e sono letti dalla classe di configurazione AppConfig.

L'applicazione e' configurata di default per accedere ad un database su file hdbsql di nome storageapp.
Il database mappato sul persistence.xml ed e' creato a runtime. La base dati iniziale e' gia'  usufruibile per fare junit.

### Autogenerazione con Hibernate Tools

E' possibile tramite Hibernate Tools, autogenerare i bean a partire dalle tabelle applicando le configurazioni basi consigliate da hibernate tools, utilizzando come file di supporto i .properties e il .cfg di hibernate definito all'uopo.
Consultare le guide sul best practise riguardo il file reveng.xml, per una autogenerazione il piu' possibile aderente alla base dati su cui si deve lavorare.
Successivamente integrare i PO generati sul sorgente dell'applicazione definendoli sul persistence.xml. I PO possono essere customizzati a seconda le esigenze.
La autogenerazione e' da intendersi come un supporto allo sviluppatore impedendoli di scrivere "boiled" code comunque necessario, e modificare le personalizzazioni che servono.

Di fatto il yarepository di cui fa uso l'applicazione dimostrativa goToBusiness riconosce un qualsiasi insieme di entita hibernate valide indicate nel packageToScan property.


## d) Vari parametri in static String

La classe

	it.alessandromodica.product.common.MagicString
	
definisce tutte le costanti stringhe significative utilizzate dall'applicazione.

# Annotation e policy

In questo progetto si e' voluto unire il colpo d'occhio dei package con la definizione di annotation su opportuni bean, in modo tale da unire in un contesto specifico le varie definizioni in annotation, e quindi all'interno del codice sorgente e che necessitano di un nuovo deploy in ogni sua modifica.
L'uso delle annotation si limita pertanto nei seguenti casi:

Al package

	it.alessandromodica.product.model.po

Sono raccolti tutti i bean scannerizzati da Spring in fase di avvio


Esempio di dichiarazione di una storeprocedure, in generale si utilizzano le annotation hibernate per "decorare" il repository con opportuni oggetti dba ad esempio store procedure o sequence id

	@Entity
	@NamedNativeQuery(name = "utentiLoggati", query = "{ call utentiLoggati(:periodo,:nickname)}", resultClass = BOUtentiConnessi.class)

La classe

	it.alessandromodica.product.common.config.AppConfig

e' annotata come 

	@Configurator
	
istruendo Spring a gestirla come classe di configurazione. 
Lo scanning delle classi puo' avvenire anche con la seguente annotazione

	@ComponentScan(basePackages="it.alessandromodica.product")

ma per questo progetto si e' scelta la strada di personalizzare il package da file di configurazione.

Per ottenere a scope applicativo i repository di lettura e scrittura e' sufficiente dichiararli come oggetto iniettato (con Spring Autowired)

	@Autowired
	protected IRepositoryQueries repoquery;

	@Autowired
	protected IRepositoryCommands repocommands;

	@Autowired
	protected AppRepository reporawsql;

Si puo notare che gli oggetti sono interfacce, e i repository sono classificati in base alle azioni di lettura (queries), scrittura (commands) e accesso sql legacy (store procedure in annotation o name query).

# Come avviare l'applicazione da un semplice hello world

Se il clone git e' andato a buon fine, maven e' stato configurato per accedere ai piu famosi repository presenti nel globo, allora potrebbe essere sufficiente eseguire alla base del progetto il comando 
 
	 mvn clean install

che esegue  una compilazione con il set di junit andati a buon fine.

Se tutto e' ok, allora nel target dovrebbe essere presente un artefatto .war con il nome da voi scelto, di default dovrebbe essere

	appdeployata

Per avviare l'applicazione da un junit o un main application e' sufficiente eseguire le seguenti istruzioni

			context = new AnnotationConfigApplicationContext(AppConfig.class);

Il contesto spring e' configurato con la classe AppConfig e si ha quindi accesso a tutti gli oggetti iniettati, repository compresi

		repocommand = context.getBean(IRepositoryCommands.class);
		repoquery = context.getBean(IRepositoryQueries.class);
		
La flessibilita'  e' tale da poter creare un junit disaccoppiando il contesto di persistenza dal contesto applicativo vero e proprio.

Sono predisposti handler per eseguire accessi a app esterne con le canoniche sequenze di autenticazione di un google sign-on e sulla classe MainContext Ã¨ possibile definire la key_id necessaria per accedere ad un applicazione registrata su appengine

%KEYGOOGLEAPI% e' da definire una volta seguite le istruzioni su google sign in on 


Nei casi in cui e' necessario acceedere ad esempio ad un decoder address di google si passa questo keygoogleapi

	URL resolveAddress = new URL(
			"https://maps.googleapis.com/maps/api/geocode/json?key=%KEYGOOGLEAPI%"
			+ coordinate.getLat() + "," + coordinate.getLon());


E predisposto per definire nuovi tipi di accessi ad altri provider di autenticazione.

# Sicurezza 
Sul file 

	login.html

nei metodi

	onSignIn(googleUser)
	signOut()

si adatta l'uri di accesso all'endpoint del servizio applicativo impostando il dominio, il nome del progetto definito nel pom e il nome dell'endpoint definito su web.xml che e' "handler"
https://%DOMAIN%/${project.webappname}/handler

Impostazioni sistemistiche piu avanzate possono definire l'uri piu idonea a seconda l'ambiente infrastrutturale.

Se tutto e' andato bene il login e logout di un account google dovrebbe riflettersi sulle tabelle ad hoc predisposte.

I metodi

	checkIntegrity
	verificaUtenza

presenti nel servizio

	it.alessandromodica.product.services.SecurityService
	
possono poi essere implementati a seconda le esigenze specifiche. Sono presenti due esempi di implementazione.

# Accesso ai dati con gli oggetti repository

In uno dei junit definiti e' possibile vedere il modo in cui Ã¨ possibile eseguire una lettura dati tramite hibernate, utilizzando il comodo oggetto repository implementato in questo progetto, e vero fulcro innovativo di tutta l'applicazione

			YAFilterLikeClause testSearch = new YAFilterLikeClause();
			testSearch.setNameField("email");
			testSearch.setValue("alessandro.modica@gmail.com");
			YAFilterSearchApp criteria = new YAFilterSearchApp(GestioneUtenti.class);
			criteria.getListLikeClause().add(testSearch);

			List<GestioneUtenti> fromDb = repoquery
					.search(criteria.getSerialized());

			if (fromDb.isEmpty())
				Assert.assertTrue("I dati non sono stati trovati, ma la query è stata eseguita correttamente", true);
			else {
				for (GestioneUtenti pluginCommonLogaccesso : fromDb) {

					log.info(pluginCommonLogaccesso.getNickname());
				}
				Assert.assertTrue(true);
			}

L'oggetto YaFilterSearchApp rappresenta la maggior parte delle operazioni di lettura che si puo' fare su un database. L'esempio mostra come eseguire una ricerca in forma like, in cui si chiede il recupero di dati dalla tabella plugingestioneutenti che abbiano la mia email. In pratica si vuole cercare l'utenza del creatore di questa applicazione.

# YaFilter

Yet Another Filter Search! Esatto! Un altro ancora!


In questo documento sintetico si vuole descrivere l'utilizzo di un pojo java finalizzato alla composizione dinamica di query HQL hibernate (usando i Criteria), senza però usare una riga di codice di hibernate.

L'obiettivo del pojo e' raccogliere le clausole definite al suo interno, serializzare in modo comodo i risultati di clausola, e infine far generare al repository la Query hibernate rispettando al meglio la sintassi HQL, da immettere nell'entitymanager.

E' un filtro studiato per ottenere risultati da una base dati mappata su hibernate, in base ad arbitrarie e complesse query, il più possibili aderenti alle esigenze specifiche, ma usando un autogeneratore standard e riusabile, oltre che tutto incapsulato in un pojo, e usato solo dal repository.



## Il risultato è il vantaggio di comporre ricerche hibernate senza usare hibernate.



E' richiesta una minima conoscenza del funzionamento di hibernate e delle piu comuni regole di composizione di una query a partire da criteria. Negli anni queste interfacce cambiano , anche profondamente nel tempo, per decisioni implementative di chi sviluppa hibernate stesso. Questo filter e' progettato per aggiornarsi facilmente alle nuove specifiche delle versioni successive di hibernate, con minime modifiche al generatore query. 

La prima release funzionante di un repository progettato attorno a questo Filter (ai tempi BOSearcher), risale al 2007, su piattaforma dotnet, framework 4. Successivamente fu riusato in ambito java (dopo porting) nel 2010 fino al 2014 su vari progetti in Java, tutt'ora utilizzati. In tutti i casi l'adozione di questo filtro ha permesso tempi di sviluppo efficaci e stabili per progetti arbitrariamente complessi.

Il pojo è intuitivo e ha bisogno di poche accortezze, che, nel tempo, sono state inquadrate fin dal momento in cui si istanzia il pojo.



La regola generale è che una istanza di un FilterSearch deve essere sempre associato a una entita hibernate su cui eseguire la ricerca, altrimenti errore null exception.

La maggior parte delle clausole sono facili inserimenti di coppie field/valore come ad esempio nelle uguaglianze, la lista di campi che non devono essere nulli o viceversa ecc... ecc... . Tuttavia alcune clausole, per la loro stretta natura di requisiti input, sono organizzati con pojo filter specifici per quel tipo di clausola.

Tutte le singole clausole sono gestite dal filter come lista di occorrenze, quindi , al netto di anomalie, le clausole sono sempre gestite in multi occorrenza, le quali vengono concatenate con logica AND dal generatore interno.

Se si vuole imporre una clausola di or, è sufficiente creare due o piu filtersearch separati in cui ciascuno esprimere i casi da associare in OR. Questo sistema permette la creazione di alberature di query.



### Per chi è destinato.

Programmatori che hanno la necessita di comporre query arbitrariamente complesse usando i criteria hibernate.



### A cosa serve il YaFilterSearcher

A usare i criteria hibernate usando un semplice insieme di pojo java.



### Come si usa

E' un oggetto pojo tradizionale serializzabile in cui è possibile popolare delle strutture dati per accogliere clausole di ricerca tipiche di un database.

Si istanzia il filtro dando come argomento la classe dell'entita su cui eseguire la ricerca (la root di hibernate)

Dopo aver compilato il filtro si passa al metodo repository.search(searcher.getSerialized())



### Di cosa ha bisogno

L'unico requisito per poter funzionare è avere i dati di input come richiesti dal chiamante , e che per ciascuno di essi sia supportata la clausola di ricerca dal filtro (il campo correttamente mappato, che non sia di tipo formula, ecc... ecc...).



### Ha requisiti di framework dell'applicativo su cui opera

No! L'unico requisito, oltre a una implementazione JPA definita a livello di progetto,  è che abbia i dati in input a disposizione per la costruzione del filter e che sia presente in libreria il BaseRepository che riconosce il filtro, oltre ad hibernate ovviamente.

L'oggetto searcher è disaccoppiato da ogni logica di come viene trasportato il dato in input e in output. Lo scopo è accedere ai risultati del repository con un criteria hibernate ben formato e arbitrariamente complesso senza usare i criteria.



### Quali clausole supporta

supporto join (beta)

uguaglianza

between tra tipi di oggetti rangeable

like

like insensitive

confronto con operatori ( =, !=, <, <=, >=)

isnull

isnotnull

iszero

isnotempty

confronto di un booleano

concatenare piu filter in or (cluster di query hql concatenate in or)

order by controllo delle clausole di desc asc per singolo campo in order

maxresult (paginazione)

firstresult (paginazione)

lista campi in proiezione (tracciato dati sottoinsieme di quello dell'entita)

clausola in

clausola notIn

clausola di negazione