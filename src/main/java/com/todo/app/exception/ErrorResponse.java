package com.todo.app.exception;

/**
 * エラーリスポンス処理クラス
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
public class ErrorResponse {
	private String message;

	public ErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}