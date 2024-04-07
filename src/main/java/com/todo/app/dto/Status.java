package com.todo.app.dto;

/**
 * TodoアイテムのステータスEnum
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
public enum Status {
	NOSTATUS(0, "未設定"),	//異常処理または今後機能追加用
	TODO(1, "未着手"),
	DOING(2, "作業中"),
	DONE(3, "完了");


	private final int index;
	private final String strValue;

	Status(int index, String strValue) {
		this.index = index;
		this.strValue = strValue;
	}

	public int getIndex() {
		return index;
	}

	public String getStrValue() {
		return strValue;
	}

	/**
	 * ステータスの文字列より、数値を取得する
	 */
	public static Integer getStatusCodeByStr(String statusStr) {
		for (Status status : Status.values()) {
			if (status.getStrValue().equals(statusStr)) {
				return status.getIndex();
			}
		}
		throw new IllegalArgumentException("未定義なステータス: " + statusStr);
	}

	/**
	 * ステータスの数値より、Enumオブジェクトを取得する
	 */
	public static Status getStatusByIndex(int index) {
		for (Status status : Status.values()) {
			if (status.getIndex() == index) {
				return status;
			}
		}
		throw new IllegalArgumentException("未定義なステータス: " + index);
	}
};
