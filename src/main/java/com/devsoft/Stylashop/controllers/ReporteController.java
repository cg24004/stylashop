package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.services.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas")
    public ResponseEntity<byte[]> generarReporteVentas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal,
            @RequestParam(defaultValue = "true") boolean detallado
    ) {
        try {
            byte[] pdf = reporteService.generarReporte(fechaInicio, fechaFinal, detallado);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_ventas.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace(); // útil para ver el error en consola
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/metodos-pago")
    public ResponseEntity<byte[]> generarReporteMetodosPago(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal
    ) {
        try {
            byte[] pdf = reporteService.generarReporteMetodosPago(fechaInicio, fechaFinal);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_metodos_pago.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
