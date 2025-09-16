package threeoone.bigproject.controller.viewcontrollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.util.MenuItemFactory;

import java.util.List;

/**
 * Class for handle Member List page
 *
 * @author HUY1902
 */
@Component
@FxmlView("MemberList.fxml")
@RequiredArgsConstructor
public class
MemberListController implements ViewController {
  private final RequestSender<ViewController> switchToEditMem;
  private final RequestSender<ViewController> switchToAddMem;
  private final RequestSender<ViewController> switchToMainMenu;
  private final RequestSender<ViewController> switchToMemberDetails;

  private final RequestSender<Member> getAllMembersRequestSender;
  private final RequestSender<Member> editMemberRequestSender;
  private final RequestSender<Member> removeMemberRequestSender;
  private final RequestSender<Member> memberDetailsRequestSender;
  private final RequestSender<ViewController> getOverdueMember;

  private final MenuBarController menuBarController;

  @FXML
  private TableColumn<Member, String> address;

  @FXML
  private TableColumn<Member, String> email;

  @FXML
  private TableColumn<Member, String> id;

  @FXML
  private TableColumn<Member, String> name;

  @FXML
  private TableColumn<Member, String> phone;

  @FXML
  private SplitPane root;

  @FXML
  private TableView<Member> table;

  @FXML
  private Button overdueButton;

  private Member chosenMember;

  @Setter @Getter
  private ObservableList<Member> allMembers;

  @Setter
  private ObservableList<Member> overdueMembers;

  /**
   * Initialized method for FXML page
   */

  @FXML
  private void initialize() {
    ContextMenu contextMenu = new ContextMenu();
    contextMenu.getItems().add(remove());
    contextMenu.getItems().add(edit());

    table.setRowFactory(tableview -> {
      TableRow<Member> row = new TableRow<>() {
        @Override
        protected void updateItem(Member item, boolean empty) {
          super.updateItem(item, empty);
          if(item != null && !empty) {
            setContextMenu(contextMenu);
          }
          else {
            setContextMenu(null);
          }
        }
      };
      //handle click on item
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
          Member member = row.getItem();
          seeMemberDetail(member);
        }
      });
      //handle show context menu
      row.setOnContextMenuRequested(event -> {
        if (!row.isEmpty()) {
          chosenMember = row.getItem();
          contextMenu.show(row, event.getScreenX(), event.getScreenY());
        }
      });
      //handle key press to show menu at true location
      row.setOnKeyPressed(event -> {
        if (event.getCode() == KeyCode.CONTEXT_MENU && !row.isEmpty()) {
          contextMenu.show(row, row.getScene().getWindow().getX() + row.getLayoutX() + row.getTranslateX(),
                  row.getScene().getWindow().getY() + row.getLayoutY() + row.getTranslateY());
        }
      });
      return row;
    });
    id.setCellValueFactory(new PropertyValueFactory<>("id"));
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    email.setCellValueFactory(new PropertyValueFactory<>("email"));
    phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    address.setCellValueFactory(new PropertyValueFactory<>("address"));

    menuBarController.highlight(SceneName.MEM_LIST);
  }

  /**
   * Creates a menu item for removing an item.
   * Displays a confirmation dialog when the menu item is selected.
   *
   * @return the menu item for removing an item
   */
  private MenuItem remove() {
    return MenuItemFactory.createMenuItem("Remove",
            "Remove Confirmation",
            "Are you sure you want to remove this member?",
            unused -> {
              removeMemberRequestSender.send(chosenMember);
              table.getItems().remove(chosenMember);
              //getAllMembersRequestSender.send(null);
              //getOverdueMember.send(null);
              //setTable(allMembers);
            });
  }

  /**
   * Creates a menu item for removing an item.
   * Displays a confirmation dialog when the menu item is selected.
   *
   * @return the menu item for removing an item
   */
  private MenuItem edit() {
    return MenuItemFactory.createMenuItem("Edit",
            "Edit Confirmation",
            "Are you sure you want to edit this member?",
            unused -> {
              editMemberRequestSender.send(chosenMember);
              switchToEditMem.send(this);
            });
  }

  /**
   * Handler for add button. Go to add new member page
   *
   * @param event event trigger button
   */
  @FXML
  private void pressAdd(ActionEvent event) {
    switchToAddMem.send(this);
  }

  @FXML
  private void pressGetOverdue(ActionEvent event) {
    if (table.getItems() == allMembers) {
      setTable(overdueMembers);
      overdueButton.setText("All Members");
    }
    else {
      setTable(allMembers);
      overdueButton.setText("Members With Overdue Document");
    }
  }

  /**
   * sends selected member to member detail page (to show it, obviously)
   *
   * @param member the member selected (from the table)
   */
  private void seeMemberDetail(Member member) {
    memberDetailsRequestSender.send(member);
    switchToMemberDetails.send(this);
  }

  /**
   * Sets the items in the {@link #table} with the given member list.
   * String arguments like: id,  name in `PropertyValueFactory` is attribute of {@link Member}
   *
   * @param memberObservableList An observable list of members to be displayed in the table
   */
  public void setTable(ObservableList<Member> memberObservableList) {
    //table.getItems().clear();
    table.setItems(memberObservableList);
  }

  /**
   * Gets the root {@link #root} of the {@code View} managed by the {@link ViewController}.
   *
   * @return the root {@link #root} of the {@code View}
   */
  @Override
  public Parent getParent() {
    return root;
  }

  /**
   * Notify the {@link ViewController} that its {@code View} is displayed.
   */
  @Override
  public void show() {
    overdueButton.setText("Members With Overdue Document");
    getOverdueMember.send(null);
    getAllMembersRequestSender.send(null);
    setTable(allMembers);
  }
}
