package com.todo.app.api;

import com.todo.app.dto.ItemAPI;
import com.todo.app.dto.NullItemAPI;
import com.todo.app.exception.ResourceNotFoundException;
import com.todo.app.service.ItemService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * アイテム取得・登録・更新・削除のREST API
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
@RestController
@RequestMapping("/items")
public class TodoItemAPIs {
	@Autowired
	private ItemService itemService = new ItemService();	// 実処理クラス

	@GetMapping("getAllItems")
	@Operation(summary = "Todoアイテムリスト取得")
	@ApiResponse(responseCode = "200", description = "アイテムのリスト取得成功")
	@ApiResponse(responseCode = "204", description = "処理成功したが、アイテムは１件も存在しない")
	public ResponseEntity<Map<String, List<ItemAPI>>> getAllItems() {
		List<ItemAPI> items = itemService.getAllItems();
		Map<String, List<ItemAPI>> response = new HashMap<>();
		response.put("items", items);

		if (!items.isEmpty()) {
			// 正常終了且つDBにアイテムありの場合
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			// 正常終了、ただしDBにアイテムが存在しない場合
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}
	}

	@PostMapping("createNewItem")
	@Operation(summary = "Todoアイテムを新規作成")
	@ApiResponse(responseCode = "200", description = "アイテム作成成功")
	public ResponseEntity<Map<String, ItemAPI>> createItem(@RequestBody ItemAPI item) {
		// アイテム作成処理
		ItemAPI createdItem = itemService.createNewItem(item);

		//　リスポンス
		Map<String, ItemAPI> response = new HashMap<>();
		response.put("newItem", createdItem);

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@PatchMapping("/updateItem")
	@Operation(summary = "指定IDのTodoアイテムの指定属性を更新")
	@ApiResponse(responseCode = "200", description = "アイテム更新成功")
	@ApiResponse(responseCode = "400", description = "指定内容不正")
	@ApiResponse(responseCode = "404", description = "指定IDに紐づくアイテム存在しない")
	public ResponseEntity<Map<String, Object>> updateItemPartial(
			@RequestBody Map<String, Object> req)
	{
		Integer id = (Integer) req.get("id");	// 更新ID
		Map<String, Object> update = (Map<String, Object>) req.get("update"); // 更新内容

		// 更新処理
		Pair<ItemAPI, String> updResult= itemService.updateItemPartial(id, update);
		ItemAPI updatedItem = updResult.getFirst();

		String failedPropList = updResult.getSecond();

		// リスポンス用
		Map<String, Object> response = new HashMap<>();
		HttpStatus statusCode;
		response.put("updateFailedProps", failedPropList);

		// 更新結果によってリスポンスのBody、Statusを決定
		if ( !(updatedItem instanceof NullItemAPI)) {
			// 更新ありの場合
			response.put("updated", "true");
			response.put("updatedItem", updatedItem);

			if ( failedPropList.isEmpty() ) {
				// リクエスト指定の全属性が更新成功
				statusCode = HttpStatus.OK;
			} else {
				// 一部更新成功
				statusCode = HttpStatus.BAD_REQUEST;
			}
		} else {
			//　更新なしの場合
			System.out.println("400");
			response.put("updateFailedProps", failedPropList);
			response.put("updated", "false");
			statusCode = HttpStatus.BAD_REQUEST;
		}

		return ResponseEntity.status(statusCode).body(response);
	}

	@PostMapping("deleteItem")
	@Operation(summary = "指定IDに紐づくTodoアイテムを削除")
	@ApiResponse(responseCode = "200", description = "アイテム削除成功")
	@ApiResponse(responseCode = "404", description = "指定IDのアイテム存在しない")
	public ResponseEntity<Map<String, Object>> deleteItemById(@RequestBody Integer id) {
		// リスポンス用
		Map<String, Object> response = new HashMap<>();
		HttpStatus statusCode;

		//　アイテム削除処理
		try{
			// 成功の場合
			itemService.deleteItem(id);
			response.put("deletedId", id);
			statusCode = HttpStatus.OK;
		} catch (ResourceNotFoundException e) {
			// 指定ID存在しない場合
			response.put("errorMessage", e.getMessage());
			statusCode = HttpStatus.NOT_FOUND;
		}

		return ResponseEntity.status(statusCode).body(response);
	}
}
