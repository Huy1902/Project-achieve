package threeoone.bigproject.services.retrieval;

import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.repositories.DocumentRepo;

/**
 * A {@code Service} that supports basic operations that retrieve {@link Document} from the database.
 * This class is a singleton bean in Spring Context.
 *
 * @see Document
 *
 * @author DUCBRICK
 */
@Service
@RequiredArgsConstructor
public class DocumentRetrievalService {
  private final DocumentRepo documentRepo;

  /**
   * Retrieves all {@link Document} in the Database.
   *
   * @return a {@link List} containing every {@link Document} in the Database
   */
  public List <Document> getAllDocuments() {
    return documentRepo.findAll();
  }

  /**
   * Gets a {@link Document} with a specific {@code id}.
   * If no {@link Document} with the input {@code id} exists in the Database,
   * this method returns {@code null}.
   *
   * @param id the {@code id} of the {@link Document} to retrieve
   *
   * @return the desired {@link Document} if it exists in the Database, otherwise, {@code null}
   */
  public Document getDocumentById(int id) {
    Optional <Document> queryResult = documentRepo.findById(id);
    return queryResult.orElse(null);
  }

  /**
   * gets a Document along with its lending details (as in who is borrowing a copy
   * and when did they), as opposed to {@link #getDocumentById(int id)} which doesn't
   * retrieve the Document's lending details
   * @param id the id of the document
   * @return the Document of said id, along with its lending details, or NULL if no document
   * has said ID
   */
  public Document getDocumentWithLendingDetails(int id) {
      return documentRepo.findWithLendingDetails(id);
  }

  /**
   * gets a random document which has copies > 0 (borrow-able)
   * if no document exists in database:
   * @return a random Document
   * TODO: handles when no document available
   */
  public Document getRandomDocument(){
    return documentRepo.findRandom();
  }

  /**
   * get the number of documents in the repo
   */
  public Integer getNumberOfDocuments() { return documentRepo.countAll(); }

  /**
   * gets the last 5 document (sort by ID)
   * if less than 5 document exist, it will return that number of documents
   * @return a List of Document
   */
  public List<Document> getLatestDocuments() {
    return documentRepo.findTop5ByOrderByIdDesc();
  }

  /**
   * Retrieves a List of Documents whose name contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param name the substring to retrieve Documents
   *
   * @return the List of Documents whose name contain the given substring
   */
  public List <Document> searchByName(@NonNull String name) {
    return documentRepo.findWithNameContaining(name);
  }

  /**
   * Retrieves a List of Documents whose author's name contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param author the substring to retrieve Documents
   *
   * @return the List of Documents whose author's names contain the given substring
   */
  public List <Document> searchByAuthor(@NonNull String author) {
    return documentRepo.findWithAuthorContaining(author);
  }

  /**
   * Retrieves a List of Documents belonging to certain category(s).
   * Returns a List of Documents whose category contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param category the category substring to retrieve Documents
   *
   * @return the List of Documents whose category contains the given substring
   */
  public List <Document> searchByCategory(@NonNull String category) {
    return documentRepo.findWithCategoryContaining(category);
  }

  /**
   * Retrieves a List of Documents whose isbn contains the given substring.
   *
   * @apiNote the list returned will not be {@code NULL}.
   * @apiNote this method is not case-sensitive.
   *
   * @param isbn the substring to retrieve Documents
   *
   * @return the List of Documents whose isbn contain the given substring
   */
  public List <Document> searchByIsbn(@NonNull String isbn) {
    return documentRepo.findWithIsbnContaining(isbn);
  }

  /**
   * Retrieves a list of Documents that has at least {@code 1} copies being overdue.
   *
   * @return the list of Documents
   */
  public List <Document> getOverdueDocuments() {
    return documentRepo.getOverdueDocuments();
  }
}