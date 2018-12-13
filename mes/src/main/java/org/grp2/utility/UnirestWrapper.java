package org.grp2.utility;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestWrapper {

    public <T> T post(String url, Class<? extends T> responseType) throws UnirestException {
        return Unirest.post(url).asObject(responseType).getBody();
    }

}
