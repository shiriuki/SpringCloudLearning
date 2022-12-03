package com.mecc.simplerest;

import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static net.devh.boot.grpc.common.util.GrpcUtils.CLOUD_DISCOVERY_METADATA_PORT;

@RefreshScope
@RestController
public class Controller {
    private final DiscoveryClient discoveryClient;
    private final ExchangeRateService exchangeRateService;

    @Value( "${msg}" )
    private String msg;

    @Value( "${securemsg}" )
    private String secureMsg;


    public Controller(DiscoveryClient discoveryClient, ExchangeRateService exchangeRateService) {
        this.discoveryClient = discoveryClient;
        this.exchangeRateService = exchangeRateService;
    }


    @GetMapping("/sayhello")
    public String sayHello() { return msg; }


    @GetMapping("/showsecure")
    public String showSecure() {
        return secureMsg;
    }


    @GetMapping("/showavailable/{service}")
    public List<Address> showAvailable(@PathVariable String service) {
        return discoveryClient.getInstances(service)
                .stream().map(i -> new Address(
                        i.getHost(),
                        i.getMetadata().get(CLOUD_DISCOVERY_METADATA_PORT) != null ?
                                Integer.parseInt(i.getMetadata().get(CLOUD_DISCOVERY_METADATA_PORT)) :
                                i.getPort()))
                .collect(Collectors.toList());
    }


    @GetMapping("/getrate/{currency}")
    public RateResponse getRate(@PathVariable String currency) {
        RateResponse response = new RateResponse();

        try {
            response.rate = exchangeRateService.getExchangeRate(currency);
            response.success = true;

        } catch (StatusRuntimeException e)  {
            response.error = e.getMessage();
        }

        return response;
    }


    @Getter @Setter @NoArgsConstructor
    static class RateResponse {
        private boolean success;
        private Double rate;
        private String error = "";
    }

    @Getter @Setter @AllArgsConstructor
    static class Address {
        private String ip;
        private Integer port;
    }
}
