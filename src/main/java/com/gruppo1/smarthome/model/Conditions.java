package com.gruppo1.smarthome.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Conditions extends SmartHomeItem implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(nullable = false, updatable = false)
    private String id;
    private Date activationDate;
    private String period;
    private Double threshold;
    private String name;

    @ManyToOne()
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne()
    @JoinColumn(name = "scene_id")
    private Scene scene;

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

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
