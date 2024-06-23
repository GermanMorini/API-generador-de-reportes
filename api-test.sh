#!/bin/bash

LOG="test.log"

# prueba de conexión
cool -Q -p /api 2>$LOG &&


# documento 1

BODY='{
    "comprobante_id":"57d078a6-9679-4f5b-b6ad-e8bb47aa4fe0",
    "total":"420.069"
    }'
cool -p /api/doc1 -m POST -b "$BODY" 2>>$LOG 1>doc1.pdf


# documento 2
BODY='{
    "dni":"42042069",
    "nombre_estudiante":"NOMBRE APELLIDO",
    "clave":"4206988",
    "legajo":"42069",
    "carrera":"CARRERA",
    "plan_estudios":"MATERIA",
    "fecha_examen":"01/01/01",
    "hora_apertura":"08:00",
    "validacion":"19689976247658417182"
    }'
cool -p /api/doc2 -m POST -b "$BODY" 2>>$LOG 1>doc2.pdf

less "test.log"
