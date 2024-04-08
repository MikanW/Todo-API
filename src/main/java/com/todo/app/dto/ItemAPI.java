package com.todo.app.dto;

import com.todo.app.util.DateFormatter;
import com.todo.app.entity.ItemDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * TodoアイテムDTO
 * API用Todoアイテムの属性を定義する
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
public class ItemAPI {
	private Integer id;			// ID
	private String title;	// タイトル
	private String detail;	// 詳細
	private String due;		// 期限 yyyy-MM-dd
	private String status;	// ステータス

	// リスポンスに提供しない属性
	private String cTime;	// 作成時間 yyyy-MM-dd
	private String mTime;	// 前回変更時間 yyyy-MM-dd
	private String dTime;	// 完了時間 yyyy-MM-dd
	private Boolean deleted;// 削除フラグ

	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * コンストラクタ
	 * デフォルト
	 */
	public ItemAPI() {
	}

	/**
	 * コンストラクタ
	 * リスポンスより指定更新可な属性によって作成する
	 */
	public ItemAPI(String title, String detail, String due, String status) {
		this.title = title;
		this.detail = detail;
		this.due = due;
		this.status = status;
	}

	/**
	 * コンストラクタ
	 * API形式のアイテムから転換用
	 */
	public ItemAPI(ItemDB itemDB ) {
		this.id = itemDB.getId();
		this.title = itemDB.getTitle();
		this.detail = itemDB.getDetail();
		this.due = DateFormatter.UnixToDateStr(itemDB.getDue());
		this.status = Status.getStatusByIndex(itemDB.getStatus())
							.getStrValue();
		this.cTime = DateFormatter.UnixToDateStr(itemDB.getcTime());
		this.mTime = DateFormatter.UnixToDateStr(itemDB.getmTime());

		// アイテム完了時間設定ありの場合
		if (!Objects.isNull(itemDB.getdTime())) {
			this.dTime = DateFormatter.UnixToDateStr(itemDB.getdTime());
		}

	}

	// getter and setters
	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}

	public String getDue() {
		return due;
	}

	public String getStatus() {
		return status;
	}

}
