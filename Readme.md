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

对于一些简单的项目来说，我们一般都是直接把相关配置放在单独的配置文件中，以 properties 或者 yml 的格式出现，更省事儿的方式是直接放到 application.properties 或 application.yml 中。在集群部署情况下，我们尝试来实现配置的集中管理，并支持配置的动态刷新。

## Config Server

- 我们新建一个Module工程，统一配置中心，保存所以的配置信息。

同样，我们作为子项目，修改相关依赖，加入对spring-cloud-config-server依赖

```xml
<modelVersion>4.0.0</modelVersion>
<parent>
    <groupId>tech.lancelot</groupId>
    <artifactId>spring-cloud-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</parent>

<artifactId>config-server</artifactId>
<packaging>jar</packaging>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- spring cloud config 服务端包 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
</dependencies>
```

- application.properties进行如下配置

```yaml
spring:
  application:
    name: config-server  # 应用名称
  cloud:
    config:
      server:
        git:
          uri: https://github.com/lizzie2008/Central-Configuration.git #配置文件所在仓库
          username: 'Github username'
          password: 'Github password'
          default-label: master #配置文件分支
          search-paths: spring-cloud-app  #配置文件所在根目录
          
server:
  port: 8888
```

- 在 Application 启动类上增加相关注解 `@EnableConfigServer`

```java
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
```

- 启动服务，接下来测试一下。

Spring Cloud Config 有它的一套访问规则，我们通过这套规则在浏览器上直接访问就可以。

```bash
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```

{application} 就是应用名称，对应到配置文件上来，就是配置文件的名称部分，例如我上面创建的配置文件。

{profile} 就是配置文件的版本，我们的项目有开发版本、测试环境版本、生产环境版本，对应到配置文件上来就是以 application-{profile}.yml 加以区分，例如application-dev.yml、application-sit.yml、application-prod.yml。

{label} 表示 git 分支，默认是 master 分支，如果项目是以分支做区分也是可以的，那就可以通过不同的 label 来控制访问不同的配置文件了。

我们在git项目中，新建spring-cloud-app/config-eureka-server.yml配置文件,然后访问配置中心服务器，看看能正常获取配置文件。

![image-20200103113636697](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20200103113641-423180.png) 

## 向服务中心注册

config-server本身作为一个服务，也可以作为服务提供方，向服务中心注册，其他的服务想要获取配置文件，只需要通过服务名称就会访问。

- 引入Eureka Client依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

- 启动类上增加`@EnableDiscoveryClient`注解

```java
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
```

- 配置文件中增加eureka注册。

```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka1:8761/eureka/,http://eureka2:8762/eureka/ #指定服务注册地址
```

- 启动eureka-server，看看config-server是否注册成功。

![image-20200103140308595](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20200103140309-444069.png)

## 服务提供端改造

- shopping-product项目中，把原先的application.yml文件重命名为bootstrap.yml，并配置Eureka Server地址、应用名称、Config的实例名称。服务启动后，会链接Eureka Server服务器，根据Config的实例名称找到对应的Config服务器，并根据实例名称（可以增加profile属性）来匹配配置文件。

```yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka1:8761/eureka/,http://eureka2:8762/eureka/ #指定服务注册地址

spring:
  application:
    name: shopping-product  #应用名称
  cloud:
    config:
      discovery:
        enabled: true
        service-id: config-server
```

- 之前服务端其余的配置，填写在github配置项目shopping-product.yml文件中

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/spring_cloud_app?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
  jpa:
    show-sql: true
    database-platform: org.hibernate.dialect.MySQLDialect

server:
  port: 9000
```

- 同样，shopping-order项目也如此改造，最后我们启动所有的服务，看是否都能正常启动。

![image-20200103151517768](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20200103151521-984976.png)  

## 配置动态刷新

- 首先，在`shopping-product.yml`增加一个配置属性来进行测试

```yaml
env: dev
```

- 新建一个测试controller来绑定这个配置属性，并提供api来返回属性的值

```java
@RestController
@RequestMapping("api/env")
public class EnvController {

    @Value("${env}")
    private String env;

    @RequestMapping
    public String printEnv() {
        return env;
    }
}
```

- 访问http://localhost:9000/api/env，返回当前的值dev。

  Spring Cloud Config 在项目启动时加载配置内容这一机制，但是如果我们修改配置文件内容后，不会自动刷新。例如我们上面的项目，当服务已经启动的时候，去修改 github 上的配置文件内容，这时候，再次刷新页面，对不起，还是旧的配置内容，新内容不会主动刷新过来。那应该怎么去触发配置信息的动态刷新呢？

- 它提供了一个刷新机制，但是需要我们主动触发。那就是 @RefreshScope 注解并结合 actuator ，注意要引入 spring-boot-starter-actuator 包。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- EnvController上增加`@RefreshScope`注解
- 发送 POST 请求到 http://localhost:9000/actuator/refresh 这个接口，默认没有开放endpoint的权限，所以这块我们首先配置开放权限

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

- 这时调用接口结束后，我们看到接口返回消息，表明env这个属性值已经刷新

```json
[
    "config.client.version",
    "env"
]
```

- 再次访问http://localhost:9000/api/env，返回当前的值就是修改后的值test，证明配置属性的值已经动态刷新，我们的程序也不用再次启动。

## 配置 Webhook

每次改了配置后，就用 postman 访问一下 refresh 接口，还是不够方便。 github 提供了一种 webhook 的方式，当有代码变更的时候，会调用我们设置的地址，来实现我们想达到的目的。

- 进入 github 仓库配置页面，选择 Webhooks ，并点击 add webhook；

![image-20200103161333071](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20200103161334-682427.png) 

- 填上回调的地址

  也就是上面提到的 actuator/refresh 这个地址，但是必须保证这个地址是可以被 github 访问到的。这样每当github上修改了配置文件，就自动通知对应的hook地址自动刷新。

## 小结

整体项目结构如下：

> spring-cloud-app
>
> ​		--config-server（统一配置中心）
>
> ​		--eureka-server（服务注册中心）
>
> ​		--shopping-common（购物公共模块）
>
> ​		--shopping-product（商品服务模块）
>
> ​		--shopping-order（订单服务模块）

更新系统架构，新建config-server节点，也向eureka-server注册，相关服务注册节点根据配置实例名称，路由到config-server节点，动态的加载配置。

![image-20200104145953564](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20200104145956-747211.png)  

# 异步消息（Stream）

## 应用场景

1、异步处理

比如用户在电商网站下单，下单完成后会给用户推送短信或邮件，发短信和邮件的过程就可以异步完成。因为下单付款是核心业务，发邮件和短信并不属于核心功能，并且可能耗时较长，所以针对这种业务场景可以选择先放到消息队列中，有其他服务来异步处理。

2、应用解耦：

假设公司有几个不同的系统，各系统在某些业务有联动关系，比如 A 系统完成了某些操作，需要触发 B 系统及 C 系统。如果 A 系统完成操作，主动调用 B 系统的接口或 C 系统的接口，可以完成功能，但是各个系统之间就产生了耦合。用消息中间件就可以完成解耦，当 A 系统完成操作将数据放进消息队列，B 和 C 系统去订阅消息就可以了。这样各系统只要约定好消息的格式就好了。

3、流量削峰

比如秒杀活动，一下子进来好多请求，有的服务可能承受不住瞬时高并发而崩溃，所以针对这种瞬时高并发的场景，在中间加一层消息队列，把请求先入队列，然后再把队列中的请求平滑的推送给服务，或者让服务去队列拉取。

4、日志处理

kafka 最开始就是专门为了处理日志产生的。

当碰到上面的几种情况的时候，就要考虑用消息队列了。如果你碰巧使用的是 RabbitMQ 或者 kafka ，而且同样也是在使用 Spring Cloud ，那可以考虑下用 Spring Cloud Stream。Spring Cloud Stream 是消息中间件组件，它集成了 kafka 和 rabbitmq ，本文以rabbitmq 为例。

## 当前项目场景

分析目前shopping-order项目中，创建订单的代码如下：

```java
/**
     * 创建订单
     *
     */
@Transactional
public String Create(OrderInput orderInput) throws Exception {

    //扣库存
    ResultVo result1=productClient.decreaseStock(orderInput.getOrderItemInputs());
    if (result1.getCode() != 0)
        throw new Exception("调用订单扣减库存接口出错：" + result1.getMsg());

    //构建订单主表
    OrderMaster orderMaster = new OrderMaster();
    BeanUtils.copyProperties(orderInput, orderMaster);
    //指定默认值
    orderMaster.setOrderId(KeyUtil.genUniqueKey("OM"));
    orderMaster.setOrderStatus(OrderStatus.NEW);
    orderMaster.setPayStatus(PayStatus.WAIT);

    //构建订单明细
    List<String> productIds = orderInput.getOrderItemInputs().stream().map(OrderItemInput::getProductId).collect(Collectors.toList());
    ResultVo<List<ProductInfoOutput>> result2 = productClient.findProductInfosByIds(String.join(",", productIds));
    if (result2.getCode() != 0)
        throw new Exception("调用订单查询接口出错：" + result2.getMsg());
    List<ProductInfoOutput> productInfoOutputs = result2.getData();

    //订单金额总计
    BigDecimal total = new BigDecimal(BigInteger.ZERO);
    for (OrderItemInput orderItemInput : orderInput.getOrderItemInputs()) {
        OrderDetail orderDetail = new OrderDetail();
        BeanUtils.copyProperties(orderItemInput, orderDetail);

        Optional<ProductInfoOutput> productInfoOutputOptional = productInfoOutputs.stream()
            .filter(s -> s.getProductId().equals(orderItemInput.getProductId())).findFirst();

        if (!productInfoOutputOptional.isPresent())
            throw new Exception(String.format("商品【%s】不存在", orderItemInput.getProductId()));

        ProductInfoOutput productInfoOutput = productInfoOutputOptional.get();
        orderDetail.setDetailId(KeyUtil.genUniqueKey("OD"));
        orderDetail.setOrderId(orderMaster.getOrderId());
        orderDetail.setProductName(productInfoOutput.getProductName());
        orderDetail.setProductPrice(productInfoOutput.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())));
        orderDetail.setProductIcon(productInfoOutput.getProductIcon());
        total = total.add(orderDetail.getProductPrice());

        orderDetailRepository.save(orderDetail);
    }

    orderMaster.setOrderAmount(total);
    orderMasterRepository.save(orderMaster);
    return orderMaster.getOrderId();
}
```

创建订单的同时，先调用商品接口扣减库存，如果占用库存成功，再生成订单。这样的话，生成订单的操作和占用商品库存的操作其实是耦合在一起的。在实际电商高并发、高流量的情况下，我们很少这么做。所以，我们要将业务解耦，实现订单和扣减库存的异步处理。

大体思路如下：生成订单==》通知商品调用库存==》商品占用库存==》通知订单占用成功==》更新订单占用库存状态

## stream-rabbit集成

shopping-order、shopping-product项目中

- 首先引入stream-rabbit依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
```

- application.yml中作相应的配置：

```yaml
spring:
  rabbitmq:
    host: aliyun.host
    port: 5672
    username: guest
    password: guest
```

- 消息接口定义

```java
public interface StreamClient {

    String INPUT = "myMessage";

    @Input(StreamClient.INPUT)
    SubscribableChannel input();

    @Output(StreamClient.INPUT)
    MessageChannel output();
}
```

- 接收端处理逻辑

```java
@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {

    @StreamListener(value = StreamClient.INPUT)
    public void process(OrderInput orderInput) {
        log.info("StreamReceiver: {}", orderInput);
    }
}
```

- 发送端处理逻辑

```java
@RestController
@RequestMapping("api/v1/stream")
@Slf4j
public class StreamController {

    private final StreamClient streamClient;

    @Autowired
    public StreamController(StreamClient streamClient) {
        this.streamClient = streamClient;
    }

    @GetMapping("/sendMessage")
    public void sendMessage() {
        OrderInput orderInput=new OrderInput();
        orderInput.setBuyerName("小王");
        orderInput.setBuyerPhone("15011111111");
        orderInput.setBuyerAddress("姥姥家");
        orderInput.setBuyerOpenid("11111");
        streamClient.output().send(MessageBuilder.withPayload(orderInput).build());
    }
}
```

启动应用程序，测试发送接口，发现spring-cloud-stream帮我们自动创建了一个队列，消息发送到这个队列，然后被接收端消费。

![image-20191231161853339](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191231161855-234392.png) 

此时，如果我们启动多个shopping-product服务实例，会有个问题，如果发送端发送一条消息，会被2个实例同时消费，在正常的业务中，这种情况是应该避免的。所以我们需要对消息进行分组，在application.yml中增加如下配置，保证只有一个服务实例来消费。

```yaml
spring:
  rabbitmq:
    host: aliyun.host
    port: 5672
    username: guest
    password: guest
  cloud:
    stream:
      bindings:
        myMessage:
          group: shopping-order
          content-type: application/json
```

## 改造Order和Product项目

shopping-order作为库存占用命令的消息发送者，首先向shopping-product发送消息stock_apply（占用库存申请），shopping-product接收此消息进行库存处理，然后将库存占用处理的结果作为消息stock_result（占用库存结果）发送，shopping-order端再收到结果消息对订单状态进行更新。

- shopping-order配置：

```yaml
spring:
  cloud:
    stream:
      bindings:
        stock_apply_output:           #占用库存申请
          destination: stock.apply
        stock_result_input:           #占用库存结果
          destination: stock.result
          group: shopping-order
```

- shopping-product配置：

```yaml
spring:
  cloud:
    stream:
      bindings:
        stock_apply_input:            #占用库存申请
          destination: stock.apply
          group: shopping-product
        stock_result_output:          #占用库存结果
          destination: stock.result
```

- shopping-order定义channel

```java
public interface OrderStream {

    String STOCK_APPLY_OUTPUT = "stock_apply_output";
    @Output(OrderStream.STOCK_APPLY_OUTPUT)
    MessageChannel stockApplyOutput();

    String STOCK_RESULT_INPUT = "stock_result_input";
    @Input(OrderStream.STOCK_RESULT_INPUT)
    SubscribableChannel stockResultInput();
}
```

- shopping-product定义channel

```java
public interface ProductStream {

    String STOCK_APPLY_INPUT = "stock_apply_input";
    @Input(ProductStream.STOCK_APPLY_INPUT)
    SubscribableChannel stockApplyInput();

    String STOCK_RESULT_OUTPUT = "stock_result_output";
    @Output(ProductStream.STOCK_RESULT_OUTPUT)
    MessageChannel stockResultOutput();
}

```

- shopping-order发送库存申请消息

```java
/**
     * 创建订单
     */
    @Transactional
    public String Create(OrderInput orderInput) throws Exception {

        //构建订单主表
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderInput, orderMaster);
        //指定默认值
        orderMaster.setOrderId(KeyUtil.genUniqueKey("OM"));
        orderMaster.setOrderStatus(OrderStatus.NEW);
        orderMaster.setPayStatus(PayStatus.WAIT);

        //构建订单明细
        List<String> productIds = orderInput.getOrderItemInputs().stream().map(OrderItemInput::getProductId).collect(Collectors.toList());
        ResultVo<List<ProductInfoOutput>> result2 = productClient.findProductInfosByIds(String.join(",", productIds));
        if (result2.getCode() != 0)
            throw new Exception("调用订单查询接口出错：" + result2.getMsg());
        List<ProductInfoOutput> productInfoOutputs = result2.getData();

        //订单金额总计
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for (OrderItemInput orderItemInput : orderInput.getOrderItemInputs()) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(orderItemInput, orderDetail);

            Optional<ProductInfoOutput> productInfoOutputOptional = productInfoOutputs.stream()
                    .filter(s -> s.getProductId().equals(orderItemInput.getProductId())).findFirst();

            if (!productInfoOutputOptional.isPresent())
                throw new Exception(String.format("商品【%s】不存在", orderItemInput.getProductId()));

            ProductInfoOutput productInfoOutput = productInfoOutputOptional.get();
            orderDetail.setDetailId(KeyUtil.genUniqueKey("OD"));
            orderDetail.setOrderId(orderMaster.getOrderId());
            orderDetail.setProductName(productInfoOutput.getProductName());
            orderDetail.setProductPrice(productInfoOutput.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())));
            orderDetail.setProductIcon(productInfoOutput.getProductIcon());
            total = total.add(orderDetail.getProductPrice());

            orderDetailRepository.save(orderDetail);
        }

        orderMaster.setOrderAmount(total);
        orderMasterRepository.save(orderMaster);

        //扣库存
        StockApplyInput stockApplyInput = new StockApplyInput();
        stockApplyInput.setOrderId(orderMaster.getOrderId());
        stockApplyInput.setOrderItemInputs(orderInput.getOrderItemInputs());
        orderStream.stockApplyOutput().send(MessageBuilder.withPayload(stockApplyInput).build());

        return orderMaster.getOrderId();
    }
```

- shopping-product处理库存申请消息，并发送库存处理结果

```java
@Service
@Slf4j
@EnableBinding(ProductStream.class)
public class ProductService {

    private final ProductInfoRepository productInfoRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductService(ProductInfoRepository productInfoRepository,
                          ProductCategoryRepository productCategoryRepository) {
        this.productInfoRepository = productInfoRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * 扣减库存
     *
     */
    @Transactional
    @StreamListener(ProductStream.STOCK_APPLY_INPUT)
    @SendTo(ProductStream.STOCK_RESULT_OUTPUT)
    public StockResultOutput processStockApply(StockApplyInput stockApplyInput) throws Exception {

        log.info("占用库存消息被消费...");
        StockResultOutput stockResultOutput = new StockResultOutput();
        stockResultOutput.setOrderId(stockApplyInput.getOrderId());

        try {
            for (OrderItemInput orderItemInput : stockApplyInput.getOrderItemInputs()) {

                Optional<ProductInfo> productInfoOptional = productInfoRepository.findById(orderItemInput.getProductId());
                if (!productInfoOptional.isPresent())
                    throw new Exception("商品不存在.");

                ProductInfo productInfo = productInfoOptional.get();
                int result = productInfo.getProductStock() - orderItemInput.getProductQuantity();
                if (result < 0)
                    throw new Exception("商品库存不满足.");

                productInfo.setProductStock(result);
                productInfoRepository.save(productInfo);
            }

            stockResultOutput.setIsSuccess(true);
            stockResultOutput.setMessage("OK");
            return stockResultOutput;
        } catch (Exception e) {
            stockResultOutput.setIsSuccess(false);
            stockResultOutput.setMessage(e.getMessage());
            return stockResultOutput;
        }

    }

}
```

- shopping-order处理库存处理结果

```java
@StreamListener(OrderStream.STOCK_RESULT_INPUT)
public void processStockResult(StockResultOutput stockResultOutput) {

    log.info("库存消息返回" + stockResultOutput);

    Optional<OrderMaster> optionalOrderMaster = orderMasterRepository.findById(stockResultOutput.getOrderId());
    if (optionalOrderMaster.isPresent()) {
        OrderMaster orderMaster = optionalOrderMaster.get();
        if (stockResultOutput.getIsSuccess()) {
            orderMaster.setOrderStatus(OrderStatus.OCCUPY_SUCCESS);
        } else {
            orderMaster.setOrderStatus(OrderStatus.OCCUPY_FAILURE);
        }
        orderMasterRepository.save(orderMaster);
    }
}
```

执行调试结果，跟踪执行结果：生成订单同时发送库存申请命令，商品模块处理库存申请成功后，返回库存占用结果告知订单模块，从而实现订单生成和商品库存占用的逻辑的解耦。

## 小结

在原有的架构基础上，我们对商品和订单服务进行了应用解耦，库存占用逻辑异步化，通过消息队列传递消息，并结合spring cloud stream对消息input和output绑定，使得在程序中很方便的进行消息发送和接收处理。

![image-20200104151114254](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20200104151115-821748.png)  

# 容器化部署

## 安装Docker

```bash
[root@localhost ~]# yum install docker
[root@localhost ~]# systemctrl enable docker	#设置docker开机启动
[root@localhost ~]# systemctrl start docker		#启动docker
```

- 配置vi /etc/docker/deamon.json，添加国内加速镜像

```json
{
 "registry-mirrors": ["http://hub-mirror.c.163.com"],
 "registry-mirrors": ["https://njrds9qc.mirror.aliyuncs.com"]
}
```

- 重启生效

```bash
[root@localhost ~]# systemctl daemon-reload
[root@localhost ~]# systemctl restart docker
```

- 验证是否成功安装

```bash
[root@localhost ~]# docker -v
Docker version 1.13.1, build 7f2769b/1.13.1
```

## 安装Docker-Compose

- 检查是否安装python-pip

```bash
[root@localhost ~]# pip -V
```

- 已安装pip则跳过该步骤，否则安装pip

```bash
[root@localhost ~]# yum -y install epel-release
[root@localhost ~]# yum -y install python-pip
[root@localhost ~]# pip install --upgrade pip
```

- 安装docker-compose

```bash
[root@localhost ~]# pip install docker-compose
```

- 安装过程中如果出现Command errored python setup.py egg_info 可尝试升级setuptools

```bash
[root@localhost ~]# pip install more-itertools==5.0.0
```

- 验证是否成功安装

```bash
[root@localhost ~]# docker-compose -v
docker-compose version 1.25.0, build b42d419
```

## Eureka部署

- 首先我们创建2个节点的配置文件

application.yml：

```yaml
spring:
  application:
    name: eureka-server #应用名称
  profiles:
    active: peer1
```

application-peer1.yml：

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://peer2:8762/eureka/ #指定服务注册地址

server:
  port: 8761  #应用服务端口
```

application-peer2.yml：

```yaml
eureka:
    client:
      service-url:
        defaultZone: http://peer1:8761/eureka/ #指定服务注册地址

    server:
      port: 8762  #应用服务端口
```

- 在eureka-server项目下新建Dockerfile文件

```dockerfile
FROM hub.c.163.com/library/java:8-alpine
ADD target/*.jar app.jar
EXPOSE 8761
EXPOSE 8762
ENTRYPOINT ["java","-jar","/app.jar"]
```

- 构建镜像：

```bash
mvn clean package -Dmaven.test.skip=true -U
docker build -t spring-cloud-app/eureka-server:v1 .
```

## MySQL部署

- 拉取MySQL的镜像文件

```bash
[root@localhost ~]# docker pull mysql:5.7
```

- 在docker-compose.yml文件中相关配置

```yaml
  mysql:
    image: docker.io/mysql:5.7
    hostname: mysql
    networks:
      - eureka-net
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: "123456"
    volumes:
      - "./mysql/conf:/etc/mysql"
      - "./mysql/logs:/var/log/mysql"
      - "./mysql/data:/var/lib/mysql"
```

## RabbitMQ

` -management` 表示有管理界面的，可以浏览器访问。5672是访问端口，15672是管理端口。

```bash
[root@localhost ~]# docker run -d --hostname rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.2-management
```

访问端口管理界面，输入默认用户名/密码 ：guest/guest

![image-20191231144756782](https://typora-lancelot.oss-cn-beijing.aliyuncs.com/typora/20191231144758-140801.png) 

## 编排镜像

docker-compose.yml：

```yaml
version: "2"
services:
  eureka1:      # 默认情况下，其他服务可以使用服务名称连接到该服务。因此，对于peer2的节点，它需要连接http://peer1:8761/eureka/，因此需要配置该服务的名称是peer1。
    image: spring-cloud-app/eureka-server:v1
    hostname: eureka1
    networks:
      - eureka-net
    ports:
      - "8761:8761"
    environment:
      - spring.profiles.active=peer1
  eureka2:
    image: spring-cloud-app/eureka-server:v1
    hostname: eureka2
    networks:
      - eureka-net
    ports:
      - "8762:8762"
    environment:
      - spring.profiles.active=peer2
  config-server:
    image: spring-cloud-app/config-server:v1
    hostname: config-server
    networks:
      - eureka-net
    ports:
      - "8888:8888"
  mysql:
    image: docker.io/mysql:5.7
    hostname: mysql
    networks:
      - eureka-net
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: "123456"
    volumes:
      - "./mysql/conf:/etc/mysql"
      - "./mysql/logs:/var/log/mysql"
      - "./mysql/data:/var/lib/mysql"
  rabbitmq:
    image: docker.io/rabbitmq:3.8.2-management
    hostname: rabbitmq
    networks:
      - eureka-net
    ports:
      - "5672:5672"
      - "15672:15672"
networks:
  eureka-net:
    driver: bridge
```

