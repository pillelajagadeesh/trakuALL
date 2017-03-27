package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 09-Nov-16.
 */

public class CaseImages extends CaseImagesRequest implements Serializable{

    Case trCase;



    public Case getTrCase() {
        return trCase;
    }

    public void setTrCase(Case trCase) {
        this.trCase = trCase;
    }


    @Override
    public String toString() {
        return "caseImages{" +
                ", image='" + image + '\'' +
                ", trCase=" + trCase +
                '}';
    }
}
