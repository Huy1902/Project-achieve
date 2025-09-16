package threeoone.bigproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import threeoone.bigproject.controller.viewcontrollers.*;
import threeoone.bigproject.view.ViewSwitcher;

/**
 * {@code JavaFx} main class.
 *
 * @author DUCBRICK
 */
public class JavaFxApplication extends Application {
  private ConfigurableApplicationContext context;

  /**
   * Initializes the {@code JavaFx} application.
   */
  @Override
  public void init() throws Exception {
    this.context = new SpringApplicationBuilder()
        .sources(BigprojectApplication.class)
        .run(getParameters().getRaw().toArray(new String[0]));
  }

  /**
   * Starts the {@code JavaFx} application.
   *
   * @param stage the primary {@link Stage} of the {@code JavaFx} application
   *
   * @throws Exception If there is an exception
   */
  @Override
  public void start(Stage stage) throws Exception {
    ViewSwitcher viewSwitcher = context.getBean(ViewSwitcher.class);
    ViewController startController = context.getBean(LoginController.class);
    viewSwitcher.setStage(stage, startController);
  }

  /**
   * Gracefully shut down {@code Spring Boot} along with {@code JavaFx}.
   *
   * @throws Exception If there is an exception
   */
  @Override
  public void stop() throws Exception {
    this.context.close();
    Platform.exit();
  }
}
