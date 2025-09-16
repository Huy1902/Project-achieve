package threeoone.bigproject.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.LendingDetail;
import threeoone.bigproject.entities.Member;

/**
 * JPA repository for {@link Member} entity.
 *
 * @author DUCBRICK
 */
public interface MemberRepo extends ListCrudRepository <Member, Integer> {
  /**
   * Retrieves a List of Members whose name contain the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param substr the substring to retrieve Members
   *
   * @return the List of Members whose name contain the given substring
   */
  @Query("SELECT m from Member m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :substr, '%'))")
  List <Member> findWithNameContaining(@Param("substr") String substr);

  /**
   * Retrieves a {@link Member} with a specific {@code id} and its {@code lendingDetails}.
   * This method only executes a single SQL query.
   * This method retrieves the desired {@link Member} and details about all of its lending {@link Document},
   * as opposed to {@link #findById}, which only retrieves the desired {@link Member} but not its {@code lendingDetails}.
   * If no {@link Member} with the specified {@code id} exists, this method returns {@code NULL}.
   *
   * @apiNote The return Member's {@code lendingDetails} list will not be {@code NULL}.
   *
   * @param id the id of the desired {@link Member}
   *
   * @return the {@link Member} with the input id, along with its {@code lendingDetails},
   *         or {@code NULL} if it doesn't exist
   *
   * @see Member
   * @see LendingDetail
   */
  @Query("SELECT m FROM Member m LEFT JOIN FETCH m.lendingDetails WHERE m.id = (:id)")
  Member findWithLendingDetails(@Param("id") int id);

  @Query("SELECT m FROM Member m ORDER BY m.id LIMIT 5")
  List<Member> findTop5ByOrderByIdDesc();

  /**
   * Retrieves a list of Members that has at least {@code 1} overdue documents.
   *
   * @return the list of Members
   */
  @Query("""
      SELECT DISTINCT ld.member
      FROM LendingDetail ld
      WHERE ld.dueTime < CURRENT_TIMESTAMP
      """)
  List <Member> getOverdueMembers();
}
