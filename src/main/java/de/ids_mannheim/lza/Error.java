package de.ids_mannheim.lza;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class Error {
    class ErrorInfo {
        String desription;
        String exception;
        String cause;

        public ErrorInfo(Exception exception) {
            this.desription = exception.getMessage();
            this.exception = exception.getClass().getName();
            if (exception.getCause() != null)
                this.cause = exception.getCause().toString();
        }

        @Override
        public String toString() {
            return "ErrorInfo{" + "desription=" + desription + ", exception=" + exception + ", cause=" + cause + '}';
        }

        public String getDesription() {
            return desription;
        }

        public String getException() {
            return exception;
        }

        public String getCause() {
            return cause;
        }

    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorInfo(exception));
    }
}
