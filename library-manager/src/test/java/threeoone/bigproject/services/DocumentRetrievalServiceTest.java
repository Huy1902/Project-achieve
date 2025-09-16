package threeoone.bigproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.repositories.DocumentRepo;
import threeoone.bigproject.services.retrieval.DocumentRetrievalService;

@ExtendWith(MockitoExtension.class)
class DocumentRetrievalServiceTest {
  @Mock
  private DocumentRepo documentRepo;

  @InjectMocks
  private DocumentRetrievalService documentRetrievalService;

  @Test
  @DisplayName("getAllDocument delegates to DocumentRepo")
  public void getAllDocumentDelegatesToRepo() {
    documentRetrievalService.getAllDocuments();
    verify(documentRepo, times(1)).findAll();
  }

  @Test
  @DisplayName("getAllDocument returns DocumentRepo.findAll()")
  public void getAllDocumentReturnsDocumentRepoFindAll() {
    User user = new User("name", "password");
    List <Document> documents = new ArrayList <> ();

    Document docA = new Document("name a", "desc a", 1);
    Document docB = new Document("name b", "desc b", 1);
    Document docC = new Document("name c", "desc c", 1);

    user.addUploadedDocument(docA);
    user.addUploadedDocument(docB);
    user.addUploadedDocument(docC);

    documents.add(docA);
    documents.add(docB);
    documents.add(docC);

    when(documentRepo.findAll()).thenReturn(documents);

    assertThat(documentRetrievalService.getAllDocuments()).isEqualTo(documents);
  }

  @Test
  @DisplayName("getAllDocument returns null when DocumentRepo.findAll() returns null")
  public void getAllDocumentReturnsNull_whenDocumentRepoFindAllReturnsNull() {
    when(documentRepo.findAll()).thenReturn(null);
    assertThat(documentRetrievalService.getAllDocuments()).isNull();
  }

  @Test
  @DisplayName("getDocumentById delegates to DocumentRepo.findById()")
  public void getDocumentById_delegatesTo_DocumentRepoFindById() {
    int id = new Random().nextInt();
    documentRetrievalService.getDocumentById(id);
    verify(documentRepo, times(1)).findById(id);
  }

  @Test
  @DisplayName("getDocumentById returns DocumentRepo.findById() when exists")
  public void getDocumentById_returns_DocumentRepoFindById_whenExists() {
    int id = new Random().nextInt();
    Document document = new Document("doc name", "doc desc", 1);
    document.setId(id);

    when(documentRepo.findById(id)).thenReturn(Optional.of(document));
    assertThat(documentRetrievalService.getDocumentById(id)).isEqualTo(document);
  }

  @Test
  @DisplayName("getDocumentById returns null when not exists")
  public void getDocumentById_returnsNull_whenNotExists() {
    int id = new Random().nextInt();
    when(documentRepo.findById(id)).thenReturn(Optional.empty());
    assertThat(documentRetrievalService.getDocumentById(id)).isNull();
  }
}