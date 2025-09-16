package threeoone.bigproject.controller.viewcontrollers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.LendingDetail;
import threeoone.bigproject.entities.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This controller manages the "Document detail" view.
 * This view presents the information of a book, currently including:
 *      The cover of the book (if it exists)
 *      The name and author of the book
 *      An overview about the book's content (the description)
 *
 */
@Component
@FxmlView("DocDetail.fxml")
@RequiredArgsConstructor
public class DocumentDetailController implements ViewController {
    private final RequestSender<ViewController> switchToPDFReader;
    private final RequestSender<ViewController> switchToLendingDetail;
    private final RequestSender<ViewController> switchToEditDoc;

    private final RequestSender<Document> lendingDetailRequestSender;
    private final RequestSender<Document> openDocByPdfReaderRequestSender;
    private final RequestSender<Document> editDocumentRequestSender;

    private Document document;

    @FXML
    private Parent root;

    @FXML
    private ImageView cover;

    @FXML
    private Label bookName;

    @FXML
    private Label author;

    @FXML
    private Label category;

    @FXML
    private TextArea bookDescription;

    @FXML
    private Label uploader;

    @FXML
    private Label ISBN;

    @FXML
    private Label uploadedDate;

    @FXML
    private Label copies;

    @FXML
    private Label available;

    @FXML
    private Button borrow;

    @FXML
    private Button ReadPDF;

    @FXML
    private Button editDocument;

    @FXML
    private TableView<LendingDetail> lendings;

    @FXML
    private TableColumn<LendingDetail, String> MemberID;

    @FXML
    private TableColumn<LendingDetail, String> MemberName;

    @FXML
    private TableColumn<LendingDetail, String> BorrowDate;

    @FXML
    private TableColumn<LendingDetail, String> ReturnDate;

    public Parent getParent() {
        return root;
    }

    public void initialize() {
        lendings.setRowFactory(tableview -> {
            TableRow<LendingDetail> row = new TableRow<>() {
                @Override
                protected void updateItem(LendingDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && item.isOverdue()) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle(null);
                    }
                }
            };
            return row;
        });
        MemberID.setCellValueFactory(data -> {
            Member member = data.getValue().getMember();
            return new SimpleStringProperty(member.getId().toString());
        });

        MemberName.setCellValueFactory(cellData -> {
            Member member = cellData.getValue().getMember();
            return new SimpleStringProperty(member.getName());
        });

        BorrowDate.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss");
            LocalDateTime date = cellData.getValue().getLendTime();
            return new SimpleStringProperty(date.format(formatter));
        });

        ReturnDate.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime date = cellData.getValue().getDueTime();
            return new SimpleStringProperty(date.format(formatter));
        });

    }

    /**
     * set the private {@code Document} ref to the Document sent by the {@code RequestSender}
     * @param document the Document sent
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    @FXML
    private void toBorrow() {
        switchToLendingDetail.send(null); //don't need this data
        lendingDetailRequestSender.send(document);
    }

    @FXML
    private void toPDFReader() {
        openDocByPdfReaderRequestSender.send(document);
        switchToPDFReader.send(this);
    }

    @FXML
    private void toEdit() {
        editDocumentRequestSender.send(document);
        switchToEditDoc.send(this);
    }

    /**
     * show the selected document
     *
     */
    @Override
    public void show() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss");
        String name = document.getName();
        bookName.setText(name);
        author.setText("By " + document.getAuthor());
        bookDescription.setText(document.getDescription());
        uploadedDate.setText(document.getUploadTime().format(formatter));
        category.setText(document.getCategory());
        uploader.setText(document.getUploader().getUsername());
        ISBN.setText(document.getIsbn());
        copies.setText(Integer.toString(document.getCopies()));
        available.setText(Integer.toString(document.getCopies() - document.getLendingDetails().size()));
        if (document.getPdfUrl() == null) {
            ReadPDF.setDisable(true);
            ReadPDF.setId("noPDF");
            ReadPDF.setText("PDF unavailable");
        }
        else {
            ReadPDF.setDisable(false);
            ReadPDF.setId(null);
            ReadPDF.setText("Read PDF");
        }
        Thread thread = new Thread(() -> {
            Image temp = new Image(document.getCoverImageUrl());
            ObservableList<LendingDetail> lendingDetail =
                    FXCollections.observableList(document.getLendingDetails());
            Platform.runLater(() -> {
                cover.setImage(temp);
                lendings.setItems(lendingDetail);
            });
        });
        thread.start();
    }
}
