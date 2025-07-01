FROM tomcat:9.0-jdk21
LABEL authors="Sorameta"
COPY ./target/ConnectFour.war /usr/local/tomcat/webapps