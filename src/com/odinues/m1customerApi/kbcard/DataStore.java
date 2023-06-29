package com.odinues.m1customerApi.kbcard;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private Map<String, String> monitoringDataStore = new ConcurrentHashMap<>();
    private String[] columnArray = null;

    public Map<String, String> getMonitoringDataStore() {
        return monitoringDataStore;
    }

    public void put(String key, String value) {
        monitoringDataStore.put(key, value);
    }

    public String[] getColumnArray() {
        return columnArray;
    }

    public void setMonitoringDataStore(Map<String, String> monitoringDataStore) {
        this.monitoringDataStore = monitoringDataStore;
    }

    public void setColumnArray(String[] columnArray) {
        this.columnArray = columnArray;
    }

    public String get(String key) {
        return monitoringDataStore.get(key);
    }

    public void getAndSetColArray(JSONObject jsonObject) {
        JSONArray jsonArray = (JSONArray) jsonObject.get("colArray");
        if (jsonArray != null && (jsonArray.getClass().isArray() || jsonArray instanceof List)) {
            this.columnArray = jsonArrayToStringArray(jsonArray);
        } else {
            this.columnArray = jsonObjectToStringArray(jsonObject);
        }
    }

    private String[] jsonArrayToStringArray(JSONArray jsonArray) {
        String[] arr = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            arr[i] = (String) jsonArray.get(i);
        }
        return arr;
    }

    private String[] jsonObjectToStringArray(JSONObject jsonObject) {
        Set keySet = jsonObject.keySet();
        String[] arr = new String[keySet.size()];

        int idx = 0;
        for (Object o : keySet) {
            arr[idx++] = (String) o;
        }
        return arr;
    }
}
