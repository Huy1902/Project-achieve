package threeoone.bigproject.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.LendingDetail;
import threeoone.bigproject.entities.Member;

import java.util.List;
import threeoone.bigproject.entities.User;

/**
 * Simple Spring Data JPA repository for {@link Document} entity.
 *
 * @author DUCBRICK
 */
public interface DocumentRepo extends ListCrudRepository <Document, Integer> {
  /**
   * keyword supported method. don't wanna change it.
   * @return List of document
   */
    List<Document> findTop5ByOrderByIdDesc();
  /**
   * Retrieves a {@link Document} with a specific {@code id} and its {@code lendingDetails}.
   * This method only executes a single SQL query.
   * This method retrieves the desired {@link Document} and details about all of its lent copies,
   * as opposed to {@link #findById}, which only retrieves the desired {@link Document} but not its {@code lendingDetails}.
   * If no {@link Document} with the specified {@code id} exists, this method returns {@code NULL}.
   *
   * @apiNote The return Document's {@code lendingDetails} list will not be {@code NULL}.
   *
   * @param id the id of the desired {@link Document}
   *
   * @return the {@link Document} with the input id, along with its {@code lendingDetails},
   *         or {@code NULL} if it doesn't exist
   *
   * @see Document
   * @see LendingDetail
   */
  @Query("SELECT d FROM Document d LEFT JOIN FETCH d.lendingDetails WHERE d.id = (:id)")
  Document findWithLendingDetails(@Param("id") int id);

  /**
   * get a random document
   * @return
   */
  @Query("SELECT d FROM Document d WHERE d.copies > 0 ORDER BY RANDOM() LIMIT 1")
  Document findRandom();

  /**
   * count the number of documents
   */
  @Query("SELECT count(*) FROM Document d")
  Integer countAll();

  /**
   * Retrieves a List of Documents whose name contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param substr the substring to retrieve Documents
   *
   * @return the List of Documents whose name contain the given substring
   */
  @Query("SELECT d from Document d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :substr, '%'))")
  List <Document> findWithNameContaining(@Param("substr") String substr);

  /**
   * Retrieves a List of Documents whose author's name contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param substr the substring to retrieve Documents
   *
   * @return the List of Documents whose author's names contain the given substring
   */
  @Query("SELECT d from Document d WHERE LOWER(d.author) LIKE LOWER(CONCAT('%', :substr, '%'))")
  List <Document> findWithAuthorContaining(@Param("substr") String substr);

  /**
   * Retrieves a List of Documents belonging to certain category(s).
   * Returns a List of Documents whose category contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param substr the category substring to retrieve Documents
   *
   * @return the List of Documents whose category contains the given substring
   */
  @Query("SELECT d from Document d WHERE LOWER(d.category) LIKE LOWER(CONCAT('%', :substr, '%'))")
  List <Document> findWithCategoryContaining(@Param("substr") String substr);

  /**
   * Retrieves a List of Documents whose isbn contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param substr the substring to retrieve Documents
   *
   * @return the List of Documents whose isbn contain the given substring
   */
  @Query("SELECT d from Document d WHERE LOWER(d.isbn) LIKE LOWER(CONCAT('%', :substr, '%'))")
  List <Document> findWithIsbnContaining(@Param("substr") String substr);

  /**
   * Retrieves a list of Documents that has at least {@code 1} copies being overdue.
   *
   * @return the list of Documents
   */
  @Query("""
      SELECT DISTINCT ld.document
      FROM LendingDetail ld
      WHERE ld.dueTime < CURRENT_TIMESTAMP
      """)
  List <Document> getOverdueDocuments();

  /**
   * Checks if a {@link Document} with a specific {@code isbn} exists.
   *
   * @param isbn the email of the desired {@link Document}
   *
   * @return true if a {@link Document} with the specified {@code isbn} exists, otherwise false
   */
  @Query("""
      SELECT
        CASE
          WHEN COUNT(d) > 0 THEN true
          ELSE false
        END
      FROM Document d
      WHERE d.isbn = (:isbn)""")
  boolean existsByIsbn(@Param("isbn") String isbn);
}
