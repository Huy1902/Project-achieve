package threeoone.bigproject.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import threeoone.bigproject.exceptions.IllegalDocumentInfoException;

/**
 * ORM Entity representing a {@link Document} in the library database.
 * <p>
 * A {@link Document} has a {@link #name},
 * a {@link #description},
 * a total of {@link #copies} physical copies,
 * an {@link #author},
 * an {@link #isbn},
 * a cover image at {@link #coverImageUrl}
 * and uploaded at {@link #uploadTime}.
 * A {@link Document} has an {@link #uploader},
 * representing the {@link User} who uploaded this {@link Document}.
 * <p>
 * {@link #uploader} is a bidirectional many-to-one relationship between {@link User} and {@link Document}.
 * JPA requires synchronization on both sides in order to persist.
 * {@link User#addUploadedDocument(Document)} sets the relationship on both sides.
 * Lombok-generated {@link #setUploader(User)} only sets the relationship on the {@link Document} side.
 * <p>
 * Likewise, {@link #lendingDetails} is a bidirectional one-to-many relationship between {@link Document} and {@link LendingDetail}.
 *
 * @author DUCBRICK
 * @see User
 */
@Entity
@Table(name = "document")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Document name must not be empty")
  @Size(max = 255, message = "Document name must have at most 255 characters")
  private String name;

  private String description;

  @NotNull(message = "Document must have a number of physical copies")
  @PositiveOrZero(message = "Document must not have a negative number of physical copies")
  private Integer copies = 0;

  @Size(max = 255, message = "Document author's name must have at most 255 characters")
  @NotBlank(message = "Document author's name must not be empty")
  private String author;

  @Column(unique = true)
  @Pattern(regexp = "^(97[89][0-9]{10}|[0-9]{9}[0-9Xx])$", message = "Invalid ISBN")
  private String isbn;

  @Column(name = "upload_time")
//  @NotNull(message = "Document must have an upload time")
  private LocalDateTime uploadTime;

  @Size(message = "Document categories must have at most 255 characters")
  private String category;

  @Column(name = "cover_image_url")
  @NotBlank(message = "Document must have a cover image")
  @Size(max = 255, message = "Document cover image URL must have at most 255 characters")
  private String coverImageUrl = "threeoone/bigproject/controller/viewcontrollers/No_image_available.png";

  @Column(name = "pdf_url")
  @Size(max = 255, message = "Document pdf URL must have at most 255 characters")
  private String pdfUrl;

  @Column(name = "info_url")
  @Size(max = 255, message = "Document info URL must have at most 255 characters")
  private String infoUrl;

  @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "uploader_id")
//  @NotNull(message = "Document must be uploaded by a User")
  @Valid
  private User uploader;

  @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
  private List<LendingDetail> lendingDetails = new ArrayList<>();

  /**
   * Constructs a new {@link Document} with the given parameters.
   *
   * @param name the name of the new {@link Document}
   */
  public Document(@NonNull String name) {
    this.name = name;
  }

  /**
   * Constructs a new {@link Document} with the given parameters.
   *
   * @param name        name of the new {@link Document}
   * @param description description of the new {@link Document}
   * @param copies      total number of copies of this Document
   */
  public Document(@NonNull String name, String description, @NonNull Integer copies) {
    this.name = name;
    this.description = description;
    this.copies = copies;
  }

  /**
   * Constructs a new {@link Document} with the given parameters.
   *
   * @param name        name of the new {@link Document}
   * @param description description of the new {@link Document}
   */
  public Document(@NonNull String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Adds a {@link LendingDetail} to the list of the Document's lent copies.
   * JPA requires synchronization on both sides in order to persist,
   * meaning a {@link LendingDetail} must exist in its Document's {@link #lendingDetails} list.
   * This method sets the relationship on both the {@link Document} side and {@link LendingDetail} side.
   *
   * @param lendingDetail detail about the lent copy
   */
  public void lendDocument(LendingDetail lendingDetail) {
    lendingDetails.add(lendingDetail);
    lendingDetail.setDocument(this);
  }
}
