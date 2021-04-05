package com.testninja.selenium.reporters.reportportal.client;

import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRS;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.*;
import java.util.UUID;

public class LaunchManager {

    private ReportPortalService reportPortalService;
    private String logsFolderPath = System.getProperty("user.dir") + File.separator + "rp_logs";

    public LaunchManager(ReportPortalService reportPortalService) {
        this.reportPortalService = reportPortalService;
    }

    public Launch startNewLaunch(StartLaunchRQ startLaunchRQ) throws IOException {
        File logsFolderPath = new File(this.logsFolderPath);
        if(!logsFolderPath.exists()) {
            logsFolderPath.mkdirs();
        }
        StartLaunchRS response = reportPortalService.startLaunch(startLaunchRQ).execute().body();
        return new Launch(response);
    }

    public void stopLaunch(Launch launch, FinishExecutionRQ finishExecutionRQ) throws IOException {
        reportPortalService.finishLaunch(launch.getLaunchGuid(), finishExecutionRQ).execute();
        File logsFolderPath = new File(this.logsFolderPath);
        logsFolderPath.deleteOnExit();
    }

    public TestItem startTestItem(Launch launch, StartTestItemRQ startTestItemRQ) throws IOException {
        startTestItemRQ.setLaunchUuid(launch.getLaunchGuid());
        return new TestItem(launch, reportPortalService.startTestItem(startTestItemRQ).execute().body());
    }

    public TestItem startTestItem(TestItem testItem, StartTestItemRQ startTestItemRQ) throws IOException {
        startTestItemRQ.setLaunchUuid(testItem.getLaunchUUid());
        return new TestItem(testItem, reportPortalService.startTestItem(testItem.getUuid(), startTestItemRQ).execute().body());
    }

    public void finishTestItem(TestItem testItem, FinishTestItemRQ finishTestItemRQ) throws IOException {
        finishTestItemRQ.setLaunchUuid(testItem.getLaunchUUid());
        reportPortalService.finishTestItem(testItem.getUuid(), finishTestItemRQ).execute().body();
    }

    public void log(TestItem testItem, SaveLogRQ saveLogRQ) throws IOException {
        saveLogRQ.setItemUuid(testItem.getUuid());
        saveLogRQ.setLaunchUuid(testItem.getLaunchUUid());
        if (saveLogRQ.getFile() != null) {
            logWithAttachment(saveLogRQ);
        } else {
            reportPortalService.log(saveLogRQ).execute();
        }
    }

    public void logWithAttachment(SaveLogRQ saveLogRQ) {
        try {
            File attachment = saveLogRQ.getFile().getFile();
            File jsonFile = constructLogAttachmentJsonFile(saveLogRQ);

            RequestBody attachmentRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), attachment);
            RequestBody jsonRequestBody = RequestBody.create(MediaType.parse("application/json"), jsonFile);
            MultipartBody.Part attachmentPart = MultipartBody.Part.createFormData(attachment.getName(), attachment.getName(), attachmentRequestBody);
            MultipartBody.Part jsonPart = MultipartBody.Part.createFormData("json_request_part", "json_request_part", jsonRequestBody);
            reportPortalService.log(attachmentPart, jsonPart).execute();
            jsonFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File constructLogAttachmentJsonFile(SaveLogRQ saveLogRQ) throws Exception {
        ObjectMapper obj = new ObjectMapper();
        String a = obj.writeValueAsString(new SaveLogRQ[]{saveLogRQ});
        String filePath = logsFolderPath + File.separator + UUID.randomUUID().toString() + ".json";
        Writer w = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
        PrintWriter pw = new PrintWriter(w);
        pw.println(a);
        pw.close();
        return new File(filePath);
    }

    public String getLaunchId(String launchGuid) {
        try {
            Long id = reportPortalService.getLaunchByUuid(launchGuid).execute().body().getLaunchId();
            return Long.toString(id);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
