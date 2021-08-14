package com.gruppo1.smarthome.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;


@Entity
public class Room implements Serializable {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;
    @Column(nullable = false)
    private String name;

    @OneToMany
    private List<Device> id_device;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
