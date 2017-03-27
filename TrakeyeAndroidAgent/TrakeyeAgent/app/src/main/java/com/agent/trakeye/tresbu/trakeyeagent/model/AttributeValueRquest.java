package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 25-Oct-16.
 */

public class AttributeValueRquest extends CaseTypeAttributeValues implements Serializable{

    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
