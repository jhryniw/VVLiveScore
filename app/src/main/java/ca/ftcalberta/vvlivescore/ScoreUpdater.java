package ca.ftcalberta.vvlivescore;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * ScoreUpdater Created by s on 10/10/2016.
 */

public class ScoreUpdater {

    private JSONObject state;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);


    private boolean isPosting = false;
    private Handler mHandle = new Handler();

    private Runnable post = new Runnable() {
        @Override
        public void run() {
        try {
            getHttpConn(state.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            isPosting = false;
        }
        }
    };

    ScoreUpdater() { state = new JSONObject(); }

    void launch() {
        if (isPosting)
            return;

        isPosting = true;
        mHandle.postDelayed(post, 1000);
    }

    void halt() {
        isPosting = false;
    }

    void setState(JSONObject newState) {
        state = newState;
    }

    private void getHttpConn(String json){
        JSONObject jsonObject = null;

        try {
            String authorization = "ftcscoring:OMGr0b0t";
            String encodedAuth = "Basic " + Base64.encodeToString(authorization.getBytes(), Base64.DEFAULT);
            HttpPost httpPost = new HttpPost("http://livescoring.ftcalberta.ca/");
            CloseableHttpClient client = HttpClientBuilder.create().build();
            StringEntity stringEntity = new StringEntity(json);

            //httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
            httpPost.addHeader("content-type", "application/json");
            //httpPost.addHeader("Authorization", encodedAuth);
            httpPost.setEntity(stringEntity);
            //Log.d("Post: ", json);

            HttpResponse response = client.execute(httpPost); // execute the post
            StatusLine statusLine = response.getStatusLine();
            Log.d("Status Line", statusLine.toString());

            //InputStream inputStream = response.getEntity().getContent();
            //String responseStr = convertStreamtoString(inputStream);
            //jsonObject = new JSONObject(responseStr);//.fromObject(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendScore( Alliance alliance, OpMode opMode, String type, int score) {

        String strScoreType = ScoreUpdater.getScoreType(opMode, alliance, type);
        JSONObject updateJson = new JSONObject();

        try {
            updateJson.put("\"" + strScoreType +"\"", score);
            updateJson.put("\"timestamp\"", System.currentTimeMillis());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        final String jsonString = updateJson.toString();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getHttpConn(jsonString);
            }
        });
    }

    public String convertStreamtoString(InputStream is){

        String line="";
        String data="";
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            while((line=br.readLine())!=null){

                data+=line;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return  data;
    }

    static String getScoreType(OpMode opMode, Alliance alliance, String type) {
        String strOpMode = opMode == OpMode.AUTONOMOUS ? "Auto" : "Tele";
        return alliance + strOpMode + type;
    }
}
