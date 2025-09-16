package com.ducbrick.finance_tracker.repos;

import com.ducbrick.finance_tracker.entities.Transaction;
import com.ducbrick.finance_tracker.entities.User;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Validated
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
  /**
   * Retrieves all {@link Transaction} of a specific {@link User} within a time window.
   * Every {@link Transaction} have its {@code timestamp} between {@code start} (inclusive) and {@code end} (exclusive).
   * The transactions will be returned as a {@link List}.
   *
   * @param start start timestamp
   * @param end end timestamp
   *
   * @return a {@link List} of {@link Transaction} whose {@code timestamp} is between {@code start} and {@code end}
   */
  @Query("""
          SELECT t
          FROM Transaction t
          WHERE t.timestamp >= :start
          AND t.timestamp < :end
          AND t.category.owner.id = :ownerId
          """)
  List <Transaction> findByTimeWindow(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("ownerId") int ownerId);

  @Query("""
          SELECT t
          FROM Transaction t
          WHERE t.category.owner.id = :ownerId
          ORDER BY t.timestamp DESC, t.id DESC
          """)
  List<Transaction> findByOwner(@Param("ownerId") int ownerId, Pageable pageable);

  @Query("""
        SELECT (COUNT(t) + :pageSize - 1) / :pageSize
        FROM Transaction t
        WHERE t.category.owner.id = :ownerId
        """)
  int getPageCount(@Param("ownerId") int ownerId, @Positive @Param("pageSize") int pageSize);

  @Query("""
        SELECT COUNT(t)
        FROM Transaction t
        WHERE t.category.id = :categoryId
        """)
  int countByCategory(int categoryId);
}
