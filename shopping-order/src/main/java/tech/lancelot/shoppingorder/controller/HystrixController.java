package tech.lancelot.shoppingorder.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Hystrix 测试
 */
@RestController
@RequestMapping("api/hystrix")
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")})
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),                //设置熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),    //请求数达到后才计算
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //休眠时间窗
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),    //错误率
    })
    @GetMapping("/getProductEnv")
    public String getProductEnv() {

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:9000/api/env", null, String.class);

    }

    // 默认服务不可达的返回信息
    private String defaultFallback() {
        return "太拥挤了, 请稍后再试~~";
    }
}
