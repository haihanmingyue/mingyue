FROM openjdk-19
COPY mingyue-0.0.1-SNAPSHOT.war /mingyue-0.0.1-SNAPSHOT.war
CMD [ "java", "/mingyue-0.0.1-SNAPSHOT.war" ]