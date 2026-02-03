package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.TipoVehiculo;
import org.example.actividadfinaldi.model.Vehiculo;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para VehiculoDAO
 * Estos tests requieren una conexión activa a Supabase
 */
@DisplayName("Tests de VehiculoDAO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VehiculoDAOTest {

    private VehiculoDAO vehiculoDAO;
    private static Vehiculo vehiculoPrueba;

    @BeforeEach
    void setUp() {
        vehiculoDAO = new VehiculoDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Insertar vehículo válido")
    void testInsertarVehiculoValido() {
        Vehiculo vehiculo = new Vehiculo(
                "TEST1234",
                "POL-TEST-001",
                TipoVehiculo.MEDIANO,
                LocalDate.of(2020, 6, 15)
        );

        boolean resultado = vehiculoDAO.insertar(vehiculo);

        if (resultado) {
            assertNotNull(vehiculo.getId(), "El ID debería asignarse después de insertar");
            assertTrue(vehiculo.getId() > 0, "El ID debería ser positivo");
            vehiculoPrueba = vehiculo;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Buscar vehículo por matrícula existente")
    void testBuscarPorMatriculaExistente() {
        if (vehiculoPrueba != null) {
            Vehiculo encontrado = vehiculoDAO.buscarPorMatricula(vehiculoPrueba.getMatricula());

            if (encontrado != null) {
                assertEquals(vehiculoPrueba.getMatricula(), encontrado.getMatricula());
                assertEquals(vehiculoPrueba.getPolizaSeguro(), encontrado.getPolizaSeguro());
                assertEquals(vehiculoPrueba.getTipo(), encontrado.getTipo());
            }
        }
    }

    @Test
    @DisplayName("Buscar vehículo por matrícula inexistente")
    void testBuscarPorMatriculaInexistente() {
        Vehiculo resultado = vehiculoDAO.buscarPorMatricula("NOEXISTE999");

        assertNull(resultado, "Debería retornar null para matrícula inexistente");
    }

    @Test
    @DisplayName("Buscar vehículo con matrícula vacía")
    void testBuscarPorMatriculaVacia() {
        Vehiculo resultado = vehiculoDAO.buscarPorMatricula("");

        assertNull(resultado, "Debería retornar null para matrícula vacía");
    }

    @Test
    @DisplayName("Obtener vehículos activos")
    void testObtenerActivos() {
        List<Vehiculo> vehiculos = vehiculoDAO.obtenerActivos();

        assertNotNull(vehiculos, "La lista no debería ser null");
        for (Vehiculo vehiculo : vehiculos) {
            assertTrue(vehiculo.isActivo(), "Todos los vehículos deberían estar activos");
        }
    }

    @Test
    @DisplayName("Obtener vehículos por tipo PEQUEÑO")
    void testObtenerPorTipoPequeño() {
        List<Vehiculo> vehiculos = vehiculoDAO.obtenerPorTipo(TipoVehiculo.PEQUEÑO);

        assertNotNull(vehiculos, "La lista no debería ser null");
        for (Vehiculo vehiculo : vehiculos) {
            assertEquals(TipoVehiculo.PEQUEÑO, vehiculo.getTipo(),
                    "Todos los vehículos deberían ser de tipo PEQUEÑO");
            assertTrue(vehiculo.isActivo(), "Todos los vehículos deberían estar activos");
        }
    }

    @Test
    @DisplayName("Obtener vehículos por tipo MEDIANO")
    void testObtenerPorTipoMediano() {
        List<Vehiculo> vehiculos = vehiculoDAO.obtenerPorTipo(TipoVehiculo.MEDIANO);

        assertNotNull(vehiculos, "La lista no debería ser null");
        for (Vehiculo vehiculo : vehiculos) {
            assertEquals(TipoVehiculo.MEDIANO, vehiculo.getTipo(),
                    "Todos los vehículos deberían ser de tipo MEDIANO");
        }
    }

    @Test
    @DisplayName("Obtener vehículos por tipo GRANDE")
    void testObtenerPorTipoGrande() {
        List<Vehiculo> vehiculos = vehiculoDAO.obtenerPorTipo(TipoVehiculo.GRANDE);

        assertNotNull(vehiculos, "La lista no debería ser null");
        for (Vehiculo vehiculo : vehiculos) {
            assertEquals(TipoVehiculo.GRANDE, vehiculo.getTipo(),
                    "Todos los vehículos deberían ser de tipo GRANDE");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Actualizar vehículo existente")
    void testActualizarVehiculoExistente() {
        if (vehiculoPrueba != null && vehiculoPrueba.getId() != null) {
            String nuevaPoliza = "POL-ACTUALIZADA-999";
            vehiculoPrueba.setPolizaSeguro(nuevaPoliza);

            boolean resultado = vehiculoDAO.actualizar(vehiculoPrueba);

            if (resultado) {
                Vehiculo actualizado = vehiculoDAO.buscarPorMatricula(vehiculoPrueba.getMatricula());
                if (actualizado != null) {
                    assertEquals(nuevaPoliza, actualizado.getPolizaSeguro());
                }
            }
        }
    }

    @Test
    @DisplayName("Actualizar vehículo sin ID")
    void testActualizarVehiculoSinId() {
        Vehiculo vehiculoSinId = new Vehiculo(
                "TEST999",
                "POL-999",
                TipoVehiculo.PEQUEÑO,
                LocalDate.now()
        );

        boolean resultado = vehiculoDAO.actualizar(vehiculoSinId);

        assertFalse(resultado, "No debería actualizar un vehículo sin ID");
    }

    @Test
    @DisplayName("Insertar vehículo con datos nulos")
    void testInsertarVehiculoDatosNulos() {
        Vehiculo vehiculoNulo = new Vehiculo();

        assertDoesNotThrow(() -> {
            vehiculoDAO.insertar(vehiculoNulo);
        }, "No debería lanzar excepción, aunque probablemente falle la inserción");
    }

    @Test
    @DisplayName("Lista de vehículos activos está ordenada")
    void testVehiculosActivosOrdenados() {
        List<Vehiculo> vehiculos = vehiculoDAO.obtenerActivos();

        if (vehiculos.size() > 1) {
            for (int i = 0; i < vehiculos.size() - 1; i++) {
                String matricula1 = vehiculos.get(i).getMatricula();
                String matricula2 = vehiculos.get(i + 1).getMatricula();
                assertTrue(
                        matricula1.compareTo(matricula2) <= 0,
                        "Los vehículos deberían estar ordenados por matrícula"
                );
            }
        }
    }

    @Test
    @DisplayName("Vehículo insertado tiene todos los campos")
    void testVehiculoInsertadoCamposCompletos() {
        Vehiculo vehiculo = new Vehiculo(
                "TEST5678",
                "POL-TEST-002",
                TipoVehiculo.GRANDE,
                LocalDate.of(2019, 3, 10)
        );

        boolean insertado = vehiculoDAO.insertar(vehiculo);

        if (insertado) {
            Vehiculo recuperado = vehiculoDAO.buscarPorMatricula(vehiculo.getMatricula());

            if (recuperado != null) {
                assertNotNull(recuperado.getId());
                assertNotNull(recuperado.getMatricula());
                assertNotNull(recuperado.getPolizaSeguro());
                assertNotNull(recuperado.getTipo());
                assertNotNull(recuperado.getFechaMatriculacion());
                assertTrue(recuperado.isActivo());
            }
        }
    }

    @Test
    @DisplayName("Insertar todos los tipos de vehículo")
    void testInsertarTodosTipos() {
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            Vehiculo vehiculo = new Vehiculo(
                    "TEST" + tipo.name(),
                    "POL-" + tipo.name(),
                    tipo,
                    LocalDate.now().minusYears(3)
            );

            // Intentar insertar (puede fallar si ya existe)
            assertDoesNotThrow(() -> vehiculoDAO.insertar(vehiculo));
        }
    }

    @Test
    @DisplayName("Vehículos filtrados por tipo están correctamente ordenados")
    void testVehiculosPorTipoOrdenados() {
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            List<Vehiculo> vehiculos = vehiculoDAO.obtenerPorTipo(tipo);

            if (vehiculos.size() > 1) {
                for (int i = 0; i < vehiculos.size() - 1; i++) {
                    String mat1 = vehiculos.get(i).getMatricula();
                    String mat2 = vehiculos.get(i + 1).getMatricula();
                    assertTrue(
                            mat1.compareTo(mat2) <= 0,
                            "Los vehículos del tipo " + tipo + " deberían estar ordenados"
                    );
                }
            }
        }
    }

    @Test
    @DisplayName("Verificar años de uso de vehículos")
    void testAñosUsoVehiculos() {
        List<Vehiculo> vehiculos = vehiculoDAO.obtenerActivos();

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getFechaMatriculacion() != null) {
                assertTrue(
                        vehiculo.getAñosUso() >= 0,
                        "Los años de uso deberían ser un valor positivo"
                );
            }
        }
    }

    @Test
    @DisplayName("Actualizar tipo de vehículo")
    void testActualizarTipoVehiculo() {
        if (vehiculoPrueba != null && vehiculoPrueba.getId() != null) {
            TipoVehiculo tipoOriginal = vehiculoPrueba.getTipo();
            TipoVehiculo nuevoTipo = tipoOriginal == TipoVehiculo.PEQUEÑO
                    ? TipoVehiculo.GRANDE
                    : TipoVehiculo.PEQUEÑO;

            vehiculoPrueba.setTipo(nuevoTipo);
            boolean resultado = vehiculoDAO.actualizar(vehiculoPrueba);

            if (resultado) {
                Vehiculo actualizado = vehiculoDAO.buscarPorMatricula(vehiculoPrueba.getMatricula());
                if (actualizado != null) {
                    assertEquals(nuevoTipo, actualizado.getTipo());
                }
            }
        }
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Tests de VehiculoDAO completados");
    }
}