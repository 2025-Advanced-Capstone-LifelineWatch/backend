package com.kgu.life_watch.global.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kgu.life_watch.global.dto.response.ApiResponse;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 전역 예외 처리 클래스
 *
 * <p>컨트롤러(@RestController)에서 발생하는 예외를 일괄적으로 처리하여 클라이언트에게 일관된 JSON 응답(ApiResponse) 형태로 반환합니다.
 *
 * <p>- LifelineException: 사용자 정의 예외를 처리하고 적절한 HTTP 상태 코드와 메시지를 반환 -
 * MethodArgumentNotValidException: @Valid 유효성 검증 실패 시 발생하는 예외를 처리
 *
 * <p>이 클래스는 Spring의 ResponseEntityExceptionHandler를 상속받아, Spring MVC 기본 예외 처리 로직도 오버라이딩하여 커스터마이징할 수
 * 있습니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * LifelineException 발생 시 처리 메서드
   *
   * <p>사용자 정의 예외에 포함된 ErrorCode를 추출하여, 해당 상태 코드와 메시지를 담은 ApiResponse를 JSON 형식으로 반환합니다.
   */
  @ExceptionHandler(LifelineException.class)
  public ResponseEntity<ApiResponse<Void>> handle(LifelineException e, HttpServletRequest request) {
    ErrorCode errorCode = e.getErrorCode();
    HttpStatus status = errorCode.getStatus();

    ApiResponse<Void> response = new ApiResponse<>(errorCode);

    return ResponseEntity.status(status).body(response);
  }

  /**
   * @Valid 유효성 검사 실패 시 발생하는 MethodArgumentNotValidException 처리
   *
   * <p>발생한 유효성 오류 중 첫 번째 메시지를 기반으로 ErrorCode를 매핑한 후, 클라이언트에게 ApiResponse 형태로 오류 정보를 반환합니다.
   *
   * <p>참고: HTTP 상태 코드는 항상 200 OK로 반환되며, 실제 에러 여부는 응답 본문의 status 필드(ErrorCode)로 판단합니다.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    // 첫 번째 발생한 검증 오류 메시지를 가져오기
    String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    // 오류 메시지로부터 ErrorCode를 찾기
    ErrorCode errorCode = ErrorCode.fromMessage(message);

    // ApiResponse 생성
    ApiResponse<Void> apiResponse = new ApiResponse<>(errorCode);

    // ResponseEntity로 ApiResponse를 반환
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  }
}
