package threeoone.bigproject.controller.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.controller.viewcontrollers.*;
import threeoone.bigproject.controller.viewcontrollers.ForgetPasswordController;
import threeoone.bigproject.view.ViewSwitcher;

/**
 * Main controller responsible for managing scene switching.
 * This controller is a Spring Component and is injected with necessary dependencies.
 * It registers to receive specific types of requests and handles them accordingly.
 * This is Controller version to use instead of DemoController before
 * It is constructed with many view controller.
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class SwitchViewController {
  private final ViewSwitcher viewSwitcher;

  private final ForgetPasswordController forgetPasswordController;
  private final RegisterController registerController;
  private final LoginController loginController;
  private final MenuController menuController;

  private final DocOverviewController docOverviewController;
  private final DocumentDetailController documentDetailController;
  private final AddNewDocController addNewDocController;
  private final EditDocumentController editDocumentController;

  private final MemberListController memberListController;
  private final EditMemController editMemController;
  private final AddNewMemController addNewMemController;
  private final MemberDetailsController memberDetailsController;

  private final LendingDetailController lendingDetailController;
  private final PDFReaderController pdfReaderController;

  private final ScanQrController scanQrController;

  /**
   * Registers request receivers for switching to different scenes.
   *
   * @param switchToDocOverview   RequestSender for switching to document overview scene.
   * @param switchToMainMenu      RequestSender for switching to main menu scene.
   * @param switchToDocDetail     RequestSender for switching to document detail scene.
   * @param switchToLogin         RequestSender for switching to login scene.
   * @param switchToSignup        RequestSender for switching to signup scene.
   * @param switchToAddNewDoc     RequestSender for switching to add new document scene.
   * @param switchToEditDoc       RequestSender for switching to edit document scene.
   * @param switchToMemList       RequestSender for switching to member list scene.
   * @param switchToEditMem       RequestSender for switching to edit member scene.
   * @param switchToAddMem        RequestSender for switching to add new member scene.
   * @param switchToLendingDetail RequestSender for switching to lending detail scene.
   */
  @Autowired
  private void registerRequestReceiver(
          RequestSender<ViewController> switchToDocOverview,
          RequestSender<ViewController> switchToDocDetail,
          RequestSender<ViewController> switchToAddNewDoc,
          RequestSender<ViewController> switchToEditDoc,

          RequestSender<ViewController> switchToMainMenu,
          RequestSender<ViewController> switchToLogin,
          RequestSender<ViewController> switchToSignup,
          RequestSender<ViewController> switchToForgetPassword,
          RequestSender<ViewController> switchToPDFReader,

          RequestSender<ViewController> switchToMemberDetails,
          RequestSender<ViewController> switchToMemList,
          RequestSender<ViewController> switchToEditMem,
          RequestSender<ViewController> switchToAddMem,

          RequestSender<ViewController> switchToLendingDetail,

          RequestSender<ViewController> switchToScanQR
  ) {
    switchToDocOverview.registerReceiver(this::switchToDocOverview);
    switchToMainMenu.registerReceiver(this::switchToMainMenu);
    switchToDocDetail.registerReceiver(this::switchToDocDetail);
    switchToLogin.registerReceiver(this::switchToLogin);
    switchToSignup.registerReceiver(this::switchToSignup);
    switchToAddNewDoc.registerReceiver(this::switchToAddNewDoc);
    switchToEditDoc.registerReceiver(this::switchToEditDoc);
    switchToMemList.registerReceiver(this::switchToMemList);
    switchToEditMem.registerReceiver(this::switchToEditMem);
    switchToAddMem.registerReceiver(this::switchToAddMem);
    switchToLendingDetail.registerReceiver(this::switchToLendingDetail);
    switchToMemberDetails.registerReceiver(this::switchToMemberDetails);
    switchToPDFReader.registerReceiver(this::switchToPDFReader);
    switchToForgetPassword.registerReceiver(this::switchToForgetPassword);
    switchToScanQR.registerReceiver(this::switchToScanQR);
  }

  /**
   * Switches to the document overview view.
   *
   * @param from the current view controller.
   */
  private void switchToDocOverview(ViewController from) {
    viewSwitcher.switchToView(docOverviewController);
  }

  /**
   * Switches to the main menu view.
   *
   * @param from the current view controller.
   */
  private void switchToMainMenu(ViewController from) {
    viewSwitcher.switchToView(menuController);
  }

  /**
   * Switches to the document detail view.
   *
   * @param from the current view controller.
   */
  private void switchToDocDetail(ViewController from) {
    viewSwitcher.switchToView(documentDetailController);
  }

  /**
   * Switches to the login view.
   *
   * @param from the current view controller.
   */
  private void switchToLogin(ViewController from) {
    viewSwitcher.switchToView(loginController);
  }

  /**
   * Switches to the signup view.
   *
   * @param from the current view controller.
   */
  private void switchToSignup(ViewController from) {
    viewSwitcher.switchToView(registerController);
  }

  /**
   * Switches to the add new document view.
   *
   * @param from the current view controller.
   */
  private void switchToAddNewDoc(ViewController from) {
    viewSwitcher.switchToView(addNewDocController);
  }

  /**
   * Switches to the edit document view.
   *
   * @param from the current view controller.
   */
  private void switchToEditDoc(ViewController from) {
    viewSwitcher.switchToView(editDocumentController);
  }

  /**
   * Switches to the member list view.
   *
   * @param from the current view controller.
   */
  private void switchToMemList(ViewController from) {
    viewSwitcher.switchToView(memberListController);
  }

  /**
   * same as the two above and under me :)
   */
  private void switchToMemberDetails(ViewController from) {
    viewSwitcher.switchToView(memberDetailsController);
  }

  /**
   * Switches to the add new member view.
   *
   * @param from the current view controller.
   */
  private void switchToAddMem(ViewController from) {
    viewSwitcher.switchToView(addNewMemController);
  }

  /**
   * Switches to the edit member view.
   *
   * @param from the current view controller.
   */
  private void switchToEditMem(ViewController from) {
    viewSwitcher.switchToView(editMemController);
  }

  /**
   * Switches to the lending detail view.
   *
   * @param from the current view controller.
   */
  private void switchToLendingDetail(ViewController from) {
    viewSwitcher.switchToView(lendingDetailController);
  }

  /**
   * Switches to the PDF Reader view.
   *
   * @param from the current view controller.
   */
  private void switchToPDFReader(ViewController from) {
    viewSwitcher.switchToView(pdfReaderController);
  }

  private void switchToForgetPassword(ViewController from) {
    viewSwitcher.switchToView(forgetPasswordController);
  }

  private void switchToScanQR(ViewController from) {
    viewSwitcher.switchToView(scanQrController);
  }
}