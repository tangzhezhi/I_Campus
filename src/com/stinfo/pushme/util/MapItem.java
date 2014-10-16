package com.stinfo.pushme.util;

public class MapItem {
    private String ID;
    private String value;
   
    public MapItem() {
        this.ID = "";
        this.value = "";
    }
    
    public MapItem(String ID, String value) {
        this.ID = ID;
        this.value = value;
    }
    
    public MapItem(String pairStr) throws Exception {
        String[] item = pairStr.split(":");
        if (item.length != 2) {
            throw new Exception("");
        }
        
        this.ID = item[0];
        this.value = item[1];
    }

    public String getID() {
        return this.ID;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.value;
    }
}