package com.ducbrick.finance_tracker.repos;

import com.ducbrick.finance_tracker.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
	@Query("SELECT c FROM Category c WHERE c.owner.id = :id")
  List<Category> findByOwnerId(@Param("id") int id);
}
