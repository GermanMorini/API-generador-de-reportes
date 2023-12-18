# indica la imagen que se va a usar para crear el contenedor
FROM openjdk:21

# crea un directorio dentro del contenedor
VOLUME /tmp

# variables de entorno y configuraciónes
ENV IMG_PATH=/img

# abre un puerto
EXPOSE 8080

# ejecuta un comando
RUN mkdir -p /img

# añade contenido, ficheros, etc... (usa rutas relativas al dockerfile)
ADD ./target/Reports-0.0.1-SNAPSHOT.jar app.jar

# comando que se ejecuta al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]