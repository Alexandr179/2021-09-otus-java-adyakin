package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try {
            writeTextFile(new ObjectMapper().writeValueAsString(sortMapByDoubleValue(data)));
        } catch (FileProcessException | IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Double> sortMapByDoubleValue(Map<String, Double> data) {
        data = data.entrySet().stream()// https://stackoverflow.com/questions/64896063/java-sort-map-by-value-with-stream
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return data;
    }

    private void writeTextFile(String jsonString) throws IOException {
        try (var bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(jsonString);
        }
    }
}
