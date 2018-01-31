package br.com.iandev.midiaindoor.controller;

import br.com.iandev.midiaindoor.core.App;
import br.com.iandev.midiaindoor.facede.ContentFacede;
import br.com.iandev.midiaindoor.facede.DeviceFacede;
import br.com.iandev.midiaindoor.model.Content;
import br.com.iandev.midiaindoor.task.Manager;
import br.com.iandev.midiaindoor.util.ViewUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

public class ContentController extends Controller {

    private static TimeZone timeZoneFormatter;
    private static Date date;

    @FXML
    ListView<Content> listView;

    private ContentFacede contentFacede;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.setCellFactory((param) -> {
            return new ItemView();
        });
        contentFacede = new ContentFacede();
        try {
            timeZoneFormatter = new DeviceFacede().get().getTimeZone();
        } catch (Exception ignored) {
//            Silence is golden
        }
        try {
            date = App.getInstance().getTimeCounter().getDate();
        } catch (Exception ignored) {
//            Silence is golden
        }
        refresh();
    }

    public void refresh() {
        listView.setItems(FXCollections.observableList(contentFacede.list()));
    }

    public static class ItemView extends ListCell<Content> {

        @FXML
        private GridPane root;

        @FXML
        private Text id;

        @FXML
        private Text description;

        @FXML
        private Text durationInterval;

        @FXML
        private Text lastPlaybackDate;

        @FXML
        private Text status;

        @FXML
        private Text person;

        @FXML
        private Text channel;

        @FXML
        private Text timeZone;

        @FXML
        private Text updateInterval;

        @FXML
        private Text updateToleranceInterval;

        @FXML
        private Text lastUpdateDate;

        @FXML
        private Text lastUpdateAttemptDate;

        @FXML
        private Text nextUpdateDate;

        @FXML
        private JFXCheckBox mustUpdate;

        @FXML
        private JFXCheckBox outOfDate;

        @FXML
        private JFXCheckBox canPlay;

        @FXML
        private Text lastAuthenticationDate;

        @FXML
        private Text tokenExpirationInterval;

        @FXML
        private Text token;

        @FXML
        private Text startDate;

        @FXML
        private Text endDate;

        @FXML
        private Text integrationApi;

        @FXML
        private Text accessUrl;

        @FXML
        private Text hash;

        @FXML
        private Text alias;

        @FXML
        private Text indexFilePath;

        @FXML
        JFXButton doUpdate;

        @Override
        protected void updateItem(Content content, boolean empty) {
            super.updateItem(content, empty);
//            super.setText(content != null ? content.toString() : "null");

            if (empty || content == null) {
                super.setText(null);
                super.setGraphic(null);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getResource(viewPath("content/item.fxml")));
            loader.setController(this);
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass()).error("", ex);
            }

            id.setText(ViewUtil.getString(content.getId()));
            description.setText(ViewUtil.getString(content.getDescription()));
            status.setText(ViewUtil.getString(content.getStatus()));

            durationInterval.setText(ViewUtil.getInterval(content.getDurationInterval()));
            lastPlaybackDate.setText(ViewUtil.getString(content.getLastPlaybackDate()));

            updateInterval.setText(ViewUtil.getInterval(content.getUpdateInterval()));
            updateToleranceInterval.setText(ViewUtil.getInterval(content.getUpdateToleranceInterval()));
            lastUpdateDate.setText(ViewUtil.getString(content.getLastUpdateDate()));
            lastUpdateAttemptDate.setText(ViewUtil.getString(content.getLastUpdateAttemptDate(), timeZoneFormatter));

            Date nextUpdateDateValue = null;
            try {
                nextUpdateDateValue = content.getNextUpdateDate();
            } catch (Exception ex) {
                // Silence is golden
            }
            nextUpdateDate.setText(ViewUtil.getString(nextUpdateDateValue, timeZoneFormatter));

            mustUpdate.setSelected(content.mustUpdate(date));
            mustUpdate.setDisable(true);
            outOfDate.setSelected(content.outOfDate(date));
            outOfDate.setDisable(true);
            canPlay.setSelected(content.canPlay(date));
            canPlay.setDisable(true);
            
            startDate.setText(ViewUtil.getString(content.getStartDate()));
            endDate.setText(ViewUtil.getString(content.getEndDate()));

            integrationApi.setText(ViewUtil.getString(content.getIntegrationApi()));
            accessUrl.setText(ViewUtil.getString(content.getAccessUrl()));
            hash.setText(ViewUtil.getString(content.getHash()));
            alias.setText(ViewUtil.getString(content.getAlias()));
            indexFilePath.setText(ViewUtil.getString(content.getIndexFilePath()));

            doUpdate.setOnAction((ActionEvent event) -> {
//                doUpdate.setEnabled(false);

                Manager manager = new Manager(content) {
                    @Override
                    public void success(Content content) {
                    }

                    @Override
                    public void error(Exception ex) {
                    }
                };

                manager.setDownloadForced(true);

                new Thread(manager).start();

            });

            super.setText(null);
            super.setGraphic(root);
        }
    }
}
