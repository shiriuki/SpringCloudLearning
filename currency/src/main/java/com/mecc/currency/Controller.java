package com.mecc.currency;

import com.mecc.currency.api.CurrencyApi;
import com.mecc.currency.api.model.CurrencyList;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RefreshScope
@RestController
public class Controller implements CurrencyApi {

    private final CurrencyService currencyService;


    public Controller(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @Override
    public ResponseEntity<CurrencyList> getCurrencies() {
        return ResponseEntity.ok().body(currencyService.getList());
    }

}
