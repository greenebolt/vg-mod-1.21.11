package com.vgmod;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {
    private static final Properties defaultValues = new Properties();
    public static String fileName;

    public static String configVersion = Constants.VGMOD_VERSION;
    public static boolean wbMessages = false;
    public static String friendList = ",Notch,GreeneBolt";
    public static List<String> friends = new ArrayList<>();

    Config(String fileName) {
        this.fileName = fileName;
    }

    public void read() {
        Properties properties = new Properties(defaultValues);

        try {
            FileReader configReader = new FileReader(fileName);
            properties.load(configReader);
            configReader.close();
        } catch (FileNotFoundException ignored) {
            // If the config does not exist, generate the default one.
            VGMod.LOGGER.info("Generating the config file at: " + fileName);
            save();
            return;
        } catch (IOException e) {
            VGMod.LOGGER.info("Failed to read the config file: " + fileName);
            e.printStackTrace();
        }

        configVersion = properties.getProperty("CONFIG_VERSION");
        wbMessages = properties.getProperty("WB_MESSAGES_ACTIVE").equals("true");
        friendList = properties.getProperty("FRIEND_LIST");
        friends.addAll(Arrays.asList(friendList.split(",")));
        VGMod.LOGGER.info("Loaded friends...");
        VGMod.LOGGER.info(friends.toString());
        if (!configVersion.equals(Config.configVersion)) {
            // The mod has been updated: Update config file
        }
    }

    public static void save() {
        try {
            File config = new File(fileName);
            boolean existed = config.exists();
            File parentDir = config.getParentFile();
            if (!parentDir.exists())
                parentDir.mkdirs();

            FileWriter configWriter = new FileWriter(config);

            writeString(configWriter, "CONFIG_VERSION", Constants.VGMOD_VERSION);
            writeBoolean(configWriter, "WB_MESSAGES_ACTIVE", wbMessages);
            writeString(configWriter, "FRIEND_LIST", friendList);
            friends.addAll(Arrays.asList(friendList.split(",")));

            configWriter.close();

            if (!existed)
                VGMod.LOGGER.info("Created the config file.");
        } catch (IOException e) {
            VGMod.LOGGER.info("Failed to write the config file: " + fileName);
            e.printStackTrace();
        }
    }

    private static void writeString(FileWriter configWriter, String name, String value) throws IOException {
        configWriter.write(name + '=' + value + '\n');
    }

    private static void writeBoolean(FileWriter configWriter, String name, boolean value) throws IOException {
        writeString(configWriter, name, value ? "true" : "false");
    }

    static {
        defaultValues.setProperty("WB_MESSAGES_ACTIVE", "false");
        defaultValues.setProperty("CONFIG_VERSION","1.0.0");
        defaultValues.setProperty("FRIEND_LIST", "Notch,Dream,GreeneBolt");
    }
}
