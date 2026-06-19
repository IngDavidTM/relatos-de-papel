package com.relatosdepapel.comms.service;

import com.relatosdepapel.comms.client.CatalogueClient;
import com.relatosdepapel.comms.client.dto.CataloguePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Agente de atención al cliente simulado sobre la API de Gemini (versión
 * gratuita). Se le facilita como contexto la información de los productos del
 * catálogo para que ofrezca respuestas razonables.
 */
@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    private static final String SYSTEM_PROMPT = """
            Eres "Papelito", el asistente virtual de atención al cliente de la librería online
            "Relatos de Papel". Respondes de forma breve, amable y en el idioma del usuario.
            Ayudas con recomendaciones de libros, disponibilidad, precios y dudas sobre pedidos y envíos.
            Si no dispones de un dato concreto, lo indicas con honestidad y sugieres contactar con soporte.
            Catálogo disponible (muestra):
            %s
            """;

    private final CatalogueClient catalogueClient;
    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    private volatile String catalogueContext;

    public GeminiService(CatalogueClient catalogueClient,
                         @Value("${gemini.api-key:}") String apiKey,
                         @Value("${gemini.model:gemini-2.0-flash}") String model,
                         @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl) {
        this.catalogueClient = catalogueClient;
        this.apiKey = apiKey;
        this.model = model;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public String ask(String userMessage) {
        if (!StringUtils.hasText(apiKey)) {
            return "El asistente no está disponible en este momento. "
                    + "Por favor, escribe a soporte@relatosdepapel.com y te ayudaremos encantados.";
        }

        try {
            String prompt = SYSTEM_PROMPT.formatted(getCatalogueContext())
                    + "\n\nMensaje del cliente: " + userMessage;

            Map<String, Object> body = Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

            GeminiResponse response = restClient.post()
                    .uri("/models/{model}:generateContent?key={key}", model, apiKey)
                    .body(body)
                    .retrieve()
                    .body(GeminiResponse.class);

            return extractText(response);
        } catch (Exception ex) {
            log.error("Error consultando a Gemini: {}", ex.getMessage());
            return "Disculpa, ahora mismo no puedo responder. Inténtalo de nuevo en unos instantes.";
        }
    }

    private String getCatalogueContext() {
        if (catalogueContext == null) {
            synchronized (this) {
                if (catalogueContext == null) {
                    catalogueContext = buildCatalogueContext();
                }
            }
        }
        return catalogueContext;
    }

    private String buildCatalogueContext() {
        try {
            CataloguePage page = catalogueClient.getBooks(40, true);
            if (page == null || page.content() == null || page.content().isEmpty()) {
                return "(catálogo no disponible temporalmente)";
            }
            StringBuilder sb = new StringBuilder();
            for (CataloguePage.BookSummary b : page.content()) {
                sb.append("- ").append(b.title())
                        .append(" (").append(b.author()).append("), ")
                        .append(b.category()).append(", ").append(b.price()).append(" €")
                        .append(b.rating() != null ? ", valoración " + b.rating() + "/5" : "")
                        .append('\n');
            }
            return sb.toString();
        } catch (Exception ex) {
            log.warn("No se pudo cargar el contexto del catálogo: {}", ex.getMessage());
            return "(catálogo no disponible temporalmente)";
        }
    }

    @SuppressWarnings("unchecked")
    private String extractText(GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            return "No he encontrado una respuesta para tu consulta.";
        }
        var parts = response.candidates().get(0).content().parts();
        if (parts == null || parts.isEmpty()) {
            return "No he encontrado una respuesta para tu consulta.";
        }
        return parts.get(0).text();
    }

    // --- Estructuras mínimas de la respuesta de Gemini ---
    record GeminiResponse(List<Candidate> candidates) {}
    record Candidate(Content content) {}
    record Content(List<Part> parts) {}
    record Part(String text) {}
}
