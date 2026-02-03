package org.example.actividadfinaldi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Vehiculo
 */
@DisplayName("Tests de Vehiculo")
class VehiculoTest {

    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        vehiculo = new Vehiculo();
    }

    @Test
    @DisplayName("Constructor vacío inicializa vehículo como activo")
    void testConstructorVacio() {
        assertTrue(vehiculo.isActivo(), "Un nuevo vehículo debería estar activo por defecto");
        assertNull(vehiculo.getId(), "El ID debería ser null inicialmente");
    }

    @Test
    @DisplayName("Constructor con parámetros inicializa correctamente")
    void testConstructorConParametros() {
        LocalDate fechaMatriculacion = LocalDate.of(2020, 6, 15);
        Vehiculo vehiculoCompleto = new Vehiculo("1234ABC", "POL-2024-001", TipoVehiculo.MEDIANO, fechaMatriculacion);

        assertEquals("1234ABC", vehiculoCompleto.getMatricula());
        assertEquals("POL-2024-001", vehiculoCompleto.getPolizaSeguro());
        assertEquals(TipoVehiculo.MEDIANO, vehiculoCompleto.getTipo());
        assertEquals(fechaMatriculacion, vehiculoCompleto.getFechaMatriculacion());
        assertTrue(vehiculoCompleto.isActivo());
    }

    @Test
    @DisplayName("Vehículo con 10 años debe ser dado de baja")
    void testDebeSerDadoDeBajaConDiezAnios() {
        LocalDate hace10Anios = LocalDate.now().minusYears(10);
        vehiculo.setFechaMatriculacion(hace10Anios);

        assertTrue(vehiculo.debeSerDadoDeBaja(), "Un vehículo con 10 años debería ser dado de baja");
    }

    @Test
    @DisplayName("Vehículo con 11 años debe ser dado de baja")
    void testDebeSerDadoDeBajaConMasDeDiezAnios() {
        LocalDate hace11Anios = LocalDate.now().minusYears(11);
        vehiculo.setFechaMatriculacion(hace11Anios);

        assertTrue(vehiculo.debeSerDadoDeBaja(), "Un vehículo con 11 años debería ser dado de baja");
    }

    @Test
    @DisplayName("Vehículo con 9 años no debe ser dado de baja")
    void testNoDebeSerDadoDeBaja() {
        LocalDate hace9Anios = LocalDate.now().minusYears(9);
        vehiculo.setFechaMatriculacion(hace9Anios);

        assertFalse(vehiculo.debeSerDadoDeBaja(), "Un vehículo con 9 años no debería ser dado de baja");
    }

    @Test
    @DisplayName("Vehículo nuevo no debe ser dado de baja")
    void testVehiculoNuevoNoDebeSerDadoDeBaja() {
        vehiculo.setFechaMatriculacion(LocalDate.now());

        assertFalse(vehiculo.debeSerDadoDeBaja(), "Un vehículo nuevo no debería ser dado de baja");
    }

    @Test
    @DisplayName("Vehículo sin fecha de matriculación no debe ser dado de baja")
    void testDebeSerDadoDeBajaSinFecha() {
        vehiculo.setFechaMatriculacion(null);

        assertFalse(vehiculo.debeSerDadoDeBaja(), "Un vehículo sin fecha no debería marcarse para baja");
    }

    @Test
    @DisplayName("Calcular años de uso correctamente")
    void testGetAñosUso() {
        LocalDate hace5Anios = LocalDate.now().minusYears(5);
        vehiculo.setFechaMatriculacion(hace5Anios);

        assertEquals(5, vehiculo.getAñosUso(), "Los años de uso deberían ser 5");
    }

    @Test
    @DisplayName("Años de uso es 0 para vehículo nuevo")
    void testGetAñosUsoVehiculoNuevo() {
        vehiculo.setFechaMatriculacion(LocalDate.now());

        assertEquals(0, vehiculo.getAñosUso(), "Los años de uso deberían ser 0 para vehículo nuevo");
    }

    @Test
    @DisplayName("Años de uso es 0 cuando no hay fecha")
    void testGetAñosUsoSinFecha() {
        vehiculo.setFechaMatriculacion(null);

        assertEquals(0, vehiculo.getAñosUso(), "Los años de uso deberían ser 0 cuando no hay fecha");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 7, 9})
    @DisplayName("Vehículos con diferentes edades válidas (< 10 años)")
    void testVehiculosValidosPorEdad(int anios) {
        LocalDate fecha = LocalDate.now().minusYears(anios);
        vehiculo.setFechaMatriculacion(fecha);

        assertFalse(vehiculo.debeSerDadoDeBaja(), "Vehículo con " + anios + " años no debería darse de baja");
        assertEquals(anios, vehiculo.getAñosUso(), "Los años de uso deberían calcularse correctamente");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 12, 15, 20})
    @DisplayName("Vehículos que deben darse de baja (>= 10 años)")
    void testVehiculosParaBajaPorEdad(int anios) {
        LocalDate fecha = LocalDate.now().minusYears(anios);
        vehiculo.setFechaMatriculacion(fecha);

        assertTrue(vehiculo.debeSerDadoDeBaja(), "Vehículo con " + anios + " años debería darse de baja");
        assertEquals(anios, vehiculo.getAñosUso(), "Los años de uso deberían calcularse correctamente");
    }

    @ParameterizedTest
    @EnumSource(TipoVehiculo.class)
    @DisplayName("Todos los tipos de vehículo son válidos")
    void testTodosLosTiposVehiculo(TipoVehiculo tipo) {
        vehiculo.setTipo(tipo);

        assertEquals(tipo, vehiculo.getTipo(), "El tipo debería establecerse correctamente");
    }

    @Test
    @DisplayName("Getters y Setters funcionan correctamente")
    void testGettersYSetters() {
        vehiculo.setId(1);
        vehiculo.setMatricula("5678XYZ");
        vehiculo.setPolizaSeguro("POL-2025-999");
        vehiculo.setTipo(TipoVehiculo.GRANDE);
        LocalDate fecha = LocalDate.of(2018, 9, 10);
        vehiculo.setFechaMatriculacion(fecha);
        vehiculo.setActivo(false);

        assertEquals(1, vehiculo.getId());
        assertEquals("5678XYZ", vehiculo.getMatricula());
        assertEquals("POL-2025-999", vehiculo.getPolizaSeguro());
        assertEquals(TipoVehiculo.GRANDE, vehiculo.getTipo());
        assertEquals(fecha, vehiculo.getFechaMatriculacion());
        assertFalse(vehiculo.isActivo());
    }

    @Test
    @DisplayName("toString genera formato correcto")
    void testToString() {
        vehiculo.setMatricula("9999ZZZ");
        vehiculo.setTipo(TipoVehiculo.PEQUEÑO);
        vehiculo.setFechaMatriculacion(LocalDate.now().minusYears(3));

        String resultado = vehiculo.toString();

        assertTrue(resultado.contains("9999ZZZ"), "El toString debería contener la matrícula");
        assertTrue(resultado.contains("PEQUEÑO"), "El toString debería contener el tipo");
        assertTrue(resultado.contains("3"), "El toString debería contener los años de uso");
    }

    @Test
    @DisplayName("Vehículo recién creado está activo")
    void testVehiculoActivoPorDefecto() {
        Vehiculo nuevoVehiculo = new Vehiculo("1111AAA", "POL-001", TipoVehiculo.MEDIANO, LocalDate.now());

        assertTrue(nuevoVehiculo.isActivo(), "Un vehículo nuevo debería estar activo");
    }

    @Test
    @DisplayName("Cambiar estado de activo a inactivo")
    void testCambiarEstadoActivo() {
        vehiculo.setActivo(true);
        assertTrue(vehiculo.isActivo());

        vehiculo.setActivo(false);
        assertFalse(vehiculo.isActivo());
    }

    @Test
    @DisplayName("Vehículos con diferentes tipos tienen representaciones diferentes")
    void testDiferentesTiposEnToString() {
        vehiculo.setMatricula("TEST123");
        vehiculo.setFechaMatriculacion(LocalDate.now().minusYears(2));

        vehiculo.setTipo(TipoVehiculo.PEQUEÑO);
        String pequeño = vehiculo.toString();

        vehiculo.setTipo(TipoVehiculo.MEDIANO);
        String mediano = vehiculo.toString();

        vehiculo.setTipo(TipoVehiculo.GRANDE);
        String grande = vehiculo.toString();

        assertNotEquals(pequeño, mediano, "Los toString de diferentes tipos deberían ser diferentes");
        assertNotEquals(mediano, grande, "Los toString de diferentes tipos deberían ser diferentes");
        assertNotEquals(pequeño, grande, "Los toString de diferentes tipos deberían ser diferentes");
    }
}