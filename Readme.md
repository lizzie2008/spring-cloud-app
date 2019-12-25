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
