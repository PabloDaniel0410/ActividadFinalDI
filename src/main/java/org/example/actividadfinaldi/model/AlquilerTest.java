package org.example.actividadfinaldi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Alquiler
 */
@DisplayName("Tests de Alquiler")
class AlquilerTest {

    private Alquiler alquiler;
    private Cliente cliente;
    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        alquiler = new Alquiler();

        // Crear cliente de prueba
        cliente = new Cliente("Juan", "Pérez", "12345678A", LocalDate.of(1990, 5, 15));
        cliente.setId(1);

        // Crear vehículo de prueba
        vehiculo = new Vehiculo("1234ABC", "POL-001", TipoVehiculo.MEDIANO, LocalDate.of(2020, 1, 1));
        vehiculo.setId(1);
    }

    @Test
    @DisplayName("Constructor vacío inicializa alquiler como activo")
    void testConstructorVacio() {
        assertTrue(alquiler.isActivo(), "Un nuevo alquiler debería estar activo por defecto");
        assertNull(alquiler.getId(), "El ID debería ser null inicialmente");
    }

    @Test
    @DisplayName("Constructor con parámetros inicializa correctamente")
    void testConstructorConParametros() {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = LocalDate.now().plusDays(7);

        Alquiler alquilerCompleto = new Alquiler(cliente, vehiculo, fechaInicio, fechaFin);

        assertEquals(cliente, alquilerCompleto.getCliente());
        assertEquals(vehiculo, alquilerCompleto.getVehiculo());
        assertEquals(fechaInicio, alquilerCompleto.getFechaInicio());
        assertEquals(fechaFin, alquilerCompleto.getFechaFin());
        assertTrue(alquilerCompleto.isActivo());
    }

    @Test
    @DisplayName("Duración de alquiler de 7 días se calcula correctamente")
    void testDuracionDias7Dias() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 8);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertEquals(7, alquiler.getDuracionDias(), "La duración debería ser 7 días");
    }

    @Test
    @DisplayName("Duración de alquiler de 1 día se calcula correctamente")
    void testDuracionDias1Dia() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 2);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertEquals(1, alquiler.getDuracionDias(), "La duración debería ser 1 día");
    }

    @Test
    @DisplayName("Duración de alquiler de 30 días se calcula correctamente")
    void testDuracionDias30Dias() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertEquals(30, alquiler.getDuracionDias(), "La duración debería ser 30 días");
    }

    @Test
    @DisplayName("Duración es 0 cuando no hay fecha de inicio")
    void testDuracionDiasSinFechaInicio() {
        alquiler.setFechaInicio(null);
        alquiler.setFechaFin(LocalDate.now());

        assertEquals(0, alquiler.getDuracionDias(), "La duración debería ser 0 cuando falta fecha de inicio");
    }

    @Test
    @DisplayName("Duración es 0 cuando no hay fecha de fin")
    void testDuracionDiasSinFechaFin() {
        alquiler.setFechaInicio(LocalDate.now());
        alquiler.setFechaFin(null);

        assertEquals(0, alquiler.getDuracionDias(), "La duración debería ser 0 cuando falta fecha de fin");
    }

    @Test
    @DisplayName("Duración es 0 cuando no hay fechas")
    void testDuracionDiasSinFechas() {
        alquiler.setFechaInicio(null);
        alquiler.setFechaFin(null);

        assertEquals(0, alquiler.getDuracionDias(), "La duración debería ser 0 cuando no hay fechas");
    }

    @Test
    @DisplayName("Fechas válidas cuando fin es posterior a inicio")
    void testFechasValidasCorrectamente() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 10);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertTrue(alquiler.fechasValidas(), "Las fechas deberían ser válidas");
    }

    @Test
    @DisplayName("Fechas inválidas cuando fin es anterior a inicio")
    void testFechasInvalidasFechaFinAnterior() {
        LocalDate inicio = LocalDate.of(2024, 1, 10);
        LocalDate fin = LocalDate.of(2024, 1, 5);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertFalse(alquiler.fechasValidas(), "Las fechas no deberían ser válidas");
    }

    @Test
    @DisplayName("Fechas inválidas cuando son iguales")
    void testFechasInvalidasFechasIguales() {
        LocalDate fecha = LocalDate.of(2024, 1, 1);

        alquiler.setFechaInicio(fecha);
        alquiler.setFechaFin(fecha);

        assertFalse(alquiler.fechasValidas(), "Las fechas iguales no deberían ser válidas");
    }

    @Test
    @DisplayName("Fechas inválidas cuando no hay fecha de inicio")
    void testFechasInvalidasSinFechaInicio() {
        alquiler.setFechaInicio(null);
        alquiler.setFechaFin(LocalDate.now());

        assertFalse(alquiler.fechasValidas(), "Las fechas no deberían ser válidas sin fecha de inicio");
    }

    @Test
    @DisplayName("Fechas inválidas cuando no hay fecha de fin")
    void testFechasInvalidasSinFechaFin() {
        alquiler.setFechaInicio(LocalDate.now());
        alquiler.setFechaFin(null);

        assertFalse(alquiler.fechasValidas(), "Las fechas no deberían ser válidas sin fecha de fin");
    }

    @Test
    @DisplayName("Fechas inválidas cuando no hay fechas")
    void testFechasInvalidasSinFechas() {
        alquiler.setFechaInicio(null);
        alquiler.setFechaFin(null);

        assertFalse(alquiler.fechasValidas(), "Las fechas no deberían ser válidas cuando faltan ambas");
    }

    @Test
    @DisplayName("Getters y Setters funcionan correctamente")
    void testGettersYSetters() {
        LocalDate inicio = LocalDate.of(2024, 2, 1);
        LocalDate fin = LocalDate.of(2024, 2, 10);

        alquiler.setId(100);
        alquiler.setCliente(cliente);
        alquiler.setVehiculo(vehiculo);
        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);
        alquiler.setActivo(false);

        assertEquals(100, alquiler.getId());
        assertEquals(cliente, alquiler.getCliente());
        assertEquals(vehiculo, alquiler.getVehiculo());
        assertEquals(inicio, alquiler.getFechaInicio());
        assertEquals(fin, alquiler.getFechaFin());
        assertFalse(alquiler.isActivo());
    }

    @Test
    @DisplayName("toString genera formato correcto")
    void testToString() {
        LocalDate inicio = LocalDate.of(2024, 3, 1);
        LocalDate fin = LocalDate.of(2024, 3, 8);

        alquiler.setCliente(cliente);
        alquiler.setVehiculo(vehiculo);
        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        String resultado = alquiler.toString();

        assertTrue(resultado.contains("Juan"), "El toString debería contener el nombre del cliente");
        assertTrue(resultado.contains("1234ABC"), "El toString debería contener la matrícula");
        assertTrue(resultado.contains(inicio.toString()), "El toString debería contener la fecha de inicio");
        assertTrue(resultado.contains(fin.toString()), "El toString debería contener la fecha de fin");
    }

    @Test
    @DisplayName("Alquiler recién creado está activo")
    void testAlquilerActivoPorDefecto() {
        Alquiler nuevoAlquiler = new Alquiler(
                cliente,
                vehiculo,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        assertTrue(nuevoAlquiler.isActivo(), "Un alquiler nuevo debería estar activo");
    }

    @Test
    @DisplayName("Cambiar estado de activo a inactivo")
    void testCambiarEstadoActivo() {
        alquiler.setActivo(true);
        assertTrue(alquiler.isActivo());

        alquiler.setActivo(false);
        assertFalse(alquiler.isActivo());
    }

    @Test
    @DisplayName("Alquiler con fechas del mismo mes")
    void testAlquilerMismoMes() {
        LocalDate inicio = LocalDate.of(2024, 6, 5);
        LocalDate fin = LocalDate.of(2024, 6, 25);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertTrue(alquiler.fechasValidas());
        assertEquals(20, alquiler.getDuracionDias());
    }

    @Test
    @DisplayName("Alquiler que cruza meses")
    void testAlquilerCruzaMeses() {
        LocalDate inicio = LocalDate.of(2024, 1, 25);
        LocalDate fin = LocalDate.of(2024, 2, 5);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertTrue(alquiler.fechasValidas());
        assertEquals(11, alquiler.getDuracionDias());
    }

    @Test
    @DisplayName("Alquiler que cruza años")
    void testAlquilerCruzaAnios() {
        LocalDate inicio = LocalDate.of(2023, 12, 28);
        LocalDate fin = LocalDate.of(2024, 1, 5);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertTrue(alquiler.fechasValidas());
        assertEquals(8, alquiler.getDuracionDias());
    }

    @Test
    @DisplayName("Alquiler de larga duración")
    void testAlquilerLargaDuracion() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 12, 31);

        alquiler.setFechaInicio(inicio);
        alquiler.setFechaFin(fin);

        assertTrue(alquiler.fechasValidas());
        assertEquals(365, alquiler.getDuracionDias());
    }
}