package tech.lancelot.shoppingproduct.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("api/env")
public class EnvController {

    @Value("${env}")
    private String env;

    @RequestMapping
    public String printEnv() {
        return env;
    }
}
