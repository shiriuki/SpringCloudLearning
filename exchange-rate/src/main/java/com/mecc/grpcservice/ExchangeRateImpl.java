package com.mecc.grpcservice;


import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;


@Slf4j
@GrpcService
public class ExchangeRateImpl extends ExchangeRateServiceGrpc.ExchangeRateServiceImplBase {

    @Override
    public void getExchangeRate(GetExchangeRateRequest request,
                                StreamObserver<GetExchangeRateResponse> responseObserver) {
        String currency = request.getCurrency().toUpperCase();
        log.info("Getting value for " + currency);

        if (!currency.equals("CRC") && !currency.equals("MXN")) {
            throw new IllegalArgumentException("don't know the exchange rate of " + currency);
        }

        GetExchangeRateResponse response = GetExchangeRateResponse.newBuilder()
                .setRate(currency.equals("CRC") ? 600 : 19.20)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
