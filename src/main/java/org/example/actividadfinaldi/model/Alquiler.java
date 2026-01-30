package org.example.actividadfinaldi.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa un alquiler de vehiculo
 */
public class Alquiler {
    private Integer id;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;

    public Alquiler() {
        this.activo = true;
    }

    public Alquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = true;
    }

    /**
     * Calcula la duracion del alquiler en dias
     * @return numero de dias
     */
    public long getDuracionDias() {
        if (fechaInicio == null || fechaFin == null) return 0;
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    /**
     * Valida que las fechas sean correctas
     * @return true si fechaFin es posterior a fechaInicio
     */
    public boolean fechasValidas() {
        if (fechaInicio == null || fechaFin == null) return false;
        return fechaFin.isAfter(fechaInicio);
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return cliente.getNombre() + " - " + vehiculo.getMatricula() +
                " (" + fechaInicio + " a " + fechaFin + ")";
    }
}
