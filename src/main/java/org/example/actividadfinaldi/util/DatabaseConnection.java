package org.example.actividadfinaldi.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Gestiona la conexión con Supabase mediante API REST
 */
public class DatabaseConnection {

    private static final HttpClient client = HttpClient.newHttpClient();

    // IMPORTANTE: Cambia esta URL por la de tu proyecto Supabase
    private static final String BASE_URL = "https://abwizvhfubnachixobdj.supabase.co/rest/v1";

    // IMPORTANTE: Obtén tu anon key desde Project Settings > API > anon public
    private static final String ANON_KEY = "sb_publishable_F5GEPGmK7xgf3PmrV6z5gQ_RieoQA7y";

    /**
     * Crea un constructor base de peticiones HTTP con los headers necesarios
     */
    private static HttpRequest.Builder baseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", ANON_KEY)
                .header("Authorization", "Bearer " + ANON_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation");
    }

    /**
     * Realiza una petición GET a Supabase
     */
    public static JSONArray get(String tabla, String filtro) throws Exception {
        String url = BASE_URL + "/" + tabla;
        if (filtro != null && !filtro.isEmpty()) {
            url += "?" + filtro;
        }

        HttpRequest request = baseRequest(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new JSONArray(response.body());
        }
        throw new Exception("Error en GET: " + response.statusCode());
    }

    /**
     * Realiza una petición POST a Supabase
     */
    public static JSONObject post(String tabla, JSONObject datos) throws Exception {
        String url = BASE_URL + "/" + tabla;

        HttpRequest request = baseRequest(url)
                .POST(HttpRequest.BodyPublishers.ofString(datos.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            JSONArray arr = new JSONArray(response.body());
            return arr.length() > 0 ? arr.getJSONObject(0) : null;
        }
        throw new Exception("Error en POST: " + response.statusCode() + " - " + response.body());
    }

    /**
     * Realiza una petición PATCH a Supabase
     */
    public static JSONObject patch(String tabla, String filtro, JSONObject datos) throws Exception {
        String url = BASE_URL + "/" + tabla + "?" + filtro;

        HttpRequest request = baseRequest(url)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(datos.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray arr = new JSONArray(response.body());
            return arr.length() > 0 ? arr.getJSONObject(0) : null;
        }
        throw new Exception("Error en PATCH: " + response.statusCode());
    }

    /**
     * Realiza una petición DELETE a Supabase
     */
    public static boolean delete(String tabla, String filtro) throws Exception {
        String url = BASE_URL + "/" + tabla + "?" + filtro;

        HttpRequest request = baseRequest(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 204 || response.statusCode() == 200;
    }

    /**
     * Codifica un valor para usarlo en URLs
     */
    public static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * Inicializa las tablas en la base de datos
     * NOTA: Con Supabase REST API, las tablas deben crearse desde el panel web
     */
    public static void inicializarTablas() {
        System.out.println("IMPORTANTE: Las tablas deben crearse desde el panel de Supabase");
        System.out.println("SQL necesario:");
        System.out.println("""
            CREATE TABLE IF NOT EXISTS clientes (
                id SERIAL PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                apellidos VARCHAR(100) NOT NULL,
                dni VARCHAR(20) UNIQUE NOT NULL,
                fecha_nacimiento DATE NOT NULL,
                activo BOOLEAN DEFAULT true
            );
            
            CREATE TABLE IF NOT EXISTS vehiculos (
                id SERIAL PRIMARY KEY,
                matricula VARCHAR(20) UNIQUE NOT NULL,
                poliza_seguro VARCHAR(50) NOT NULL,
                tipo VARCHAR(20) NOT NULL,
                fecha_matriculacion DATE NOT NULL,
                activo BOOLEAN DEFAULT true
            );
            
            CREATE TABLE IF NOT EXISTS alquileres (
                id SERIAL PRIMARY KEY,
                cliente_id INTEGER REFERENCES clientes(id),
                vehiculo_id INTEGER REFERENCES vehiculos(id),
                fecha_inicio DATE NOT NULL,
                fecha_fin DATE NOT NULL,
                activo BOOLEAN DEFAULT true
            );
        """);
    }

    /**
     * Método de compatibilidad (no hace nada con REST API)
     */
    public static void closeConnection() {
        // No es necesario cerrar conexiones con REST API
    }
}