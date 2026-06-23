package com.gastos.gastos_compartidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DetalleGastoRequestDTO {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @Positive(message = "El porcentaje debe ser mayor que cero")
    private BigDecimal porcentaje;

    @Positive(message = "El monto asignado debe ser mayor que cero")
    private BigDecimal montoAsignado;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getMontoAsignado() {
        return montoAsignado;
    }

    public void setMontoAsignado(BigDecimal montoAsignado) {
        this.montoAsignado = montoAsignado;
    }
}