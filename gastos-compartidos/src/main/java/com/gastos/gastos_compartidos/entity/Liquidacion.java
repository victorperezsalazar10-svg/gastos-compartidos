package com.gastos.gastos_compartidos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "liquidaciones")
public class Liquidacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "deudor_id")
    private Usuario deudor;

    @ManyToOne
    @JoinColumn(name = "acreedor_id")
    private Usuario acreedor;

    private BigDecimal monto;

    private LocalDate fechaGeneracion;

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    public Liquidacion() {
    }

    public Long getId() {
        return id;
    }

    public Usuario getDeudor() {
        return deudor;
    }

    public void setDeudor(Usuario deudor) {
        this.deudor = deudor;
    }

    public Usuario getAcreedor() {
        return acreedor;
    }

    public void setAcreedor(Usuario acreedor) {
        this.acreedor = acreedor;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}