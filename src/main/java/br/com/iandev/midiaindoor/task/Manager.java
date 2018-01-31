package br.com.iandev.midiaindoor.task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.com.iandev.midiaindoor.core.App;
import br.com.iandev.midiaindoor.core.TimeCounter;
import br.com.iandev.midiaindoor.core.integration.ManagerException;
import br.com.iandev.midiaindoor.model.Content;
import br.com.iandev.midiaindoor.facede.ContentFacede;
import br.com.iandev.midiaindoor.facede.UnsuccessfulException;
import br.com.iandev.midiaindoor.facede.NotFoundException;
import org.apache.log4j.Logger;

public class Manager extends IntegrationTask<Content> {

    private final Content content;
    private ContentFacede contentFacede;
    private boolean downloadForced = false;
    private boolean autoUpdateList = false;

    private static List<Content> contentList;
    private static Iterator<Content> contentIterator;

    private static final Logger logger = Logger.getLogger(Manager.class);

    public Manager(final Content content) {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        this.contentFacede = new ContentFacede();
        this.content = content;
    }

//    public Manager(final List<Content> contentList) {
//        if (contentList == null) {
//            throw new IllegalArgumentException("contentList must not be null");
//        }
//        this.content = null;
//        this.contentFacede = new ContentFacede();
//        Manager.contentList = new ArrayList<>(contentList);
//    }

    public Manager() {
        this.content = null;
        this.autoUpdateList = true;
        this.contentFacede = new ContentFacede();
    }

    @Override
    public final Content execute() throws Exception {
        super.setUp();

        TimeCounter timeCounter = App.getInstance().getTimeCounter();
        Content content = contentFacede.get(this.content == null ? getContentIterator().next() : this.content);

        if (mustUpdate(content, timeCounter.getDate())) {
            content.setLastUpdateAttemptDate(timeCounter.getDate());
            contentFacede.save(content);

            doUpdate(content);

            content.setLastUpdateDate(timeCounter.getDate());
            contentFacede.save(content);
        }

        return content;
    }

    private Content doUpdate(Content content) throws ManagerException, JSONException, IOException, UnsuccessfulException, NotFoundException {
        br.com.iandev.midiaindoor.core.integration.bdo.Manager manager = new br.com.iandev.midiaindoor.core.integration.bdo.Manager();

        manager.setIdConteudo(content.getId().toString());
        manager.setConteudo(content.getAlias());
        manager.setAccessURL(content.getAccessUrl());
        manager.setPackageFile(content.getPackageFile());

        JSONObject json = manager.getInformation();

        String hash = json.getString("hash");
        String indexFilePath = json.getString("indexFilePath");
        Long durationInterval = content.getDurationInterval();

        if (json.has("durationInterval")) {
            durationInterval = json.getLong("durationInterval");
        }

        if (mustDownload(hash, indexFilePath, content)) {
            manager.doDownload();
            content.uncompressPackage();
            content.setHash(hash);
            content.setIndexFilePath(indexFilePath);
            content.setDurationInterval(durationInterval);
        }
        return content;
    }

    public boolean mustDownload(String hash, String indexFilePath, Content content) {
        return this.isDownloadForced() || !hash.equals(content.getHash()) || !indexFilePath.equals(content.getIndexFilePath());
    }

    public boolean mustUpdate(Content content, Date date) {
        return content.mustUpdate(date) || this.isDownloadForced();
    }

    public boolean isDownloadForced() {
        return downloadForced;
    }

    public void setDownloadForced(boolean downloadForced) {
        this.downloadForced = downloadForced;
    }

    private Iterator<Content> getContentIterator() throws NotFoundException {
        if (contentIterator == null || !contentIterator.hasNext()) {
            if (autoUpdateList) {
                contentList = newContentList();
            }
            contentIterator = contentList.iterator();
        }
        return contentIterator;
    }

    public List<Content> newContentList() throws NotFoundException {
        List<Content> contentList = contentFacede.list();

        Collections.sort(contentList, (Content content1, Content content2) -> {
            if (content1.getLastUpdateAttemptDate() == null) {
                return -1;
            }

            if (content2.getLastUpdateAttemptDate() == null) {
                return 1;
            }

            return content1.getLastUpdateAttemptDate().compareTo(content2.getLastUpdateAttemptDate());
        });

        return contentList;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
