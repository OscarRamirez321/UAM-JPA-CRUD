# UAM - JPA CRUD (Categoría & Libro)

Proyecto de ejemplo siguiendo la estructura indicada en la presentación (persistence.xml, util/JPAConexion, paquete `entities` y `services` con `ICRUD` y `MyDao`, y clase `Main`).

## Requisitos
- Java 17
- Maven 3.8+

## Ejecutar
```bash
mvn -q -e -DskipTests package
java -jar target/uam-jpa-crud-1.0.0-jar-with-dependencies.jar
```
La base de datos H2 se crea en `target/uamdb` y se autogestiona con `hibernate.hbm2ddl.auto=update`.

## Estructura
- `src/main/resources/persistence.xml`
- `edu.uam.persistence.util.JPAConexion`
- `edu.uam.persistence.entities.Categoria`
- `edu.uam.persistence.entities.Libro`
- `edu.uam.persistence.services.ICRUD`
- `edu.uam.persistence.services.MyDao`
- `edu.uam.persistence.app.Main`


