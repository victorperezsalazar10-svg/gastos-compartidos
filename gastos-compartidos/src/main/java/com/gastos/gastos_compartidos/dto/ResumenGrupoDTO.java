package com.gastos.gastos_compartidos.dto;

import java.math.BigDecimal;

public class ResumenGrupoDTO {

    private Long grupoId;
    private String nombreGrupo;

    private Integer totalMiembros;
    private Integer totalGastos;

    private BigDecimal montoTotalGastado;
    private BigDecimal montoPendienteLiquidar;

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public Integer getTotalMiembros() {
        return totalMiembros;
    }

    public void setTotalMiembros(Integer totalMiembros) {
        this.totalMiembros = totalMiembros;
    }

    public Integer getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(Integer totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getMontoTotalGastado() {
        return montoTotalGastado;
    }

    public void setMontoTotalGastado(BigDecimal montoTotalGastado) {
        this.montoTotalGastado = montoTotalGastado;
    }

    public BigDecimal getMontoPendienteLiquidar() {
        return montoPendienteLiquidar;
    }

    public void setMontoPendienteLiquidar(
            BigDecimal montoPendienteLiquidar) {

        this.montoPendienteLiquidar =
                montoPendienteLiquidar;
    }
}