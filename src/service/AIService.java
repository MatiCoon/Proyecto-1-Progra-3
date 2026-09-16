package service;

import com.google.gson.*;
import model.Categoria;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AIService {
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=";

    public static ReservaExtraidaDTO extraerDatosReserva(String textoNatural, List<Categoria> categoriasDisponibles) throws Exception {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("Variable de entorno GEMINI_API_KEY no configurada.");
        }

        String categoriasStr = categoriasDisponibles.stream()
                .map(Categoria::getDescripcion)
                .collect(Collectors.joining(", "));

        String prompt = String.format(
                "Hoy es %s. Extrae la informacion de reserva del texto: \"%s\". " +
                        "Categorias disponibles en el sistema: [%s]. " +
                        "Responde con un JSON que tenga exactamente estos campos: " +
                        "\"actividad\" (string), \"fecha\" (YYYY-MM-DD), \"horaInicio\" (HH:MM), \"horaFin\" (HH:MM), " +
                        "\"categorias\" (lista de strings de las categorias disponibles que coincidan o se infieran).",
                LocalDate.now(), textoNatural, categoriasStr
        );

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("responseMimeType", "application/json");

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contents);
        requestBody.add("generationConfig", generationConfig);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en API Gemini (" + response.statusCode() + "): " + response.body());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        String jsonText = jsonResponse.getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        return new Gson().fromJson(jsonText, ReservaExtraidaDTO.class);
    }
}