package org.example.actividadfinaldi.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Representa un vehiculo del sistema de alquiler
 */
public class Vehiculo {
    private Integer id;
    private String matricula;
    private String polizaSeguro;
    private TipoVehiculo tipo;
    private LocalDate fechaMatriculacion;
    private boolean activo;

    public Vehiculo() {
        this.activo = true;
    }

    public Vehiculo(String matricula, String polizaSeguro, TipoVehiculo tipo, LocalDate fechaMatriculacion) {
        this.matricula = matricula;
        this.polizaSeguro = polizaSeguro;
        this.tipo = tipo;
        this.fechaMatriculacion = fechaMatriculacion;
        this.activo = true;
    }

    /**
     * Verifica si el vehiculo debe darse de baja por antiguedad (10 años)
     * @return true si tiene 10 o mas años
     */
    public boolean debeSerDadoDeBaja() {
        if (fechaMatriculacion == null) return false;
        return Period.between(fechaMatriculacion, LocalDate.now()).getYears() >= 10;
    }

    /**
     * Calcula los años de uso del vehiculo
     * @return años desde matriculacion
     */
    public int getAniosUso() {
        if (fechaMatriculacion == null) return 0;
        return Period.between(fechaMatriculacion, LocalDate.now()).getYears();
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPolizaSeguro() {
        return polizaSeguro;
    }

    public void setPolizaSeguro(String polizaSeguro) {
        this.polizaSeguro = polizaSeguro;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(LocalDate fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return matricula + " - " + tipo + " (" + getAniosUso() + " años)";
    }
}