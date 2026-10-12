# Notenverwaltung

## Ordnerstruktur

```
notenverwaltung/
├── .mvn/wrapper/               Maven Wrapper
├── bin/                        Build-Ausgabe der IDE (Eclipse)
└── src/main/
    ├── java/de/fh/swf/notenverwaltung/
    │   ├── controller/         HTTP-Endpunkte, delegieren an die Services
    │   ├── service/            Geschäftslogik (Notenprüfung, Endnote, Creditpoints)
    │   ├── repository/         Spring-Data-JPA-Repositories für den Datenbankzugriff
    │   └── entity/             JPA-Entitäten
    └── resources/
        └── static/             Frontend (statische HTML-Seiten)
```
