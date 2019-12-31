mvn clean package -Dmaven.test.skip=true -U
docker build -t spring-cloud-app/eureka-server:v1 .
