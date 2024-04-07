package com.todo.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * 日付・時間フォーマット用ライブラリー
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
public class DateFormatter {
	private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * タイムスタンプより日付文字列を取得する(yyyy-MM-dd)
	 *
	 * @param unixTime 保存用タイムスタンプ
	 * @return String タイムスタンプに紐づく日付文字列(yyyy-MM-dd)
	 */
	public static String UnixToDateStr(Long unixTime) {
		Date date = new Date(unixTime);
		return formatter.format(date);
	}

	/**
	 * 入力文字列日付に紐づくタイムスタンプを取得する（ms)
	 * 備考：日付のみの入力、HH:mm:ss は一律00:00:00とする
	 *
	 * @param dateStr 文字列の日付。フォーマット：yyyy-MM-dd
	 * @return Long DBに保存用タイムスタンプ（ms計）
	 */
	public static Long DateStrToUnix( String dateStr ) {
		Long unixTimestamp = 0L;

		try {
			unixTimestamp = formatter.parse(dateStr).getTime();

		}catch (ParseException e) {
			e.printStackTrace();
		}
		return unixTimestamp;
	}

	/**
	 * 現在時刻のタイムスタンプを取得する（ms)
	 *
	 * @return Long DBに保存用タイムスタンプ（ms計）
	 */
	public static Long getNowUnixStamp() {
		return Instant.now().toEpochMilli();
	}

}
