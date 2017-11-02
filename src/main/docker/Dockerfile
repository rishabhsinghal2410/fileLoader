FROM java:8
VOLUME /tmp
ADD fileloader.jar app.jar
ADD checkMySqlUp.sh checkMySqlUp.sh
RUN bash -c 'chmod +x /checkMySqlUp.sh'
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=container", "-jar", "/app.jar"]