package threeoone.bigproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.requestbodies.LendingDetail;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.controller.viewcontrollers.ViewController;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.entities.User;

/**
 * {@code Spring} configuration class.
 *
 * @author DUCBRICK
 */
@Configuration
public class SpringConfig {

  /*************************************************************************
   *  All request sender for login and register
   ***************************************************************************/

  /**
   * Register a {@link RequestSender} of type {@code loginRequest} into {@code Spring} context.
   * Send a LoginRequest which holds {@link User} from 'LoginPage' to service.
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<User> loginRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Request sender for Logout request
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<User> logoutRequestSender() {return new RequestSender<>();}

  /**
   * Register a {@link RequestSender} of type {@code registerRequest} into {@code Spring} context.
   * Send a registerRequest which holds {@link User} from 'RegisterPage' to service.
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<User> registerRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a request to save new lending
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<LendingDetail> saveNewLending() {
    return new RequestSender<>();
  }


  /*************************************************************************
   *  All request sender for document handler: edit, remove, borrow,... (not contain query)
   ***************************************************************************/

  /**
   * Send a request to update available action on a specific document
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<Document> updateDocActionRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a request to get all Document in database
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<User> getListAllDocumentRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a new change on given document request to service
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<Document> commitChangeDocRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Register a {@link RequestSender} of type {@code documentRequest} into {@code Spring} context.
   * Send Document entities {@link Document} from view to another view which need Document.
   * For example, {@link threeoone.bigproject.controller.viewcontrollers.DocumentDetailController}
   * need to know what Document user click on in
   * {@link threeoone.bigproject.controller.viewcontrollers.DocOverviewController} to show them.
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<Document> documentDetailRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a addDocument Request which holds {@link Document} from 'AddNewDocument' page
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<Document> addDocumentRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for sending edit document requests.
   *
   * @return a new {@code RequestSender} instance for {@code Document}
   */
  @Bean
  public RequestSender<Document> editDocumentRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for sending remove document requests.
   *
   * @return a new {@code RequestSender} instance for {@code Document}
   */
  @Bean
  public RequestSender<Document> removeDocumentRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for sending borrow document requests.
   *
   * @return a new {@code RequestSender} instance for {@code Document}
   */
  @Bean
  public RequestSender<Document> borrowDocumentRequestSender() {
    return new RequestSender<>();
  }


  /**
   * Creates a bean for opening document by pdf reader requests.
   *
   * @return a new {@code RequestSender} instance for {@code Document}
   */
  @Bean
  public RequestSender<Document> openDocByPdfReaderRequestSender() {
    return new RequestSender<>();
  }


  /**
   * Sends a RequestSender that has the document from "Document detail" specifically.
   */
  @Bean
  public RequestSender<Document> lendingDetailRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a request to service to take all overdue book and set it back to table in {@link threeoone.bigproject.controller.viewcontrollers.DocOverviewController}
   *
   * @return corresponding {@link RequestSender}
   */
  @Bean
  public RequestSender<ViewController> getListOfOverdueDoc() {
    return new RequestSender<>();
  }

  /*************************************************************************
   *  All request sender for member handler: edit, remove, add, ... (not contains query)
   ***************************************************************************/

  /**
   * Send a request to service to get all member
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<Member> getAllMembersRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for sending edit member requests.
   *
   * @return a new {@code RequestSender} instance for {@code Member}
   */
  @Bean
  public RequestSender<Member> editMemberRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for sending commit change member requests.
   *
   * @return a new {@code RequestSender} instance for {@code Member}
   */
  @Bean
  public RequestSender<Member> commitChangeMemberRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for sending remove member requests.
   *
   * @return a new {@code RequestSender} instance for {@code Member}
   */
  @Bean
  public RequestSender<Member> removeMemberRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for sending add member requests.
   *
   * @return a new {@code RequestSender} instance for {@code Member}
   */
  @Bean
  public RequestSender<Member> addMemberRequestSender() {
    return new RequestSender<>();
  }

  /**
   * sends a member entity to a view that needs it.
   *
   * @return a new {@code RequestSender} instance for {@code Member}
   * repeating code is a sign of code smell
   */
  @Bean
  public RequestSender<Member> memberDetailsRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Call service {@link threeoone.bigproject.services.retrieval.MemberRetrievalService} to get list
   * of members who have overdue documents (at least 1) and set it for table in {@link threeoone.bigproject.controller.viewcontrollers.MemberListController}
   *
   * @return {@link RequestSender} with method registered in {@link threeoone.bigproject.controller.controllers.ActionOnMemController}
   */
  @Bean
  public RequestSender<ViewController> getOverdueMember() {
    return new RequestSender<>();
  }

  @Bean
  RequestSender<SwitchScene> getTopFiveMembersRequestSender() {
    return new RequestSender<>();
  }

  @Bean
  RequestSender<SwitchScene> getLastestDocumentsRequestSender() {
    return new RequestSender<>();
  }

  @Bean
  RequestSender<Document> getRandomDocumentRequestSender() {
    return new RequestSender<>();
  }


  /*************************************************************************
   *  All request sender for document query: by isbn, by name, by id, by author, by category
   ***************************************************************************/

  /**
   * Send ISBN to GoogleAPI service search by ISBN
   *
   * @return the {@link RequestSender}to be registered
   */
  @Bean
  public RequestSender<String> queryISBNGoogleRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a query request by document name to service
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<String> queryDocByNameRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a query request by document author to service
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<String> queryDocByAuthorRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a query request by document category to service
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<String> queryDocByCategoryRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a query request by document id to service
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<Integer> queryDocByIdRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Send a query request to recommendation system
   *
   * @return the {@link RequestSender} to be registered
   */
  @Bean
  public RequestSender<String> queryRecommendDocRequestSender() {
    return new RequestSender<>();
  }

  // Hey bro that getDocumentByID you use so idk, i hold it back in case you need to use that

  /**
   * get a document of the ID sent
   * TODO: EVIL DOPPELGANGER ABOVE ME, DO SOMETHING ABOUT IT.
   *
   * @return
   */
  @Bean
  public RequestSender<Integer> getTodayDocumentRequestSender() {
    return new RequestSender<>();
  }

  /*************************************************************************
   *  All request sender for member query: by id, by name
   ***************************************************************************/

  /**
   * Creates and returns a bean for RequestSender that handles queries for member information by name.
   *
   * @return a new instance of RequestSender for queries by member name.
   */
  @Bean
  public RequestSender<String> queryMemByNameRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates and returns a bean for RequestSender that handles queries for member information by ID.
   *
   * @return a new instance of RequestSender for queries by member ID.
   */
  @Bean
  public RequestSender<Integer> queryMemByIdRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Creates and returns a bean for RequestSender that handles queries for member information by ID
   * from Lending Detail Page.
   *
   * @return a new instance of RequestSender for queries by member ID.
   */
  @Bean
  public RequestSender<Integer> queryMemByIdFromLendingRequestSender() {
    return new RequestSender<>();
  }

  /*************************************************************************
   *  All request sender for switch scene
   ***************************************************************************/

  /**
   * Creates a bean for RequestSender that switches to the document overview scene.
   *
   * @return an instance of RequestSender for switching to the document overview scene.
   */
  @Bean
  public RequestSender<ViewController> switchToDocOverview() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the main menu scene.
   *
   * @return an instance of RequestSender for switching to the main menu scene.
   */
  @Bean
  public RequestSender<ViewController> switchToMainMenu() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the document detail scene.
   *
   * @return an instance of RequestSender for switching to the document detail scene.
   */
  @Bean
  public RequestSender<ViewController> switchToDocDetail() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the login page.
   *
   * @return an instance of RequestSender for switching to the login page.
   */
  @Bean
  public RequestSender<ViewController> switchToLogin() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the signup page.
   *
   * @return an instance of RequestSender for switching to the signup page.
   */
  @Bean
  public RequestSender<ViewController> switchToSignup() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the add new document page.
   *
   * @return an instance of RequestSender for switching to the add new document page.
   */
  @Bean
  public RequestSender<ViewController> switchToAddNewDoc() {
    return new RequestSender<>();
  }


  /**
   * Creates a bean for RequestSender that switches to the edit document page.
   *
   * @return an instance of RequestSender for switching to the edit document page.
   */
  @Bean
  public RequestSender<ViewController> switchToEditDoc() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the member list page.
   *
   * @return an instance of RequestSender for switching to the member list page.
   */
  @Bean
  public RequestSender<ViewController> switchToMemList() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the edit member information page.
   *
   * @return an instance of RequestSender for switching to the edit member information page.
   */
  @Bean
  public RequestSender<ViewController> switchToEditMem() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the add new member page.
   *
   * @return an instance of RequestSender for switching to the add new member page.
   */
  @Bean
  public RequestSender<ViewController> switchToAddMem() {
    return new RequestSender<>();
  }


  /**
   * Creates a bean for RequestSender that switches to the lending detail view.
   *
   * @return an instance of RequestSender for switching to the lending detail view.
   */
  @Bean
  public RequestSender<ViewController> switchToLendingDetail() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the PDF Reader view.
   *
   * @return an instance of RequestSender for switching to the PDF Reader view.
   */
  @Bean
  public RequestSender<ViewController> switchToPDFReader() {
    return new RequestSender<>();
  }

  /**
   * same as the one above me <3
   *
   * @return an instance of SAME AS THE ONE ABOVE ME BUT MEMBER DETAIL.
   */
  @Bean
  public RequestSender<ViewController> switchToMemberDetails() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to the Forget Password view.
   *
   * @return an instance of RequestSender for switching to the Forget Password view.
   */
  @Bean
  public RequestSender<ViewController> switchToForgetPassword() {
    return new RequestSender<>();
  }

  /**
   * Creates a bean for RequestSender that switches to Scan QR view.
   *
   * @return an instance of RequestSender for switching to the Scan QR view.
   */
  @Bean
  public RequestSender<ViewController> switchToScanQR() {
    return new RequestSender<>();
  }

  /**
   * Requests to redirect the user to the password-reset page.
   * <p>
   * This type of request contains a single parameter of type {@link User}.
   * <p>
   * Receiver of this type of request should redirect the application to the page in which
   * the provided {@link User} can reset their password.
   *
   * @return the {@link RequestSender} of the described request type
   */
  @Bean
  public RequestSender<User> redirectToPasswordResetPageRequestSender() {
    return new RequestSender<>();
  }


  /**
   * Requests to reset the {@code password} of a specific {@link User}.
   * <p>
   * This type of request contains a single parameter of type {@link User}.
   * <p>
   * Receiver of this type of request should update the data of the {@link User}
   * entity instance parameter to the Database.
   * Specifically, the {@link User} parameter should be a valid detached entity,
   * whose data is the same as a managed entity in the JPA context,
   * except the {@code password} field, which carries the new password to be updated.
   *
   * @return the {@link RequestSender} of the described request type
   */
  @Bean
  public RequestSender<User> resetPasswordRequestSender() {
    return new RequestSender<>();
  }

  /**
   * Returns a requestSender that request a deletion of the lending detail
   * with the provided integer as it ID.
   */
  @Bean
  public RequestSender<Integer> deleteLending() {
    return new RequestSender<>();
  }

  /**
   * Return a RequestSender.
   */
  @Bean
  public RequestSender<String> sendResetLinkRequestSender() {
    return new RequestSender<>();
  }

  /*************************************************************************
   *  All request sender for QR
   ***************************************************************************/

  /**
   * Return a Request Sender that queries qr image from server and set back to View Controller requests it
   *
   * @return the {@link RequestSender}
   */
  @Bean
  public RequestSender<ViewController> getQRImageFromServer() {
    return new RequestSender<>();
  }

  /**
   * Return ISBN which is scanned from QR Code and set it for add new document page
   *
   * @return the {@link RequestSender
   * }
   */
  @Bean
  public RequestSender<String> setISBNToAddNewPage() {
    return new RequestSender<>();
  }
}