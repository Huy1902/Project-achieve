package threeoone.bigproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.repositories.MemberRepo;
import threeoone.bigproject.services.retrieval.MemberRetrievalService;

@ExtendWith(MockitoExtension.class)
class MemberRetrievalServiceTest {
  @Mock
  private MemberRepo memberRepo;

  @InjectMocks
  private MemberRetrievalService memberRetrievalService;

  @Test
  @DisplayName("getAll() delegates to memberRepo")
  void getAll_delegatesTo_memberRepo() {
    memberRetrievalService.getAll();

    verify(memberRepo, times(1)).findAll();
  }

  @Test
  @DisplayName("findById() delegates to memberRepo")
  void findById_delegatesTo_memberRepo() {
    int id = 1;

    when(memberRepo.findById(id)).thenReturn(Optional.empty());

    assertThat(memberRetrievalService.findById(id)).isNull();

    verify(memberRepo, times(1)).findById(id);
  }

  @Test
  @DisplayName("findWhoseNamesContain() delegates to memberRepo")
  void findWhoseNamesContain_delegatesTo_memberRepo() {
    String string = "skibidi";

    when(memberRepo.findWithNameContaining(string)).thenReturn(new ArrayList <> ());

    List <Member> returned = memberRetrievalService.findWhoseNamesContain(string);

    assertThat(returned).isNotNull();
    assertThat(returned.isEmpty()).isTrue();

    verify(memberRepo, times(1)).findWithNameContaining(string);
  }
}