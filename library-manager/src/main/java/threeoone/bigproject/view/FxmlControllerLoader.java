package threeoone.bigproject.view;

import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.viewcontrollers.*;

/**
 * A component responsible for loading the application's FXML files into their {@link ViewController}.
 * This class is a singleton bean in {@code Spring} context.
 *
 * @see ViewController
 * @see FxWeaver
 *
 * @author DUCBRICK
 */
@Component
public class FxmlControllerLoader {
  /**
   * Loads FXML files into {@link ViewController} using {@link FxWeaver}.
   *
   * @param fxWeaver autowired {@link FxWeaver} bean to load FXML files
   */
  public FxmlControllerLoader(FxWeaver fxWeaver) {
      fxWeaver.load(MenuController.class);
      fxWeaver.load(DocumentDetailController.class);
      fxWeaver.load(DocOverviewController.class);
      fxWeaver.load(LoginController.class);
      fxWeaver.load(MenuController.class);
      fxWeaver.load(RegisterController.class);
      fxWeaver.load(AddNewDocController.class);
      fxWeaver.load(MenuBarController.class);
      fxWeaver.load(EditDocumentController.class);
      fxWeaver.load(MemberDetailsController.class);
      fxWeaver.load(MemberListController.class);
      fxWeaver.load(EditMemController.class);
      fxWeaver.load(AddNewMemController.class);
      fxWeaver.load(LendingDetailController.class);
      fxWeaver.load(RecommenderController.class);
      fxWeaver.load(ScanQrController.class);
      fxWeaver.load(PDFReaderController.class);
      fxWeaver.load(ForgetPasswordController.class);
      fxWeaver.load(ResetPasswordView.class);
  }
}
