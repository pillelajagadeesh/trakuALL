package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Tresbu on 14-Oct-16.
 */

public class UserInfo implements Serializable {

    private String login;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean activated = false;
    private String langKey;
    private Set<String> authorities;
    private Set<String> geofences;
    private TrakeyeType trakeyeType;
    private Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues;
    private int fromTime;
    private int toTime;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getGeofences() {
        return geofences;
    }

    public void setGeofences(Set<String> geofences) {
        this.geofences = geofences;
    }

    public TrakeyeType getTrakeyeType() {
        return trakeyeType;
    }

    public void setTrakeyeType(TrakeyeType trakeyeType) {
        this.trakeyeType = trakeyeType;
    }

    public Set<TrakeyeTypeAttributeValue> getTrakeyeTypeAttributeValues() {
        return trakeyeTypeAttributeValues;
    }

    public void setTrakeyeTypeAttributeValues(Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues) {
        this.trakeyeTypeAttributeValues = trakeyeTypeAttributeValues;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTime() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "login='" + login + '\'' +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", activated=" + activated +
                ", langKey='" + langKey + '\'' +
                ", authorities=" + authorities +
                ", geofences=" + geofences +
                ", trakeyeType=" + trakeyeType +
                ", trakeyeTypeAttributeValues=" + trakeyeTypeAttributeValues +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }
}
