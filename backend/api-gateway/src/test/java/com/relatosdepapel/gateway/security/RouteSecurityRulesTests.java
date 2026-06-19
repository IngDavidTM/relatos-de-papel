package com.relatosdepapel.gateway.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteSecurityRulesTests {

    private final RouteSecurityRules rules = new RouteSecurityRules();

    @Test
    void exposesOnlyClientFacingAuthenticationOperations() {
        assertTrue(rules.isPublic(HttpMethod.POST, "/api/auth/tokens"));
        assertTrue(rules.isPublic(HttpMethod.POST, "/api/auth/tokens/refresh"));
        assertTrue(rules.isPublic(HttpMethod.POST, "/api/auth/tokens/revoke"));
        assertFalse(rules.isPublic(HttpMethod.POST, "/api/auth/tokens/validate"));
        assertTrue(rules.isInternalOnly("/api/auth/tokens/validate"));
    }

    @Test
    void protectsCustomerDataAndCatalogueWrites() {
        assertTrue(rules.isPublic(HttpMethod.GET, "/api/catalogue/books"));
        assertFalse(rules.isPublic(HttpMethod.GET, "/api/orders/me/recent"));
        assertTrue(rules.requiresAdmin(HttpMethod.POST, "/api/catalogue/books"));
        assertFalse(rules.requiresAdmin(HttpMethod.GET, "/api/catalogue/books"));
    }
}
