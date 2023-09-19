# StudCar

StudCar – studentische Mitfahrgelegenheiten ist eine Webanwendung, die es Studierenden
ermöglicht andere Studierende in ihren privaten Fahrzeugen zur Hochschule mitzunehmen.
Die Idee ist dabei ähnlich wie die von BlaBlaCar. Der signifikante Unterschied besteht dabei darin,
dass StudCar zum einen vollständig kostenlos ist und sich zum anderen ausschließlich nur für Fahrten zur
Hochschule Osnabrück eignet. Diese Anwendung wurde von Andreas Morasch und Hendrik Purschke im Rahmen der
Veranstaltung Software Architektur an der Hochschule Osnabrück implementiert.

## Installation und Start der Anwendung

Zunächst muss die Anwendung lokal auf Ihrem Rechner vorliegen. Dazu navigieren Sie in das StudCar Repository in GitLab.
Klicken Sie nun auf der rechten Seite auf "Clone" und kopieren Sie von dort aus den HTTPS Link.

Sie können nun einen beliebigen Speicherort auf Ihrem lokalen Rechner wählen. Navigieren Sie zu diesem Speicherort über die Konsole
mithilfe von "cd _der Pfad zu Ihrem Speicherort_". Schreiben Sie nun in die Konsole "git clone _hier HTTPS Link einfügen_" um das
Projekt lokal auf Ihren Rechner zu laden.

Sie können nun in der Konsole in das Projekt gehen indem Sie erneut "cd _der Pfad zu Ihrem Speicherort_\StudCar" eingeben.
Nun befinden Sie sich im inneren des Projektes und können dieses von hier aus starten.

Damit die Anwendung läuft benötigt diese eine laufende Dockerengine. Dazu muss Docker in der Regel erst gestartet werden.

Sie haben nun die Möglichkeiten das Projekt mithilfe von "./mvnw compile" zu kompilieren, "./mvnw test" Tests auszuführen und
mit "./mvnw compile quarkus:dev" zu starten. Die Anwendung läuft dann über ihren Rechner auf localhost:8080, welchen Sie im Browsertab
eingeben müssen um zum UserInterface zu gelangen.

Sie werden nun von Keycloak dazu aufgefordert Nutzerdaten einzugeben. Wählen Sie dazu einen beliebigen Nutzer aus der folgenden Auflistung aus. Alle Nutzer haben die gleichen Berechtigungen und können auf alles Zugreifen.

Nutzer: alice & Passwort: alice
Nutzer: jdoe & Passwort: jdoe
Nutzer: jsmith & Passwort: jsmith

Die einzelnen Schnittstellen der Anwendung lassen sich über OpenAPI und SwaggerUI einsehen. Dabei sind die Pfade, jeweils über den localhost:8080/,
die Folgenden:

openAPI: openapi/?format=json
SwaggerUI: swaggerui/

## Referenzen

- Die quarkus-realm.json kommt in ihrer initialen Form von https://github.com/quarkusio/quarkus-quickstarts/blob/main/security-keycloak-authorization-quickstart/config/quarkus-realm.json
