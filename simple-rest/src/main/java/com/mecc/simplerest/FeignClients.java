package com.mecc.simplerest;

import com.mecc.currency.api.CurrencyApi;
import org.springframework.cloud.openfeign.FeignClient;

public class FeignClients {

    @FeignClient(name="currency", url="${CURRENCY_SERVICE:localhost:9004}")
    public interface CurrencyClient extends CurrencyApi { }
}
