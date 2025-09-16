package threeoone.bigproject.controller.viewcontrollers;

import javafx.scene.Parent;
import javafx.scene.Node;

/**
 * A component that handles the internal logic of a {@code View}, which is an FXML file.
 *
 * @author DUCBRICK
 */
public interface ViewController {
  /**
   * Gets the root {@link Node} of the {@code View} managed by the {@link ViewController}.
   *
   * @return the root {@link Node} of the {@code View}
   */
  public Parent getParent();

  /**
   * Notify the {@link ViewController} that its {@code View} is displayed.
   */
  public void show();
}
