package com.todo.app.util;

import org.springframework.http.*;

import java.util.Date;


/**
 * API用HttpHeader作成ライブラリー
 *
 * @author Zuxian Wan
 * @since 2024-04-07
 */
public class HttpHeader {
	public static HttpHeaders getHeader() {
		HttpHeaders headers = new HttpHeaders();

		headers.setCacheControl(CacheControl.noCache());
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setDate(new Date().toInstant());

		// TODO:他のヘッダー（文字コード、認証など）
		// 機能実装未のため、一旦スキップ

		return headers;
	}

}