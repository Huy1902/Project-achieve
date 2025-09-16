package threeoone.bigproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.repositories.MemberRepo;
import threeoone.bigproject.services.persistence.MemberEditingService;

@ExtendWith(MockitoExtension.class)
class MemberEditingServiceTest {
  @Mock
  private MemberRepo memberRepo;

  @InjectMocks
  private MemberEditingService memberEditingService;

  @Test
  @DisplayName("Update a NULL Member")
  public void updateNull() {
    assertThatThrownBy(() -> memberEditingService.update(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Update an existing Member")
  public void updateExistingMember() {
    Member member = new Member("name");
    member.setId(1);

    when(memberRepo.existsById(member.getId())).thenReturn(true);

    memberEditingService.update(member);

    verify(memberRepo, times(1)).save(member);
  }

  @Test
  @DisplayName("Delete a Member")
  public void delete() {
    int id = 1;

    memberEditingService.delete(id);

    verify(memberRepo, times(1)).deleteById(id);
  }
}