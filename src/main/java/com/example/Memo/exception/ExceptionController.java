package com.example.Memo.exception;

import com.example.Memo.util.RequestUtil;
import com.example.Memo.web.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.util.stream.Collectors.joining;

@Slf4j
@RestControllerAdvice //@RestController에서 발생한 Exception 캐치
public class ExceptionController {

    /**
     * 공통적인 예외처리를 위힌 컨트롤러
     *
     * @Valid 유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        /*{
            "error": "Bad Request",
            "errors": [{
                "arguments": [{
                    "arguments": null,
                    "code": "name",
                    "codes": [
                        "postStoreRequestDto.name",
                        "name"
                    ],
                    "defaultMessage": "name"
                }],
                "codes": [
                    "NotNull.postStoreRequestDto.name",
                    "NotNull.name",
                    "NotNull.java.lang.String",
                    "NotNull"
                ],
                "bindingFailure": false,
                "code": "NotNull",
                "defaultMessage": "가게명 입력 필수",
                "field": "name",
                "objectName": "postStoreRequestDto",
                "rejectedValue": null
            }],
            "message": "Validation failed for object='postStoreRequestDto'. Error count: 1",
            "path": "/beydrologis/merchant/api/v1/store/add",
            "status": 400,
            "timestamp": "2020-04-14T01:57:55.226+0000"
        }*/
        log.error("methodValidException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        ResponseDto responseDto = makeMethodValidExceptionResponse(e.getBindingResult());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    private ResponseDto makeMethodValidExceptionResponse(BindingResult bindingResult) {
        Integer code = RequestUtil.REQUEST_PARAMETER_FORM_ERROR_CODE;
        String detail = "";

        //에러가 있다면
        if (bindingResult.hasErrors()) {
            //DTO에 설정한 meaasge값을 가져온다
            detail = bindingResult.getFieldError().getDefaultMessage();
            /*
            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();
            String description = "";
            switch (bindResultCode){
                case "NotNull":
                    break;
                case "Min":
                    break;
            }*/
        }

        return ResponseDto.builder()
                .msg(detail)
                .code(code)
                .build();
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDto> bindException(BindException e, HttpServletRequest request) {
        log.error("bindException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        log.error("requestParam: ", StringUtils.join(request.getParameterMap()));
        String errorMessageList = e.getFieldErrors().stream().map(fieldError -> {
            if (fieldError.contains(TypeMismatchException.class))
                return String.format("(%s) %s", fieldError.getField(), RequestUtil.REQUEST_PARAMETER_FORM_ERROR_MSG);
            else
                return fieldError.getDefaultMessage();
        }).collect(joining(", "));
        ResponseDto responseDto = ResponseDto.builder()
                .msg(errorMessageList)
                .code(RequestUtil.REQUEST_PARAMETER_ILLEGAL_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto> illegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("illegalArgumentException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(e.getMessage())
                .code(RequestUtil.REQUEST_PARAMETER_ILLEGAL_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNonexistentException.class)
    public ResponseEntity<ResponseDto> objectNonexistentException(ObjectNonexistentException e, HttpServletRequest request) {
        log.error("ObjectNonexistentException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(e.getMessage())
                .code(RequestUtil.REQUEST_OBJECT_NONEXISTENT_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectInaccessibleException.class)
    public ResponseEntity<ResponseDto> objectInaccessibleException(ObjectInaccessibleException e, HttpServletRequest request) {
        log.error("objectInaccessibleException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(e.getMessage())
                .code(RequestUtil.REQUEST_OBJECT_INACCESSIBLE_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectStorageIOException.class)
    public ResponseEntity<ResponseDto> objectStorageIOException(ObjectStorageIOException e, HttpServletRequest request) {
        log.error("objectStorageIOException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.IMAGE_SAVE_ERROR_MSG)
                .code(RequestUtil.IMAGE_SAVE_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseDto> iOException(IOException e, HttpServletRequest request) {
        log.error("iOException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.IMAGE_SAVE_ERROR_MSG)
                .code(RequestUtil.IMAGE_SAVE_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ResponseDto> numberFormatException(NumberFormatException e, HttpServletRequest request) {
        log.error("NumberFormatException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        log.error("requestParam: ", StringUtils.join(request.getParameterMap()));
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.REQUEST_PARAMETER_FORM_ERROR_MSG)
                .code(RequestUtil.REQUEST_PARAMETER_FORM_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ResponseDto> conversionFailedException(NumberFormatException e, HttpServletRequest request) {
        log.error("conversionFailedException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        log.error("requestParam: ", StringUtils.join(request.getParameterMap()));
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.REQUEST_PARAMETER_FORM_ERROR_MSG)
                .code(RequestUtil.REQUEST_PARAMETER_FORM_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> httpMessageNotReadableException(Exception e, HttpServletRequest request) {
        log.error("httpMessageNotReadableException invoked, method:{}, url:{}, trace:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.REQUEST_PARAMETER_FORM_ERROR_MSG)
                .code(RequestUtil.REQUEST_PARAMETER_FORM_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> httpRequestMethodNotSupportedException(Exception e, HttpServletRequest request) {
        log.error("httpRequestMethodNotSupportedException invoked, method:{}, url:{}, trace:{}, exceptionType:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage(), e.getClass().getCanonicalName());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.SERVER_URL_NOT_SUPPORT_MSG)
                .code(RequestUtil.SERVER_URL_NOT_SUPPORT_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDto> maxUploadSizeExceededException(Exception e, HttpServletRequest request) {
        log.error("maxUploadSizeExceededException invoked, method:{}, url:{}, trace:{}, exceptionType:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage(), e.getClass().getCanonicalName());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.IMAGE_OVER_MAX_SIZE_ERROR_MSG)
                .code(RequestUtil.IMAGE_OVER_MAX_SIZE_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> exception(Exception e, HttpServletRequest request) {
        log.error("exception invoked, method:{}, url:{}, trace:{}, exceptionType:{}", request.getMethod(), request.getRequestURI(), e.getLocalizedMessage(), e.getClass().getCanonicalName());
        ResponseDto responseDto = ResponseDto.builder()
                .msg(RequestUtil.SERVER_ERROR_MSG)
                .code(RequestUtil.SERVER_ERROR_CODE)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}