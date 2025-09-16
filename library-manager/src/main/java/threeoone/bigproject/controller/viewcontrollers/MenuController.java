package threeoone.bigproject.controller.viewcontrollers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.LendingDetail;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author purupurupkl
 * main menu (so-called Dashboard). shows up after logging in
 * shows current date - time, one table for newest uploaded documents, one for members info and one for latest book issues
 * (which is not even working rn).
 * also has a funny random button that sends you to a random document's detail page.
 */
@Component
@FxmlView("Menu.fxml")
@RequiredArgsConstructor
public class MenuController implements ViewController {
    private final RequestSender<ViewController> switchToDocDetail;
    private final RequestSender<Document> getRandomDocumentRequestSender;
    private final RequestSender<Integer> getTodayDocumentRequestSender;
    private final RequestSender<Document> documentDetailRequestSender;
    private final RequestSender<SwitchScene> getTopFiveMembersRequestSender;
    private final RequestSender<SwitchScene> getLastestDocumentsRequestSender;
    private final MenuBarController menuBarController;

    @FXML
    private Parent root;


    @FXML
    private Label Greeting;

    @FXML
    private Label time;


    @FXML
    private Button Random;

    /**
     * Member list table. Show 5 member of smallest ID.
     */
    @FXML
    private TableView<Member> MemberList;
    @FXML
    private TableColumn<Member, String> MemberID;
    @FXML
    private TableColumn<Member, String> MemberName;

    /**
     * Latest document table.
     */
    @FXML
    private TableView<Document> LatestDocuments;
    @FXML
    private TableColumn<Document, String> BookID;
    @FXML
    private TableColumn<Document, String> Title;
    @FXML
    private TableColumn<Document, String> Author;
    @FXML
    private TableColumn<Document, String> CopiesAvailable;
    @FXML
    private TableColumn<Document, String> Description;

    private Document randomDocument;

    @Setter
    @Getter
    private Document TodayDocument;

    @FXML
    private ImageView TodayCover;

    @FXML
    private Label TodayDescription;

    @FXML
    private Label TodayTitle;

    @Override
    public Parent getParent() {
        return root;
    }

    /**
     * initializes the table and sets up a Timeline for the clock
     */
    public void initialize() {
        MemberID.setCellValueFactory(new PropertyValueFactory<>("id"));
        MemberName.setCellValueFactory(new PropertyValueFactory<>("name"));

        BookID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Title.setCellValueFactory(new PropertyValueFactory<>("name"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Author.setCellValueFactory(new PropertyValueFactory<>("author"));
        CopiesAvailable.setCellValueFactory(new PropertyValueFactory<>("copies"));

        menuBarController.highlight(SceneName.MAIN_MENU);
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss");
                    time.setText(LocalDateTime.now().format(formatter));
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            });
        });
        thread.start();
    }


    public void setUserList(ObservableList<Member> memberList) {
        MemberList.setItems(memberList);

    }

    public void setLatestDocuments(ObservableList<Document> latestDocuments) {
        LatestDocuments.setItems(latestDocuments);
    }

    /**
     * setter for the random book.
     */
    public void setRandomBook(Document document) {
        randomDocument = document;
    }

    /**
     * set the greeting based on current time.
     * @return a string as the greeting
     */
    private String getGreeting() {
        LocalTime now = LocalTime.now();

        if(now.isBefore(LocalTime.NOON)) {
            return "Good morning!";
        } else if (now.isBefore(LocalTime.of(18,0))) {
            return "Good afternoon!";
        } else if (now.isBefore(LocalTime.of(22,0))) {
            return "Good evening!";
        } else {
            return "Good night!\nGet some sleep :)";
        }
    }

    /**
     * sends user to a random document's detail page.
     */
    @FXML
    public void randomBook() {
        getRandomDocumentRequestSender.send(null);
        if(randomDocument != null) {
            documentDetailRequestSender.send(randomDocument);
            switchToDocDetail.send(this);
        }
    }

    /**
     * switch to document detail page to see today's document's details.
     */
    @FXML
    private void todayDocumentDetail() {
        documentDetailRequestSender.send(TodayDocument);
        switchToDocDetail.send(this);
    }

    /**
     * sets greeting based on time and set data for tables.
     */
    @Override
    public void show() {
        getTopFiveMembersRequestSender.send(new SwitchScene(SceneName.MAIN_MENU));
        getLastestDocumentsRequestSender.send(new SwitchScene(SceneName.MAIN_MENU));
        Greeting.setText(getGreeting());
        setTodayDocumentDetail();
    }

    /**
     * show today's document's detail.
     * if there's no document, do nothing (default text in fxml will show instead).
     */
    public void setTodayDocumentDetail() {
        getTodayDocumentRequestSender.send(LocalDate.now().getDayOfYear());
        if (TodayDocument != null) {
            TodayTitle.setText(TodayDocument.getName());
            TodayDescription.setText(TodayDocument.getDescription());


            Thread thread = new Thread(() -> Platform.runLater(() ->{
                TodayCover.setImage(new Image(TodayDocument.getCoverImageUrl()));
            }));
            thread.start();
        }
    }
}
