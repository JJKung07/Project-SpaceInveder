package se233.project2.controller;

import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Keys {
    private HashMap<KeyCode,Boolean> keys;
    public Keys(){
        keys = new HashMap<>();
    }
    public void add(KeyCode key){
        keys.put(key,true);
    }
    public void remove(KeyCode key){
        keys.put(key,false);
    }
    public boolean ispressed(KeyCode key){
        return keys.getOrDefault(key,false);
    }

}
