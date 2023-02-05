package com.garanti.TeknikServis.excepton;



import com.garanti.TeknikServis.response.RestErrorResponse;
import com.garanti.TeknikServis.response.RestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler
    public final ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest webRequest){

        String message = ex.getMessage();
        String detail = webRequest.getDescription(false);

        return getResponseEntity(message, detail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest webRequest){

        String message = ex.getMessage();
        String detail = webRequest.getDescription(false);

        return getResponseEntity(message, detail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler
    public final ResponseEntity<?> handleUnexpectedException(UnexpectedException ex, WebRequest webRequest){
        String message = ex.getMessage();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest){
        String message = ex.getMessage();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, HttpStatus.IM_USED);
    }
    @ExceptionHandler
    public final ResponseEntity<?> handleItemNotFoundExceptionExceptions(EntityNoContentException ex, WebRequest webRequest){

        String message = ex.getMessage();
        String detail = webRequest.getDescription(false);
        return getResponseEntity(message, detail, HttpStatus.I_AM_A_TEAPOT);
    }
    @ExceptionHandler
    public final ResponseEntity<?> handleMultipleProposalCreationException(MultipleProposalCreationException ex, WebRequest webRequest){
        String message = ex.getMessage();
        String detail = webRequest.getDescription(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        Date errorDate = new Date();
        RestErrorResponse restErrorResponse = new RestErrorResponse(sdf.format(errorDate), message, detail);
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(restErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = "Validation failed!";
        StringBuilder detail = new StringBuilder();

        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();

        if(!errorList.isEmpty()){

            for (ObjectError objectError : errorList) {
                String defaultMessage = objectError.getDefaultMessage();

                detail.append(defaultMessage).append("\n");
            }
        } else {
            detail = new StringBuilder(ex.getBindingResult().toString());
        }

        return getResponseEntity(message, detail.toString(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(String message, String detail, HttpStatus httpStatus) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        Date errorDate = new Date();

        RestErrorResponse restErrorResponse = new RestErrorResponse(sdf.format(errorDate), message, detail);

        RestResponse<RestErrorResponse> restResponse = RestResponse.error(restErrorResponse);


        return new ResponseEntity<>(restResponse, httpStatus);
    }
}