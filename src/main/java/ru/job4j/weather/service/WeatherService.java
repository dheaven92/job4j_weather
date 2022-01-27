package ru.job4j.weather.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.job4j.weather.model.Weather;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final Map<Integer, Weather> weatherData = new ConcurrentHashMap<>();

    public WeatherService() {
        weatherData.putAll(Map.of(
                1, new Weather(1, "Moscow", 20),
                2, new Weather(2, "St. Petersburg", 18),
                3, new Weather(3, "Kiev", 23),
                4, new Weather(4, "Minsk", 22)
        ));
    }

    public Flux<Weather> findAll() {
        return Flux.fromIterable(weatherData.values());
    }

    public Mono<Weather> findById(int id) {
        return Mono.justOrEmpty(weatherData.get(id));
    }

    public Mono<Weather> getHottest() {
        return Mono.justOrEmpty(
                weatherData.values().stream()
                        .max(Comparator.comparingInt(Weather::getTemperature))
        );
    }

    public Flux<Weather> findAllWithTemperatureGreaterThan(int temperature) {
        return Flux.fromIterable(
                weatherData.values().stream()
                        .filter(weather -> weather.getTemperature() > temperature)
                        .collect(Collectors.toList())
        );
    }
}
