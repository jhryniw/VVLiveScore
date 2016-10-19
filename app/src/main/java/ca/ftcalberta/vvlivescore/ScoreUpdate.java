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
 * ScoreUpdate Created by s on 10/10/2016.
 */

public class ScoreUpdate {

    private UUID phoneId;
    private UUID scoreId;
    private ScoreType type;
    private Alliance alliance;
    private OpMode opMode;
    private Long timeStamp;
    private int score;

    private boolean isActive = false;
    private boolean isPosting = false;
    private Handler mHandle = new Handler();

    private Runnable post = new Runnable() {
        @Override
        public void run() {
            try {
                String strJson = getUpdateJSON().toString();
                JSONObject resp = getHttpConn(strJson);

                //TODO: Check for resp code 200

                if(isPosting)
                    mHandle.postDelayed(post, 1000);
            }
            catch (JSONException e) {
                e.printStackTrace();
                isPosting = false;
            }
        }
    };

    public ScoreUpdate(UUID pId, ScoreType t, Alliance a, OpMode o, int s){
        phoneId = pId;
        scoreId = UUID.randomUUID();
        type = t;
        alliance = a;
        opMode = o;
        timeStamp = System.currentTimeMillis();
        score = s;
    }

    public JSONObject getUpdateJSON() throws JSONException {
        JSONObject update = new JSONObject();
        update.put("Alliance",alliance);
        update.put("Vortex",type);
        update.put("Mode",opMode);
        update.put("TotalScore",score);
        //update.put("phoneId", phoneId.toString());
        //update.put("scoreId", scoreId.toString());
        //update.put("timeStamp",timeStamp);
        return update;
    }

    public void launch() {
        if (isPosting)
            return;

        isPosting = true;
        post.run();
    }

    public void halt() {
        isPosting = false;
    }

    public JSONObject getHttpConn(String json){
        JSONObject jsonObject=null;

        try {
            String authorization = "ftcscoring:OMGr0b0t";
            String encodedAuth = "Basic " + Base64.encodeToString(authorization.getBytes(), Base64.DEFAULT);
            HttpPost httpPost = new HttpPost("http://livescoring.ftcalberta.ca/");
            CloseableHttpClient client = HttpClientBuilder.create().build();
            StringEntity stringEntity = new StringEntity("d=" + "");

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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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
}
