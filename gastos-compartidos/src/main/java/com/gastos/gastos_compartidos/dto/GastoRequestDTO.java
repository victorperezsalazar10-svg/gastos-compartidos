package com.gastos.gastos_compartidos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GastoRequestDTO {

    private String descripcion;

    private BigDecimal montoTotal;

    private LocalDate fechaGasto;

    private String tipoDivision;

    private Long pagadorId;

    private Long grupoId;

    private List<DetalleGastoRequestDTO> detalles;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public LocalDate getFechaGasto() {
        return fechaGasto;
    }

    public void setFechaGasto(LocalDate fechaGasto) {
        this.fechaGasto = fechaGasto;
    }

    public String getTipoDivision() {
        return tipoDivision;
    }

    public void setTipoDivision(String tipoDivision) {
        this.tipoDivision = tipoDivision;
    }

    public Long getPagadorId() {
        return pagadorId;
    }

    public void setPagadorId(Long pagadorId) {
        this.pagadorId = pagadorId;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    public List<DetalleGastoRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleGastoRequestDTO> detalles) {
        this.detalles = detalles;
    }
}