package com.todo.app.entity;

import com.todo.app.dto.ItemAPI;
import com.todo.app.util.DateFormatter;
import com.todo.app.dto.Status;
import jakarta.persistence.*;

/**
 * Todoアイテムクラス
 * DB用Todoアイテムを定義する
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
@Entity
@Table(name = "item")
public class ItemDB {
	// リスポンスに提供する属性
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;		// ID

	@Column(name = "title", length = 50)
	private String title;	// タイトル

	@Column(name = "detail", length = 1000)
	private String detail;	// 詳細

	@Column(name = "due")
	private Long due;		// 期限

	@Column(name = "status")
	private Integer status;	// ステータス

	// DB専用、リスポンスに提供しない属性
	@Column(name = "ctime")
	private Long cTime;		// 作成時間

	@Column(name = "mtime")
	private Long mTime;		//　前回変更時間

	@Column(name = "dtime")
	private Long dTime;		// 完了時間

	@Column(name = "deleted")
	private Integer deleted; // 削除フラグ

	/**
	 * コンストラクタ
	 * デフォルト
	 */
	public ItemDB() {
	}

	/**
	 * コンストラクタ
	 * API形式オブジェクトより作成用
	 */
	public ItemDB(ItemAPI itemApi) {
		Long currentTimeStamp = DateFormatter.getNowUnixStamp();
		this.title = itemApi.getTitle();
		this.detail = itemApi.getDetail();
		this.status = Status.getStatusCodeByStr(itemApi.getStatus());
		this.due = DateFormatter.DateStrToUnix(itemApi.getDue());

		this.cTime = currentTimeStamp;
		this.mTime = currentTimeStamp;

		this.deleted = 0;	// 削除フラグ：デフォルト=OFF
	}

	// getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getDue() {
		return due;
	}

	public void setDue(Long due) {
		this.due = due;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getcTime() {
		return cTime;
	}

	public void setcTime(Long cTime) {
		this.cTime = cTime;
	}

	public Long getmTime() {
		return mTime;
	}

	public void setmTime(Long mTime) {
		this.mTime = mTime;
	}

	public Long getdTime() {
		return dTime;
	}

	public void setdTime(Long dTime) {
		this.dTime = dTime;
	}
}
