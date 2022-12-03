package com.mecc.simplerest;

import com.mecc.grpcservice.ExchangeRateServiceGrpc.ExchangeRateServiceBlockingStub;
import com.mecc.grpcservice.GetExchangeRateRequest;
import com.mecc.grpcservice.GetExchangeRateResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;


@Service
public class ExchangeRateService {

    @GrpcClient("exchange-rate")
    private ExchangeRateServiceBlockingStub exchangeRateService;


    public Double getExchangeRate(String currency) {
        GetExchangeRateRequest.Builder requestBuilder = GetExchangeRateRequest.newBuilder();
        requestBuilder.setCurrency(currency);

        GetExchangeRateResponse response = exchangeRateService.getExchangeRate(requestBuilder.build());
        return response.getRate();
    }
}
