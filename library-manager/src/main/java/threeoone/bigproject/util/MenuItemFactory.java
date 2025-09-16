package threeoone.bigproject.util;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;

import java.util.function.Consumer;

/**
 * Class for adjust and auto create MenuItem
 *
 * @author HUY1902
 */
public class MenuItemFactory {

  /**
   * Create menuItem with confirmation alert
   *
   * @param menuItemText text of menu item
   * @param alertTitle   title for alert
   * @param alertContent content of alert
   * @param onOkAction   action if user press OK
   * @return
   */
  public static MenuItem createMenuItem(
          String menuItemText,
          String alertTitle,
          String alertContent,
          Consumer<Void> onOkAction) {
    MenuItem item = new MenuItem(menuItemText);
    item.setOnAction(event -> {
      Alert alert = new Alert(AlertType.CONFIRMATION, alertContent, ButtonType.OK, ButtonType.CANCEL);
      alert.setTitle(alertTitle);
      alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          // Execute the action passed as a parameter
          onOkAction.accept(null);
        }
      });
    });
    return item;
  }
}

