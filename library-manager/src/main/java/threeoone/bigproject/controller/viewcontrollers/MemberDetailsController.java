package threeoone.bigproject.controller.viewcontrollers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.LendingDetail;
import threeoone.bigproject.entities.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * this view present information about a specific member, which include their name,
 *  * address, phone number, email and the books they are currently borrowing.
 * </p>
 * the books which the member is borrowing is displayed in a interactive table
 * user can see documents details or delete the lending (as in, letting a member return a document)
 * TODO: laggy document detail
 */
@Component
@FxmlView("MemberDetails.fxml")
@RequiredArgsConstructor @Setter
public class MemberDetailsController implements ViewController{
    private Member member;

    private final RequestSender<Document> documentDetailRequestSender;
    private final RequestSender<ViewController> switchToDocDetail;
    private final RequestSender<ViewController> switchToEditMem;
    private final RequestSender<Member> editMemberRequestSender;

    /**
     * allows Lending Detail deletion in the database
     */
    private final RequestSender<Integer> deleteLending;

    @FXML
    private Parent root;

    private ContextMenu contextMenu;

    @FXML
    private Label Name;

    @FXML
    private Label Phone;

    @FXML
    private Label Email;

    @FXML
    private Label Address;

    @FXML
    private Button Edit;

    /**
     * the table and its columns
     */
    @FXML
    private TableView<LendingDetail> BorrowingBooks;

    @FXML
    private TableColumn<LendingDetail, String> BorrowTime;

    @FXML
    private TableColumn<LendingDetail, Integer> DocumentID;

    @FXML
    private TableColumn<LendingDetail, String> Title;

    @FXML
    private TableColumn<LendingDetail, String> dueTime;

    /**
     * this list holds LendingDetail from the selected member. updates in this list
     * does NOT affect the database.
     */
    private ObservableList<LendingDetail> lendingDetailObservableList;

    private LendingDetail lendingDetail;


    /**
     * initialize the TableView and the ContextMenu for returning document
     */
    public void initialize() {
        initTable();
        contextMenu = new ContextMenu();
        MenuItem returnDocument = new MenuItem("Return Document");
        contextMenu.getItems().add(returnDocument);

            returnDocument.setOnAction(event -> {
            int ID = lendingDetail.getId();
            deleteLending.send(ID);
            update(lendingDetail);
        });

    }

    /**
     * update the observable list (delete a detail from list)
     * this function only delete from the list that the Member object possesses,
     * it does NOT affect the database.
     * @param detail the LendingDetail in the list to be deleted
     */
    private void update(LendingDetail detail) {
        lendingDetailObservableList.remove(detail);
    }

    /**
     * initializes the columns' property
     * and handles user's interaction with the table (double-clicking on a row sends user to DocumentDetail of the
     * document on that row)
     */
    private void initTable() {
        BorrowingBooks.setRowFactory(tableview -> {
            TableRow<LendingDetail> row = new TableRow<>() {
                @Override
                protected void updateItem(LendingDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && item.isOverdue()) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle("");
                    }
                }
            };
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Document doc = row.getItem().getDocument();
                    gotoDocDetail(doc);
                }
            });
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    lendingDetail = row.getItem();
                }
                else {
                    contextMenu.hide();
                }
            });
            return row;
        });

        BorrowTime.setCellValueFactory(cellData ->{
            LocalDateTime date = cellData.getValue().getLendTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
            return new SimpleStringProperty(date.format(formatter));
        });
        DocumentID.setCellValueFactory(cellData -> {
            Document doc = cellData.getValue().getDocument();
            return new SimpleIntegerProperty(doc.getId()).asObject();
        });
        Title.setCellValueFactory(cellData -> {
            Document doc = cellData.getValue().getDocument();
            return new SimpleStringProperty(doc.getName());
        });
        dueTime.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDueTime();
            return new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")));
        });
    }

    /**
     * used in initTable() to handle changing view to DocDetail
     * @param document the document of the selected row
     */
    private void gotoDocDetail(Document document) {
        documentDetailRequestSender.send(document);
        switchToDocDetail.send(this);
    }

    @FXML
    private void toEditInfo() {
        editMemberRequestSender.send(member);
        switchToEditMem.send(this);
    }

    @Override
    public Parent getParent() {
        return root;
    }

    /**
     * TODO: retrieve lending details of member
     * display member's info and set table's data to member's lending details
     */
    @Override
    public void show() {
        lendingDetailObservableList = FXCollections.observableList(member.getLendingDetails());
        BorrowingBooks.setItems(lendingDetailObservableList);
        Name.setText(member.getName());
        Phone.setText(member.getAddress());
        Address.setText(member.getAddress());
        Email.setText(member.getEmail());
    }
}
