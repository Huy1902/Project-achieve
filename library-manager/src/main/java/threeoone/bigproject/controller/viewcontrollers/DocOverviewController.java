package threeoone.bigproject.controller.viewcontrollers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.util.MenuItemFactory;

import java.util.List;

/**
 * Controller class for the Document Overview scene.
 * Handles the interaction logic for the document overview view.
 * <p>
 * Provides methods for interacting with the documents table and handling
 * double-click events to view document details.
 * </p>
 *
 * @author HUY1902
 */
@Component
@FxmlView("DocOverview.fxml")
@RequiredArgsConstructor
public class DocOverviewController implements ViewController {
  private final RequestSender<User> getListAllDocumentRequestSender;
  private final RequestSender<Document> updateDocActionRequestSender;
  private final RequestSender<ViewController> switchToEditDoc;
  private final RequestSender<ViewController> switchToDocDetail;
  private final RequestSender<ViewController> switchToMainMenu;
  private final RequestSender<ViewController> switchToAddNewDoc;
  private final RequestSender<ViewController> switchToLendingDetail;
  private final RequestSender<ViewController> switchToPDFReader;

  private final RequestSender<Document> documentDetailRequestSender;
  private final RequestSender<Document> editDocumentRequestSender;
  private final RequestSender<Document> removeDocumentRequestSender;
  private final RequestSender<Document> borrowDocumentRequestSender;
  private final RequestSender<Document> openDocByPdfReaderRequestSender;
  private final RequestSender<ViewController> getListOfOverdueDoc;


  private final MenuBarController menuBarController;

  @FXML
  private Parent root;

  @FXML
  private TableColumn<Document, String> description;

  @FXML
  private TableColumn<Document, Number> number;

  @FXML
  private TableColumn<Document, String> name;

  @FXML
  private TableColumn<Document, String> uploader;

  @FXML
  private TableView<Document> table;

  @FXML
  private Button overdueButton;

  @Setter
  private Document chosenDoc;

  @Setter
  private ObservableList<Document> allDocuments;

  @Setter
  private ObservableList<Document> overdueDocuments;


  /**
   * Initializes the controller. Sets up the row double-click listener for the
   * document table.
   */
  public void initialize() {
    ContextMenu contextMenu = new ContextMenu();
    contextMenu.getItems().add(edit());
    contextMenu.getItems().add(borrow());
    contextMenu.getItems().add(remove());
    contextMenu.getItems().add(openPDF());

    table.setRowFactory(tableview -> {
      TableRow<Document> row = new TableRow<>() {
        @Override
        protected void updateItem(Document item, boolean empty) {
          super.updateItem(item, empty);
          if (item != null && !empty) {
            setContextMenu(contextMenu);
          } else {
            setContextMenu(null);
          }
        }
      };
      //handle click on item
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
          Document document = row.getItem();
          pressDocToGoToDetail(document);
        }
      });
      //handle show context menu
      row.setOnContextMenuRequested(event -> {
        if (!row.isEmpty()) {
          chosenDoc = row.getItem();
          updateDocActionRequestSender.send(chosenDoc);
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
    description.setCellValueFactory(new PropertyValueFactory<>("description"));
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    number.setCellValueFactory(cellData
            -> new SimpleIntegerProperty(table.getItems().indexOf(cellData.getValue()) + 1));
    uploader.setCellValueFactory(cellData
            -> new SimpleStringProperty(cellData.getValue().getUploader().getUsername()));

    menuBarController.highlight(SceneName.DOC_OVERVIEW);
  }

  /**
   * Sets the search result to the list view and displays it.
   *
   * @param result the search result to be displayed
   */
  public void setResult(ObservableList<Document> result) {
    //table.getItems().clear();
    table.getItems().addAll(result);
  }

  /**
   * Create open item for context menu
   *
   * @return openPDF in context menu
   */
  private MenuItem openPDF() {
    return MenuItemFactory.createMenuItem("Open PDF",
            "Open Confirmation", "Are you sure you want to open this document?",
            unused -> {
              openDocByPdfReaderRequestSender.send(chosenDoc);
              if (chosenDoc != null) {
                switchToPDFReader.send(null);
              }
            });
  }

  /**
   * Create borrow item for context menu
   *
   * @return borrow item in context menu
   */
  private MenuItem borrow() {
    return MenuItemFactory.createMenuItem("Borrow",
            "Borrow Confirmation", "Are you sure you want to borrow this document?",
            unused -> {
              switchToLendingDetail.send(null);
              borrowDocumentRequestSender.send(chosenDoc);
            });
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
            "Are you sure you want to remove this document?",
            unused -> {
              removeDocumentRequestSender.send(chosenDoc);
              allDocuments.remove(chosenDoc);
              overdueDocuments.remove(chosenDoc);
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
            "Are you sure you want to edit this document?",
            unused -> {
              editDocumentRequestSender.send(chosenDoc);
              switchToEditDoc.send(null);
            });
  }


  /**
   * Sets the items in the document table with the given list.
   *
   * @param list An observable list of documents to be displayed in the table
   */
  public void setTable(ObservableList<Document> list) {
    table.setItems(list);
  }

  /**
   * Handles the event when a document is double-clicked.
   * Sends the document request and switches the scene to document details view.
   *
   * @param document The document that was double-clicked
   */
  private void pressDocToGoToDetail(Document document) {
    documentDetailRequestSender.send(document);
    switchToDocDetail.send(this);
  }

  @FXML
  private void pressGetOverdue() {
    if (table.getItems() == overdueDocuments) {
      overdueButton.setId("all-doc-redder-button");
      overdueButton.setText("Get Overdue Documents");
      setTable(allDocuments);

    } else {
      overdueButton.setId("overdue-red-button");
      overdueButton.setText("Get All Documents");
      setTable(overdueDocuments);
    }
  }

  /**
   * Switching to Add New Doc page when press Add Doc button
   *
   * @param event event trigger add new doc button
   */
  @FXML
  private void pressAddDoc(ActionEvent event) {
    switchToAddNewDoc.send(this);
  }

  /**
   * Returns the root node of the view controlled by this controller.
   *
   * @return The root node of the view
   */
  @Override
  public Parent getParent() {
    return root;
  }

  /**
   * Notifies the controller that its view is being displayed.
   */
  @Override
  public void show() {
    overdueButton.setId("all-doc-redder-button");
    overdueButton.setText("Get Overdue Documents");
    getListOfOverdueDoc.send(null);
    getListAllDocumentRequestSender.send(null);
    setTable(allDocuments);
  }
}
