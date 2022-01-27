package ru.job4j.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.model.Weather;
import ru.job4j.weather.service.WeatherService;

import java.time.Duration;

@RequiredArgsConstructor
@RequestMapping("/weather")
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> getAll() {
        Flux<Weather> data = weatherService.findAll();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> getById(@PathVariable int id) {
        return weatherService.findById(id);
    }

    @GetMapping(value = "/hottest")
    public Mono<Weather> getHottest() {
        return weatherService.getHottest();
    }

    @GetMapping(value = "/greater-than/{temperature}")
    public Flux<Weather> getAllGreaterThan(@PathVariable int temperature) {
        return weatherService.findAllWithTemperatureGreaterThan(temperature);
    }
}
