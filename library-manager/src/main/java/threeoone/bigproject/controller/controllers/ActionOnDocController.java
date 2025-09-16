package threeoone.bigproject.controller.controllers;

import javafx.collections.FXCollections;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.controller.viewcontrollers.*;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.DocumentAlreadyExistException;
import threeoone.bigproject.exceptions.NotLoggedInException;
import threeoone.bigproject.services.persistence.DocumentPersistenceService;
import threeoone.bigproject.services.retrieval.DocumentRetrievalService;
import threeoone.bigproject.util.Alerts;

import java.util.List;

/**
 * Interacts with view controllers and services to handle logic for document manipulation.
 * Retrieves information from services and sends actions to views based on that information.
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class ActionOnDocController {
  private final DocumentDetailController documentDetailController;
  private final DocumentRetrievalService documentRetrievalService;
  private final DocOverviewController docOverviewController;
  private final LendingDetailController lendingDetailController;
  private final MenuController menuController;
  private final EditDocumentController editDocumentController;
  private final DocumentPersistenceService documentPersistenceService;
  private final PDFReaderController pdfReaderController;
  private final AddNewDocController addNewDocController;
  private final RequestSender<ViewController> switchToDocOverview;
  private final RequestSender<ViewController> getListOfOverdueDoc;

  private final Logger logger = LoggerFactory.getLogger(ActionOnDocController.class);

  /**
   * Registers request receivers for document handling.
   *
   * @param documentDetailRequestSender      request sender for document details
   * @param getListAllDocumentRequestSender  request sender to get all documents
   * @param getLastestDocumentsRequestSender request sender to get the latest documents
   * @param getRandomDocumentRequestSender   request sender to get a random document
   * @param editDocumentRequestSender        request sender to edit a document
   * @param removeDocumentRequestSender      request sender to remove a document
   * @param borrowDocumentRequestSender      request sender to borrow a document
   * @param addDocumentRequestSender         request sender to add a new document
   * @param commitChangeDocRequestSender     request sender to commit changes to a document
   * @param openDocByPdfReaderRequestSender  request sender to open a document in the PDF reader
   */
  @Autowired
  private void registerRequestReceiver(
          RequestSender<Document> documentDetailRequestSender,
          RequestSender<User> getListAllDocumentRequestSender,
          RequestSender<Integer> getTodayDocumentRequestSender,
          RequestSender<SwitchScene> getLastestDocumentsRequestSender,
          RequestSender<Document> getRandomDocumentRequestSender,
          RequestSender<Document> editDocumentRequestSender,
          RequestSender<Document> removeDocumentRequestSender,
          RequestSender<Document> borrowDocumentRequestSender,
          RequestSender<Document> addDocumentRequestSender,
          RequestSender<Document> commitChangeDocRequestSender,
          RequestSender<Document> lendingDetailRequestSender,
          RequestSender<Document> openDocByPdfReaderRequestSender) {
    documentDetailRequestSender.registerReceiver(this::documentDetail);
    getListAllDocumentRequestSender.registerReceiver(this::getListAllDocument);
    editDocumentRequestSender.registerReceiver(this::editDocument);
    removeDocumentRequestSender.registerReceiver(this::removeDocument);
    borrowDocumentRequestSender.registerReceiver(this::borrowDocument);
    addDocumentRequestSender.registerReceiver(this::addDocument);
    getLastestDocumentsRequestSender.registerReceiver(this::getLatestDocByIdDesc);
    getRandomDocumentRequestSender.registerReceiver(this::randomDocument);
    getTodayDocumentRequestSender.registerReceiver(this::getDailyDocument);
    commitChangeDocRequestSender.registerReceiver(this::commitChangeDoc);
    lendingDetailRequestSender.registerReceiver(this::lendingDetail);
    openDocByPdfReaderRequestSender.registerReceiver(this::openDocByPDFReader);
    getListOfOverdueDoc.registerReceiver(this::getListOfOverdueDoc);
  }

  /**
   * Call {@link DocumentRetrievalService} to get list of overdue document
   * and set it for table of {@link DocOverviewController}
   *
   * @param viewController from
   */
  private void getListOfOverdueDoc(ViewController viewController) {
    Alerts.showErrorWithLogger(() -> {
      docOverviewController.setOverdueDocuments(FXCollections.observableList(documentRetrievalService.getOverdueDocuments()));
    }, logger);
  }

  /**
   * Opens a document in the PDF reader.
   * If an error occurs, this method showing alerts
   * and stay in Doc Overview page
   *
   * @param document the document to be opened
   */
  private void openDocByPDFReader(Document document) {
    try {
      pdfReaderController.setDocument(document);
    } catch (Exception e) {
      Alerts.showAlertWarning("Error!!!", e.getMessage());
      docOverviewController.setChosenDoc(null);
      logger.warn(e.getMessage());
    }
  }

  /**
   * Handles the reception of a document detail request and forwards the document
   * to the DocumentDetailController.
   *
   * @param document the document whose details are to be shown
   */
  private void documentDetail(Document document) {
    Alerts.showErrorWithLogger(()->{
      Document temp = documentRetrievalService.getDocumentWithLendingDetails(document.getId());
      documentDetailController.setDocument(temp);
    }, logger);
  }

  /**
   * Returns a list of all documents in the dataset.
   *
   * @param user the user requesting the list (not used)
   */
  private void getListAllDocument(User user) {
    Alerts.showErrorWithLogger(()->{
      docOverviewController.setAllDocuments(FXCollections.observableList(documentRetrievalService.getAllDocuments()));
    }, logger);
  }

  /**
   * Used in "Document detail" to send document information to "Lending detail" page.
   *
   * @param document the document currently shown in "Document detail"
   */
  private void lendingDetail(Document document) {
    lendingDetailController.setDocument(document);
  }

  /**
   * Edits the given document by setting it in the edit document controller.
   *
   * @param document the document to be edited
   */
  private void editDocument(Document document) {
    editDocumentController.setDocument(document);
  }

  /**
   * Removes the given document by deleting it using the document persistence service.
   *
   * @param document the document to be removed
   */
  private void removeDocument(Document document) {
    Alerts.showErrorWithLogger(()->{
      documentPersistenceService.delete(document.getId());
    }, logger);
  }

  /**
   * Borrows the given document.
   *
   * @param document the document to be borrowed
   */
  private void borrowDocument(Document document) {
    lendingDetailController.setDocument(document);
  }

  /**
   * Adds a new document using the document persistence service.
   *
   * @param document the document to be added
   */
  private void addDocument(Document document) {
    if (document == null) {
      logger.warn("Tried to add a NULL Document");
      return;
    }

    try {
      document = documentPersistenceService.add(document);
      addNewDocController.setDocument(document);
      switchToDocOverview.send(null);
    }
    catch (DocumentAlreadyExistException e) {
      Alerts.showAlertWarning("Error!", e.getMessage());
    }
    catch (NotLoggedInException e) {
      /*Should not happen*/
      logger.warn(e.getMessage());
    }
    catch (RuntimeException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Randomly selects a document and sets it in the menu controller.
   *
   * @param d the document (not used)
   */
  private void randomDocument(Document d) {
    Alerts.showErrorWithLogger(()->{
      menuController.setRandomBook(documentRetrievalService.getRandomDocument());
    }, logger);
  }

  /**
   * Sets the latest documents in the menu controller based on ID in descending order.
   *
   * @param switchScene the switch scene
   */
  private void getLatestDocByIdDesc(SwitchScene switchScene) {
    Alerts.showErrorWithLogger(()->{
      menuController.setLatestDocuments(
              FXCollections.observableList(documentRetrievalService.getLatestDocuments())
      );
    }, logger);
  }

  /**
   * used in menu to get a document based on today's date
   */
  private void getDailyDocument(Integer day) {

    List<Document> list = documentRetrievalService.getAllDocuments();
    if (!list.isEmpty()) {
      menuController.setTodayDocument(list.get(day % list.size()));
    }
  }

    private void getDocumentById(Integer id) {
      Alerts.showErrorWithLogger(() -> {
        menuController.setRandomBook(documentRetrievalService.getDocumentById(id));
      }, logger);
    }
  /**
   * Commits changes to the specified document by updating it in the persistence service.
   * If an exception occurs during the update, a warning alert is shown with the error message.
   *
   * @param document the document to be updated
   */
  private void commitChangeDoc(Document document) {
    Alerts.showErrorWithLogger(()->{
      documentPersistenceService.update(document);
    }, logger);
  }
}
