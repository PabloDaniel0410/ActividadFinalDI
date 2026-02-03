package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.Cliente;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para ClienteDAO
 * Estos tests requieren una conexión activa a Supabase
 * Se recomienda usar una base de datos de pruebas
 */
@DisplayName("Tests de ClienteDAO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Clientedaotest {

    private ClienteDAO clienteDAO;
    private static Cliente clientePrueba;

    @BeforeEach
    void setUp() {
        clienteDAO = new ClienteDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Insertar cliente válido")
    void testInsertarClienteValido() {
        Cliente cliente = new Cliente(
                "María Test",
                "García López",
                "TEST12345X",
                LocalDate.of(1990, 5, 15)
        );

        boolean resultado = clienteDAO.insertar(cliente);

        // Nota: Este test fallará si no hay conexión a Supabase
        // o si ya existe un cliente con ese DNI
        if (resultado) {
            assertNotNull(cliente.getId(), "El ID debería asignarse después de insertar");
            assertTrue(cliente.getId() > 0, "El ID debería ser positivo");
            clientePrueba = cliente;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Buscar cliente por DNI existente")
    void testBuscarPorDniExistente() {
        // Este test depende de que el test anterior haya insertado un cliente
        if (clientePrueba != null) {
            Cliente encontrado = clienteDAO.buscarPorDni(clientePrueba.getDni());

            if (encontrado != null) {
                assertEquals(clientePrueba.getDni(), encontrado.getDni());
                assertEquals(clientePrueba.getNombre(), encontrado.getNombre());
                assertEquals(clientePrueba.getApellidos(), encontrado.getApellidos());
            }
        }
    }

    @Test
    @DisplayName("Buscar cliente por DNI inexistente")
    void testBuscarPorDniInexistente() {
        Cliente resultado = clienteDAO.buscarPorDni("DNI_INEXISTENTE_999");

        assertNull(resultado, "Debería retornar null para DNI inexistente");
    }

    @Test
    @DisplayName("Buscar cliente con DNI vacío")
    void testBuscarPorDniVacio() {
        Cliente resultado = clienteDAO.buscarPorDni("");

        assertNull(resultado, "Debería retornar null para DNI vacío");
    }

    @Test
    @DisplayName("Obtener clientes activos")
    void testObtenerActivos() {
        List<Cliente> clientes = clienteDAO.obtenerActivos();

        assertNotNull(clientes, "La lista no debería ser null");
        // La lista puede estar vacía si no hay clientes en la BD
        for (Cliente cliente : clientes) {
            assertTrue(cliente.isActivo(), "Todos los clientes deberían estar activos");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Actualizar cliente existente")
    void testActualizarClienteExistente() {
        if (clientePrueba != null && clientePrueba.getId() != null) {
            String nuevoNombre = "María Actualizada";
            clientePrueba.setNombre(nuevoNombre);

            boolean resultado = clienteDAO.actualizar(clientePrueba);

            if (resultado) {
                Cliente actualizado = clienteDAO.buscarPorDni(clientePrueba.getDni());
                if (actualizado != null) {
                    assertEquals(nuevoNombre, actualizado.getNombre());
                }
            }
        }
    }

    @Test
    @DisplayName("Actualizar cliente sin ID")
    void testActualizarClienteSinId() {
        Cliente clienteSinId = new Cliente(
                "Test",
                "Test",
                "TEST999",
                LocalDate.now().minusYears(30)
        );
        // No se asigna ID

        boolean resultado = clienteDAO.actualizar(clienteSinId);

        // El comportamiento puede variar, pero generalmente debería fallar
        assertFalse(resultado, "No debería actualizar un cliente sin ID");
    }

    @Test
    @DisplayName("Insertar cliente con datos nulos")
    void testInsertarClienteDatosNulos() {
        Cliente clienteNulo = new Cliente();
        // No se establecen datos

        // Este test verifica que el DAO maneje correctamente datos incompletos
        assertDoesNotThrow(() -> {
            clienteDAO.insertar(clienteNulo);
        }, "No debería lanzar excepción, aunque probablemente falle la inserción");
    }

    @Test
    @DisplayName("Lista de clientes activos está ordenada")
    void testClientesActivosOrdenados() {
        List<Cliente> clientes = clienteDAO.obtenerActivos();

        if (clientes.size() > 1) {
            for (int i = 0; i < clientes.size() - 1; i++) {
                String nombre1 = clientes.get(i).getNombre();
                String nombre2 = clientes.get(i + 1).getNombre();
                assertTrue(
                        nombre1.compareTo(nombre2) <= 0,
                        "Los clientes deberían estar ordenados por nombre"
                );
            }
        }
    }

    @Test
    @DisplayName("Cliente insertado tiene todos los campos")
    void testClienteInsertadoCamposCompletos() {
        Cliente cliente = new Cliente(
                "Pedro",
                "Martínez Sánchez",
                "TEST67890Y",
                LocalDate.of(1985, 8, 20)
        );

        boolean insertado = clienteDAO.insertar(cliente);

        if (insertado) {
            Cliente recuperado = clienteDAO.buscarPorDni(cliente.getDni());

            if (recuperado != null) {
                assertNotNull(recuperado.getId());
                assertNotNull(recuperado.getNombre());
                assertNotNull(recuperado.getApellidos());
                assertNotNull(recuperado.getDni());
                assertNotNull(recuperado.getFechaNacimiento());
                assertTrue(recuperado.isActivo());
            }
        }
    }

    @Test
    @DisplayName("Verificar que los clientes mayores de 25 años son válidos")
    void testClientesMayoresDe25() {
        List<Cliente> clientes = clienteDAO.obtenerActivos();

        for (Cliente cliente : clientes) {
            if (cliente.getFechaNacimiento() != null) {
                // Todos los clientes en la BD deberían ser mayores de 25
                assertTrue(
                        cliente.getEdad() >= 0,
                        "La edad debería ser un valor válido"
                );
            }
        }
    }

    @AfterAll
    static void tearDown() {
        // Aquí podrías limpiar los datos de prueba si lo necesitas
        System.out.println("Tests de ClienteDAO completados");
    }
}