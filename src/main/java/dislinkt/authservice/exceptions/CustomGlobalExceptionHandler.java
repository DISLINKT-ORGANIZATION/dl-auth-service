package dislinkt.authservice.exceptions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UsernameAlreadyExists.class)
	public void handleUsernameAlreadyExists(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), "Username already exists.");
	}
	
	@ExceptionHandler(EmailAlreadyExists.class)
	public void handleEmailAlreadyExists(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), "Email already exists.");
	}

}
