package threeoone.bigproject.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.viewcontrollers.ViewController;

/**
 * A component responsible for switching between one {@code View} to another.
 * This class is a singleton bean in {@code Spring} context.
 * <p>
 * A {@code View} is an FXML file, which, after being loaded, is represented by a {@link Parent},
 * which is managed by its corresponding {@link ViewController}.
 * <p>
 * {@code ViewSwitcher} requires an instance of {@link Stage} and a first {@link ViewController},
 * which it obtains through {@link #setStage}.
 * The {@link Stage} is the GUI window of the application.
 * The {@link ViewController} contains the first {@code View} to be displayed.
 *
 * @see ViewController
 *
 * @author DUCBRICK
 */
@Component
public class ViewSwitcher {
  private Scene scene;
  /**
   * Configures the {@code ViewSwitcher} by passing an instance of {@link Stage} and {@link ViewController}
   *
   * @param stage the primary {@link Stage} of the application that the {@code ViewSwitcher} manages
   * @param startController the {@link ViewController} that manages the first {@code View} to be displayed
   */
  public void setStage(Stage stage, ViewController startController) {
    Parent root = startController.getParent();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    startController.show();
  }

  /**
   * Switch to another {@code View} and call {@link ViewController#show()}.
   *
   * @param controller the {@link ViewController} containing the {@code View} to switch to
   */
  public void switchToView(ViewController controller) {
    Parent root = controller.getParent();
    scene.setRoot(root);
    controller.show();
  }
}
