package org.example.actividadfinaldi.service;

import org.example.actividadfinaldi.model.Alquiler;
import org.example.actividadfinaldi.model.Cliente;
import org.example.actividadfinaldi.model.TipoVehiculo;
import org.example.actividadfinaldi.model.Vehiculo;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para AlquilerFileService
 */
@DisplayName("Tests de AlquilerFileService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Alquilerfileservicetest {

    private AlquilerFileService fileService;
    private static final String ARCHIVO_TEST = "alquileres.txt";
    private Cliente clientePrueba;
    private Vehiculo vehiculoPrueba;

    @BeforeEach
    void setUp() {
        fileService = new AlquilerFileService();

        // Crear objetos de prueba
        clientePrueba = new Cliente(
                "María",
                "González López",
                "12345678A",
                LocalDate.of(1990, 5, 15)
        );
        clientePrueba.setId(1);

        vehiculoPrueba = new Vehiculo(
                "1234ABC",
                "POL-2024-001",
                TipoVehiculo.MEDIANO,
                LocalDate.of(2020, 6, 10)
        );
        vehiculoPrueba.setId(1);
    }

    @Test
    @Order(1)
    @DisplayName("Guardar alquiler en archivo")
    void testGuardarAlquiler() {
        Alquiler alquiler = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 22)
        );
        alquiler.setId(1);

        boolean resultado = fileService.guardarAlquiler(alquiler);

        assertTrue(resultado, "El alquiler debería guardarse correctamente");
    }

    @Test
    @Order(2)
    @DisplayName("Archivo de alquileres existe después de guardar")
    void testArchivoExiste() {
        Alquiler alquiler = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        fileService.guardarAlquiler(alquiler);

        File archivo = new File(ARCHIVO_TEST);
        assertTrue(archivo.exists(), "El archivo de alquileres debería existir");
    }

    @Test
    @Order(3)
    @DisplayName("Contenido del archivo tiene formato correcto")
    void testFormatoContenido() throws IOException {
        Alquiler alquiler = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 8)
        );

        fileService.guardarAlquiler(alquiler);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TEST))) {
            String ultimaLinea = null;
            String linea;
            while ((linea = reader.readLine()) != null) {
                ultimaLinea = linea;
            }

            assertNotNull(ultimaLinea, "El archivo debería tener contenido");
            assertTrue(ultimaLinea.contains("María"), "Debería contener el nombre del cliente");
            assertTrue(ultimaLinea.contains("González López"), "Debería contener los apellidos");
            assertTrue(ultimaLinea.contains("12345678A"), "Debería contener el DNI");
            assertTrue(ultimaLinea.contains("1234ABC"), "Debería contener la matrícula");
            assertTrue(ultimaLinea.contains("2024-02-01"), "Debería contener la fecha de inicio");
            assertTrue(ultimaLinea.contains("2024-02-08"), "Debería contener la fecha de fin");
        }
    }

    @Test
    @DisplayName("Guardar múltiples alquileres")
    void testGuardarMultiplesAlquileres() {
        List<Alquiler> alquileres = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Alquiler alquiler = new Alquiler(
                    clientePrueba,
                    vehiculoPrueba,
                    LocalDate.now().plusDays(i),
                    LocalDate.now().plusDays(i + 7)
            );
            alquileres.add(alquiler);
        }

        for (Alquiler alquiler : alquileres) {
            boolean resultado = fileService.guardarAlquiler(alquiler);
            assertTrue(resultado, "Cada alquiler debería guardarse correctamente");
        }
    }

    @Test
    @DisplayName("Contenido incluye timestamp")
    void testContieneTimestamp() throws IOException {
        Alquiler alquiler = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        fileService.guardarAlquiler(alquiler);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TEST))) {
            String ultimaLinea = null;
            String linea;
            while ((linea = reader.readLine()) != null) {
                ultimaLinea = linea;
            }

            assertNotNull(ultimaLinea);
            // El timestamp debería tener formato yyyy-MM-dd HH:mm:ss
            assertTrue(
                    ultimaLinea.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*"),
                    "Debería contener un timestamp con formato correcto"
            );
        }
    }

    @Test
    @DisplayName("Contenido incluye duración en días")
    void testContieneDuracionDias() throws IOException {
        Alquiler alquiler = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 11) // 10 días
        );

        fileService.guardarAlquiler(alquiler);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TEST))) {
            String ultimaLinea = null;
            String linea;
            while ((linea = reader.readLine()) != null) {
                ultimaLinea = linea;
            }

            assertNotNull(ultimaLinea);
            assertTrue(ultimaLinea.contains("Dias: 10"), "Debería contener la duración en días");
        }
    }

    @Test
    @DisplayName("Archivo se actualiza con append")
    void testArchivoAppend() throws IOException {
        // Guardar primer alquiler
        Alquiler alquiler1 = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );
        fileService.guardarAlquiler(alquiler1);

        // Contar líneas
        int lineasAntesDeSegundo = contarLineas();

        // Guardar segundo alquiler
        Alquiler alquiler2 = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(15)
        );
        fileService.guardarAlquiler(alquiler2);

        // Contar líneas nuevamente
        int lineasDespuesDeSegundo = contarLineas();

        assertEquals(
                lineasAntesDeSegundo + 1,
                lineasDespuesDeSegundo,
                "Debería haber una línea más después de guardar el segundo alquiler"
        );
    }

    @Test
    @DisplayName("Formato incluye todos los separadores")
    void testFormatoConSeparadores() throws IOException {
        Alquiler alquiler = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        fileService.guardarAlquiler(alquiler);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TEST))) {
            String ultimaLinea = null;
            String linea;
            while ((linea = reader.readLine()) != null) {
                ultimaLinea = linea;
            }

            assertNotNull(ultimaLinea);

            // Verificar que contiene el separador "|"
            int separadores = ultimaLinea.length() - ultimaLinea.replace("|", "").length();
            assertTrue(separadores >= 5, "Debería contener al menos 5 separadores |");
        }
    }

    @Test
    @DisplayName("Guardar alquiler con diferentes tipos de vehículo")
    void testGuardarDiferentesTiposVehiculo() {
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            Vehiculo vehiculo = new Vehiculo(
                    "TEST" + tipo.name(),
                    "POL-" + tipo.name(),
                    tipo,
                    LocalDate.now()
            );
            vehiculo.setId(1);

            Alquiler alquiler = new Alquiler(
                    clientePrueba,
                    vehiculo,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5)
            );

            boolean resultado = fileService.guardarAlquiler(alquiler);
            assertTrue(resultado, "Debería guardar alquiler con vehículo tipo " + tipo);
        }
    }

    @Test
    @DisplayName("Guardar alquiler con diferentes duraciones")
    void testGuardarDiferentesDuraciones() {
        int[] duraciones = {1, 3, 7, 14, 30, 60};

        for (int dias : duraciones) {
            Alquiler alquiler = new Alquiler(
                    clientePrueba,
                    vehiculoPrueba,
                    LocalDate.now(),
                    LocalDate.now().plusDays(dias)
            );

            boolean resultado = fileService.guardarAlquiler(alquiler);
            assertTrue(resultado, "Debería guardar alquiler de " + dias + " días");
        }
    }

    @Test
    @DisplayName("Contenido legible por humanos")
    void testContenidoLegible() throws IOException {
        Alquiler alquiler = new Alquiler(
                clientePrueba,
                vehiculoPrueba,
                LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 4, 8)
        );

        fileService.guardarAlquiler(alquiler);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TEST))) {
            String ultimaLinea = null;
            String linea;
            while ((linea = reader.readLine()) != null) {
                ultimaLinea = linea;
            }

            assertNotNull(ultimaLinea);
            // Verificar que contiene etiquetas descriptivas
            assertTrue(ultimaLinea.contains("Cliente:"), "Debería tener etiqueta Cliente:");
            assertTrue(ultimaLinea.contains("DNI:"), "Debería tener etiqueta DNI:");
            assertTrue(ultimaLinea.contains("Vehiculo:"), "Debería tener etiqueta Vehiculo:");
            assertTrue(ultimaLinea.contains("Inicio:"), "Debería tener etiqueta Inicio:");
            assertTrue(ultimaLinea.contains("Fin:"), "Debería tener etiqueta Fin:");
            assertTrue(ultimaLinea.contains("Dias:"), "Debería tener etiqueta Dias:");
        }
    }

    /**
     * Método auxiliar para contar líneas en el archivo
     */
    private int contarLineas() throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TEST))) {
            while (reader.readLine() != null) {
                count++;
            }
        }
        return count;
    }

    @AfterAll
    static void cleanup() {
        // Nota: En un entorno de producción, podrías querer limpiar
        // el archivo de prueba aquí
        System.out.println("Tests de AlquilerFileService completados");
        System.out.println("El archivo " + ARCHIVO_TEST + " contiene los registros de prueba");
    }
}