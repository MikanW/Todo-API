package com.todo.app.dto;

/**
 * Todoアイテム属性のEnum
 *
 * @author Zuxian Wan
 * @since 2024-04-07
 */
public enum ItemProp {
	ID("id"),
	TITLE("title"),
	DETAIL("detail"),
	DUE("due"),
	STATUS("status"),
	CTIME("cTime"),
	MTIME("mTime"),
	DTIME("dTime"),
	DELETED("deleted");

	private final String strValue;

	ItemProp(String strValue) {
		this.strValue = strValue;
	}

	public String getStrValue() {
		return strValue;
	}

	/**
	 * 属性名の文字列より、属性のEnumを取得する
	 *
	 */
	public static ItemProp getPropByNameStr(String propNameStr) {
		for (ItemProp prop : ItemProp.values()) {
			if (prop.getStrValue().equals(propNameStr)) {
				return prop;
			}
		}
		throw new IllegalArgumentException("未定義な属性名: " + propNameStr);
	}

	/**
	 * アイテム属性がリクエスト指定より更新可否を判断する
	 *
	 * @param propNameStr　リクエストが指定する属性名
	 * @return true: 更新可
	 * 		   false: 更新不可
	 */
	public static boolean isPropUpdatableFromReq(String propNameStr) {
		try {
			return switch (getPropByNameStr(propNameStr)) {
				case TITLE, DETAIL, DUE, STATUS -> true;
				case ID, CTIME, MTIME, DTIME, DELETED -> false;
			};
		}catch (IllegalArgumentException e) {
			return false;
		}
	}
}
