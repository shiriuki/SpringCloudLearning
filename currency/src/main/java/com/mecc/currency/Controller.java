package com.mecc.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RefreshScope
@RestController
public class Controller {

    private final CurrencyService currencyService;


    public Controller(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping("/list")
    public ListResponse list() {
        return new ListResponse(true, currencyService.getList(), "");
    }


    @Getter @Setter @AllArgsConstructor
    static class ListResponse {
        private boolean success;
        private List<CurrencyService.CurrencyInfo> list;
        private String error = "";
    }
}
