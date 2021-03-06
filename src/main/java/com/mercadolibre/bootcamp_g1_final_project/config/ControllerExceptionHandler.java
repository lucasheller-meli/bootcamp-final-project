package com.mercadolibre.bootcamp_g1_final_project.config;

import com.mercadolibre.bootcamp_g1_final_project.exceptions.*;
import com.newrelic.api.agent.NewRelic;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiError(
						HttpStatus.BAD_REQUEST.name(),
						e.getMessage(),
						HttpStatus.BAD_REQUEST.value()
				));
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiError> noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
		ApiError apiError = new ApiError("route_not_found", String.format("Route %s not found", req.getRequestURI()), HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { ApiException.class })
	protected ResponseEntity<ApiError> handleApiException(ApiException e) {
		Integer statusCode = e.getStatusCode();
		boolean expected = HttpStatus.INTERNAL_SERVER_ERROR.value() > statusCode;
		NewRelic.noticeError(e, expected);
		if (expected) {
			LOGGER.warn("Internal Api warn. Status Code: " + statusCode, e);
		} else {
			LOGGER.error("Internal Api error. Status Code: " + statusCode, e);
		}

		ApiError apiError = new ApiError(e.getCode(), e.getDescription(), statusCode);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<ApiError> handleUnknownException(Exception e) {
		LOGGER.error("Internal error", e);
		NewRelic.noticeError(e);

		ApiError apiError = new ApiError("internal_error", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = SectionInWarehouseNotFoundException.class)
	public String NotFoundSectionInWarehouseException(SectionInWarehouseNotFoundException notFoundSectionInWarehouse) {
		return notFoundSectionInWarehouse.getMessage();
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = SectionNotExistException.class)
	public String NotExistSectionException(SectionNotExistException notFoundSectionException) {
		return notFoundSectionException.getMessage();
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = ProductNotExistException.class)
	public String NotExistProductException(ProductNotExistException productNotExistException) {
		return productNotExistException.getMessage();
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = WarehouseNotExistException.class)
	public String NotExistWarehouseException(WarehouseNotExistException warehouseNotExistException) {
		return warehouseNotExistException.getMessage();
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = ListProductPerDuedateNotExistException.class)
	public String ListProductPerDuedateNotExistException(ListProductPerDuedateNotExistException listProductPerDuedateNotExistException) {
		return listProductPerDuedateNotExistException.getMessage();
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = CategoryPerDuedateNotFoundException.class)
	public String NotFoundProductInBatch(CategoryPerDuedateNotFoundException categoryPerDuedateNotFoundException) {
		return categoryPerDuedateNotFoundException.getMessage();
	}

}