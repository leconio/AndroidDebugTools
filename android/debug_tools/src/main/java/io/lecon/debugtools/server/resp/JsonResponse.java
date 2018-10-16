package io.lecon.debugtools.server.resp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.lecon.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public class JsonResponse implements Response {

    private final JSONObject mJsonObject;

    public JsonResponse(Result result) {
        mJsonObject = new JSONObject();
        try {
            mJsonObject.put("success", result.isSuccessful());
            mJsonObject.put("message", result.getMessage());
            mJsonObject.put("obj", result.getObj() == null ? "null" : new JSONObject(result.getObj()));
        } catch (JSONException e) {
            try {
                mJsonObject.put("obj", new JSONArray(result.getObj()));
            } catch (JSONException e1) {
                e1.printStackTrace();
                try {
                    mJsonObject.put("success", false);
                    mJsonObject.put("message", "JsonResponse 错误");
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }

    }

    @Override
    public byte[] getContent() {
        return mJsonObject.toString().getBytes();
    }
}
