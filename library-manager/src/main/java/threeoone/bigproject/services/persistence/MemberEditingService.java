package threeoone.bigproject.services.persistence;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.repositories.MemberRepo;

/**
 * A service to modify, which is to add, update, or delete {@link Member} entities from the Database.
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Service
@RequiredArgsConstructor
@Validated
public class MemberEditingService {
  private final MemberRepo memberRepo;

  /**
   * Saves a new {@link Member}.
   *
   * @apiNote This method returns the saved {@link Member} Entity instance,
   * which may be different from the given instance and may have different data.
   *
   * @param member the {@link Member} to add
   *
   * @throws ConstraintViolationException when the given {@link Member} violates existing constraints
   * @throws RuntimeException when unexpected errors occur when working with Database (such as constraints violations)
   *
   * @return the saved {@link Document} Entity instance, which may be different from the given instance
   */
  @Transactional
  public Member add(@NonNull @Valid Member member) {
    member.setId(null);
    return memberRepo.save(member);
  }

  /**
   * Updates an existing {@link Member} whose {@code id} is the same as the given Member's.
   * The given Member's {@code id} must match with an existing {@link Member} in the Database.
   *
   * @apiNote This method returns the saved {@link Member} Entity instance,
   * which may be different from the given instance and may have different data.
   *
   * @param member the {@link Member} to update
   *
   * @throws IllegalArgumentException when the given Member's {@code id} doesn't match any's in the Database
   * @throws ConstraintViolationException when the given {@link Member} violates existing constraints
   * @throws RuntimeException when unexpected errors occur when working with Database (such as constraints violations)
   *
   * @return the saved {@link Member} Entity instance, which may be different from the given instance
   */
  @Transactional
  public Member update(@NonNull @Valid Member member) {
    if (member.getId() == null) {
      throw new IllegalArgumentException("Attempting to update a Member with no ID");
    }

    if (!memberRepo.existsById(member.getId())) {
      throw new IllegalArgumentException("No Members with that ID exist");
    }

    return memberRepo.save(member);
  }

  /**
   * Deletes the {@link Member} with the given {@code id}.
   * If the {@link Member} is not found in the persistence store it is silently ignored.
   *
   * @param id the {@code id} of the {@link Member} to delete
   */
  @Transactional
  public void delete(int id) {
    memberRepo.deleteById(id);
  }
}
