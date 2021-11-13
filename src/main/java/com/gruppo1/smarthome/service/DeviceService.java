package com.gruppo1.smarthome.service;

import com.gruppo1.smarthome.adapter.AdapterService;
import com.gruppo1.smarthome.crud.api.CrudOperation;
import com.gruppo1.smarthome.crud.beans.CrudOperationExecutor;
import com.gruppo1.smarthome.crud.impl.*;
import com.gruppo1.smarthome.crud.memento.Memento;
import com.gruppo1.smarthome.crud.memento.MementoCareTaker;
import com.gruppo1.smarthome.model.Device;
import com.gruppo1.smarthome.model.FactoryDevice;
import com.gruppo1.smarthome.model.Room;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class DeviceService {
    private final CrudOperationExecutor operationExecutor;
    private final MementoCareTaker mementoCareTaker;

    @Autowired
    public DeviceService(CrudOperationExecutor operationExecutor, MementoCareTaker mementoCareTaker) {
        this.operationExecutor = operationExecutor;
        this.mementoCareTaker = mementoCareTaker;
    }

    public Device addDevice(JSONObject deviceJson) throws JSONException {
        if (!validateJson(deviceJson))
            return null;

        Device newDevice = (Device) operationExecutor.execute(new GetByNameOperationImpl(), deviceJson.get("name").toString(), this);
        if (Objects.isNull(newDevice)) {
            String typeDevice = deviceJson.get("type").toString().toLowerCase();
            FactoryDevice factory = new FactoryDevice();
            newDevice = factory.getDevice(typeDevice);
            if (Objects.nonNull(newDevice)) {
                AdapterService adapterDevice = new AdapterService();
                adapterDevice.adapt(deviceJson, newDevice);
                Room room = validateRoom(deviceJson);
                newDevice.setRoom(room);
                CrudOperation operationToPerform = new AddOperationImpl();
                mementoCareTaker.add(new Memento(operationToPerform, newDevice, "Add device"));
                return (Device) operationExecutor.execute(operationToPerform, newDevice);
            }
        }
        return null;
    }

    public List<Device> findAllDevices() {
        CrudOperation operationToPerform = new GetOperationImpl();
        mementoCareTaker.add(new Memento(operationToPerform, new Device("Device"), "Find all devices"));
        return (List<Device>) operationExecutor.execute(operationToPerform, this);
    }

    public Device findDeviceByName(String name) {
        CrudOperation operationToPerform = new GetByNameOperationImpl();
        Device result = (Device) operationExecutor.execute(operationToPerform, name, this);
        mementoCareTaker.add(new Memento(operationToPerform, result, "Find a device given a name"));
        return result;
    }

    public Device updateDevice(String deviceNameToUpdate, Device updatedDevice) {

        //TODO: hide more the id handling
        Device oldDevice = (Device) operationExecutor.execute(new GetByNameOperationImpl(), deviceNameToUpdate, this);
        if (Objects.nonNull(oldDevice)) {
            updatedDevice.setId(oldDevice.getId());
            UpdateOperationImpl operationToPerform = new UpdateOperationImpl();
            mementoCareTaker.add(new Memento(operationToPerform, oldDevice, "Update device"));
            return (Device) operationExecutor.execute(operationToPerform, updatedDevice);
        }
        return null;
    }

    public Integer deleteDevice(String name) {
        Device device = (Device) operationExecutor.execute(new GetByNameOperationImpl(), name, this);
        if (Objects.nonNull(device)) {
            Device deviceToDelete = (Device) operationExecutor.execute(new GetByNameOperationImpl(), name, this);
            CrudOperation operationToPerform = new DeleteOperationImpl();
            mementoCareTaker.add(new Memento(operationToPerform, deviceToDelete, "Delete device"));
            return (Integer) operationExecutor.execute(operationToPerform, name, this);
        }
        return 0;
    }


    public Integer countDevices() {
        CrudOperation operationToPerform = new GetOperationImpl();
        mementoCareTaker.add(new Memento(operationToPerform, new Device("Devices"), "Count devices")); //TODO
        return ((List<Device>) operationExecutor.execute(operationToPerform, null)).size();
    }

    private Boolean validateJson(JSONObject objectToCheck) {
        if (objectToCheck.has("type") && objectToCheck.has("name")) {
            return true;
        }
        return false;
    }

    private Room validateRoom(JSONObject deviceJson) throws JSONException {
        Room room;
        CrudOperation operationToPerform = new GetByNameOperationImpl();
        if (deviceJson.has("room_name")) {
            room = (Room) operationExecutor.execute(operationToPerform, deviceJson.get("room_name").toString(), "Room");
            if (Objects.isNull(room)) {
                room = (Room) operationExecutor.execute(operationToPerform, "Default", "Room");
            }
        } else {
            room = (Room) operationExecutor.execute(operationToPerform, "Default", "Room");
        }
        return room;
    }
}