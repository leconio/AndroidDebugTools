package cn.liucl.debugtools.server.resp;

import org.json.JSONException;
import org.json.JSONObject;

import cn.liucl.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public class JsonResponse implements Response {

    private final JSONObject mJsonObject;

    public JsonResponse(Result result){
        mJsonObject = new JSONObject();
        try {
            mJsonObject.put("success", result.isSuccessful());
            mJsonObject.put("message", result.getMessage());
            mJsonObject.put("obj", new JSONObject(new String(result.getContent())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public byte[] getContent() {
        return mJsonObject.toString().getBytes();
    }
}
