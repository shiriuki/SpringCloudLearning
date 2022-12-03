package com.mecc.grpcservice;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class ExchangeRateImpl extends ExchangeRateServiceGrpc.ExchangeRateServiceImplBase {

    @Override
    public void getExchangeRate(GetExchangeRateRequest request,
                                StreamObserver<GetExchangeRateResponse> responseObserver) {
        String currency = request.getCurrency();

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
