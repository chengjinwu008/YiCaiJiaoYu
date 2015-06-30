package com.cjq.yicaijiaoyu.utils;

import android.content.Context;

import com.cjq.yicaijiaoyu.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by android on 2015/5/18.
 */
public class Validator {
    public interface VerifyListener {
        void verifyFinished(boolean state);
    }

    public static void verify(final Context context, final String num, final VerifyListener listener){
        new Thread(){
            @Override
            public void run() {
                InputStream stream = context.getResources().openRawResource(R.raw.soap12);
                String res=null;
                try {
                    String oString = StreamUtil.readStreamToString(stream);
                    String pat = "\\$mobile";
                    Pattern pattern = Pattern.compile(pat);
                    Matcher matcher = pattern.matcher(oString);
                    while (matcher.find()){
                        res = matcher.replaceAll(num);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    String code = ValidatorHttpRequestUtil.request(res);
                    if(listener!=null)
                        listener.verifyFinished(code!=null);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void verify(final String num, final VerifyListener listener){

        new Thread(){
            @Override
            public void run() {
                JSONObject code = null;
                try {
                    code = ValidatorHttpRequestUtil.requestTaobaoGET(num);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(code!=null){
                    if(listener!=null)
                        try {
                            listener.verifyFinished( code.get("telString")!=null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.verifyFinished(false);
                        }
                }else{
                    if(listener!=null)
                        listener.verifyFinished(false);
                }
            }
        }.start();
    }
}
