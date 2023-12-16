package com.report;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/docs")
@Slf4j
public class Controller {

      private final String templatePath = "src/main/resources/templates/report1.jrxml";

      @PostMapping
      public ResponseEntity<byte[]> genDocument(@RequestBody Parameters params) throws JRException {
            HashMap<String, Object> p = new HashMap<>();
            p.put("comprobante_id", params.getComprobante_id());
            p.put("fecha", params.getFecha());
            p.put("total", params.getTotal());
            // classpath es el directorio resources
            p.put("imageDir", "classpath:/static/");

            JasperReport repo = JasperCompileManager.compileReport(templatePath);
            JasperPrint jp = JasperFillManager.fillReport(repo, p, new JREmptyDataSource());

            // ResponseEntity es una clase que permite dar una respuesta personalizada
            return ResponseEntity
                    .status(HttpStatus.CREATED)                       // 201 se usa cuando se genera algún recurso exitosamente
                    .contentType(MediaType.APPLICATION_PDF)           // indica que el contenido de la respuesta va a ser un PDF
                    .body(JasperExportManager.exportReportToPdf(jp)); // el cuerpo de la respuesta
      }

      @Getter @Setter
      static private class Parameters {
            private String comprobante_id, fecha, total;
      }
}
