package com.gruppo1.smarthome.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Conditions implements Serializable {

    @EmbeddedId
    private ConditionId conditionId;
    private Date activationDate;
    private String period;
    private Double threshold;


    @ManyToOne
    @MapsId("deviceId")
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne
    @MapsId("sceneId")
    @JoinColumn(name = "scene_id")
    private Scene scene;


    public Conditions() {
    }

    public Conditions(ConditionId conditionId, Date activationDate, Double threshold) {
        this.conditionId = conditionId;
        this.activationDate = activationDate;
        this.threshold = threshold;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public void setConditionId(ConditionId conditionId) {
        this.conditionId = conditionId;
    }

    public Date getActivation() {
        return activationDate;
    }

    public void setActivation(Date activation) {
        this.activationDate = activation;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }
}