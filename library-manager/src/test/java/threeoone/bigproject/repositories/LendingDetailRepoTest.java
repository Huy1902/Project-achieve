package threeoone.bigproject.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
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
class LendingDetailRepoTest {
  @Autowired
  private LendingDetailRepo lendingDetailRepo;

  @Test
  @DisplayName("Test build & run")
  public void test() {
    Member member = new Member();
    member.setName("a name");
    member.setPhoneNumber("0969696969");
    member.setAddress("Ohio");
    member.setEmail("skibidi@mog.rizz");

    Document document = new Document("name");
    document.setAuthor("sigma");

    User user = new User();
    user.setUsername("name");
    user.setPassword("skibidi");
    user.setEmail("skjdfkas@rizzler.mog");

    LendingDetail lendingDetail = new LendingDetail();
    lendingDetail.setLendTime(LocalDateTime.now());
    lendingDetail.setDueTime(LocalDateTime.now());

    member.lendDocument(lendingDetail);
    document.lendDocument(lendingDetail);
    user.addUploadedDocument(document);

    lendingDetail = lendingDetailRepo.save(lendingDetail);


  }
}