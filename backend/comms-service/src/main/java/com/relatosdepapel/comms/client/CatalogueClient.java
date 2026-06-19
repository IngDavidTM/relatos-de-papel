package com.relatosdepapel.comms.client;

import com.relatosdepapel.comms.client.dto.CataloguePage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente para volcar la información de los productos del catálogo y construir
 * el contexto que se le facilita al agente de IA.
 */
@FeignClient(name = "catalogue-service")
public interface CatalogueClient {

    @GetMapping("/api/catalogue/books")
    CataloguePage getBooks(@RequestParam("size") int size,
                           @RequestParam("visibility") boolean visibility);
}
