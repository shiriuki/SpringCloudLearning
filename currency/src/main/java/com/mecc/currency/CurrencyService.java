package com.mecc.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CurrencyService {

    public List<CurrencyInfo> getList() {
        return new ArrayList<>() {{
            add(new CurrencyInfo("CRC", "Costa Rican Colon"));
            add(new CurrencyInfo("MXN", "Mexican Peso"));
        }};
    };


    @Getter
    @Setter
    @AllArgsConstructor
    static class CurrencyInfo {
        private String code;
        private String name;
    }
}