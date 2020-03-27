![Pandemie Inc.](pandemieinc-logo.png "Pandemie Inc.")

Unser Beitrag zum [informatiCup 2020 - Pandemie!](https://github.com/informatiCup/informatiCup2020).

> Der informatiCup 2020 widmet sich der Aufgabe, die Menschheit vor der Auslöschung durch eine Pandemie zu retten.

Unsere Dokumentation und Präsentation befinden sich hier auch im Repository.
Die Dokumentation zum Source-Code kann [hier](https://supernascher.github.io/Pandemie-Inc/) betrachetet werden
oder mittels `mvn javadoc:javadoc` selbstständig erstellt werden.

## Abhängigkeiten

Folgende Abhängigkeiten werden für das Bauen unseres Projekts benötigt:

- Java (Version >= 8) (fürs Kompilieren das JDK, bzw. OpenJDK)
- Maven

Weitere Abhängigkeiten (Spring Boot, JUnit und ND4J) werden später von Maven verwaltet.

Wir bieten auch ein Maven-Wrapper an, falls es nicht möglich ist Maven zu installieren.
Alternativ bieten wir die Möglichkeit über Docker unsere Software zu benutzen.


## Entwicklung

1) `mvn spring-boot:run`

Damit startet Maven Spring, nachdem es den geänderten Quellcode kompiliert hat.


## Bauen der Software

1) `mvn package`
2) `java -jar target/pandemieinc-0.0.1-SNAPSHOT.jar`

Mit `mvn clean` wird das Repository aufgeräumt, also die kompilierten Dateien gelöscht.


## Docker

Wir bieten auch ein **Dockerfile** an, womit ein "Image" gebaut werden kann, dass dann mittels [Docker](https://www.docker.com)
gestartet und benutzt werden kann.

- Bauen: `docker build -t tubs/pandemieinc .`
- Starten: `docker run -p 8080:8080 tubs/pandemieinc`


## Sonstiges

Falls Probleme mit Maven auftreten sollte, wir bieten ein Maven-Wrapper an.
Einfach `./mvnw` (Linux/Mac OS/BSD) oder `./mvnw.cmd` (Windows) benutzen, statt `mvn`.


Falls man beim Starten unserer Software keine IP-Adresse und Port angibt, ist
unsere Websoftware unter `127.0.0.1:8080` oder `localhost:8080` erreichbar.
