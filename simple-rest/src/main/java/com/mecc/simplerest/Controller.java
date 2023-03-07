package com.mecc.simplerest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class Controller {

    private final Service service;

    @Value( "${msg}" )
    private String msg;

    @Value( "${securemsg}" )
    private String secureMsg;


    public Controller(Service service) {
        this.service = service;
    }


    @GetMapping("/sayhello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok().body(msg);
    }


    @GetMapping("/showsecure")
    public ResponseEntity<String> showSecure() {
        return ResponseEntity.ok().body(secureMsg);
    }


    @GetMapping("/getrate/{currency}")
    public ResponseEntity<Service.RateInfo> getRate(@PathVariable String currency) {

        try {
            return ResponseEntity.ok().body(service.getExchangeRate(currency));

        } catch (Throwable e) {
            return ResponseEntity.noContent().build();
        }
    }
}
