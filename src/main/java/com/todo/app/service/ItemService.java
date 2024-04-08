package com.todo.app.service;

import com.todo.app.dto.*;
import com.todo.app.entity.ItemDB;
import com.todo.app.exception.ResourceNotFoundException;
import com.todo.app.repository.ItemRepository;
import com.todo.app.util.DateFormatter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * アイテム取得・登録・更新・削除処理ロジック
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
@Service
public class ItemService {
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * DBから全アイテム（未削除）を取得する
	 *
	 * @return 全アイテム（未削除）のList
	 */
	public List<ItemAPI> getAllItems() {
		// Select処理、条件=未削除
		List<ItemDB> items = itemRepository.findAll(ItemFilters.notDeleted());
		return items.stream()
				.map(item -> ItemObjConverter.DBtoAPI(item))
				.collect(Collectors.toList());
	}

	/**
	 * アイテム登録
	 * - リクエストからをのアイテム情報をDB用形式に変換
	 * - DBにInsertする
	 *
	 */
	public ItemAPI createNewItem(ItemAPI itemApi) {
		//　TODO　登録内容妥当性チェック

		//　リクエストからのAPI用アイテムをDB用へ変換
		ItemDB itemDb = ItemObjConverter.APItoDB(itemApi);

		ItemDB savedItemDb = itemRepository.save(itemDb);

		// API用オブジェクトに変換してリターンする
		return ItemObjConverter.DBtoAPI(savedItemDb);
	}

	/**
	 * アイテム一部または全属性更新
	 * - 属性更新妥当性チェック
	 * - リクエストからをのアイテム情報をDB用形式に変換
	 * - DBにUpdateする
	 * @return Pair<ItemAPI, List<String>> 更新後のアイテム、更新不可属性のリスト
	 */
	public Pair<ItemAPI, String> updateItemPartial(Integer id, Map<String, Object> updates) {
		// リクエスト指定IDよりDBからアイテム取得

		ItemDB itemDb = itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DBから指定idのアイテムが見つからない " + id));
		BeanWrapper beanWrapper = new BeanWrapperImpl(itemDb);	// 更新用臨時アイテム

		// 属性存在チェックと更新可否チェック
		AtomicReference<String> illegalProps = new AtomicReference<>("");	// チェックエラー属性のリスト
		AtomicBoolean needUpdate = new AtomicBoolean(false);	// 更新要否フラグ

		// 属性毎にチェック
		updates.forEach((propertyName, propertyValue) -> {
			PropertyDescriptor propertyDescriptor = beanWrapper.getPropertyDescriptor(propertyName);
			if (propertyDescriptor != null && propertyDescriptor.getWriteMethod() != null) {
				try {
					if (ItemProp.isPropUpdatableFromReq(propertyName)) {
						// 更新可の場合、DB用属性値に転換し、臨時アイテムに設定する
						// TODO:設定値妥当性チェック
						Object newValueDB = ItemObjConverter.ConvertToDBPropValue(propertyName, propertyValue);
						beanWrapper.setPropertyValue(propertyName, newValueDB);
						needUpdate.set(true);	// 一個でも更新可の属性があれば、更新フラグをONとする

						// ステータスを完了に設定する場合、完了時間も設定する
						if ( propertyName == ItemProp.STATUS.getStrValue()
								&& newValueDB.equals(Status.DONE.getIndex())) {
							beanWrapper.setPropertyValue(ItemProp.DTIME.getStrValue(), DateFormatter.getNowUnixStamp());
						}

					} else {
						// 更新不可、当属性名をエラー属性リストに追記
						illegalProps.set(illegalProps.get().concat(propertyName).concat(" "));
					}
				} catch (IllegalArgumentException e) {
					// 属性不正の場合、当属性名をエラー属性リストに追記
					illegalProps.set(illegalProps.get().concat(propertyName).concat(" "));
				}
			}
		});

		if (needUpdate.get()) {
			//　更新要の場合、更新時間も設定
			beanWrapper.setPropertyValue(ItemProp.MTIME.getStrValue(), DateFormatter.getNowUnixStamp());
			//　DB更新処理
			ItemAPI updatedItem = ItemObjConverter.DBtoAPI(itemRepository.save(itemDb));
			return Pair.of(updatedItem, illegalProps.get().stripTrailing());
		} else {
			//　更新不要の場合、NULLアイテムとエラー属性をリターンする
			return Pair.of(new NullItemAPI(), illegalProps.get().stripTrailing());
		}
	}

	/**
	 * 指定IDのアイテム削除
	 * - IDに紐づくアイテムの存在チェック
	 * - アイテムの削除
	 *
	 * @return 削除ID
	 */

	public Integer deleteItem(Integer id) {
		// 存在チェック
		ItemDB itemDb = itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DBから指定idのアイテムが見つからない " + id));

		// 存在の場合、削除フラグをONとし、DBに保存する
		BeanWrapper beanWrapper = new BeanWrapperImpl(itemDb);
		beanWrapper.setPropertyValue("deleted", 1);
		ItemDB deletedItem = itemRepository.save(itemDb);

		return deletedItem.getId();
	}
}
