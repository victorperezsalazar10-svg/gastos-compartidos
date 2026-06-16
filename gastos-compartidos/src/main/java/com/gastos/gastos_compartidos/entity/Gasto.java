package com.gastos.gastos_compartidos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private BigDecimal montoTotal;

    private LocalDate fechaGasto;

    private String tipoDivision;

    @ManyToOne
    @JoinColumn(name = "pagador_id")
    private Usuario pagador;

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    public Gasto() {
    }

    public Long getId() {
        return id;
    }

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

    public Usuario getPagador() {
        return pagador;
    }

    public void setPagador(Usuario pagador) {
        this.pagador = pagador;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}