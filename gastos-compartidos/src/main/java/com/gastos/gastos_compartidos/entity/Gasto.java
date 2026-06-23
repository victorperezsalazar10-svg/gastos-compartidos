package com.gastos.gastos_compartidos.entity;

import com.gastos.gastos_compartidos.enums.TipoDivision;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Column(nullable = false)
    private BigDecimal montoTotal;

    @NotNull(message = "La fecha del gasto es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaGasto;

    @NotNull(message = "El tipo de división es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDivision tipoDivision;

    @ManyToOne
    @JoinColumn(name = "pagador_id", nullable = false)
    private Usuario pagador;

    @ManyToOne
    @JoinColumn(name = "grupo_id", nullable = false)
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

    public TipoDivision getTipoDivision() {
        return tipoDivision;
    }

    public void setTipoDivision(TipoDivision tipoDivision) {
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

    public void setId(Long id) {
        this.id = id;
    }
}