FROM openjdk:8
EXPOSE 8080
ADD target/productlist_docker.jar productlist_docker.jar
ENTRYPOINT ["java","-jar","/productlist_docker.jar"]