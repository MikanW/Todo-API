package com.todo.app.dto;

import com.todo.app.entity.ItemDB;
import com.todo.app.util.DateFormatter;

import java.util.Map;


/**
 * TodoアイテムのAPIに提供する形式・DB用形式の相互転換ライブラリー
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
public class ItemObjConverter {
	/**
	 * DB用よりをAPI用を作成
	 */
	public static ItemAPI DBtoAPI(ItemDB item) {
		return new ItemAPI(item);
	}

	/**
	 * API用よりをDB用を作成
	 */
	public static ItemDB APItoDB(ItemAPI item) {
		return new ItemDB(item);
	}

	/**
	 * アイテム属性値をAPI用からDB用に変換する
	 *
	 * @param propName　属性名
	 * @param propValue　変換元：API用属性値
	 * @return Object 変換先：DB用属性値
	 */
	public static Object ConvertToDBPropValue(String propName, Object propValue) {
		try {
			ItemProp targetPropEnum = ItemProp.getPropByNameStr(propName);
			Object dbValue = ConvertToDBValue(targetPropEnum, propValue);
			return dbValue;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("未定義な属性: " + propName);
		}
	}

	/**
	 * アイテム属性によって値をAPI用からDB用に変換する
	 *
	 * @param propEnum　属性のEnum
	 * @param propValue　変換元：API用属性値
	 * @return Object 変換先：DB用属性値
	 */
	public static Object ConvertToDBValue(ItemProp propEnum, Object propValue) {
		Object dbValue = switch (propEnum) {
			// 数字属性・文字列属性、変換不要
			case ID, TITLE, DETAIL, DELETED -> propValue;
			// 日付属性、タイムスタンプへ変換
			case DUE, CTIME, MTIME, DTIME -> DateFormatter.DateStrToUnix((String)propValue);
			// ステータス、数字形式ステータスコードへ変換
			case STATUS -> Status.getStatusCodeByStr((String)propValue);
			// 定義属性以外
			default -> throw new IllegalArgumentException("未定義な属性: " + propEnum);
		};

		return dbValue;
	}

	/**
	 * TODO　API用よりをDB用を作成を作成時、妥当性チェック
	 */
/*	private boolean dataValid(Map<String, Object> props) {
		error
		// リクエストより指定不可属性
		for (key : props.keySet()) {

		}
		return true;
	}*/
}
