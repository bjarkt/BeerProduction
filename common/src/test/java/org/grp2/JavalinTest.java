package org.grp2;

import io.javalin.Javalin;
import org.grp2.javalin.AbstractAPI;
import org.grp2.javalin.JavalinSetup;
import org.junit.Test;

import java.io.IOException;
import java.net.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class JavalinTest {
    private Javalin app;
    private int port = 6999;

    @Test
    public void WebServerOpeningOnPort() {
        startApi();

        int statusCode = hostAvailabilityCheck("http://localhost:" + port + "/routes");

        assertTrue(statusCode != -1);

        assertEquals(statusCode, 200);

        stopApi();
    }

    private void startApi() {
        app = JavalinSetup.setup(port);
        AbstractAPI api = new AbstractAPI(port) {
            @Override
            public void start() {
                app.disableStartupBanner();
                app.start();
            }
        };
        api.start();
    }

    private void stopApi() {
        app.stop();
    }

    private int hostAvailabilityCheck(String url) {
        int statusCode = -1;
        try{
            URL _url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)_url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            statusCode = connection.getResponseCode();
        }catch(UnknownHostException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return statusCode;
    }
}
