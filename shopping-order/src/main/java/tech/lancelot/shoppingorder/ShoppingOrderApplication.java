package tech.lancelot.shoppingorder;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "tech.lancelot.shoppingorder.client")
//@SpringBootApplication
//@EnableDiscoveryClient
//@EnableCircuitBreaker
@SpringCloudApplication
@EnableHystrixDashboard
public class ShoppingOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingOrderApplication.class, args);
    }
}
