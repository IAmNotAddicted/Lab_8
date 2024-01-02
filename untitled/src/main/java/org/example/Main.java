package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main
{
    private static final String API_URL = "https://api.open-meteo.com/weather";

    public static void main(String[] args)
    {
        try
        {
            String apiResponse = fetchDataFromApi();

            List<WeatherData> weatherDataList = parseWeatherData(apiResponse);


            findExtremeTemperatures(weatherDataList);

            findMostHumidStations(weatherDataList);


            findStationsWithConsecutiveRainyDays(weatherDataList);

            findStationsWithTemperatureIncrease(weatherDataList);


            calculateMonthlyAverages(weatherDataList);

            findMonthWithHighestWindSpeed(weatherDataList);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
}
    private static String fetchDataFromApi() throws IOException
    {
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(API_URL);


        HttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        try (InputStream inputStream = entity.getContent())
        {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(inputStream);

            return jsonNode.toString();
        }
    }

    private static List<WeatherData> parseWeatherData(String apiResponse) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(apiResponse);

        JsonNode dataArray = jsonNode.get("data");

        return objectMapper.readValue(dataArray.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, WeatherData.class));
    }

    private static void findExtremeTemperatures(List<WeatherData> weatherDataList)
    {
        Map<String, Double> averageTemperatureByStation = weatherDataList.stream()
                .collect(Collectors.groupingBy(WeatherData::getStationId,
                        Collectors.averagingDouble(WeatherData::getTemperature)));

        List<String> hottestStations = averageTemperatureByStation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> coldestStations = averageTemperatureByStation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static void findMostHumidStations(List<WeatherData> weatherDataList)
    {
    }

    private static void findStationsWithConsecutiveRainyDays(List<WeatherData> weatherDataList)
    {
    }

    private static void findStationsWithTemperatureIncrease(List<WeatherData> weatherDataList)
    {
    }

    private static void calculateMonthlyAverages(List<WeatherData> weatherDataList)
    {
    }

    private static void findMonthWithHighestWindSpeed(List<WeatherData> weatherDataList)
    {
    }

    private static class WeatherData
    {
        private String date;
        private String stationId;
        private double temperature;
        private int humidity;
        private double precipitation;
        private double windSpeed;

        public WeatherData(String date, String stationId, double temperature, int humidity, double precipitation, double windSpeed)
        {
            this.date = date;
            this.stationId = stationId;
            this.temperature = temperature;
            this.humidity = humidity;
            this.precipitation = precipitation;
            this.windSpeed = windSpeed;
        }
    }