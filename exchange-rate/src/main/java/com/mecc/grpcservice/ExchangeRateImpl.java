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

        if (!currency.equals("CRC") && !currency.equals("MXN")) {
            throw new IllegalArgumentException("don't know the exchange rate of " + currency);
        }

        double val;

        if (currency.equals("CRC")) {
            double total = 0;

            for (int i = 0; i < 100000; ++i) {
                String randomStr = String.valueOf( Math.random() );
                double random = Double.valueOf(randomStr.substring(randomStr.length() - 2));
                total +=  Math.tan(random);
            }

            val = total;

        } else {
            val = 19.20;
        }

        GetExchangeRateResponse response = GetExchangeRateResponse.newBuilder()
                .setRate(val)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("Returned value for " + currency + " " + val);
    }

}
