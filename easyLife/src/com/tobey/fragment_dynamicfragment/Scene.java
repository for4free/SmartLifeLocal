package com.tobey.fragment_dynamicfragment;

public class Scene {

    private String scenename;

    public Scene(String bandname){
        this.scenename = bandname;
    }

    public String getScenename() {
        return scenename;
    }

    public void setScenename(String bandname) {
        this.scenename = bandname;
    }
}