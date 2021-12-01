package com.gruppo1.smarthome.adapter;

import com.gruppo1.smarthome.model.Device;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class DeviceAdapter {

    void run(JSONObject deviceJson, Device deviceToAdapt) throws JSONException {
        if(deviceJson.has("type"))
            deviceToAdapt.setType(deviceJson.get("type").toString());
        if(deviceJson.has("name"))
            deviceToAdapt.setName(deviceJson.get("name").toString());
        if(deviceJson.has("status"))
            deviceToAdapt.setStatus(Boolean.parseBoolean(deviceJson.get("status").toString()));
    }
}
