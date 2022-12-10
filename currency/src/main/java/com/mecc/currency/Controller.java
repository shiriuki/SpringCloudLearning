package com.mecc.currency;

import com.mecc.currency.api.CurrencyApi;
import com.mecc.currency.api.model.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RefreshScope
@RestController
public class Controller implements CurrencyApi {

    private final CurrencyService currencyService;


    public Controller(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @Override
    public ResponseEntity<List<Currency>> getCurrencies() {
        log.info("Returning currencies info.");
        return ResponseEntity.ok().body(currencyService.getList());
    }

}
