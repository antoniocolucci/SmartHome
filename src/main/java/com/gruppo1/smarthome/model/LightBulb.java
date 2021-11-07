package com.gruppo1.smarthome.model;

import javax.persistence.*;

@Entity
public class LightBulb extends Device{
    @Id
    @Column(nullable = false, updatable = false)
    private String id;
    private Integer brightness;
    private Integer colorTemp;


    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public void setColorTemp(Integer colorTemp){
        this.colorTemp = colorTemp;
    }

    public Integer getBrightness(){return brightness;}

    public Integer getColorTemp(){return colorTemp;}
}