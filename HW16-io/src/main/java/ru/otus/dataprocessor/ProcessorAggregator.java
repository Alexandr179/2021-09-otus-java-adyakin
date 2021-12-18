package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // HOW DON'T MAKE..

//        NavigableMap<String, Double> map = new TreeMap<>();
//        data.forEach(measurement -> map.merge(measurement.getName(), measurement.getValue(), Double::sum));
//        return map;

        // ..also so man get sorted collection
        return data.stream().collect(Collectors.groupingBy(
                Measurement::getName, TreeMap::new,
                Collectors.summingDouble(Measurement::getValue))
        );
    }
}
