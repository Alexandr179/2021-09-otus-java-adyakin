package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private String jsonString;

    public ResourcesFileLoader(String resourceFileName) {
        try (InputStream resourceAsStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(resourceFileName)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            jsonString = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Measurement> load() {
        var gson = new Gson();
        return Arrays.asList(gson.fromJson(jsonString, Measurement[].class));
    }
}
