package com.gruppo1.smarthome.model;

import com.gruppo1.smarthome.command.api.Actions;
import com.gruppo1.smarthome.command.api.CrudOperation;
import com.gruppo1.smarthome.command.impl.AddOperationImpl;
import com.gruppo1.smarthome.command.impl.GetOperationImpl;
import com.gruppo1.smarthome.model.device.*;
import com.gruppo1.smarthome.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Transactional
public class DataGenerator {

    private final List<Profile> profiles;
    private final List<Room> rooms;
    private final List<Device> devices;
    private final List<Scene> scenes;
    private final List<Condition> conditions;
    private final ProfileRepo profileRepo;
    private final RoomRepo roomRepo;
    private final DeviceRepo deviceRepo;
    private final SceneRepo sceneRepo;
    private final ConditionRepo conditionRepo;

    @Autowired
    public DataGenerator(ProfileRepo profileRepo, RoomRepo roomRepo, DeviceRepo deviceRepo, SceneRepo sceneRepo, ConditionRepo conditionRepo) {
        this.profiles = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.devices = new ArrayList<>();
        this.scenes = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.profileRepo = profileRepo;
        this.roomRepo = roomRepo;
        this.deviceRepo = deviceRepo;
        this.sceneRepo = sceneRepo;
        this.conditionRepo = conditionRepo;
    }

    @Bean("generateProfiles")
    public List<Profile> generateProfiles() {
        CrudOperation addProfileOperation = new AddOperationImpl(profileRepo);
        profiles.add(new Profile("sextence0@fc2.com", "Nancy", "Jones", "pluto88"));
        profiles.add(new Profile("mariorossi@n2o.com", "Guest", "account", "tomato"));
        // save a few profiles
        profiles.forEach(addProfileOperation::execute);
        return profiles;
    }

    @Bean("generateRooms")
    public List<Room> generateRooms() {
        CrudOperation addRoomOperation = new AddOperationImpl(roomRepo);
        rooms.add(new Room("Hall"));
        rooms.add(new Room("Bathroom"));
        rooms.add(new Room("Default"));
        rooms.add(new Room("Bedroom 2"));
        rooms.add(new Room("Kitchen"));
        rooms.add(new Room("Studio"));
        rooms.add(new Room("Living room"));
        rooms.add(new Room("Guest room"));
        // save a few rooms
        rooms.forEach(addRoomOperation::execute);
        return rooms;
    }

    @Bean("generateDevices")
    @DependsOn("generateRooms")
    public List<Device> generateDevices() {
        Random rand = new Random();
        CrudOperation addDeviceOperation = new AddOperationImpl(deviceRepo);
        CrudOperation getRoomOperation = new GetOperationImpl(roomRepo);
        final List<Room> rooms = (List<Room>) (List<?>) getRoomOperation.execute();
        //TODO: How to manage the time (alarm clock)?
        devices.add(new AlarmClock("Spongebob block", "Alarm clock", "7:00 AM", "Workday", "Rain drops"));
        devices.add(new Conditioner("Samsung air", "Conditioner", 20, "Default"));
        devices.add(new Conditioner("Dyson", "Conditioner", 30, "Sun"));
        devices.add(new LightBulb("Desk lamp", "Lightbulb", 50, "Normal"));
        devices.add(new LightBulb("Studio lights", "Lightbulb", 75, "Cold"));
        devices.add(new LightBulb("Bedroom 1 ambient", "Lightbulb", 35, "Warm"));
        devices.add(new LightBulb("Living rgb", "Lightbulb", 65, "Variable"));
        devices.add(new Speaker("Soundbar", "Speaker", 100, "Bose", "SCS-2021"));
        devices.add(new Speaker("Woofer kit", "Speaker", 20, "Aiwa", "XR-76"));
        devices.add(new Television("Tv living", "Television", "Samsung", "TGKSLS09", 15, 0));
        devices.add(new Television("Tv kitchen", "Television", "LG", "TGKSLS09", 30, 8));
        // save a few devices
        devices.forEach(device -> {
            device.setRoom(rooms.get(rand.nextInt(rooms.size())));
            addDeviceOperation.execute(device);
        });
        return devices;
    }

    @Bean("generateScenes")
    public List<Scene> generateScenes() {
        CrudOperation addSceneOperation = new AddOperationImpl(sceneRepo);
        scenes.add(new Scene("Daily routine", false, "Daily"));
        scenes.add(new Scene("Week routine", false, "Weekly"));
        scenes.add(new Scene("Month routine", false, "Monthly"));
        scenes.add(new Scene("Weekends routine", false, null));
        scenes.add(new Scene("General routine", false, null));
        //save a few scenes
        scenes.forEach(addSceneOperation::execute);
        return scenes;
    }

    @Bean("generateConditions")
    @DependsOn({"generateDevices", "generateScenes"})
    public List<Condition> generateConditions(ConditionRepo conditionRepo, DeviceRepo deviceRepo, SceneRepo sceneRepo) {
        CrudOperation addConditionOperation = new AddOperationImpl(conditionRepo);
        CrudOperation getDeviceOperation = new GetOperationImpl(deviceRepo);
        CrudOperation getSceneOperation = new GetOperationImpl(sceneRepo);

        final List<Device> devices = (List<Device>) (List<?>) getDeviceOperation.execute();
        final List<Scene> scenes = (List<Scene>) (List<?>) getSceneOperation.execute();
        final Random rand = new Random();

        conditions.add(new Condition("SpongeBob block power on", Actions.POWER_ON, new Date(System.currentTimeMillis()+120000), rand.nextInt(30), devices.get(0), scenes.get(0)));
        conditions.add(new Condition("Samsung Air warmer", Actions.WARMER, new Date(System.currentTimeMillis()+240000), rand.nextInt(30), devices.get(1), scenes.get(1)));
        conditions.add(new Condition("Desk lamp colder", Actions.COLDER, new Date(System.currentTimeMillis()+360000), rand.nextInt(30), devices.get(3), scenes.get(2)));
        conditions.add(new Condition("Soundbar raise volume up", Actions.RAISE_VOLUME_UP, new Date(System.currentTimeMillis()+480000), rand.nextInt(30), devices.get(7), scenes.get(3)));
        conditions.add(new Condition("Woofer kit lower volume down", Actions.LOWER_VOLUME_DOWN, new Date(System.currentTimeMillis()+600000), rand.nextInt(30), devices.get(5), scenes.get(1)));
        conditions.add(new Condition("Tv living power on", Actions.POWER_ON, new Date(System.currentTimeMillis()+720000), rand.nextInt(30), devices.get(9), scenes.get(4)));


        conditions.forEach(addConditionOperation::execute);
        return conditions;
    }

    @Bean
    @DependsOn({"generateProfiles", "generateRooms", "generateDevices", "generateScenes", "generateConditions"})
    public CommandLineRunner insertData() {
        final Logger log = LoggerFactory.getLogger(DataGenerator.class);
        AtomicInteger rowNum = new AtomicInteger();

        return args -> {
            log.info("-------------------------------");
            log.info("Profiles inserted at start-up");
            profiles.forEach(profile -> log.info(rowNum.getAndIncrement() + ") " + profile.toString()));

            rowNum.getAndSet(0);
            log.info("-------------------------------");
            log.info("Rooms inserted at start-up");
            rooms.forEach(room -> log.info(rowNum.getAndIncrement() + ") " + room.toString()));

            rowNum.getAndSet(0);
            log.info("-------------------------------");
            log.info("Devices inserted at start-up");
            devices.forEach(device -> log.info(rowNum.getAndIncrement() + ") " + device.toString()));

            rowNum.getAndSet(0);
            log.info("-------------------------------");
            log.info("Scenes inserted at start-up");
            scenes.forEach(scene -> log.info(rowNum.getAndIncrement() + ") " + scene.toString()));

            rowNum.getAndSet(0);
            log.info("-------------------------------");
            log.info("Conditions inserted at start-up");
            conditions.forEach(condition -> log.info(rowNum.getAndIncrement() + ") " + condition.toString()));
            log.info("-------------------------------");
        };
    }
}
