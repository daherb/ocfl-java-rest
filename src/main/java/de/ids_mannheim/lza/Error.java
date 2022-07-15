package de.ids_mannheim.lza;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class Error {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.toString());
    }
}
