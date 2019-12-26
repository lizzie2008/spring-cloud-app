package tech.lancelot.shoppingorder.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Test
    void getProductByRestTemplate() {
        //1.第一种方法，使用RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:9000/api/products", String.class);
        log.info(response);
    }

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Test
    void getProductByLoadBalance(){

        //2.第二种方法，先获取负载均衡的地址再调用API
        ServiceInstance instance = loadBalancerClient.choose("shopping-product");
        String url=String.format("http://%s:%s/api/products",instance.getHost(),instance.getPort());
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("port:"+instance.getPort()+response);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void getProductByServerId() {

        String response = restTemplate.getForObject("http://shopping-product/api/products", String.class);
        log.info(response);
    }
}