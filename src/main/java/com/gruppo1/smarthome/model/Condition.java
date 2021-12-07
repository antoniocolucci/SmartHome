package com.gruppo1.smarthome.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruppo1.smarthome.command.api.Actions;
import com.gruppo1.smarthome.memento.Memento;
import com.gruppo1.smarthome.model.device.Device;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Condition extends SmartHomeItem implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(nullable = false, updatable = false)
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date activationDate;
    private Integer threshold;
    private String name;
    private Actions action;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne()
    @JoinColumn(name = "scene_id")
    private Scene scene;

    public Condition() {
    }

    public Condition(String name) {
        this.name = name;
    }

    public Condition(String name, Actions action, Date activationDate, Integer threshold, Device device, Scene scene) {
        this.name = name;
        this.action = action;
        this.activationDate = activationDate;
        this.threshold = threshold;
        this.device = device;
        this.scene = scene;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activation) {
        this.activationDate = activation;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Memento createMemento() {
        return null;
    }

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

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "threshold=" + threshold +
                ", name='" + name + '\'' +
                ", device=" + device.getName() +
                ", scene=" + scene.getName() +
                ", action=" + action +
                ", activation date=" + activationDate +
                '}';
    }

    private class MementoCondition extends Memento {

        private String memId;
        private String memName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        private Date memActivationDate;
        private Integer memThreshold;
        private Actions memAction;
        private Device memDevice;
        private Scene memScene;

        public MementoCondition() {
            this.memId = id;
            this.memName = name;
            this.memActivationDate = activationDate;
            this.memThreshold = threshold;
            this.memAction = action;
            this.memDevice = device;
            this.memScene = scene;
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
