package com.gastos.gastos_compartidos.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime fecha;
    private int status;
    private String error;
    private String mensaje;

    public ErrorResponse() {
    }

    public ErrorResponse(
            LocalDateTime fecha,
            int status,
            String error,
            String mensaje) {

        this.fecha = fecha;
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}