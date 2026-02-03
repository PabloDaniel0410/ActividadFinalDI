package org.example.actividadfinaldi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Cliente
 */
@DisplayName("Tests de Cliente")
class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    @DisplayName("Constructor vacío inicializa cliente como activo")
    void testConstructorVacio() {
        assertTrue(cliente.isActivo(), "Un nuevo cliente debería estar activo por defecto");
        assertNull(cliente.getId(), "El ID debería ser null inicialmente");
    }

    @Test
    @DisplayName("Constructor con parámetros inicializa correctamente")
    void testConstructorConParametros() {
        LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
        Cliente clienteCompleto = new Cliente("Juan", "Pérez García", "12345678A", fechaNacimiento);

        assertEquals("Juan", clienteCompleto.getNombre());
        assertEquals("Pérez García", clienteCompleto.getApellidos());
        assertEquals("12345678A", clienteCompleto.getDni());
        assertEquals(fechaNacimiento, clienteCompleto.getFechaNacimiento());
        assertTrue(clienteCompleto.isActivo());
    }

    @Test
    @DisplayName("Cliente con 25 años exactos es mayor de 25")
    void testEsMayorDe25Exacto() {
        LocalDate hace25Anios = LocalDate.now().minusYears(25);
        cliente.setFechaNacimiento(hace25Anios);

        assertTrue(cliente.esMayorDe25(), "Un cliente con 25 años exactos debería ser válido");
    }

    @Test
    @DisplayName("Cliente con 26 años es mayor de 25")
    void testEsMayorDe25ConMargen() {
        LocalDate hace26Anios = LocalDate.now().minusYears(26);
        cliente.setFechaNacimiento(hace26Anios);

        assertTrue(cliente.esMayorDe25(), "Un cliente con 26 años debería ser válido");
    }

    @Test
    @DisplayName("Cliente con 24 años no es mayor de 25")
    void testNoEsMayorDe25() {
        LocalDate hace24Anios = LocalDate.now().minusYears(24);
        cliente.setFechaNacimiento(hace24Anios);

        assertFalse(cliente.esMayorDe25(), "Un cliente con 24 años no debería ser válido");
    }

    @Test
    @DisplayName("Cliente sin fecha de nacimiento no es mayor de 25")
    void testEsMayorDe25SinFecha() {
        cliente.setFechaNacimiento(null);

        assertFalse(cliente.esMayorDe25(), "Un cliente sin fecha de nacimiento no debería ser válido");
    }

    @Test
    @DisplayName("Calcular edad correctamente")
    void testGetEdad() {
        LocalDate hace30Anios = LocalDate.now().minusYears(30);
        cliente.setFechaNacimiento(hace30Anios);

        assertEquals(30, cliente.getEdad(), "La edad calculada debería ser 30 años");
    }

    @Test
    @DisplayName("Edad es 0 cuando no hay fecha de nacimiento")
    void testGetEdadSinFecha() {
        cliente.setFechaNacimiento(null);

        assertEquals(0, cliente.getEdad(), "La edad debería ser 0 cuando no hay fecha");
    }

    @ParameterizedTest
    @ValueSource(ints = {25, 30, 40, 50, 100})
    @DisplayName("Clientes con diferentes edades válidas")
    void testEdadesValidas(int edad) {
        LocalDate fechaNacimiento = LocalDate.now().minusYears(edad);
        cliente.setFechaNacimiento(fechaNacimiento);

        assertTrue(cliente.esMayorDe25(), "Cliente con " + edad + " años debería ser válido");
        assertEquals(edad, cliente.getEdad(), "La edad debería calcularse correctamente");
    }

    @Test
    @DisplayName("Getters y Setters funcionan correctamente")
    void testGettersYSetters() {
        cliente.setId(1);
        cliente.setNombre("María");
        cliente.setApellidos("López Martínez");
        cliente.setDni("87654321B");
        LocalDate fecha = LocalDate.of(1985, 3, 20);
        cliente.setFechaNacimiento(fecha);
        cliente.setActivo(false);

        assertEquals(1, cliente.getId());
        assertEquals("María", cliente.getNombre());
        assertEquals("López Martínez", cliente.getApellidos());
        assertEquals("87654321B", cliente.getDni());
        assertEquals(fecha, cliente.getFechaNacimiento());
        assertFalse(cliente.isActivo());
    }

    @Test
    @DisplayName("toString genera formato correcto")
    void testToString() {
        cliente.setNombre("Pedro");
        cliente.setApellidos("Sánchez Ruiz");
        cliente.setDni("11223344C");

        String resultado = cliente.toString();

        assertTrue(resultado.contains("Pedro"), "El toString debería contener el nombre");
        assertTrue(resultado.contains("Sánchez Ruiz"), "El toString debería contener los apellidos");
        assertTrue(resultado.contains("11223344C"), "El toString debería contener el DNI");
    }

    @Test
    @DisplayName("Cliente recién creado está activo")
    void testClienteActivoPorDefecto() {
        Cliente nuevoCliente = new Cliente("Ana", "García", "99887766D", LocalDate.of(1995, 8, 10));

        assertTrue(nuevoCliente.isActivo(), "Un cliente nuevo debería estar activo");
    }

    @Test
    @DisplayName("Cambiar estado de activo a inactivo")
    void testCambiarEstadoActivo() {
        cliente.setActivo(true);
        assertTrue(cliente.isActivo());

        cliente.setActivo(false);
        assertFalse(cliente.isActivo());
    }
}
