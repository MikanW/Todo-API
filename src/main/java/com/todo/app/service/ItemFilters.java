package com.todo.app.service;

import com.todo.app.entity.ItemDB;
import org.springframework.data.jpa.domain.Specification;


/**
 * アイテム取得時の条件・WHERE句
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
public class ItemFilters {

	/**
	 * 条件：アイテムの削除フラグがOFF
	 *
	 * @return 全アイテムのAPI形式オブジェクトのList
	 */
	public static Specification<ItemDB> notDeleted() {
		return (root, query, cb) -> cb.equal(root.get("deleted"), 0);
	}
}
