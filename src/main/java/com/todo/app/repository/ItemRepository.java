package com.todo.app.repository;

import com.todo.app.entity.ItemDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * DB処理
 *
 * @author Zuxian Wan
 * @since 2024-04-06
 */
@Repository
public interface ItemRepository extends JpaRepository<ItemDB, Integer> , JpaSpecificationExecutor<ItemDB> {
}