package com.relatosdepapel.orders.client;

import com.relatosdepapel.orders.client.dto.CatalogueBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalogue-service", path = "/api/catalogue/books")
public interface CatalogueClient {

    @GetMapping("/{id}")
    CatalogueBookResponse findBookById(@PathVariable("id") Long id);
}
