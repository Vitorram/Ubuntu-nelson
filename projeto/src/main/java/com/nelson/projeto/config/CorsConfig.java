package com.nelson.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration cors = new CorsConfiguration();
        // Allow any subdomain hosted under app.github.dev (covers codespaces subdomains like
        // urban-cod-...-8080.app.github.dev and urban-cod-...-5500.app.github.dev)
        cors.addAllowedOriginPattern("https://*.app.github.dev");
        // Also allow some local dev origins (optional, useful when running locally)
        cors.addAllowedOriginPattern("http://localhost:5500");
        cors.addAllowedOriginPattern("http://127.0.0.1:5500");

        // Allow all headers the browser may send (Content-Type, Authorization, etc.)
        cors.addAllowedHeader("*");

        // Allow common HTTP methods (includes preflight OPTIONS)
        cors.addAllowedMethod("GET");
        cors.addAllowedMethod("POST");
        cors.addAllowedMethod("PUT");
        cors.addAllowedMethod("DELETE");
        cors.addAllowedMethod("OPTIONS");

        // Allow credentials (cookies/authorization headers) if front-end needs them.
        // Using addAllowedOriginPattern allows the server to echo the request origin
        // which keeps credentials working safely (unlike '*' which is incompatible
        // with credentials).
        cors.setAllowCredentials(true);

        // Let the browser cache preflight results for a while to reduce round-trips.
        cors.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);

        return new CorsFilter(source);
    }
}
