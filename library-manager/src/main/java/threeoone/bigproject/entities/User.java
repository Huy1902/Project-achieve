package threeoone.bigproject.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import lombok.*;


/**
 * ORM Entity representing a user of the application.
 * <p>
 * This entity stores information required for user login and the documents they have uploaded.
 * A {@link User} has a unique {@link #username}, a {@link #password} used for authentication
 * and a unique {@link #email} address.
 * A {@link User} can upload a number of {@link Document}, represented by {@link #uploadedDocuments}.
 * <p>
 * {@link #uploadedDocuments} is a bidirectional one-to-many relationship between {@link User} and {@link Document}.
 * JPA requires synchronization on both sides in order to persist.
 * {@link #addUploadedDocument(Document)} sets the relationship on both sides.
 * Lombok-generated {@link Document#setUploader(User)} only sets the relationship on the {@link Document} side.
 * <p>
 * {@link PasswordResetToken} has a uni-directional many-to-one relationship with {@link User}.
 * So, client must manually delete any {@link PasswordResetToken} associated to a specific {@link User} before deleting it.
 *
 * @see Document
 *
 * @author DUCBRICK
 */
@Entity
@Table(name = "app_user")
@NoArgsConstructor @Getter @Setter @AllArgsConstructor @Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true)
  @NotBlank(message = "Username must not be empty")
  @Size(max = 127, message = "Username must have at most 127 characters")
  private String username;

  @NotEmpty(message = "User password must not be empty")
  @Size(max = 127, message = "User password must have at most 127 characters")
  private String password;

  @Column(unique = true)
  @NotBlank(message = "User must have an email")
  @Email(message = "User must have a valid email")
  @Size(max = 127, message = "User email must have at most 127 character")
  private String email;

  @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL, orphanRemoval = true)
  private List <Document> uploadedDocuments = new ArrayList <> ();

  /**
   * Adds a {@link Document} to the list of the User's uploaded documents.
   * JPA requires synchronization on both sides in order to persist,
   * meaning a {@link Document} must exist in its author's {@link #uploadedDocuments} list.
   * This method sets the relationship on both the {@link User} side and {@link Document} side.
   *
   * @param document a Document uploaded by this User
   */
  public void addUploadedDocument(Document document) {
    uploadedDocuments.add(document);
    document.setUploader(this);
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
