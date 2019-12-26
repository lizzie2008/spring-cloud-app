# 创建工程

- 新建一个工程：选择Spring Cloud Bootstrap，对应的Spring Boot 版本2.2.2。

![image-20191223145912954](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223145916-192957.png) 

- 项目生成后，看到对应的Spring版本的依赖没有问题。

![image-20191223150248620](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223150251-287472.png) 

- 因为是父工程，我们将打包格式改成pom，并把src等无用的文件删除。

```xml
<groupId>tech.lancelot</groupId>
<artifactId>spring-cloud-app</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>spring-cloud-app</name>
<description>Demo project for Spring Cloud</description>
<packaging>pom</packaging>
```

# 服务注册中心（Eureka）

## Eureka Server

- 新建Module->选择Eureka Server

![image-20191223151658588](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223151658-503196.png)

- 因为Module作为子项目，我们改写下对应的POM文件。

  ```xml
  <parent>
      <groupId>tech.lancelot</groupId>
      <artifactId>spring-cloud-app</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <groupId>tech.lancelot</groupId>
  <artifactId>eureka-server</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>eureka-server</name>
  <description>Registry Center</description>
  
  <dependencies>
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
      </dependency>
  </dependencies>
  ```

- 重新Build一下项目，能正常编译。但是此时Eureka Server是不能正常启动工作的，需要在application类增加

`@EnableEurekaServer`。

此时，我们再运行Eureka Server，发现可以正常启动服务注册服务器，服务端口8080，注册地址：http://localhost:8761/eureka/。

![image-20191223152545360](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223152546-811106.png) 

- 打开浏览器，访问8080端口，查看可视化管理界面。

![image-20191223152817997](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223152819-939500.png) 

-  当然，我们没有做任何配置，并且控制台一直报错，这是因为默认情况下，本身也是需要获取注册信息和注册到注册中心，而此时找不到对应服务器。我们可以修改配置文件，做相应的配置。调整服务端口为8761，重新启动后，发现不再报错。

```yaml
eureka:
  client:
    fetch-registry: false #设置不从注册中心获取注册信息
    register-with-eureka: false #设置自身不作为客户端注册到注册中心

spring:
  application:
    name: eureka-server #应用名称

server:
  port: 8761  #应用服务端口
```

## Eureka Client

- 我们再建一个Module工程，作为服务客户端，向Eureka Server服务中心注册。

![image-20191223160003051](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223160004-250266.png)   

- 同样，我们修改POM文件，依赖于父项目，注意这里需要引入`eureka-client`和`spring-boot-starter-web`依赖。

  ```xml
  <parent>
      <groupId>tech.lancelot</groupId>
      <artifactId>spring-cloud-app</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </parent>
  
  <groupId>tech.lancelot</groupId>
  <artifactId>shopping-provider</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>shopping-provider</name>
  <description>shopping service provider</description>
  
  <dependencies>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
      </dependency>
  </dependencies>
  ```

- 需要在application类增加`@EnableDiscoveryClient`，同时修改配置文件。

  ```yaml
  eureka:
    client:
      serviceUrl:
        defaultZone: http://localhost:8761/eureka/ #指定服务注册地址
  
  spring:
    application:
      name: shopping-provider  #应用名称
  ```

- 重启Eureka Client，启动后再次访问Eureka Server管理界面，可以发现order-provider服务已注册。

![image-20191223164945996](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223164946-795714.png) 

## 注册中心高可用

- 之前我们的Eureka Server是单点服务，实际生产中，经常是多台注册中心，因此我们尝试下配置2台注册中心。

  启动服务器实例1：

  ```yaml
  eureka:
    client:
  #    fetch-registry: false #设置不从注册中心获取注册信息
  #    register-with-eureka: false #设置自身不作为客户端注册到注册中心
      defaultZone: http://localhost:8762/eureka/ #指定服务注册地址
  
  spring:
    application:
      name: eureka-server1 #应用名称
  
  server:
    port: 8761  #应用服务端口
  ```

  启动服务器实例2：

  ```yaml
  eureka:
    client:
  #    fetch-registry: false #设置不从注册中心获取注册信息
  #    register-with-eureka: false #设置自身不作为客户端注册到注册中心
      defaultZone: http://localhost:8761/eureka/ #指定服务注册地址
  
  spring:
    application:
      name: eureka-server2 #应用名称
  
  server:
    port: 8762  #应用服务端口
  ```

- 重启2台注册中心，启动后分别访问2台的管理界面，可以看到2台注册中心已经相互注册。

![image-20191223171046707](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223171047-139370.png) 

![image-20191223170807848](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191223170808-100492.png) 

## 小结

项目增加2个服务模块，并向Eureka Server注册：shopping-product（商品服务）、shopping-order（订单服务），实现相应业务逻辑，这部分详细实现不再阐述。

整体项目结构如下：

> spring-cloud-app
>
> ​		--eureka-server（服务注册中心）
>
> ​		--shopping-common（购物公共模块）
>
> ​		--shopping-product（商品服务模块）
>
> ​		--shopping-order（订单服务模块）

系统架构如图，比较简单，一个集群服务中心，目前有2个服务提供并注册：

![image-20191225113818560](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191225113820-81863.png) 

# 统一配置中心

上个环境中，我们有2个服务提供者，首先看下各自的配置，可以发现很大一部分都是重复的。

如果微服务架构中没有使用统一配置中心时，所存在的问题：

- 配置文件分散在各个项目里，不方便维护
- 配置内容安全与权限，实际开发中，开发人员是不知道线上环境的配置的
- 更新配置后，项目需要重启

```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/ #指定服务注册地址

spring:
  application:
    name: shopping-order  #应用名称
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/spring_cloud_app?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
  jpa:
    show-sql: true
    database-platform: org.hibernate.dialect.MySQLDialect

server:
  port: 9010
```

因此，我们尝试来实现配置的集中管理，并支持配置的动态刷新。



# 负载均衡(Ribbon)

Spring Cloud Ribbon 是一个客户端的负载均衡器，它提供对大量的HTTP和TCP客户端的访问控制。

客户端负载均衡即是当浏览器向后台发出请求的时候，客户端会向 Eureka Server 读取注册到服务器的可用服务信息列表，然后根据设定的负载均衡策略（没有设置即用默认的），抉择出向哪台服务器发送请求。

假设有以下业务场景，shopping-order模块需要调用shopping-product提供的API接口。我们看如何实现。

## RestTemplate调用

第一种方法使用构造RestTemplate，调用远程API，这种方法url是写死，如果启动多台shopping-product服务的话，那又该如何？

```java
@Test
void getProductByRestTemplate() {
    //1.第一种方法
    RestTemplate restTemplate = new RestTemplate();
    String response = restTemplate.getForObject("http://localhost:9000/api/products", String.class);
    Assert.hasLength(response,"未获取内容");
}
```

## 负载均衡调用

第二种方法：我们启动2个shopping-product服务实例，分别是9000端口和9001端口，运行测试发现，会根据loadBalancerClient负载均衡机制帮我们选择一个服务地址，进行访问调用。

```java
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
```

## 应用名称调用

但这样依旧很是麻烦，接下来看第三种方法。第三种方法屏蔽了API的具体url信息，只用ServerId，并根据负载均衡规则，自动路由到对应的地址。

因为eureka包中已经添加了对Ribbon的依赖，我们可以增加断点，调试程序，发现进入RibbonLoadBalancerClient-->choose方法，返回负载均衡策略选择的ServiceInstance。

```java
@Component
public class RestTemplateConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void getProductByServerId() {

        String response = restTemplate.getForObject("http://shopping-product/api/products", String.class);
        log.info(response);
    }
}
```

当然，我们也可以指定应用服务的负载均衡策略：

```yaml
shopping-order:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

## 小结

目前系统架构如图，实现shopping-product和shopping-order集群化部署，调用方式通过客户端负载均衡，来路由消费端的请求。

![image-20191226152727107](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191226152728-944675.png) 

# 声明式服务调用(Feign)

Feign是一个声明式的Web Service客户端，它的目的就是让Web Service调用更加简单。Feign提供了HTTP请求的模板，通过编写简单的接口和插入注解，就可以定义好HTTP请求的参数、格式、地址等信息。

而Feign则会完全代理HTTP请求，我们只需要像调用方法一样调用它就可以完成服务请求及相关处理。Feign整合了Ribbon和Hystrix(关于Hystrix我们后面再讲)，可以让我们不再需要显式地使用这两个组件。

总起来说，Feign具有如下特性：

- 可插拔的注解支持，包括Feign注解和JAX-RS注解;
- 支持可插拔的HTTP编码器和解码器;
- 支持Hystrix和它的Fallback;
- 支持Ribbon的负载均衡;
- 支持HTTP请求和响应的压缩。

## 服务端实现

shopping-product服务提供端暴露API。

```java
@GetMapping("/productInfos")
public List<ProductInfoOutput> findProductInfosByIds(@RequestParam(required = false) String productIds) throws Exception {
    //如果传入商品id参数
    if (StringUtils.isNotEmpty(productIds)) {
        List<String> ids = Arrays.asList(productIds.split(","));
        List<ProductInfo> productInfos = productService.findProductInfosByIds(ids);
        List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
        return productInfoOutputs;
    }else{
        List<ProductInfo> productInfos = productService.findProductInfos();
        List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
        return productInfoOutputs;
    }
}
```

## 客户端实现

- 引入Feign

shopping-order模块需要调用shopping-product接口，首先我们在服务调用端增加Maven依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```

启动类标注开启Feign服务

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ShoppingOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingOrderApplication.class,args);
    }
}
```

- 创建声明式服务

```java
/**
 * 声明式服务
 */
@FeignClient("shopping-product/api/v1")
public interface ProductClient {

    @GetMapping("/productInfos")
    List<ProductInfoOutput> findProductInfosByIds(@RequestParam(required = false) String productIds);
}
```

@FeignClient(“服务名称”)映射服务调用，本质还是http请求，只不过Feign帮我们屏蔽了底层的请求路由，对开发者完全透明，使得调用远程服务感觉跟调用本地服务一致的编码体验。

本地调用测试，可以正常返回接口数据。

```java
@GetMapping("/orders/findProductInfosByIds")
public List<ProductInfoOutput> findProductInfosByIds(){
    List<ProductInfoOutput> productInfoOutputs = productClient
        .findProductInfosByIds("157875196366160022, 157875227953464068");
    return productInfoOutputs;
}
```

## 小结

在实现负载均衡基础上，封装声明式服务调用。实现shopping-order对shopping-product的透明调用，系统架构如图如下。

![image-20191226153226801](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191226153227-511711.png) 