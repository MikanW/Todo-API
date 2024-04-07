package com.todo.app.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * アプリ全範囲で稼働する例外処理（想定）
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * DBアクセスエラー
	 */
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
		// エラーレスポンスのBodyを構成
		ErrorResponse errorResponse = new ErrorResponse("データベースエラーが発生した：" + ex.getMessage());
		// ResponseEntityにステータス500とBodyを設定
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
