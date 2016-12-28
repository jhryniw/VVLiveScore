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

    public JSONObject state;

    private boolean isPosting = false;
    private Handler mHandle = new Handler();

    public Runnable post = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject resp = getHttpConn(state.toString());

                //TODO: Check for resp code 200

                if(isPosting)
                    mHandle.postDelayed(post, 1000);
            }
            catch (Exception e) {
                e.printStackTrace();
                isPosting = false;
            }
        }
    };

    public ScoreUpdater() {
        state = new JSONObject();
    }

    public void launch() {
        if (isPosting)
            return;

        isPosting = true;
        mHandle.postDelayed(post, 1000);
    }

    public void halt() {
        isPosting = false;
    }

    public JSONObject getHttpConn(String json){
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
            Log.d("Status Line: ", statusLine.toString());
            InputStream inputStream = response.getEntity().getContent();
            String responseStr = convertStreamtoString(inputStream);
            jsonObject = new JSONObject(responseStr);//.fromObject(jsonResponse);

        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void sendScore( Alliance alliance, OpMode opMode, String type, int score) {

        String strScoreType = ScoreUpdater.getScoreType(opMode, alliance, type);
        JSONObject updateJson = new JSONObject();

        try {
            updateJson.put("\"" + strScoreType +"\"", score);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        getHttpConn(updateJson.toString());
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

    public static String getScoreType(OpMode opMode, Alliance alliance, String type) {
        String strOpMode = opMode == OpMode.AUTONOMOUS ? "Auto" : "Tele";
        return alliance.toString() + strOpMode + type;
    }
}
