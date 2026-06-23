package com.gastos.gastos_compartidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GastoRequestDTO {

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    private BigDecimal montoTotal;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fechaGasto;

    @NotBlank(message = "El tipo de división es obligatorio")
    private String tipoDivision;

    @NotNull(message = "El pagador es obligatorio")
    private Long pagadorId;

    @NotNull(message = "El grupo es obligatorio")
    private Long grupoId;

    @Valid
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