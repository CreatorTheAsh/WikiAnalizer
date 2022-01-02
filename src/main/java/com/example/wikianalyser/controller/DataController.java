package com.example.wikianalyser.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.time.Duration;

@Controller
public class DataController {
    @GetMapping(path = "/recentChangesstreaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getRecentChanges() {
        return WebClient.create()
                .get().uri("https://en.wikipedia.org/w/api.php?" +
                        "action=feedrecentchanges&limit=3")
                .retrieve().bodyToFlux(String.class)
                .delaySubscription(Duration.ofSeconds(5))
                .repeat();
    }
}
