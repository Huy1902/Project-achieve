package threeoone.bigproject.services.persistence;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.LendingDetail;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.repositories.DocumentRepo;
import threeoone.bigproject.repositories.LendingDetailRepo;
import threeoone.bigproject.repositories.MemberRepo;

@Service
@RequiredArgsConstructor
public class LendingPersistenceService {
  @Value("${document.lending.deadline.day}")
  private int lendDeadlineAsDays;

  private final LendingDetailRepo lendingDetailRepo;
  private final MemberRepo memberRepo;
  private final DocumentRepo documentRepo;

  /**
   * Saves a new {@link LendingDetail} from the given {@code memberId} and {@code documentId}.
   * This method constructs a new {@link LendingDetail} Entity instance
   * from the given {@code memberId}, {@code documentId},
   * the lend time as {@link LocalDateTime} at the moment,
   * the due time as specified by the {@code document.lending.deadline.day} property,
   * sets up bidirectional relationships and saves it to the Database.
   * The {@link Document} must have at least {@code 1} physical copy in order to be lent.
   *
   * @param memberId the {@code id} of the lending {@link Member}
   * @param documentId the {@code id} of the lent {@link Document}
   *
   * @throws IllegalArgumentException when no {@link Document} or {@link Member} with the given {@code id} exists
   * @throws NoSuchElementException when the {@link Document} has no remaining physical copies
   * @throws RuntimeException when unexpected errors occur when working with Database (such as constraints violation)
   *
   * @return the constructed {@link LendingDetail}.
   */
  @Transactional
  public LendingDetail lend(int memberId, int documentId) {
    Member member = memberRepo.findWithLendingDetails(memberId);

    if (member == null) {
      throw new IllegalArgumentException("No Member with that id exists");
    }

    Document document = documentRepo.findWithLendingDetails(documentId);

    if (document == null) {
      throw new IllegalArgumentException("No Document with that id exists");
    }

    int remainingCopies = document.getCopies() - document.getLendingDetails().size();

    if (remainingCopies == 0) {
      throw new NoSuchElementException("The requested Document has no remaining physical copies");
    }

    LendingDetail lendingDetail = new LendingDetail();
    lendingDetail.setLendTime(LocalDateTime.now());
    lendingDetail.setDueTime(lendingDetail.getLendTime().plusDays(lendDeadlineAsDays));

    member.lendDocument(lendingDetail);
    document.lendDocument(lendingDetail);

    return lendingDetailRepo.save(lendingDetail);
  }


  /*
  /**
   * Saves a {@link LendingDetail}.
   * <p>
   * If the given {@link LendingDetail} has no {@code id} (or in other words, its {@code id} is {@code NULL}),
   * this method adds a new {@link LendingDetail} to the Database.
   * <br>
   * If the {@link LendingDetail} has an {@code id},
   * this method updates a pre-existing {@link LendingDetail} whose {@code id} is the same as the given LendingDetail's.
   *
   * @apiNote This method returns the saved {@link LendingDetail} Entity instance,
   * which may be different from the given instance and may have different data.
   *
   * @param lendingDetail the {@link LendingDetail} to save
   *
   * @throws NoSuchElementException when the given LendingDetail's {@code id} doesn't exist in the Database
   * @throws RuntimeException when unexpected errors occur when working with Database (such as constraints violation)
   *
   * @return the saved {@link LendingDetail} Entity instance, which may be different from the given instance
   */
  /*
  @Transactional
  private LendingDetail update(@NonNull LendingDetail lendingDetail) {
    if (lendingDetail.getId() != null && !lendingDetailRepo.existsById(lendingDetail.getId())) {
      throw new NoSuchElementException("Attempting to update a non-existent LendingDetail");
    }
    return  lendingDetailRepo.save(lendingDetail);
  }
  */


  /**
   * Deletes the {@link LendingDetail} with the given {@code id}.
   * If the {@link LendingDetail} is not found in the persistence store it is silently ignored.
   *
   * @param id the {@code id} of the {@link LendingDetail} to delete
   */
  @Transactional
  public void delete(int id) {
    lendingDetailRepo.deleteById(id);
  }
}
