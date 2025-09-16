package threeoone.bigproject.controller.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.viewcontrollers.*;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.services.BookRecommendService;
import threeoone.bigproject.services.retrieval.DocumentRetrievalService;
import threeoone.bigproject.services.GoogleAPIService;
import threeoone.bigproject.services.retrieval.MemberRetrievalService;
import threeoone.bigproject.util.Alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controller class for handling queries (contain {@link Document} and {@link  Member})
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class QueryController {
  private final Logger logger = LoggerFactory.getLogger(QueryController.class);

  private final AddNewDocController addNewDocController;
  private final RecommenderController recommenderController;
  private final GoogleAPIService googleAPIService;
  private final MemberRetrievalService memberRetrievalService;
  private final MemberSearchBarController memberSearchBarController;
  private final DocumentRetrievalService documentRetrievalService;
  private final LendingDetailController lendingDetailController;
  private final BookRecommendService bookRecommendService;
  private final DocOverviewController docOverviewController;
  private final MemberListController memberListController;

  /**
   * Registers the request sender for handling ISBN queries.
   */
  @Autowired
  public void registerRequestSender(RequestSender<String> queryISBNGoogleRequestSender,
                                    RequestSender<String> queryDocByNameRequestSender,
                                    RequestSender<String> queryDocByAuthorRequestSender,
                                    RequestSender<String> queryDocByCategoryRequestSender,
                                    RequestSender<Integer> queryDocByIdRequestSender,
                                    RequestSender<String> queryMemByNameRequestSender,
                                    RequestSender<Integer> queryMemByIdRequestSender,
                                    RequestSender<Integer> queryMemByIdFromLendingRequestSender,
                                    RequestSender<String> queryRecommendDocRequestSender) {
    queryISBNGoogleRequestSender.registerReceiver(this::getQueryAndFulFill);
    queryDocByIdRequestSender.registerReceiver(this::queryDocByID);
    queryDocByNameRequestSender.registerReceiver(this::queryDocByName);
    queryDocByAuthorRequestSender.registerReceiver(this::queryDocByAuthor);
    queryDocByCategoryRequestSender.registerReceiver(this::queryDocByCategory);
    queryMemByNameRequestSender.registerReceiver(this::queryMemByName);
    queryMemByIdRequestSender.registerReceiver(this::queryMemById);
    queryMemByIdFromLendingRequestSender.registerReceiver(this::queryMemByIdFromLending);
    queryRecommendDocRequestSender.registerReceiver(this::queryRecommendDoc);
  }

  /**
   * Queries through {@link GoogleAPIService} then gives information about the book back to {@link AddNewDocController}.
   *
   * @param isbn the ISBN to query
   */
  public void getQueryAndFulFill(String isbn) {
    AtomicReference<Document> result = new AtomicReference<>();
    Alerts.showErrorWithLogger(() -> {
      result.set(googleAPIService.findBookByISBN(isbn));
    }, logger);
    if (result.get() == null) {
      result.set(new Document());
    }
    Platform.runLater(() -> addNewDocController.fulfillInfo(result.get()));
  }

  /**
   * Queries a member by their ID and sets the result in the member search bar controller.
   *
   * @param id the ID of the member to be queried
   */
  private void queryMemById(Integer id) {
    Member member = memberRetrievalService.findById(id);
    List<Member> result = new ArrayList<>();
    if(member != null) {
      result.add(member);
    }
    memberListController.setTable(FXCollections.observableArrayList(result));
  }

  /**
   * Queries members by their name and sets the results in the member search bar controller.
   *
   * @param name the name of the member(s) to be queried
   */
  private void queryMemByName(String name) {
    List<Member> result = memberRetrievalService.findWhoseNamesContain(name);
    memberListController.setTable(FXCollections.observableArrayList(result));
  }


  /**
   * Queries a document by its ID and sets it in the menu controller.
   *
   * @param id the ID of the document to be queried
   */
  private void queryDocByID(Integer id) {
    Document result = documentRetrievalService.getDocumentById(id);
    docOverviewController.setResult(FXCollections.observableArrayList(result));
  }

  /**
   * Queries a document by its name.
   *
   * @param name the name of the document to be queried
   */
  private void queryDocByName(String name) {
    List<Document> result = documentRetrievalService.searchByName(name);
    docOverviewController.setResult(FXCollections.observableArrayList(result));
  }

  /**
   * Queries a document by its author.
   *
   * @param author the author of the document to be queried
   */
  private void queryDocByAuthor(String author) {
    List<Document> result = documentRetrievalService.searchByAuthor(author);
    docOverviewController.setResult(FXCollections.observableArrayList(result));
  }

  /**
   * Queries a document by its category.
   *
   * @param category the category of the document to be queried
   */
  private void queryDocByCategory(String category) {
    List<Document> result = documentRetrievalService.searchByCategory(category);
    docOverviewController.setResult(FXCollections.observableArrayList(result));
  }

  /**
   * Queries recommend document for a document with title
   * Show alert if no recommend for that document
   *
   * @param title the title of the document need to get recommend
   */
  private void queryRecommendDoc(String title) {
    List<String> result = bookRecommendService.getRecommendedBooks(title);
    if (result.isEmpty()) {
      Platform.runLater(() -> Alerts.showAlertWarning("No information!", "No recommend for that document"));
      return;
    }
    recommenderController.setResult(FXCollections.observableArrayList(result));
  }

  private void queryMemByIdFromLending(Integer id) {
    Alerts.showErrorWithLogger(() -> {
      Member member = memberRetrievalService.findById(id);
      lendingDetailController.setMember(member);
    }, logger);
  }
}
