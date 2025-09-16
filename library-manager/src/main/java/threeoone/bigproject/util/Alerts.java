package threeoone.bigproject.util;

import jakarta.validation.ConstraintViolationException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.slf4j.Logger;

/**
 * Util class for alerts create or showing
 *
 * @author HUY1902
 */

public class Alerts {
  /**
   * Create new confirmation alert with given title and message,
   * header is set to be empty
   *
   * @param title   alert title
   * @param message alert message
   * @return new alert with given param
   */
  public static Alert alertConfirmation(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    return alert;
  }

  /**
   * Create new information alert with given title and message,
   * header is set to be empty
   *
   * @param title   alert title
   * @param content alert message
   */
  public static void showAlertInfo(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
    alert.close();
  }

  /**
   * Create new warning alert with given title
   *
   * @param title   alert title
   * @param content alert message
   */
  public static void showAlertWarning(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }


  /**
   * Execute function and display appropriate error alert if any error happens
   *
   * @param function {@code Runnable}
   */
  public static void showErrorCauseByFunction(Runnable function) {
    try {
      function.run();
    } catch (ConstraintViolationException exception) {
      Platform.runLater(() -> Alerts.showAlertWarning("Error!", exception.getConstraintViolations().iterator().next().getMessage()));
    } catch (Exception e) {
      Platform.runLater(() -> Alerts.showAlertWarning("Error!", e.getMessage()));
    }
  }

  /**
   * Execute function and display appropriate error alert if any error happens
   *
   * @param logger logging warn
   * @param function {@code Runnable}
   */
  public static void showErrorWithLogger(Runnable function, Logger logger) {
    try {
      function.run();
    } catch (ConstraintViolationException exception) {
      Platform.runLater(() -> Alerts.showAlertWarning("Error!", exception.getConstraintViolations().iterator().next().getMessage()));
      logger.warn( exception.getConstraintViolations().iterator().next().getMessage());
    } catch (Exception e) {
      Platform.runLater(() -> Alerts.showAlertWarning("Error!", e.getMessage()));
      logger.error(e.getMessage());
    }
  }
}
