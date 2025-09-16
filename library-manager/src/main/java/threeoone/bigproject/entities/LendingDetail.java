package threeoone.bigproject.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ORM Entity representing detail about a lent {@link Document}.
 * A {@link LendingDetail} has a {@link #lendTime}, representing the timestamp when the Document was lent,
 * and {@link #dueTime}, representing the deadline before which the lending {@link Member} is required to return the lent {@link Document}.
 * <p>
 * {@link #member} is a bidirectional many-to-one relationship between {@link Member} and {@link LendingDetail}.
 * JPA requires synchronization on both sides in order to persist.
 * {@link Member#lendDocument(LendingDetail)}} sets the relationship on both sides.
 * Lombok-generated {@link #setMember(Member)} only sets the relationship on the {@link LendingDetail} side.
 * <p>
 * Likewise, {@link #document} is a bidirectional many-to-one relationship between {@link Document} and {@link LendingDetail}.
 *
 * @author DUCBRICK
 */
@Entity
@Table(name = "lending_detail")
@NoArgsConstructor
@Getter
@Setter
public class LendingDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "lend_time")
  @NotNull(message = "Details about a document lending must include a lend time")
  private LocalDateTime lendTime;

  @Column(name = "due_time")
  @NotNull(message = "Details about a document lending must include a due time")
  private LocalDateTime dueTime;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @NotNull(message = "Details about a document lending must include the lending member")
  @Valid
  private Member member;

  @JoinColumn(name = "document_id", nullable = false)
  @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @NotNull(message = "Details about a document lending must include the lent document")
  @Valid
  private Document document;

  public LendingDetail(LocalDateTime lendTime) {
    this.lendTime = lendTime;
  }

  public boolean isOverdue() {
    return this.dueTime.isBefore(LocalDateTime.now());
  }
}
