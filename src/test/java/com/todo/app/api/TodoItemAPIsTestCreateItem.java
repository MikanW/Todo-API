package com.todo.app.api;

import com.todo.app.dto.ItemAPI;
import com.todo.app.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * APIテストクラス getAllItemsの動作を確認する
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoItemAPIsTestCreateItem {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemServ;

	/**
	 * 正常系：正常終了、且つアイテム作成成功
	 */
	@Test
	public void createItem_Normal() {
		ItemAPI newItem = new ItemAPI("dummyTitle", "dummyDetail", "2028-01-01", "作業中");
		when( itemServ.createNewItem(newItem)).thenReturn(newItem);
	}
}