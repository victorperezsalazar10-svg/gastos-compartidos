package com.gastos.gastos_compartidos.dto;

import java.math.BigDecimal;

public class RankingUsuarioDTO {

    private String nombreUsuario;

    private BigDecimal totalPagado;

    private BigDecimal totalConsumido;

    private BigDecimal saldo;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getTotalConsumido() {
        return totalConsumido;
    }

    public void setTotalConsumido(BigDecimal totalConsumido) {
        this.totalConsumido = totalConsumido;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}