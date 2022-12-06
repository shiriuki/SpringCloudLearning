package com.mecc.currency;

import com.mecc.currency.api.model.Currency;
import com.mecc.currency.api.model.CurrencyList;
import org.springframework.stereotype.Service;


@Service
public class CurrencyService {

    public CurrencyList getList() {
        return new CurrencyList() {{
            addCurrenciesItem(new Currency().code("CRC").name("Costa Rican Colon"));
            addCurrenciesItem(new Currency().code("MXN").name("Mexican Peso"));
        }};
    }
}