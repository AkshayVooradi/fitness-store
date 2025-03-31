package com.fitnessStore.backend.ExceptionHandling;

import com.mongodb.DuplicateKeyException;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
        return new ResponseEntity<>("This method request is not allowed on this EP", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StackOverflowError.class)
    public ResponseEntity<?> handleStackOverflowError(StackOverflowError stackOverflowError){
        return new ResponseEntity<>("You are using EQUALS, CONTAINS, REMOVE type of methods which is creating a circular dependency causing this error",HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        return new ResponseEntity<>(resourceNotFoundException.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKeyException(DuplicateKeyException duplicateKeyException){
        return new ResponseEntity<>("The item is already present"+duplicateKeyException.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MongoTransactionException.class)
    public ResponseEntity<?> handleMongoTransactionException(MongoTransactionException mongoTransactionException){
        return new ResponseEntity<>(mongoTransactionException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException){
        return new ResponseEntity<>("Illegal argument"+illegalArgumentException.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InputArgumentException.class)
    public ResponseEntity<?> handleInputArgumentException(InputArgumentException inputArgumentException){
        return new ResponseEntity<>(inputArgumentException.getMessage(),HttpStatus.BAD_REQUEST);
    }


}
