package com.mecc.currency;

import com.mecc.currency.api.model.Currency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CurrencyService {

    public List<Currency> getList() {
        return new ArrayList<>() {{
            add(new Currency().code("CRC").name("Costa Rican Colon"));
            add(new Currency().code("MXN").name("Mexican Peso"));
        }};
    }
}