package io.lecon.debugtools.server.resp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.lecon.debugtools.server.Result;

/**
 * Created by spawn on 17-9-28.
 */

public class JsonResponse extends BaseResponse {

    public JsonResponse(Result result) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("success", result.isSuccessful());
            jsonObject.put("message", result.getMessage());
            jsonObject.put("obj", result.getObj() == null ? "null" : new JSONObject(result.getObj()));
        } catch (JSONException e) {
            try {
                jsonObject.put("obj", new JSONArray(result.getObj()));
            } catch (JSONException e1) {
                e1.printStackTrace();
                try {
                    jsonObject.put("success", false);
                    jsonObject.put("message", "JsonResponse 错误");
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }

        bytes = jsonObject.toString().getBytes();
    }
}
