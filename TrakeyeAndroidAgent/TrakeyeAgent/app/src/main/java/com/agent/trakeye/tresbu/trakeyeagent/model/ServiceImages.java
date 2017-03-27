package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 09-Nov-16.
 */

public class ServiceImages extends ServiceImagesRequest implements Serializable{

    Service trService;



    public Service getTrCase() {
        return trService;
    }

    public void setTrCase(Service trCase) {
        this.trService = trCase;
    }


    @Override
    public String toString() {
        return "serviceImages{" +
                ", image='" + image + '\'' +
                ", trService=" + trService +
                '}';
    }
}
