# 2. Zadaća

Ovo je *fork* s implementacijom zahtjeva.

## Zadatak

Implemetirajte sustav za upravljanje bibliotekama po uzoru na [_Maven Central_](https://mvnrepository.com/repos/central).
Interakcija sa sustavom odvija se isključivo preko HTTP REST APIja, nije potrebno implementirati nikakvo drugo korisničko sučelje.
Definicija APIja i konkretni funkcionalni zahtjevi su dani u nastavku.

Zadaća se sastoji od dva dijela:

1. **Implementacija rješenja**: individualna implementacija rješenja zadatka objavljena na GitHubu u obliku Pull requesta na novi, inicijalni repozitorij. **Rok: četvrtak, 16.05.2024. 12:00**
2. **Recenzije (Code review)**: recenzija rješenja kolega studenata u obliko pull request reviewa na GitHubu. **Rok: utorak, 21.05.2024. 15:00** 

Uzimaju se u obzir sve zadaće predane isključivo do **16.05.2024. u 12:00**. Nakon tog roka, zadaća se više neće uzimati u obzir.
Neposredno nakon tog roka, predane zadaće bit će raspoređene kolegama za Code review. Code review traje do **21.05.2024. u 15:00** nakon čega se zadaće zaključavaju i ocjenjuju.
Kod ocjenjivanja, u obzir će se uzimati isključivo zadnji commit na **main** grani. Rezultat Code reviewa mora biti uspješno spajanje (**merge**) na **main** granu.
Tijekom Code reviewa, kod se može mijenjati i ažurirati, sukladno diskusiji i komentarima kolega.


## Funkcionalni zahtjevi

### Resursi

Osnovni resurs sustava je biblioteka, `library`, koja ima sljedeće nužne atribute:

- `groupId` - identifikator grupe biblioteka, npr. `org.springframework`, obavezno polje
- `artifactId` - identifikator biblioteke unutar grupe, npr. `spring-core`, obavezno polje
- `versions` - lista identifikatora podresoursa `version` koji reprezentira dostupne verzije biblioteke
- `name` - naziv biblioteke, obavezno polje
- `description` - opis biblioteke, opcionalno polje

Svaki `library` ima dodatni cjelobrojni identifikator `id` koji je jedinstven (u kontekstu `library` resursa) i generira se automatski, inkrementalno, prilikom kreiranja nove biblioteke.

Podresurs `version` ima sljedeće atribute:

- `semanticVersion` - semantička verzija, obavezno polje. Budući da je semantička verzija već implementirana u prethodnoj zadaći, zbog jednostavnosti, u svrhu ovog akademskog primjera, koristite samo osnovnu `MAJOR.MINOR.PATCH` definiciju sematičke verzije, bez `prerelease` i `build metadata` dijelova.
- `description` - opis verzije, opcionalno polje
- `deprecated` - oznaka da li je verzija zastarjela, obavezno polje

Svaki `version` ima dodatni cjelobrojni identifikator `id` koji je jedinstven (u kontekstu `version` resursa) i generira se automatski, inkrementalno, prilikom kreiranja nove verzije biblioteke.

Dodatno, svaki `version` ima i date-time atribut ([ISO 8601](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME) format) `releaseDate` koji se automatski generira prilikom kreiranja nove verzije.

Dodatni uvjeti:

- Kombinacija `groupId` i `artifactId` mora biti jedinstvena u sustavu.
- Veza između `library` i `version` je 1:N. Jedna biblioteka može imati više verzija, ali svaka verzija pripada samo jednoj biblioteci.


### Operacije

Implementirajte sljedeće operacije nad resursom `library`:

- `GET /libraries` - dohvaća sve biblioteke.
  Potrebno je implementirati filtere po atributima `groupId` i `artifactId`.
  Filteri se primjenjuju na sve biblioteke koje zadovoljavaju uvjete svih filtera. 
  Endpoint vraća status 200 i listu biblioteka.
  Ukoliko niti jedan filter nije zadovoljen, vraća se prazna lista.
  Ukoliko filteri nisu validni prema definiciji iznad, endpoint vraća status 400.
  Rezultat je paginiran, a koristi se nenegativan query parametar `page` za označavanje stranice i pozitivan query parametar `size` za označavanje veličine stranice.
  Ukoliko nisu zadani, koriste se defaultne vrijednosti `page=0` i `size=20`.
  Dovoljno je vratiti listu verzija za svaku biblioteku, ne cijelu strukturu verzije.

  Primjeri korištenja:

  ```shell
    $ curl 'http://localhost:8080/libraries' -H 'Authorization: App la9psd71atbpgeg7fvvx'
    $ curl 'http://localhost:8080/libraries?groupId=org.springframework' -H 'Authorization: App la9psd71atbpgeg7fvvx'
    $ curl 'http://localhost:8080/libraries?groupId=org.springframework&artifactId=spring-core' -H 'Authorization: App la9psd71atbpgeg7fvvx'
    $ curl 'http://localhost:8080/libraries?page=1&size=10' -H 'Authorization: App la9psd71atbpgeg7fvvx'
  ```

  Primjeri odgovora:

  ```json
  {
    "results": [
      {
        "id": 100,
        "groupId": "org.springframework",
        "artifactId": "spring-core",
        "versions": [1000, 1001, 1002],
        "name": "Spring Core",
        "description": "Spring Core Framework"
      },
      {
        "id": 200,
        "groupId": "org.springframework",
        "artifactId": "spring-web",
        "versions": [2000],
        "name": "Spring Web",
        "description": "Spring Web Framework"
      }
    ],
    "page": 0,
    "size": 20,
    "totalPages": 1,
    "totalResults": 2
  }
  ```
  
- `POST /libraries` - stvara novu biblioteku.

  Endpoint prima JSON objekt koji predstavlja novu biblioteku.
  Ukoliko je biblioteka uspješno stvorena, endpoint vraća status 201 i novostvorenu biblioteku.
  Ukoliko je biblioteka neispravna, endpoint vraća status 400.
  Ukoliko već postoji biblioteka sa istom kombinacijom `groupId` i `artifactId`, endpoint vraća status 409.
  Verzije se kreiraju kroz nezavisni endpoint, te ih nije moguće kreirati prilikom stvaranja biblioteke.
  Zahtjev koji uključuje verzije, smatra se neispravnim.

  Primjer korištenja:

  ```shell
    $ curl -X POST 'http://localhost:8080/libraries' \
      -H 'Content-Type: application/json' \
      -H 'Authorization: App la9psd71atbpgeg7fvvx' \
      -d '{
        "groupId": "org.springframework",
        "artifactId": "spring-core",
        "name": "Spring Core",
        "description": "Spring Core Framework"
      }'
  ```
  Primjer odgovora:

  ```json
  {
    "id": 100,
    "groupId": "org.springframework",
    "artifactId": "spring-core",
    "versions": [],
    "name": "Spring Core",
    "description": "Spring Core Framework"
  }
  ```
  
- `GET /libraries/{id}` - dohvaća biblioteku po identifikatoru.

   Endpoint vraća status 200 i biblioteku.
   Ukoliko biblioteka ne postoji, endpoint vraća status 404.
   
   Primjer korištenja:

   ```shell
      $ curl 'http://localhost:8080/libraries/100' -H 'Authorization: App la9psd71atbpgeg7fvvx'
   ```
   
   Primjer odgovora:

    ```json
    {
      "id": 100,
      "groupId": "org.springframework",
      "artifactId": "spring-core",
      "versions": [1000, 1001, 1002],
      "name": "Spring Core",
      "description": "Spring Core Framework"
    }
    ```

- `PATCH /libraries/{id}` - ažurira biblioteku po identifikatoru.

  Moguće je ažurirati isključivo `name` i `description`.
  Endpoint prima JSON objekt koji predstavlja ažurirane podatke.
  Ukoliko je biblioteka uspješno ažurirana, endpoint vraća status 200 i ažuriranu biblioteku.
  Ukoliko biblioteka ne postoji, endpoint vraća status 404.
  Ukoliko su podaci neispravni, endpoint vraća status 400.

  Primjer korištenja:

  ```shell
      $ curl -X PATCH 'http://localhost:8080/libraries/100' \
        -H 'Authorization: App la9psd71atbpgeg7fvvx' \
        -H 'Content-Type: application/json' \
        -d '{
          "name": "Spring Core Framework",
          "description": "Spring Core Framework"
        }'
  ```
  Primjer odgovora:

  ```json
  {
      "id": 100,
        "groupId": "org.springframework",
        "artifactId": "spring-core",
        "versions": [1000],
        "name": "Spring Core Framework",
        "description": "Spring Core Framework"
    }
    ```
  
- `DELETE /libraries/{id}` - briše biblioteku po identifikatoru.

  Endpoint vraća status 204 (No Content).
  Ukoliko biblioteka ne postoji, endpoint vraća status 404.

  Primjer korištenja:

  ```shell
    $ curl -X DELETE 'http://localhost:8080/libraries/100' -H 'Authorization: App la9psd71atbpgeg7fvvx'
  ```
  **Napomena:** Maven Central repository ne dozvoljava brisanje biblioteka, ali u ovom akademskom primjeru je to dopušteno.

- `GET /libraries/{id}/versions` - dohvaća sve verzije biblioteke po identifikatoru biblioteke.

  Endpoint vraća status 200 i listu verzija biblioteke.
  Ukoliko biblioteka ne postoji, endpoint vraća status 404.
  Rezultat je paginiran, na ekvivalentan način kao i za biblioteke.
  Polje `releaseDate` se vraća UTC zoni, u formatu prema [ISO 8601 standardu](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME).

  Primjer korištenja:

  ```shell
    $ curl 'http://localhost:8080/libraries/100/versions' -H 'Authorization: App la9psd71atbpgeg7fvvx'
  ```
  Primjer odgovora:

  ```json
  {
    "results": [
      {
        "id": 1000,
        "semanticVersion": "5.3.9",
        "description": "Spring Core Framework 5.3.9",
        "releaseDate": "2024-05-01T12:30:00Z",
        "deprecated": false
      },
      {
        "id": 1001,
        "semanticVersion": "5.3.10",
        "description": "Spring Core Framework 5.3.10",
        "releaseDate": "2024-05-10T12:00:00Z",
        "deprecated": false
      }
    ],
    "page": 0,
    "size": 20,
    "totalPages": 1,
    "totalResults": 2
  }
  ```

- `GET /libraries/{id}/versions/{id}` - dohvaća verziju biblioteke po identifikatoru verzije.

  Endpoint vraća status 200 i verziju biblioteke.
  Ukoliko biblioteka ili verzija ne postoji, endpoint vraća status 404.
  Polje `releaseDate` se vraća UTC zoni, u formatu prema [ISO 8601 standardu](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME).

  Primjer korištenja:

  ```shell
    $ curl 'http://localhost:8080/libraries/100/versions/1000' -H 'Authorization: App la9psd71atbpgeg7fvvx'
  ```
  Primjer odgovora:

  ```json
  {
    "id": 1000,
    "semanticVersion": "5.3.9",
    "description": "Spring Core Framework 5.3.9",
    "releaseDate": "2024-05-01T12:30:00Z",
    "deprecated": false
  }
  ```
  
- `POST /libraries/{id}/versions` - stvara novu verziju biblioteke.

  Endpoint prima JSON objekt koji predstavlja novu verziju biblioteke.
  Ukoliko je verzija uspješno stvorena, endpoint vraća status 201 i novostvorenu verziju.
  Ukoliko je verzija neispravna, endpoint vraća status 400.
  Ukoliko već postoji verzija te biblioteke sa istom semantičkom verzijom, endpoint vraća status 409.
  Ukoliko biblioteka ne postoji, endpoint vraća status 404.
  Polje `releaseDate` je automatski generirano na serverskoj strani.
  Polje `deprecated` je automatski postavljeno na `false` ukoliko nije eksplicitno navedeno.

  Primjer korištenja:

  ```shell
    $ curl -X POST 'http://localhost:8080/libraries/100/versions' \
      -H 'Authorization: App la9psd71atbpgeg7fvvx' \
      -H 'Content-Type: application/json' \
      -d '{
        "semanticVersion": "5.3.10",
        "description": "Spring Core Framework 5.3.10",
        "deprecated": false
      }'
  ```
  Primjer odgovora:

  ```json
  {
    "id": 1001,
    "semanticVersion": "5.3.10",
    "description": "Spring Core Framework 5.3.10",
    "releaseDate": "2024-05-10T12:00:00Z",
    "deprecated": false
  }
  ```
  
- `PATCH /libraries/{id}/versions/{id}` - ažurira verziju biblioteke po identifikatoru verzije. 
  
  Endpoint prima JSON objekt koji predstavlja ažurirane podatke.
  Ukoliko je verzija uspješno ažurirana, endpoint vraća status 200 i ažuriranu verziju.
  Moguće je ažurirati isključivo `description` i `deprecated` polja, u slučaju da se mijenjaju neki drugi podaci, potrebno je vratiti status 400.
  Ako je verzija jednom označena kao zastarijela, njezin status se više ne može promijeniti te se u tom slulčaju vraća status 400.
  Ukoliko verzija ne postoji, endpoint vraća status 404.

  Primjer korištenja:

  ```shell
    $ curl -X PATCH 'http://localhost:8080/libraries/100/versions/1000' \
      -H 'Content-Type: application/json' \
      -H 'Authorization: App la9psd71atbpgeg7fvvx' \
      -d '{
        "description": "Spring Core Framework 5.3.9",
        "deprecated": true
      }'
  ```
  Primjer odgovora:

  ```json
  {
    "id": 1000,
    "semanticVersion": "5.3.9",
    "description": "Spring Core Framework 5.3.9",
    "releaseDate": "2024-05-01T12:30:00Z",
    "deprecated": true
  }
  ```
  Verzije nije moguće brisati, već samo označiti kao zastarjele.

### Poruke o greškama

Ukoliko dođe do greške prilikom obrade zahtjeva, endpoint vraća odgovarajući HTTP status opisan u prethodnim odlomcima i JSON resurs sa informacijama o greški sljedeće strukture:

- `message` - tekstualni opis greške
- `action` - tekstualni opis akcije koju korisnik može poduzeti kako bi ispravio grešku

Primjeri mogućih poruka:

```json
{
    "message": "Library with groupId 'org.springframework', artifactId 'spring-core' and semantic version '5.3.9' already exists",
    "action": "Use PATCH method to update existing version or create new version with a different semantic version."
}
```

```json
{
    "message": "Library with id '100' does not exist",
    "action": "Use POST method to create a new library if needed."
}
```

### Autentifikacija

Sustav zahtjeva osnovni oblik API Key autentifikacije korisnika.

U svrhu ovog akademskog primjera, potrebno je dozvoliti autentifikaciju ukoliko je u zaglavlju HTTP zahtjeva prisutan `Authorization` zaglavlje sa nekom od sljedećih vrijednosti:

- `App la9psd71atbpgeg7fvvx`
- `App ox9w79g2jwctzww2hcyb`
- `App othyqhps18srg7fdj0p9`

API ključeve (vrijednost nakon `App ` prefixa) je potrebno inicijalno pohraniti u bazu podataka i vezati uz tri korisnika.
Dovoljno je da entitet korisnika ima neki identifikator i korisničko ime.
Upravljanje korisnicima nije moguće preko REST APIja.
Ukoliko korisnik nije autentificiran, svaki endpoint vraća status 401.

**Svi primjeri operacije navedeni iznad moraju odraditi autentifikaciju, Authorization zaglavlje s ispravnim API ključem je nužan uvjet.**

### Tehnički zahtjevi

1. Projekt mora biti objavljen kao git repozitorij na [GitHubu](https://github.com/), u obliku Pull requesta s detaljnim opisom, spreman za recenziju.
2. Projekt se sastoji od dvije nezavisne komponente:
   1. Spring Boot aplikacija koja implementira opisane funkcionalnosti.
   2. MySQL ili MariaDB baza podataka.
3. Spring Boot aplikacija mora biti napisana u Javi 21 i koristiti [Spring Boot](https://spring.io/projects/spring-boot) (verziju `3.2.5`). Mora se moći izvršiti `mvn clean install` u direktoriju gdje se nalazi aplikacija.
4. Spring Boot aplikacija mora podići web poslužitelj (_server_) koji sluša dolazne HTTP upite (_request_) na localhostu (`localhost`) na portu `8080`.
5. Potrebno je napisati unit i integracijske testove za sve funkcionalnosti sustava.
   Svi napisani testovi moraju prolaziti.
   Potrebno je postići pokrivenost testova od najmanje 90%.
   Pokrivenost testova računa se pomoću [JaCoCo](https://www.jacoco.org/jacoco/) alata.
   Plugin za JaCoCo je potrebno konfigurirati u `pom.xml` datoteci.
   Za testnu bazu podataka potrebno je koristiti [H2 bazu podataka](https://www.baeldung.com/spring-boot-h2-database).
6. Obje komponete sustava se moraju moći pokrenut pomoću `docker-compose` alata.
   Datoteka `docker-compose.yml` mora se nalaziti u root direktoriju repozitorija.
   Oba dijela sustava moraju se uspješno pokrenuti u zasebnim Docker kontejnerima koristeći isključivo `docker compose up -d` naredbu.
   Potrebno je pripaziti na međuovisnost kontejnera.
7. Baza podataka mora biti inicijalizirana sa navedenim API ključevima i cijelokupnom schemom prilikom pokretanja sustava. Potrebno je napisati `docker-compose.yml` datoteku koja pokreće oba dijela sustava.
8. Potrebno je osigurati da se stanje baze podataka očuva prilikom ponovnog pokretanja aplikacije i ponovnog pokretanja baze podataka.
9. Projekt mora ponuditi OpenAPI specifikaciju na portu `8081` i ruti `/openapi` koristeći `springdoc-openapi` biblioteku.
   Za dohvat OpenAPI specifikacije nije potrebna autentifikacija.
