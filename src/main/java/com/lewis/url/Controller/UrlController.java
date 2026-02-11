package com.lewis.url.Controller;

import com.lewis.url.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/shorturl")
public class UrlController {

    @Autowired
    private com.lewis.url.UrlRepository repository;

    @PostMapping
    public Map<String, Object> shorten(@RequestParam String url) {
        // FCC check: simple validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return Map.of("error", "invalid url");
        }

        ShortUrl saved = repository.save(new ShortUrl(url));
        return Map.of("original_url", saved.getOriginalUrl(), "short_url", saved.getId());
    }

    @GetMapping("/{id}")
    public void redirectToFullUrl(@PathVariable Long id, HttpServletResponse response) throws IOException {
        com.lewis.url.ShortUrl entry = repository.findById(id).orElse(null);
        if (entry != null) {
            response.sendRedirect(entry.getOriginalUrl());
        } else {
            response.sendError(404, "URL not found");
        }
    }
}