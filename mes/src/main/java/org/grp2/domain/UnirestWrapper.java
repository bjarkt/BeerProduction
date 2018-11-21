package org.grp2.domain;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.grp2.javalin.Message;

public class UnirestWrapper {

    public <T> T post(String url, Class<? extends T> responseType) throws UnirestException {
        return Unirest.post(url).asObject(responseType).getBody();
    }

}
