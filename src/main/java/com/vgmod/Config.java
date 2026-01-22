package com.vgmod;
import java.io.*;
import java.util.Properties;

public class Config {
    private static final Properties defaultValues = new Properties();
    private String fileName;

    public static boolean wbMessages = false;

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

        wbMessages = parseIntOrDefault(properties.getProperty(Constants.CONFIG_WB_MESSAGES), 1) != 0;
    }

    private static int parseIntOrDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void save() {
        try {
            File config = new File(fileName);
            boolean existed = config.exists();
            File parentDir = config.getParentFile();
            if (!parentDir.exists())
                parentDir.mkdirs();

            FileWriter configWriter = new FileWriter(config);

            writeBoolean(configWriter, Constants.CONFIG_WB_MESSAGES, wbMessages);

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
        writeString(configWriter, name, value ? "1" : "0");
    }

    static {
        defaultValues.setProperty(Constants.CONFIG_WB_MESSAGES, "0");
    }
}
