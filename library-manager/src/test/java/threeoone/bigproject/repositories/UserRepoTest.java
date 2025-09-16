package threeoone.bigproject.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepoTest {
  @Autowired
  private UserRepo userRepo;

  @Autowired
  private DocumentRepo documentRepo;

  private final String s = "qwertyuiopasdfghjklzxcvbnm";
  private final Random rand = new Random();

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
  @DisplayName("Count before and after single insertion")
  public void countBeforeAndAfterSingleInsertion() {
    long countBefore = userRepo.count();

    User user = newUser();
    userRepo.save(user);

    long countAfter = userRepo.count();

    assertThat(countAfter).isEqualTo(countBefore + 1);
  }

  @Test
  @DisplayName("Count before and after multiple insertion")
  public void countBeforeAndAfterMultipleInsertion() {
    long countBefore = userRepo.count();

    List <User> users = new ArrayList <> ();
    users.add(newUser());
    users.add(newUser());
    users.add(newUser());
    users.add(newUser());

    for (User user : users) {
      userRepo.save(user);
    }

    long countAfter = userRepo.count();

    assertThat(countAfter).isEqualTo(countBefore + users.size());
  }

  @Test
  @DisplayName("Count before and after list insertion")
  public void countBeforeAndAfterListInsertion() {
    long countBefore = userRepo.count();

    List <User> users = new ArrayList <> ();
    users.add(newUser());
    users.add(newUser());
    users.add(newUser());
    users.add(newUser());

    userRepo.saveAll(users);

    long countAfter = userRepo.count();

    assertThat(countAfter).isEqualTo(countBefore + users.size());
  }

  @Test
  @DisplayName("Insert documents and retrieve uploaders")
  public void insertDocuments_retrieveAuthor() {
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

    user = docA.getUploader();

    assertThat(user).isSameAs(docA.getUploader());
    assertThat(user).isSameAs(docB.getUploader());
    assertThat(user).isSameAs(docC.getUploader());

    user = userRepo.findUserAndUploadedDocuments(user.getId());
    List <Document> documents = user.getUploadedDocuments();

    assertThat(documents).isNotNull();
    assertThat(documents.size()).isEqualTo(3);

    assertThat(documents.contains(docA)).isTrue();
    assertThat(documents.contains(docB)).isTrue();
    assertThat(documents.contains(docC)).isTrue();
  }

  @Test
  @DisplayName("Insert user and retrieve document")
  public void insertUserRetrieveDocument() {
    User user = newUser();
    Document document = newDocument();
    user.addUploadedDocument(document);

    user = userRepo.save(user);

    List <Document> documents = user.getUploadedDocuments();

    assertThat(documents).isNotNull();

    assertThat(documents.size()).isEqualTo(1);

    document = documents.getFirst();

    assertThat(document).isSameAs(documentRepo.findById(document.getId()).orElse(null));
  }

  @Test
  @DisplayName("Retrieve user with no document with findUserAndUploadedDocuments")
  public void retrieveUserWithNoDocumentWith_findUserAndUploadedDocuments() {
    User user = newUser();
    user = userRepo.save(user);

    assertThat(userRepo.findUserAndUploadedDocuments(user.getId())).isSameAs(user);
  }

  @Test
  @DisplayName("Find user by login name")
  public void findByUsername() {
    User user = newUser();
    user = userRepo.save(user);
    assertThat(user).isSameAs(userRepo.findByUsername(user.getUsername()));
  }

  @Test
  @DisplayName("Find non-existent user by login name")
  public void findNonExistentUserByLoginName() {
    String random = "jshdflkasdjfsadf";
    assertThat(userRepo.findByUsername(random)).isNull();
  }

  @Test
  @DisplayName("Find non-existent user and documents by id")
  public void findNonExistentUserById_findUserAndUploadedDocuments() {
    int randomId = new Random().nextInt(-100, 100);
    assertThat(userRepo.findUserAndUploadedDocuments(randomId)).isNull();
  }
}