FROM openjdk:8
ADD build/libs/com.kafka.emailservice-0.1.0.jar docker-kafka-emailservice.jar
EXPOSE 8087
ENTRYPOINT ["java","-jar","docker-kafka-emailservice.jar"]