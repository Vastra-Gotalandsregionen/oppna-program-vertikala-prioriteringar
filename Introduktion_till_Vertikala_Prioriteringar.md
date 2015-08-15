# Introduktion #

![https://oppna-program-vertikala-prioriteringar.googlecode.com/svn/wiki/images/start-screen.png](https://oppna-program-vertikala-prioriteringar.googlecode.com/svn/wiki/images/start-screen.png)

Vertikala Prioriteringar är en webbapplikation som ska stödja Medicinska prioriteringar inom vården. Sektorsråden, sakkunniga inom avgränsade vårdområden (exempelvis infektion eller Barn- ungdompsykiatri), som utför prioriteringarna kan logga in på portalen och lägga till/ändra/publicera prioriteringsunderlagen.

Ett arbetsflöde gör det möjligt att dela upp ansvaret, en mindre grupp kan lägga in förslag på ändring i systemet som sedan godkänns för publicering gemensamt i sektorsrådet. Publicering innebär att dokumentationen märks som godkänd, att uppgifterna bedömts som fullständiga. Efter det kan även användare utan specifika rättigheter till systemet gå in och läsa materialet.

Användarnas rättigheter och deras medlemskap till vissa prioriteringsråd styr vilken data de kan se och ändra i systemet. Administrationen av användare sköts av administratörer genom gui:t i applikationen.

# Medicinska prioriteringar #
Prioriteringar inom en viss medicinsk specialitet, till exempel val av behandling för en enskild patient eller att behandla en patient före en annan kallas vertikala medicinska prioriteringar. Dessa prioriteringar görs av personalen i hälso- och sjukvården.

I Västra Götalandsregionen har de medicinska sektorsråden utarbetat prioriteringar inom de medicinska specialiteterna. Detta har gjorts på ett systematiskt sätt utifrån samma grundläggande principer. Dessa medicinska prioriteringar har börjat tillämpas i vården.

Riksdagens fyra prioriteringsgrunder och den etiska plattformen ligger till grund för arbetet. Utöver detta ingår följande uppgifter och principer i de medicinska prioriteringarna.

## Symtom/diagnos ##
  * Angelägenhetsgrad utifrån patientens tillstånd
  * Typ av åtgärd, till exempel operation eller medicinsk behandling
  * Åtgärdens effekt, nytta och eventuell risk för patienten
  * Evidens; vetenskapliga belägg för effekt
  * Åtgärdens nytta i relation till kostnaden; hälsoekonomisk värdering
  * Medicinsk acceptabel väntetid (antal veckor) till besök eller behandling
  * Vårdnivå - egenvård, primärvård, länssjukvård, högspecialsiserad vård

Resultatet av denna process är förteckningar där respektive medicinsk specialitet har dokumenterat sina prioriteringar. Klicka i det grå fältet till vänster för prioriteringarna inom varje medicinsk specialitet.


# Teknisk översikt #
Vertikala prioriteringar är implementerad som en webbapplikation med hjälp av Spring MVC. Användargränssnittet är utvecklat för att fungera i de senare versionerna av Chrome, Firefox och Internet Explorer samt i Internet Explorer 7 (som för närvarande är standard i målorganisationen).

I enlighet med övriga webb-baserade system inom VGR är inte VP beroende av att användaren har JavaScript påslaget. Gränssnittet tillförs extra funktionalitet om man har det på men det är inte nödvändigt för att kunna söka och se den inlagda informationen. Skriptkod som finns baseras på Javascript-ramverket YUI3, vilket borgar för att koden ska kunna köras i flera olika webbläsarimplementationer.

Applikationen körs i en Apache Tomcat webbserver. Inga specifika beroenden finns till denna driftmiljö. Applikationen kan förmodligen köras i vilken annan Java/Webb-miljö som helst.

Inmatad data lagras i en relationsdatabas. I nuläget drivs både utvecklingsmaskiner och skarp miljö av PostgreSQL installationer. Anslutning till databasen görs med Hibernate och JPA som programmeringsinterface. Alla frågor som skickas ifrån applikationen har gjorts i JPQL. De här ramverken och standarderna gör det lätt att byta databassystem i framtiden.

## Starta en egen VP-server ##
För att starta en egen instans av Vertikala Prioriteringar genomför steg 0-4 nedan:
> 0, Ladda hem källkod.
Använd en subversionklient för att ladda ned källkoden lokalt. Se 'Source'-fliken på projektets sida.
  1. Konfigurera databaskoppling.
Skapa/ändra properties-filerna security.properties och datasource.properties under biblioteket
/oppna-program-vertikala-prioriteringar\trunk\core-bc\modules\web\src\main\resources\
'security.properties' innehåller lösenord och användare för anslutningen till databasen.
```

database.user=scott
database.password=tiger
```
Exempelvis.
'datasource.properties' håller andra uppgifter om databaskopplingen och konfiguration av Hibernate och JPA.

```

database.driver=org.postgresql.Driver
database.url=jdbc:postgresql:vertikala_prioriteringar
hibernate.database.showsql=false
hibernate.vendor.generateDdl=true
hibernate.vendor.databasePlatform=org.hibernate.dialect.PostgreSQLDialect
hibernate.vendor.database=POSTGRESQL
```
(Exemplet är för en databas som körs med PostgreSQL RDBMS)

Första gången som applikationen körs mot databasen är det viktigt att nyckel/värde hibernate.vendor.generateDdl=true är med. Inställningen får Hibernate att automatiskt skapa upp databasens tabeller och relationer.

> 2, Bygg projektet.
Använd Maven-kommando ```
 mvn clean install -Pprod ```.

> 3, Tillför JDBC-implementation till driftmiljön.
Kopiera in JAR-fil som håller databasens JDBC-klasser under webbserverns miljöbibliotek. I Apache Tomcat är detta [APACHE\_HOME](APACHE_HOME.md)/lib (detta för att inte binda applikationsbygget i sig självt hårt mot en specifik drivrutin).

> 4, Driftsättning.
Driftsätt war-filen som byggdes i steg 2. I Apache Tomcat kopieras filen in under webbapps-katalogen.

## Databas-schema ##
![https://oppna-program-vertikala-prioriteringar.googlecode.com/svn/wiki/images/schema.png](https://oppna-program-vertikala-prioriteringar.googlecode.com/svn/wiki/images/schema.png)