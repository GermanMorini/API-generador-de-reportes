package com.report;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/api/")
@Slf4j
public class Controller {

      private final String templatePath = "src/main/resources/templates/";

      @PostMapping("doc1")
      public ResponseEntity<byte[]> genRepo1(@RequestBody Reporte1 params) throws JRException {
            LocalDateTime fecha = LocalDateTime.now();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

            HashMap<String, Object> p = new HashMap<>();

            // classpath es el directorio resources
            p.put("imageDir", "classpath:/static/");
            p.put("comprobante_id", params.comprobante_id);
            p.put("fecha", df.format(fecha));
            p.put("total", params.total);

            JasperReport repo = JasperCompileManager.compileReport(templatePath + "report1.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(repo, p, new JREmptyDataSource());

            // ResponseEntity es una clase que permite dar una respuesta personalizada
            return ResponseEntity
                    .status(HttpStatus.CREATED)                       // 201 se usa cuando se genera algún recurso exitosamente
                    .contentType(MediaType.APPLICATION_PDF)           // indica que el contenido de la respuesta va a ser un PDF
                    .body(JasperExportManager.exportReportToPdf(jp)); // el cuerpo de la respuesta
      }

      @PostMapping("doc2")
      public ResponseEntity<byte[]> genRepo2(@RequestBody Reporte2 params) throws JRException {
            LocalDate ldt = LocalDate.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");

            HashMap<String, Object> p = new HashMap<>();

            p.put("imageDir", "classpath:/static/");
            p.put("dni", params.dni);
            p.put("nombre_estudiante", params.nombre_estudiante);
            p.put("clave", params.clave);
            p.put("legajo", params.legajo);
            p.put("carrera", params.carrera);
            p.put("plan_estudios", params.plan_estudios);
            p.put("fecha_examen", params.fecha_examen);
            p.put("hora_apertura", params.hora_apertura);
            p.put("dia_emision", dtf.format(ldt));
            p.put("validacion", params.validacion);
            p.put("validez", dtf.format(ldt.plusMonths(3).minusDays(10)));

            JasperReport repo = JasperCompileManager.compileReport(templatePath + "report2.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(repo, p, new JREmptyDataSource());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(JasperExportManager.exportReportToPdf(jp));
      }

      // el getter hace falta, porque sino no puede leer la info que le pasamos
      @Getter
      static private class Reporte1 {
            private String comprobante_id, total;
      }

      @Getter
      static private class Reporte2 {
            private String dni, nombre_estudiante, clave, legajo, carrera, plan_estudios, fecha_examen, hora_apertura, validacion;
      }
}
