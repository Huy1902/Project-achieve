package threeoone.bigproject.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.LendingDetail;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MemberRepoTest {
  @Autowired
  private MemberRepo memberRepo;

  @Test
  @DisplayName("Test build & run")
  public void test() {
    Member m1 = new Member();
    m1.setName("name");
    m1.setPhoneNumber("1234567890");
    m1.setAddress("an address");
    m1.setEmail("abc@xyz.com");
    m1 = memberRepo.save(m1);

    Member m2 = memberRepo.findById(m1.getId()).get();
    m2.setName("another name");

    m2 = memberRepo.save(m2);

    assertThat(m1.getName()).isEqualTo("another name");
  }

  @Test
  @DisplayName("Find no members whose names contain a specific string")
  void noMemberWhoseNameContainString() {
    Member m1 = new Member();
    m1.setName("Bob");
    m1.setPhoneNumber("1234567890");
    m1.setAddress("an address");
    m1.setEmail("abc@xyz.com");

    Member m2 = new Member();
    m2.setName("John");
    m2.setPhoneNumber("1234567890");
    m2.setAddress("an address");
    m2.setEmail("abc@xyz.com");

    Member m3 = new Member();
    m3.setName("Ducbrick");
    m3.setPhoneNumber("1234567890");
    m3.setAddress("an address");
    m3.setEmail("abc@xyz.com");

    Member m4 = new Member();
    m4.setName("Succ");
    m4.setPhoneNumber("1234567890");
    m4.setAddress("an address");
    m4.setEmail("abc@xyz.com");

    m1 = memberRepo.save(m1);
    m2 = memberRepo.save(m2);
    m3 = memberRepo.save(m3);
    m4 = memberRepo.save(m4);

    assertThat(memberRepo.findWithNameContaining("impossible").size()).isEqualTo(0);
  }

  @Test
  @DisplayName("Find members whose names contain a specific string")
  void membersWhoseNamesContainString() {
    Member m1 = new Member();
    m1.setName("sjdffdBobjsfsdjkh");
    m1.setPhoneNumber("1234567890");
    m1.setAddress("an address");
    m1.setEmail("abc@xyz.com");

    Member m2 = new Member();
    m2.setName("fdgqwcbObqfdc");
    m2.setPhoneNumber("1234567890");
    m2.setAddress("an address");
    m2.setEmail("abc@xyz.com");

    Member m3 = new Member();
    m3.setName("FCVfdgcFGCRZuccFCrdf");
    m3.setPhoneNumber("1234567890");
    m3.setAddress("an address");
    m3.setEmail("abc@xyz.com");

    Member m4 = new Member();
    m4.setName("KJMIjlkmIOjkmsUCCIJKLM");
    m4.setPhoneNumber("1234567890");
    m4.setAddress("an address");
    m4.setEmail("abc@xyz.com");

    m1 = memberRepo.save(m1);
    m2 = memberRepo.save(m2);
    m3 = memberRepo.save(m3);
    m4 = memberRepo.save(m4);

    List <Member> query = memberRepo.findWithNameContaining("bob");

    assertThat(query.size()).isEqualTo(2);
    assertThat(query.contains(m1)).isTrue();
    assertThat(query.contains(m2)).isTrue();

    query = memberRepo.findWithNameContaining("ucc");

    assertThat(query.size()).isEqualTo(2);
    assertThat(query.contains(m3)).isTrue();
    assertThat(query.contains(m4)).isTrue();
  }

  @Test
  @DisplayName("Retrieve a Member with no lending Documents")
  public void memberWithNoLending() {
    Member member = new Member();
    member.setName("name");
    member.setPhoneNumber("1234567890");
    member.setAddress("an address");
    member.setEmail("abc@xyz.com");
    member = memberRepo.save(member);

    member = memberRepo.findWithLendingDetails(member.getId());

    assertThat(member.getLendingDetails()).isNotNull();
    assertThat(member.getLendingDetails().size()).isEqualTo(0);
  }

  @Test
  @DisplayName("Retrieve a Member and its lending Documents")
  public void memberWithLending() {
    Member member = new Member();
    member.setName("name");
    member.setPhoneNumber("1234567890");
    member.setAddress("an address");
    member.setEmail("abc@xyz.com");

    User user = new User();
    user.setUsername("slkdfj");
    user.setPassword("saldkfj");
    user.setEmail("lskadf@sdfl.com");

    Document docA = new Document();
    docA.setName("lksjdf");
    docA.setAuthor("slkfj");

    Document docB = new Document();
    docB.setName("lkjf");
    docB.setAuthor("lksfaa");

    LendingDetail detailA = new LendingDetail();
    detailA.setDueTime(LocalDateTime.now());
    detailA.setLendTime(LocalDateTime.now());

    LendingDetail detailB = new LendingDetail();
    detailB.setLendTime(LocalDateTime.now());
    detailB.setDueTime(LocalDateTime.now());

    user.addUploadedDocument(docA);
    user.addUploadedDocument(docB);

    member.lendDocument(detailA);
    member.lendDocument(detailB);

    docA.lendDocument(detailA);
    docB.lendDocument(detailB);

    member = memberRepo.save(member);

    member = memberRepo.findWithLendingDetails(member.getId());

    List <LendingDetail> lendingDetails = member.getLendingDetails();

    assertThat(lendingDetails).isNotNull();
    assertThat(lendingDetails.size()).isEqualTo(2);

    assertThat(lendingDetails.contains(detailA)).isTrue();
    assertThat(lendingDetails.contains(detailB)).isTrue();
  }
}