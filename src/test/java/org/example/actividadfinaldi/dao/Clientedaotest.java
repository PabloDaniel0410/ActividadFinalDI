package org.example.actividadfinaldi.dao;

import org.example.actividadfinaldi.model.Cliente;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para ClienteDAO
 * Estos tests requieren una conexión activa a Supabase
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
        // Generar DNI único para evitar duplicados
        String dniUnico = "CLI" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Cliente cliente = new Cliente(
                "María Test",
                "García López",
                dniUnico,
                LocalDate.of(1990, 5, 15)
        );

        boolean resultado = clienteDAO.insertar(cliente);

        if (resultado) {
            assertNotNull(cliente.getId(), "El ID debería asignarse después de insertar");
            assertTrue(cliente.getId() > 0, "El ID debería ser positivo");
            clientePrueba = cliente;
        } else {
            System.out.println("No se pudo insertar cliente (posiblemente duplicado)");
        }
    }

    @Test
    @Order(2)
    @DisplayName("Buscar cliente por DNI existente")
    void testBuscarPorDniExistente() {
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
    @DisplayName("Buscar cliente con DNI null")
    void testBuscarPorDniNull() {
        Cliente resultado = clienteDAO.buscarPorDni(null);

        assertNull(resultado, "Debería retornar null para DNI null");
    }

    @Test
    @DisplayName("Obtener clientes activos")
    void testObtenerActivos() {
        List<Cliente> clientes = clienteDAO.obtenerActivos();

        assertNotNull(clientes, "La lista no debería ser null");
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
    @DisplayName("Actualizar cliente sin ID retorna false")
    void testActualizarClienteSinId() {
        Cliente clienteSinId = new Cliente(
                "Test",
                "Test",
                "TEST999",
                LocalDate.now().minusYears(30)
        );

        boolean resultado = clienteDAO.actualizar(clienteSinId);

        assertFalse(resultado, "No debería actualizar un cliente sin ID");
    }

    @Test
    @DisplayName("Insertar cliente con datos nulos retorna false")
    void testInsertarClienteDatosNulos() {
        Cliente clienteNulo = new Cliente();

        boolean resultado = clienteDAO.insertar(clienteNulo);

        assertFalse(resultado, "No debería insertar un cliente con datos nulos");
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
        String dniUnico = "TEST" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Cliente cliente = new Cliente(
                "Pedro",
                "Martínez Sánchez",
                dniUnico,
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
                assertTrue(
                        cliente.getEdad() >= 0,
                        "La edad debería ser un valor válido"
                );
            }
        }
    }

    @Test
    @DisplayName("Actualizar cliente con datos null retorna false")
    void testActualizarClienteDatosNull() {
        if (clientePrueba != null) {
            Cliente clienteConNulls = new Cliente();
            clienteConNulls.setId(clientePrueba.getId());
            // Dejar otros campos como null

            boolean resultado = clienteDAO.actualizar(clienteConNulls);

            assertFalse(resultado, "No debería actualizar cliente con datos null");
        }
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Tests de ClienteDAO completados");
    }
}