package com.mecc.simplerest;

import com.mecc.grpcservice.ExchangeRateServiceGrpc.ExchangeRateServiceBlockingStub;
import com.mecc.grpcservice.GetExchangeRateRequest;
import com.mecc.grpcservice.GetExchangeRateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import java.util.List;
import java.util.stream.Collectors;

import static net.devh.boot.grpc.common.util.GrpcUtils.CLOUD_DISCOVERY_METADATA_PORT;


@org.springframework.stereotype.Service
public class Service {
    private final DiscoveryClient discoveryClient;

    @GrpcClient("exchange-rate")
    private ExchangeRateServiceBlockingStub exchangeRateService;


    public Service(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


    public List<Address> showAvailable(String service) {
        return discoveryClient.getInstances(service)
                .stream().map(i -> new Address(
                        i.getHost(),
                        i.getMetadata().get(CLOUD_DISCOVERY_METADATA_PORT) != null ?
                                Integer.parseInt(i.getMetadata().get(CLOUD_DISCOVERY_METADATA_PORT)) :
                                i.getPort()))
                .collect(Collectors.toList());
    }


    public RateInfo getExchangeRate(String currency) {
        currency = currency.toUpperCase();

        GetExchangeRateRequest.Builder requestBuilder = GetExchangeRateRequest.newBuilder();
        requestBuilder.setCurrency(currency);
        GetExchangeRateResponse response = exchangeRateService.getExchangeRate(requestBuilder.build());

        return new RateInfo("USD", currency, response.getRate());
    }


    @Getter
    @Setter
    @AllArgsConstructor
    static class Address {
        private String ip;
        private Integer port;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    static class RateInfo {
        private String fromCurrency;
        private String toCurrency;
        private Double rate;
    }
}
