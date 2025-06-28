package com.tibbertil.botforbibyt.exception;

import com.tibbertil.botforbibyt.dto.StatusResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<StatusResponseDto> handleNotFoundRequest(RuntimeException ex){
        StatusResponseDto statusResponseDto = StatusResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(statusResponseDto, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {FetchPriceException.class})
    public ResponseEntity<StatusResponseDto> handleFetchPriceException(RuntimeException ex){
        StatusResponseDto statusResponseDto = StatusResponseDto.builder()
                .status(HttpStatus.BAD_GATEWAY.getReasonPhrase())
                .message(ex.getMessage())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(statusResponseDto, headers, HttpStatus.BAD_GATEWAY);
    }
}
