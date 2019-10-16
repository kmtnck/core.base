/*
 * In questo script sono definite le viste e le store procedure minime necessarie per integrare una funzionalità predisposta per essere usufruibile ma che è gestita da un ambiente legacy.
 * 
 * */

DROP TABLE IF EXISTS VUtentiLoggatiDettaglio;
CREATE VIEW VUtentiLoggatiDettaglio AS select idaccesso ,ipaddress ,istante ,descrizione from PluginCommonLogaccesso order by istante desc;

DROP TABLE IF EXISTS VGestioneUtentiOuth;
CREATE
VIEW VGestioneUtentiOuth 
AS 
select
(
	select sessioni.idtoken from PluginGestioneUtentiOauthSessioni sessioni 
	where (sessioni.idouth = utentigoogle.idouth) 
	order by sessioni.istante desc limit 1
) 
AS lastidtoken,utentigoogle.idouth AS idouth,utentigoogle.istante AS dataregistrazioneouth,utentigoogle.foto AS foto,utentigoogle.nomeutente AS nomeutente,utentiapp.nickname AS nickname,utentigoogle.email AS emailaccount,utentigoogle.nickname AS nicknameoauth,utentiapp.idutente AS idutente,utentiapp.email AS emailapp,utentiapp.istante AS dataregistrazioneapp,utentiapp.publickey AS publickey,utentiapp.privatekey AS privatekey,(case when (utentigoogle.email = utentiapp.email) then 'VERIFICATO' when (utentiapp.email is not null) then 'APPROVATO' when isnull(utentiapp.email) then 'EMAIL MANCANTE' when (utentigoogle.email <> utentiapp.email) then 'MISMATCH' else 'ACCESSO NEGATO' end) AS stato from (PluginGestioneUtenti utentiapp left join PluginGestioneUtentiOauth utentigoogle on(((utentiapp.email = utentigoogle.email) or (utentiapp.nickname = utentigoogle.nickname))));


DROP TABLE IF EXISTS VGestioneUtenti;
CREATE 
VIEW VGestioneUtenti 
AS 
select utenti.idutente AS idutente,utenti.nickname AS nickname,utenti.istante AS dataregistrazione,utenti.email AS email,concat(utenti.publickey,'',utenti.privatekey) AS apptoken,utenti.publickey AS publickey,utenti.privatekey AS privatekey,infoauth.valueparametro AS csrftoken,oauth.dataregistrazioneouth AS dataregistrazioneoauth,oauth.foto AS foto,oauth.nomeutente AS nomeutente,oauth.emailaccount AS emailaccount,oauth.stato AS stato 
from 
(
	(	PluginGestioneUtenti utenti 
		join 
		PluginGestioneUtentiInfoautenticazione infoauth) 
		left join 
		VGestioneUtentiOuth oauth on((utenti.idutente = oauth.idutente))
)
where ((utenti.idutente = infoauth.idutente) 
and (infoauth.contesto = 'cookie') 
and (infoauth.nomeparametro = 'csrftoken'));


