FROM openjdk:21
VOLUME /tmp
ENV IMG_PATH=/img
EXPOSE 8080
RUN mkdir -p /img
ADD ./out/artifacts/Reports_jar/Reports.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]