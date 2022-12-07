package com.mecc.simplerest;

import com.mecc.currency.api.model.Currency;
import com.mecc.grpcservice.ExchangeRateServiceGrpc.ExchangeRateServiceBlockingStub;
import com.mecc.grpcservice.GetExchangeRateRequest;
import com.mecc.grpcservice.GetExchangeRateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.mecc.simplerest.FeignClients.CurrencyClient;

import static net.devh.boot.grpc.common.util.GrpcUtils.CLOUD_DISCOVERY_METADATA_PORT;


@org.springframework.stereotype.Service
public class Service {
    private final DiscoveryClient discoveryClient;

    @GrpcClient("exchange-rate")
    private ExchangeRateServiceBlockingStub exchangeRateService;

    private CurrencyClient currencyClient;


    public Service(DiscoveryClient discoveryClient, CurrencyClient currencyClient) {
        this.discoveryClient = discoveryClient;
        this.currencyClient = currencyClient;
    }


    public RateInfo getExchangeRate(String currencyCode) throws Exception {
        ResponseEntity<List<Currency>> currencyResponse = currencyClient.getCurrencies();
        if (currencyResponse.getStatusCode() != HttpStatus.OK) throw new Exception("Can't get list of valid currencies.");

        List<Currency> currencies = currencyResponse.getBody();
        Optional<Currency> currency = currencies.stream().filter(
                c -> c.getCode().equalsIgnoreCase(currencyCode)).findFirst();
        if (!currency.isPresent()) throw new Exception("Currency code is not valid.");

        GetExchangeRateRequest.Builder requestBuilder = GetExchangeRateRequest.newBuilder();
        requestBuilder.setCurrency(currencyCode);
        GetExchangeRateResponse rateResponse = exchangeRateService.getExchangeRate(requestBuilder.build());

        return new RateInfo("USD",
                "USA Dollar",
                                currencyCode,
                                currency.get().getName(),
                                rateResponse.getRate());
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
        private String fromCurrencyName;
        private String toCurrency;
        private String toCurrencyName;
        private Double rate;
    }
}
