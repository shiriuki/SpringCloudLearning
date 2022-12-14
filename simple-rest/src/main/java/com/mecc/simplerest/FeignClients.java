package com.mecc.simplerest;

import com.mecc.currency.api.CurrencyApi;
import org.springframework.cloud.openfeign.FeignClient;

public class FeignClients {

    @FeignClient("${CURRENCY_SERVICE_NAME:currency}")
    public interface CurrencyClient extends CurrencyApi { }
}
