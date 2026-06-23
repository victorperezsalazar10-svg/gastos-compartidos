package com.gastos.gastos_compartidos.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gastos.gastos_compartidos.dto.GastoRequestDTO;
import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.service.CsvService;
import com.gastos.gastos_compartidos.service.GastoService;

@RestController
@RequestMapping("/gastos")
@CrossOrigin(origins = "*")
public class GastoController {

    private final GastoService gastoService;
    private final CsvService csvService;

    public GastoController(GastoService gastoService, CsvService csvService) {
        this.gastoService = gastoService;
        this.csvService = csvService;
    }

    @GetMapping("/grupo/{grupoId}")
    public List<Gasto> listarPorGrupo(@PathVariable Long grupoId) {
        return gastoService.listarPorGrupo(grupoId);
    }

    @GetMapping
    public List<Gasto> listar() {
        return gastoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Gasto buscarPorId(@PathVariable Long id) {
        return gastoService.buscarPorId(id);
    }

    @PostMapping
    public Gasto guardar(@RequestBody GastoRequestDTO request) {
        return gastoService.guardar(request);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        gastoService.eliminar(id);
        return "Gasto eliminado correctamente";
    }

    @GetMapping("/grupo/{grupoId}/exportar")
    public ResponseEntity<String> exportarCsv(@PathVariable Long grupoId) {
        String csv = csvService.generarCsvGrupo(grupoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=gastos_grupo.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }

    @GetMapping("/{id}/detalles")
    public List<DetalleGasto> detalles(@PathVariable Long id) {
        return gastoService.obtenerDetalles(id);
    }
}