package com.mecc.simplerest;

import io.grpc.StatusRuntimeException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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
    public String sayHello() { return msg; }


    @GetMapping("/showsecure")
    public String showSecure() {
        return secureMsg;
    }


    @GetMapping("/showavailable/{serviceId}")
    public ServiceAvailableResponse showAvailable(@PathVariable String serviceId) {
        ServiceAvailableResponse response = new ServiceAvailableResponse();

        try {
            response.list = service.showAvailable(serviceId);
            response.success = true;

        } catch (Throwable e)  {
            response.error = e.getMessage();
        }

        return response;
    }


    @GetMapping("/getrate/{currency}")
    public RateResponse getRate(@PathVariable String currency) {
        RateResponse response = new RateResponse();

        try {
            response.rate = service.getExchangeRate(currency);
            response.success = true;

        } catch (StatusRuntimeException e)  {
            response.error = e.getMessage();
        }

        return response;
    }


    @Getter @Setter @NoArgsConstructor
    static class ServiceAvailableResponse {
        private boolean success;
        private List<Service.Address> list;
        private String error = "";
    }


    @Getter @Setter @NoArgsConstructor
    static class RateResponse {
        private boolean success;
        private Double rate;
        private String error = "";
    }

}
