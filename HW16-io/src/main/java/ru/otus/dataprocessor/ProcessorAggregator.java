package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        NavigableMap<String, Double> map = new TreeMap<>();
        BiFunction<Double, Double, Double> bFunc = Double::sum;
        data.forEach(measurement -> map.merge(measurement.getName(), measurement.getValue(), bFunc));
        return map;
    }
}
