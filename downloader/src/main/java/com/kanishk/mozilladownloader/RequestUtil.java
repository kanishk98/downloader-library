package com.kanishk.mozilladownloader;

import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class RequestUtil {

    public String generateUID() {
        return UUID.randomUUID().toString();
    }

    public boolean isValidURL(String link) {
        try {
            new URL(link);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    public Request setRequest(Request request, RequestParams requestParams) {
        request.setDownloadOnEnqueue(requestParams.isDownloadOnEnqueue());
        request.setEnqueueAction(requestParams.getEnqueueAction());
        request.setExtras(requestParams.getExtras());
        // TODO: Look into request.setGroupIds();
        request.setNetworkType(requestParams.getNetworkType());
        request.setPriority(Priority.valueOf(requestParams.getPriority()));
        request.setTag(requestParams.getTag());
        return request;
    }
}
