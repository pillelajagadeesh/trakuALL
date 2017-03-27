package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 09-Nov-16.
 */

public class CaseImagesRequest implements Serializable{

    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "caseImages{" +

                ", image='" + image + '\'' +

                '}';
    }
}
