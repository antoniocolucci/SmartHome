package com.gruppo1.smarthome.crud.beans;

import com.gruppo1.smarthome.crud.api.BaseSmartHomeRepository;
import com.gruppo1.smarthome.model.*;
import com.gruppo1.smarthome.repository.DeviceRepo;
import com.gruppo1.smarthome.repository.ProfileRepo;
import com.gruppo1.smarthome.repository.RoomRepo;
import com.gruppo1.smarthome.repository.SceneRepo;
import com.gruppo1.smarthome.repository.ConditionsRepo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        this.context = context;
    }

    public static BaseSmartHomeRepository getRepository(Object item) {
        if (item instanceof Device || item.toString().contains("Device")) {
            return context.getBean(DeviceRepo.class);
        } else if (item instanceof Room || item.toString().contains("Room")) {
            return context.getBean(RoomRepo.class);
        } else if (item instanceof Scene || item.toString().contains("Scene")) {
            return context.getBean(SceneRepo.class);
        } else if (item instanceof Profile || item.toString().contains("Profile")) {
            return context.getBean(ProfileRepo.class);
        } else if (item instanceof Conditions || item.toString().contains("Conditions")) {
            return context.getBean(ConditionsRepo.class);
        } else return null;
    }
}