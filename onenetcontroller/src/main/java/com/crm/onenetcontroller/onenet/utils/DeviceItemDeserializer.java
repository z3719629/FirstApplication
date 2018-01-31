package com.crm.onenetcontroller.onenet.utils;

import com.crm.onenetcontroller.onenet.ActivateCode;
import com.crm.onenetcontroller.onenet.DSItem;
import com.crm.onenetcontroller.onenet.DeviceItem;
import com.crm.onenetcontroller.onenet.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeviceItemDeserializer implements JsonDeserializer<DeviceItem> {
    @Override
    public DeviceItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        DeviceItem deviceItem = new DeviceItem();
        deviceItem.setId(jsonObject.get("id").getAsString());
        deviceItem.setTitle(jsonObject.get("title").getAsString());
        JsonElement desc = jsonObject.get("desc");
        deviceItem.setDesc(desc != null ? desc.getAsString() : "");
        JsonElement isPrivate = jsonObject.get("private");
        deviceItem.setPrivate(isPrivate != null ? isPrivate.getAsBoolean() : true);
        JsonElement protocol = jsonObject.get("protocol");
        deviceItem.setProtocol(protocol != null ? protocol.getAsString() : "HTTP");
        deviceItem.setOnline(jsonObject.get("online").getAsBoolean());
        JsonElement locationElement = jsonObject.get("location");
        if (locationElement != null) {
            JsonObject locationObject = locationElement.getAsJsonObject();
            if (locationObject != null) {
                Location location = new Location();
                location.setLat(locationObject.get("lat").getAsString());
                location.setLon(locationObject.get("lon").getAsString());
                deviceItem.setLocation(location);
            }
        }
        deviceItem.setCreateTime(jsonObject.get("create_time").getAsString());
        JsonElement authInfo = jsonObject.get("auth_info");
        if (authInfo != null) {
            if (authInfo.isJsonObject()) {
                deviceItem.setAuthInfo(authInfo.getAsJsonObject().toString());
            } else {
                deviceItem.setAuthInfo(authInfo.getAsString());
            }
        }
        JsonElement activateCodeElement = jsonObject.get("activate_code");
        if (activateCodeElement != null) {
            JsonObject activateCodeObject = activateCodeElement.getAsJsonObject();
            if (activateCodeObject != null) {
                ActivateCode activateCode = new ActivateCode();
                activateCode.setMt(activateCodeObject.get("mt").getAsString());
                activateCode.setMid(activateCodeObject.get("mid").getAsString());
                deviceItem.setActivateCode(activateCode);
            }
        }
        // DSItem
        JsonElement datastreamsElement = jsonObject.get("datastreams");
        if (datastreamsElement != null) {
            JsonArray datastreamsArray = datastreamsElement.getAsJsonArray();
            if (datastreamsArray != null) {
                List<DSItem> dsItemList = new ArrayList<>();
                for(int i=0;i<datastreamsArray.size();i++) {
                    DSItem dsItem = new DSItem();
                    JsonObject object = datastreamsArray.get(i).getAsJsonObject();
                    dsItem.setId(object.get("id").getAsString());
                    dsItem.setUnit(object.get("unit").getAsString());
                    dsItem.setUnitSymbol(object.get("unit_symbol").getAsString());
                    dsItemList.add(dsItem);
                }
                deviceItem.setDatastreams(dsItemList);
            }
        }
        return deviceItem;
    }
}
