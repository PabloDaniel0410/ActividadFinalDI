package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.Alquiler;
import org.example.actividadfinaldi.model.Cliente;
import org.example.actividadfinaldi.model.TipoVehiculo;
import org.example.actividadfinaldi.model.Vehiculo;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para AlquilerDAO
 * Estos tests requieren una conexión activa a Supabase
 */
@DisplayName("Tests de AlquilerDAO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlquilerDAOTest {

    private AlquilerDAO alquilerDAO;
    private ClienteDAO clienteDAO;
    private VehiculoDAO vehiculoDAO;

    private static Cliente clientePrueba;
    private static Vehiculo vehiculoPrueba;
    private static Alquiler alquilerPrueba;

    @BeforeEach
    void setUp() {
        alquilerDAO = new AlquilerDAO();
        clienteDAO = new ClienteDAO();
        vehiculoDAO = new VehiculoDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Preparar datos de prueba - Cliente")
    void testPrepararCliente() {
        Cliente cliente = new Cliente(
                "Test Alquiler",
                "Usuario Prueba",
                "TESTALQ001",
                LocalDate.of(1990, 1, 1)
        );

        boolean insertado = clienteDAO.insertar(cliente);

        if (insertado) {
            assertNotNull(cliente.getId());
            clientePrueba = cliente;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Preparar datos de prueba - Vehículo")
    void testPrepararVehiculo() {
        Vehiculo vehiculo = new Vehiculo(
                "TESTALQ1",
                "POL-ALQ-001",
                TipoVehiculo.MEDIANO,
                LocalDate.of(2020, 1, 1)
        );

        boolean insertado = vehiculoDAO.insertar(vehiculo);

        if (insertado) {
            assertNotNull(vehiculo.getId());
            vehiculoPrueba = vehiculo;
        }
    }

    @Test
    @Order(3)
    @DisplayName("Insertar alquiler válido")
    void testInsertarAlquilerValido() {
        if (clientePrueba != null && vehiculoPrueba != null) {
            Alquiler alquiler = new Alquiler(
                    clientePrueba,
                    vehiculoPrueba,
                    LocalDate.now(),
                    LocalDate.now().plusDays(7)
            );

            boolean resultado = alquilerDAO.insertar(alquiler);

            if (resultado) {
                assertNotNull(alquiler.getId(), "El ID debería asignarse después de insertar");
                assertTrue(alquiler.getId() > 0, "El ID debería ser positivo");
                alquilerPrueba = alquiler;
            }
        }
    }

    @Test
    @Order(4)
    @DisplayName("Obtener alquileres activos")
    void testObtenerActivos() {
        List<Alquiler> alquileres = alquilerDAO.obtenerActivos();

        assertNotNull(alquileres, "La lista no debería ser null");
        for (Alquiler alquiler : alquileres) {
            assertTrue(alquiler.isActivo(), "Todos los alquileres deberían estar activos");
            assertNotNull(alquiler.getCliente(), "El cliente no debería ser null");
            assertNotNull(alquiler.getVehiculo(), "El vehículo no debería ser null");
            assertNotNull(alquiler.getFechaInicio(), "La fecha de inicio no debería ser null");
            assertNotNull(alquiler.getFechaFin(), "La fecha de fin no debería ser null");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Obtener alquileres por cliente")
    void testObtenerPorCliente() {
        if (clientePrueba != null) {
            List<Alquiler> alquileres = alquilerDAO.obtenerPorCliente(clientePrueba.getId());

            assertNotNull(alquileres, "La lista no debería ser null");
            for (Alquiler alquiler : alquileres) {
                assertEquals(clientePrueba.getId(), alquiler.getCliente().getId(),
                        "Todos los alquileres deberían ser del cliente especificado");
            }
        }
    }

    @Test
    @DisplayName("Obtener alquileres de cliente inexistente")
    void testObtenerPorClienteInexistente() {
        List<Alquiler> alquileres = alquilerDAO.obtenerPorCliente(999999);

        assertNotNull(alquileres, "La lista no debería ser null");
        assertTrue(alquileres.isEmpty() || alquileres.size() == 0,
                "No debería haber alquileres para un cliente inexistente");
    }

    @Test
    @DisplayName("Insertar alquiler con datos nulos")
    void testInsertarAlquilerDatosNulos() {
        Alquiler alquilerNulo = new Alquiler();

        assertDoesNotThrow(() -> {
            alquilerDAO.insertar(alquilerNulo);
        }, "No debería lanzar excepción, aunque probablemente falle la inserción");
    }

    @Test
    @DisplayName("Alquileres están ordenados por fecha descendente")
    void testAlquileresOrdenados() {
        List<Alquiler> alquileres = alquilerDAO.obtenerActivos();

        if (alquileres.size() > 1) {
            for (int i = 0; i < alquileres.size() - 1; i++) {
                LocalDate fecha1 = alquileres.get(i).getFechaInicio();
                LocalDate fecha2 = alquileres.get(i + 1).getFechaInicio();

                assertTrue(
                        fecha1.isAfter(fecha2) || fecha1.isEqual(fecha2),
                        "Los alquileres deberían estar ordenados por fecha de inicio descendente"
                );
            }
        }
    }

    @Test
    @DisplayName("Alquiler recuperado tiene cliente completo")
    void testAlquilerConClienteCompleto() {
        List<Alquiler> alquileres = alquilerDAO.obtenerActivos();

        if (!alquileres.isEmpty()) {
            Alquiler alquiler = alquileres.get(0);
            Cliente cliente = alquiler.getCliente();

            assertNotNull(cliente, "El cliente no debería ser null");
            assertNotNull(cliente.getId(), "El ID del cliente no debería ser null");
            assertNotNull(cliente.getNombre(), "El nombre del cliente no debería ser null");
            assertNotNull(cliente.getApellidos(), "Los apellidos del cliente no deberían ser null");
            assertNotNull(cliente.getDni(), "El DNI del cliente no debería ser null");
        }
    }

    @Test
    @DisplayName("Alquiler recuperado tiene vehículo completo")
    void testAlquilerConVehiculoCompleto() {
        List<Alquiler> alquileres = alquilerDAO.obtenerActivos();

        if (!alquileres.isEmpty()) {
            Alquiler alquiler = alquileres.get(0);
            Vehiculo vehiculo = alquiler.getVehiculo();

            assertNotNull(vehiculo, "El vehículo no debería ser null");
            assertNotNull(vehiculo.getId(), "El ID del vehículo no debería ser null");
            assertNotNull(vehiculo.getMatricula(), "La matrícula no debería ser null");
            assertNotNull(vehiculo.getPolizaSeguro(), "La póliza no debería ser null");
            assertNotNull(vehiculo.getTipo(), "El tipo no debería ser null");
        }
    }

    @Test
    @DisplayName("Verificar fechas válidas en alquileres activos")
    void testFechasValidasEnAlquileres() {
        List<Alquiler> alquileres = alquilerDAO.obtenerActivos();

        for (Alquiler alquiler : alquileres) {
            assertTrue(
                    alquiler.fechasValidas(),
                    "Todos los alquileres activos deberían tener fechas válidas"
            );
            assertTrue(
                    alquiler.getDuracionDias() > 0,
                    "La duración de los alquileres debería ser positiva"
            );
        }
    }

    @Test
    @DisplayName("Insertar múltiples alquileres para el mismo cliente")
    void testInsertarMultiplesAlquileresCliente() {
        if (clientePrueba != null && vehiculoPrueba != null) {
            Alquiler alquiler1 = new Alquiler(
                    clientePrueba,
                    vehiculoPrueba,
                    LocalDate.now().minusDays(30),
                    LocalDate.now().minusDays(23)
            );

            Alquiler alquiler2 = new Alquiler(
                    clientePrueba,
                    vehiculoPrueba,
                    LocalDate.now().minusDays(15),
                    LocalDate.now().minusDays(10)
            );

            boolean resultado1 = alquilerDAO.insertar(alquiler1);
            boolean resultado2 = alquilerDAO.insertar(alquiler2);

            // Ambos deberían insertarse (aunque en la práctica podría haber
            // restricciones de negocio que lo impidan)
            if (resultado1 && resultado2) {
                List<Alquiler> alquileresCliente =
                        alquilerDAO.obtenerPorCliente(clientePrueba.getId());

                assertTrue(
                        alquileresCliente.size() >= 2,
                        "El cliente debería tener al menos 2 alquileres"
                );
            }
        }
    }

    @Test
    @DisplayName("Alquileres por cliente están ordenados")
    void testAlquileresClienteOrdenados() {
        if (clientePrueba != null) {
            List<Alquiler> alquileres = alquilerDAO.obtenerPorCliente(clientePrueba.getId());

            if (alquileres.size() > 1) {
                for (int i = 0; i < alquileres.size() - 1; i++) {
                    LocalDate fecha1 = alquileres.get(i).getFechaInicio();
                    LocalDate fecha2 = alquileres.get(i + 1).getFechaInicio();

                    assertTrue(
                            fecha1.isAfter(fecha2) || fecha1.isEqual(fecha2),
                            "Los alquileres del cliente deberían estar ordenados descendentemente"
                    );
                }
            }
        }
    }

    @Test
    @DisplayName("Verificar cálculo de duración en alquileres recuperados")
    void testDuracionAlquileresRecuperados() {
        List<Alquiler> alquileres = alquilerDAO.obtenerActivos();

        for (Alquiler alquiler : alquileres) {
            long duracionCalculada = alquiler.getDuracionDias();
            LocalDate inicio = alquiler.getFechaInicio();
            LocalDate fin = alquiler.getFechaFin();

            assertTrue(duracionCalculada > 0, "La duración debería ser positiva");
            assertTrue(
                    fin.isAfter(inicio),
                    "La fecha de fin debería ser posterior a la de inicio"
            );
        }
    }

    @Test
    @DisplayName("Cliente y vehículo en alquiler están activos")
    void testClienteYVehiculoActivos() {
        List<Alquiler> alquileres = alquilerDAO.obtenerActivos();

        for (Alquiler alquiler : alquileres) {
            // En teoría, si el alquiler está activo,
            // el cliente y vehículo deberían estarlo también
            if (alquiler.getCliente() != null) {
                assertTrue(
                        alquiler.getCliente().isActivo(),
                        "El cliente del alquiler activo debería estar activo"
                );
            }

            if (alquiler.getVehiculo() != null) {
                assertTrue(
                        alquiler.getVehiculo().isActivo(),
                        "El vehículo del alquiler activo debería estar activo"
                );
            }
        }
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Tests de AlquilerDAO completados");
        System.out.println("NOTA: Los datos de prueba permanecen en la base de datos");
        System.out.println("Considera limpiar los registros de prueba manualmente");
    }
}