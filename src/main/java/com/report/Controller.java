package com.report;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@Slf4j
public class Controller {

      @GetMapping
      public ResponseEntity<byte[]> helpPage() throws IOException {
            log.info("[INFO] mostrando la página de ayuda");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_HTML)
                    .body(FileCopyUtils.copyToByteArray(getClass().getResourceAsStream("/static/helppage.html")));
      }

      @PostMapping("/doc1")
      public ResponseEntity<byte[]> genRepo1(@RequestBody Reporte1 params) {
            try {

            log.info("[INFO] construyendo reporte 1");

            LocalDateTime fecha = LocalDateTime.now();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

            // este hashmap son los parámetros del formulario
            HashMap<String, Object> p = new HashMap<>();

            // classpath es el directorio resources
            p.put("imageDir", "classpath:/static/");
            p.put("comprobante_id", params.comprobante_id);
            p.put("fecha", df.format(fecha));
            p.put("total", params.total);
            checkParameters(p);

            // getResourceAsStream() obtiene un recurso ubicado en src/main/resources
            JasperReport repo = JasperCompileManager.compileReport(getClass().getResourceAsStream("/templates/report1.jrxml"));
            JasperPrint jp = JasperFillManager.fillReport(repo, p, new JREmptyDataSource());

            // ResponseEntity es una clase que permite dar una respuesta personalizada
            ResponseEntity<byte[]> re = ResponseEntity
                    .status(HttpStatus.CREATED)                       // 201 cuando se genera algún recurso exitosamente
                    .contentType(MediaType.APPLICATION_PDF)           // indica que el contenido de la respuesta va a ser un PDF
                    .body(JasperExportManager.exportReportToPdf(jp)); // el cuerpo de la respuesta (el PDF)
            log.info("[INFO] reporte 1 generado con éxito");
            return re;

            } catch (NullPointerException e) {
                  log.error("[ERROR] ocurrió un error: " + e.getMessage());
                  return ResponseEntity.badRequest().build(); // 400 cuando los datos pasados tienen mala sintaxis
            } catch (Exception je) {
                  log.error("[ERROR] ocurrió un error al construir el PDF: " + je.getMessage());
                  return ResponseEntity.internalServerError().build(); // 500 cuando ocurre un fallo por parte del servidor
            }
      }

      @PostMapping("/doc2")
      public ResponseEntity<byte[]> genRepo2(@RequestBody Reporte2 params) {
            try {

            log.info("[INFO] construyendo reporte 2");

            LocalDate ldt = LocalDate.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");

            HashMap<String, Object> p = new HashMap<>();

            p.put("imageDir", "classpath:/static/");
            p.put("dni", params.dni);
            p.put("nombre_estudiante", params.nombre_estudiante.toUpperCase());
            p.put("clave", params.clave);
            p.put("legajo", params.legajo);
            p.put("carrera", params.carrera.toUpperCase());
            p.put("plan_estudios", params.plan_estudios.toUpperCase());
            p.put("fecha_examen", params.fecha_examen);
            p.put("hora_apertura", params.hora_apertura);
            p.put("dia_emision", dtf.format(ldt));
            p.put("validacion", params.validacion);
            p.put("validez", dtf.format(ldt.plusMonths(3).minusDays(10)));
            checkParameters(p);

            JasperReport repo = JasperCompileManager.compileReport(getClass().getResourceAsStream("/templates/report2.jrxml"));
            JasperPrint jp = JasperFillManager.fillReport(repo, p, new JREmptyDataSource());

            ResponseEntity<byte[]> re = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(JasperExportManager.exportReportToPdf(jp));
            log.info("[INFO] reporte 2 generado con éxito");
            return re;

            } catch (NullPointerException e) {
                  log.error("[ERROR] ocurrió un error: " + e.getMessage());
                  return ResponseEntity.badRequest().build();
            } catch (Exception je) {
                  log.error("[ERROR] ocurrió un error al construir el PDF: " + je.getMessage());
                  return ResponseEntity.internalServerError().build();
            }
      }

      private void checkParameters(HashMap<String, Object> params) throws NullPointerException {
            params.forEach((k, v) -> {
                  if (v == null) throw new NullPointerException("No se pudo construir el PDF a partir de estos valores: (" + k + " - " + v + ")");
            });
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
