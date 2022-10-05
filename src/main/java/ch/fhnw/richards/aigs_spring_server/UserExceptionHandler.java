package ch.fhnw.richards.aigs_spring_server;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class UserExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(UserExceptionHandler.class);

    @ExceptionHandler(value = {UserException.class})
    @ResponseBody
    ResponseEntity<ErrorResponse> userError(UserException ex) {
        LOG.error("Exception in login " , ex);
        ErrorResponse response = new ErrorResponse("User error", ex.getMessage());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
