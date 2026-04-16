package com.producto.producto.infrastructure.exception;

// Importaciones necesarias de Spring y Java
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Esta clase intercepta TODOS los errores de tu API
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Expresiones regulares para capturar valores inválidos en errores de tipo Double e Integer
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("\"([^\"]+)\".*Double");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("\"([^\"]+)\".*Integer");

    // 🔥 1. ERROR CUANDO EL JSON VIENE MAL FORMADO O CON TIPOS MAL
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonError(HttpMessageNotReadableException ex) {

        String error = ex.getMessage(); // mensaje interno del error
        String mensaje = "Error en el formato de los datos enviados";
        String sugerencia = "Verifica el formato JSON de los campos";
        String valor = "";  // valor incorrecto recibido
        String campo = "";  // campo donde ocurrió el error

        // Si el error tiene que ver con un Double (ej: precio)
        if (error.contains("Double")) {
            Matcher matcher = DOUBLE_PATTERN.matcher(error);

            if (matcher.find()) {
                valor = matcher.group(1); // extrae el valor incorrecto

                mensaje = "El campo 'precio' tiene un valor inválido";
                sugerencia = "Debe ser un número sin comillas, por ejemplo: 4000 o 4000.0";
                campo = "precio";
            }

            // Si el error es de tipo Integer (ej: stock o id)
        } else if (error.contains("Integer")) {
            Matcher matcher = INTEGER_PATTERN.matcher(error);

            if (matcher.find()) {
                valor = matcher.group(1);

                mensaje = "El campo 'stock' o 'productoId' tiene un valor inválido";
                sugerencia = "Debe ser un número entero sin comillas, por ejemplo: 100";
                campo = "stock/productoId";
            }

            // Si el JSON está mal estructurado
        } else if (error.contains("JSON parse error")) {
            mensaje = "JSON mal formado";
            sugerencia = "Verifica que el JSON esté bien estructurado";
        }

        // Retorna la respuesta en formato JSON con error 400
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Error en el formato JSON",
                "mensaje", mensaje,
                "campo", campo,
                "valor_recibido", valor,
                "sugerencia", sugerencia
        ));
    }

    // ⚠️ 2. ERRORES DE VALIDACIÓN MANUAL (throw new IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidationErrors(IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation Error",
                "mensaje", ex.getMessage(), // mensaje que tú defines
                "sugerencia", "Verifica los datos enviados"
        ));
    }

    // 📋 3. ERRORES DE VALIDACIÓN AUTOMÁTICA (@Valid en DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        StringBuilder errores = new StringBuilder();

        // Recorre todos los errores de validación
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.append(error.getField()) // nombre del campo
                    .append(": ")
                    .append(error.getDefaultMessage()) // mensaje del error
                    .append("; ");
        });

        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation Error",
                "mensaje", "Errores de validación",
                "detalles", errores.toString(),
                "sugerencia", "Corrige los campos marcados"
        ));
    }

    // 4. ERRORES DE BASE DE DATOS (UNIQUE, NULL, FK, etc)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDatabaseErrors(DataIntegrityViolationException ex) {

        String mensaje = "Error de integridad de datos";
        String sugerencia = "Verifica las restricciones de la base de datos";

        // Analiza el mensaje del error para personalizarlo
        if (ex.getMessage() != null) {

            // Si es error de duplicado (UNIQUE)
            if (ex.getMessage().contains("Duplicate entry")) {
                mensaje = "Ya existe un producto con ese nombre";
                sugerencia = "Usa un nombre diferente para el producto";

                // Si falta un campo obligatorio (NOT NULL)
            } else if (ex.getMessage().contains("cannot be null")) {
                mensaje = "Campo obligatorio faltante";
                sugerencia = "Asegúrate de enviar todos los campos requeridos";
            }
        }

        // Retorna error 409 (conflicto)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Data Integrity Error",
                "mensaje", mensaje,
                "sugerencia", sugerencia
        ));
    }

    //CUANDO FALTA UN PARÁMETRO EN LA URL
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParameter(MissingServletRequestParameterException ex) {

        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Missing Parameter",
                "mensaje", "Falta el parámetro requerido: " + ex.getParameterName(),
                "sugerencia", "Agrega el parámetro " + ex.getParameterName() + " a la solicitud"
        ));
    }

    //ERROR GENERAL (CUALQUIER ERROR NO CONTROLADO)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralErrors(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "mensaje", "Error interno del servidor",
                "sugerencia", "Intenta más tarde o contacta al administrador"
        ));
    }
}