package threeoone.bigproject.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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
class DocumentRepoTest {
  @Autowired
  private DocumentRepo documentRepo;

  @Autowired
  private UserRepo userRepo;

  private final String s = "qwertyuiopasdfghjklzxcvbnm";
  private final Random rand = new Random();

  @Test
  @DisplayName("Test compile & runtime")
  public void testCompile() {
    documentRepo.count();
  }

  private String randomString() {
    StringBuilder builder = new StringBuilder();

    for (int i = 1; i <= 6; i++) {
      builder.append(s.charAt(rand.nextInt(s.length())));
    }

    return builder.toString();
  }

  private User newUser() {
    User user = new User();
    user.setUsername(randomString());
    user.setPassword(randomString());
    user.setEmail(randomString() + "@" + randomString() + ".com");
    return user;
  }

  private Document newDocument() {
    Document document = new Document();
    document.setName(randomString());
    document.setCopies(rand.nextInt(1, 10));
    document.setAuthor(randomString());
    return document;
  }

  @Test
  @DisplayName("Insert a document without uploader")
  public void insertDocumentWithoutAuthor() {
    Document document = new Document("name", "description", 1);
    assertThatThrownBy(() -> documentRepo.save(document));
  }

  @Test
  @DisplayName("Insert into database")
  public void insertIntoDatabase() {
    User user = newUser();

    Document document = newDocument();
    user.addUploadedDocument(document);

    long countBefore = documentRepo.count();

    documentRepo.save(document);

    assertThat(documentRepo.count()).isEqualTo(countBefore + 1);

    Document anotherDoc = newDocument();
    user.addUploadedDocument(anotherDoc);

    documentRepo.save(anotherDoc);

    assertThat(documentRepo.count()).isEqualTo(countBefore + 2);

    document.setName(document.getName());

    documentRepo.save(document);

    assertThat(documentRepo.count()).isEqualTo(countBefore + 2);
  }

  @Test
  @DisplayName("Insert documents with the same uploader")
  public void insertDocumentsWithSameAuthor() {
    User user = newUser();

    Document docA = newDocument();
    Document docB = newDocument();
    Document docC = newDocument();

    user.addUploadedDocument(docA);
    user.addUploadedDocument(docB);
    user.addUploadedDocument(docC);

    docA = documentRepo.save(docA);
    docB = documentRepo.save(docB);
    docC = documentRepo.save(docC);

    User sameAuthor = docA.getUploader();

    assertThat(docB.getUploader()).isSameAs(sameAuthor);
    assertThat(docC.getUploader()).isSameAs(sameAuthor);
  }

  @Test
  @DisplayName("Insert documents with different uploaders")
  public void insertDocumentsWithDifferentAuthors() {
    User userA = newUser();
    User userB = newUser();

    Document docA = newDocument();
    Document docB = newDocument();

    userA.addUploadedDocument(docA);
    userB.addUploadedDocument(docB);

    docA = documentRepo.save(docA);
    docB = documentRepo.save(docB);

    assertThat(docA.getUploader()).isNotSameAs(docB.getUploader());
  }

  @Test
  @DisplayName("Cascade insert document and uploader")
  public void cascadeInsert() {
    User user = newUser();
    Document document = newDocument();
    user.addUploadedDocument(document);

    long countBefore = userRepo.count();

    documentRepo.save(document);

    assertThat(userRepo.count()).isEqualTo(countBefore + 1);

    assertThat(document.getUploader()).isSameAs(userRepo.findById(document.getUploader().getId()).orElse(null));
  }

  @Test
  @DisplayName("Retrieve a Document with no lending copies")
  public void documentWithNoLending() {
    User user = newUser();

    Document document = newDocument();

    user.addUploadedDocument(document);

    document = documentRepo.save(document);

    document = documentRepo.findWithLendingDetails(document.getId());

    assertThat(document.getLendingDetails()).isNotNull();
    assertThat(document.getLendingDetails().size()).isEqualTo(0);
  }

  @Test
  @DisplayName("Retrieve a Document and its lending copies")
  public void documentWithLending() {
    Member memA = new Member();
    memA.setName("name A");
    memA.setPhoneNumber("1234567890");
    memA.setAddress("an address");
    memA.setEmail("abc@xyz.com");

    Member memB = new Member();
    memB.setName("name B");
    memB.setPhoneNumber("1234567890");
    memB.setAddress("an address");
    memB.setEmail("abc@xyz.com");

    User user = newUser();

    Document document = newDocument();

    user.addUploadedDocument(document);

    LendingDetail detailA = new LendingDetail();
    detailA.setLendTime(LocalDateTime.now());
    detailA.setDueTime(LocalDateTime.now());

    LendingDetail detailB = new LendingDetail();
    detailB.setLendTime(LocalDateTime.now());
    detailB.setDueTime(LocalDateTime.now());

    memA.lendDocument(detailA);
    memB.lendDocument(detailB);

    document.lendDocument(detailA); 
    document.lendDocument(detailB);

    document = documentRepo.save(document);

    document = documentRepo.findWithLendingDetails(document.getId());

    List<LendingDetail> lendingDetails = document.getLendingDetails();

    assertThat(lendingDetails).isNotNull();
    assertThat(lendingDetails.size()).isEqualTo(2);

    assertThat(lendingDetails.contains(detailA)).isTrue();
    assertThat(lendingDetails.contains(detailB)).isTrue();
  }
}