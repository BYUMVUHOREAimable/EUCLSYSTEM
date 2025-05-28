package com.bai.euclsystem.exceptions;

import com.bai.euclsystem.dtos.response.auth.ErrorResponse;
import com.bai.euclsystem.dtos.response.auth.Response;
import com.bai.euclsystem.enums.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class TokenException extends RuntimeException {
  private final HttpStatus status = HttpStatus.UNAUTHORIZED;

  public TokenException(String message) {
    super(message);
  }

  public ResponseEntity<Response> getResponseEntity(){
    List<String> details = new ArrayList<>();
    details.add(super.getMessage());
    ErrorResponse errorResponse = new ErrorResponse().setMessage("You do not have authority to access this resource").setDetails(details);
    Response<ErrorResponse> response = new Response<>();
    response.setPayload(errorResponse);
    response.setType(ResponseType.UNAUTHORIZED);
    return new ResponseEntity<Response>(response, status);
  }
}
