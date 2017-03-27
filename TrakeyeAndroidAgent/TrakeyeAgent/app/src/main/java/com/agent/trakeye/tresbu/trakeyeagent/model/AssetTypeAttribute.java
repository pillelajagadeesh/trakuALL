package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 17-Oct-16.
 */

public class AssetTypeAttribute implements Serializable {
    long id;
    String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AssetTypeAttributes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
