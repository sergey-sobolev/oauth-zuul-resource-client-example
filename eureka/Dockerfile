FROM 372103282386.dkr.ecr.us-east-2.amazonaws.com/openjdk:9-jre
ADD ./build/libs/eurekaserver.jar /app/
CMD ["java", "-Xmx200m", "--add-modules", "java.xml.bind", "-jar", "/app/eurekaserver.jar"]
EXPOSE 8000
