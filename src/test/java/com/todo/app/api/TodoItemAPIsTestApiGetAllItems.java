package com.todo.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.dto.ItemAPI;
import com.todo.app.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * APIテストクラス getAllItemsの動作を確認する
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoItemAPIsTestApiGetAllItems {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemServ;

	/**
	 * 正常系：正常終了、且つアイテムを取得した場合
	 */
	@Test
	public void testGetAllItems_WithData() throws Exception {
		// ダミーデータ
		ItemAPI item1 = new ItemAPI("買い出し", "お肉、洗剤、ほうれん草、果物", "2024-04-01", "未着手");
		ItemAPI item2 = new ItemAPI("〇〇本を読み終わる", "電子版のURL", "2024-04-06", "作業中");
		List<ItemAPI> list = Arrays.asList(item1, item2);
		when( itemServ.getAllItems()).thenReturn(list);

		mockMvc.perform(get("/items/getAllItems"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items.length()").value(2));
				// TODO データ内容などについて詳しい検証
	}

	/**
	 * 正常系：正常終了、アイテム存在しない場合
	 */
	@Test
	public void testGetAllItems_WithoutData() throws Exception {
		// ダミーデータ
		List<ItemAPI> list = Collections.emptyList();
		when( itemServ.getAllItems()).thenReturn(list);

		mockMvc.perform(get("/items/getAllItems"))
				.andExpect(status().is(204))
				.andExpect(jsonPath("$.items.length()").value(0));
	}

	/**
	 * 異常系：MySQL異常時の動作を確認する
	 */
	@Test
	public void testGetAllItems_throwsException() throws Exception {
		// 001: DB接続エラーの場合
		when(itemServ.getAllItems()).thenThrow(new DataAccessResourceFailureException("Database connection failed"));

		mockMvc.perform(get("/items/getAllItems"))
				.andExpect(status().is(500))
				.andExpect(jsonPath("$.message").value("データベースエラーが発生した：Database connection failed"));

		// TODO 他DBエラーの検証
		// 00x: 〇〇の場合
		// 他DBエラー例外を設定
		// アサーション: HTTPステータス、各種エラーメッセージなど
	}


	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
