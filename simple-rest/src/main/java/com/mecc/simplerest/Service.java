package com.mecc.simplerest;

import com.mecc.currency.api.model.Currency;
import com.mecc.grpcservice.ExchangeRateServiceGrpc.ExchangeRateServiceBlockingStub;
import com.mecc.grpcservice.GetExchangeRateRequest;
import com.mecc.grpcservice.GetExchangeRateResponse;
import com.mecc.simplerest.FeignClients.CurrencyClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service
public class Service {

    @GrpcClient("exchange-rate")
    private ExchangeRateServiceBlockingStub exchangeRateService;

    private CurrencyClient currencyClient;


    public Service(CurrencyClient currencyClient) {
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
