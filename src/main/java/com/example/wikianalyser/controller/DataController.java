package com.example.wikianalyser.controller;

import com.example.wikianalyser.model.JSONParser;
import com.example.wikianalyser.model.XMLParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.Flow;

@Controller
public class DataController {

    @GetMapping(path = "/recentChangesstreaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Mono<String> getRecentChanges() {
        Mono<String> stringFlux =  WebClient.create()
                .get().uri("https://uk.wikipedia.org/w/api.php?" +
                        "action=feedrecentchanges")
                .retrieve().bodyToFlux(String.class)
                .take(100)
                .delaySubscription(Duration.ofSeconds(3))
                .repeat()
                .reduce("", (s1, s2) -> s1 + s2)
                .map(s -> XMLParser.parse(s));
        return stringFlux;
    }

    @GetMapping(path = "/recentActivity&username={username}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Mono<String> getUserActivity(@PathVariable final String username) {
        return WebClient.create()
                .get().uri("https://uk.wikipedia.org/w/api.php?" +
                        "action=query&format=json&list=usercontribs&ucuser=" + username)
                .retrieve().bodyToFlux(String.class)
                .delaySubscription(Duration.ofSeconds(3))
                .reduce("", (s1, s2) -> s1 + s2)
                .map(s -> JSONParser.parse(s));
                //.repeat();
    }

    @GetMapping(path = "/userStats&username={username}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Mono<String> getUserStatistics(@PathVariable final String username) {
        return WebClient.create()
                .get().uri("https://uk.wikipedia.org/w/api.php?" +
                        "action=query&format=json&list=usercontribs&ucuser=" + username)
                .retrieve().bodyToFlux(String.class)
                .delaySubscription(Duration.ofSeconds(3))
                .reduce("", (s1, s2) -> s1 + s2)
                .map(s -> JSONParser.getStats(s));
        //.repeat();
    }

    @GetMapping(path = "/mostActiveUser",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getMostActiveUser() {
        return WebClient.create()
                .get().uri("https://en.wikipedia.org/w/api.php?" +
                        "action=feedrecentchanges&limit=3")
                .retrieve().bodyToFlux(String.class)
                .delaySubscription(Duration.ofSeconds(5));
        //.repeat();
    }

    @GetMapping(path = "/topTenTopics",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getTopTenTopics() {
        return WebClient.create()
                .get().uri("https://en.wikipedia.org/w/api.php?" +
                        "action=feedrecentchanges&limit=3")
                .retrieve().bodyToFlux(String.class)
                .delaySubscription(Duration.ofSeconds(5));
        //.repeat();
    }
}
