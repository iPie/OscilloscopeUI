package oscilloscopeui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ajarax
 */
public class Config {

    private static String CONFIG_FILE_PATH = System.getProperty("user.dir").concat("\\OscilloscopeUI.cfg");
    //private static String CONFIG_FILE_PATH = "d:/Soft/Java/NetBeansProjects/OscilloscopeUI/dist/OscilloscopeUI.cfg";

    public static class SerialPort {

        public static int BAUND_RATE = 115200;
        public static int DATA_BITS = 8;
        public static int STOP_BITS = 1;
        public static int PARITY = 0;
        public static boolean DEBUG_SERIAL_PORT = false;

        public static void setBaundRate(int value) {
            BAUND_RATE = value;
        }

        public static void setDataBits(int value) {
            DATA_BITS = value;
        }

        public static void setStopBits(int value) {
            STOP_BITS = value;
        }

        public static void setParity(int value) {
            PARITY = value;
        }

        public static void setDebugSerialPort(boolean value) {
            DEBUG_SERIAL_PORT = value;
        }
    }

    public static class Device {

        public static byte DEVICE_START_COMMAND = 0x52;
        public static byte DEVICE_STOP_COMMAND = 0x53;

        public static void setDeviceStartCommand(byte value) {
            DEVICE_START_COMMAND = value;
        }

        public static void setDeviceStopCommand(byte value) {
            DEVICE_STOP_COMMAND = value;
        }
    }

    public static class DynamicChart {

        public static boolean AUTO_SCALE = false;
        public static double MIN_RANGE = -5;
        public static double MAX_RANGE = 5;
        public static double REFERENCE_VOLTAGE = 2.56;
        public static int ADC_RESOLUTION = 1024;
        public static double GAIN = -1.38;
        public static double MULTIPLIER = 5.54545455;
        public static int BUFFER_OFFSET = 200;
        public static int ANTIALIAS_SAMPLES_COUNT = 64;
        public static int CALIBRATION_VALUES_COUNT = 32;

        public static void setAutoScale(boolean value) {
            AUTO_SCALE = value;
        }

        public static void setMinRange(double value) {
            MIN_RANGE = value;
        }

        public static void setMaxRange(double value) {
            MAX_RANGE = value;
        }

        public static void setReferenceVoltage(double value) {
            REFERENCE_VOLTAGE = value;
        }

        public static void setADCResolution(int value) {
            ADC_RESOLUTION = value;
        }

        public static void setGain(double value) {
            GAIN = value;
        }

        public static void setMultiplier(double value) {
            MULTIPLIER = value;
        }

        // TODO: restrict overflow (max = int size / value)
        public static void setBufferOffset(int value) {
            BUFFER_OFFSET = value;
        }

        public static void setAntialiasSamplesCount(int value) {
            ANTIALIAS_SAMPLES_COUNT = value;
        }

        // TODO: restrict overflow!
        public static void setCalibrationValuesCount(int value) {
            CALIBRATION_VALUES_COUNT = value;
        }
    }

    public static void loadConfigFile() {
        Properties configFile = new Properties();
        try {
            FileReader file = new FileReader(CONFIG_FILE_PATH);
            configFile.load(file);
        } catch (Exception e) {
            Logger.getLogger(Config.class.getName()).log(Level.WARNING, "Failed to open configuration file", e);
            return;
        }
        try {
            // Commented values probably should not be saved
            //Config.DynamicChart.setAutoScale(Boolean.parseBoolean(configFile.getProperty("AUTO_SCALE")));
            Config.DynamicChart.setMinRange(Double.parseDouble(configFile.getProperty("MIN_RANGE")));
            Config.DynamicChart.setMaxRange(Double.parseDouble(configFile.getProperty("MAX_RANGE")));
            Config.DynamicChart.setReferenceVoltage(Double.parseDouble(configFile.getProperty("REFERENCE_VOLTAGE")));
            Config.DynamicChart.setADCResolution(Integer.parseInt(configFile.getProperty("ADC_RESOLUTION")));
            Config.DynamicChart.setGain(Double.parseDouble(configFile.getProperty("GAIN")));
            Config.DynamicChart.setMultiplier(Double.parseDouble(configFile.getProperty("MULTIPLIER")));
            Config.DynamicChart.setBufferOffset(Integer.parseInt(configFile.getProperty("BUFFER_OFFSET")));
            //Config.DynamicChart.setAntialiasSamplesCount(Integer.parseInt(configFile.getProperty("ANTIALIAS_SAMPLES_COUNT")));
            Config.DynamicChart.setCalibrationValuesCount(Integer.parseInt(configFile.getProperty("CALIBRATION_VALUES_COUNT")));
            Config.SerialPort.setBaundRate(Integer.parseInt(configFile.getProperty("BAUND_RATE")));
            Config.SerialPort.setDataBits(Integer.parseInt(configFile.getProperty("DATA_BITS")));
            Config.SerialPort.setStopBits(Integer.parseInt(configFile.getProperty("STOP_BITS")));
            Config.SerialPort.setParity(Integer.parseInt(configFile.getProperty("PARITY")));
            Config.SerialPort.setDebugSerialPort(Boolean.parseBoolean(configFile.getProperty("DEBUG_SERIAL_PORT")));
            Config.Device.setDeviceStartCommand(Byte.parseByte(configFile.getProperty("DEVICE_START_COMMAND")));
            Config.Device.setDeviceStopCommand(Byte.parseByte(configFile.getProperty("DEVICE_STOP_COMMAND")));
        } catch (Exception e) {
            Logger.getLogger(Config.class.getName()).log(Level.WARNING, "Invalid configuration file", e);
        }
    }

    public static void saveConfigFile() {
        Properties configFile = new Properties();
        try {
            // Commented values probably should not be saved
            //configFile.setProperty("AUTO_SCALE", Boolean.toString(Config.DynamicChart.AUTO_SCALE));
            configFile.setProperty("MIN_RANGE", Double.toString(Config.DynamicChart.MIN_RANGE));
            configFile.setProperty("MAX_RANGE", Double.toString(Config.DynamicChart.MAX_RANGE));
            configFile.setProperty("REFERENCE_VOLTAGE", Double.toString(Config.DynamicChart.REFERENCE_VOLTAGE));
            configFile.setProperty("ADC_RESOLUTION", Integer.toString(Config.DynamicChart.ADC_RESOLUTION));
            configFile.setProperty("GAIN", Double.toString(Config.DynamicChart.GAIN));
            configFile.setProperty("MULTIPLIER", Double.toString(Config.DynamicChart.MULTIPLIER));
            configFile.setProperty("BUFFER_OFFSET", Integer.toString(Config.DynamicChart.BUFFER_OFFSET));
            //configFile.setProperty("ANTIALIAS_SAMPLES_COUNT", Integer.toString(Config.DynamicChart.ANTIALIAS_SAMPLES_COUNT));
            configFile.setProperty("CALIBRATION_VALUES_COUNT", Integer.toString(Config.DynamicChart.CALIBRATION_VALUES_COUNT));
            configFile.setProperty("BAUND_RATE", Integer.toString(Config.SerialPort.BAUND_RATE));
            configFile.setProperty("DATA_BITS", Integer.toString(Config.SerialPort.DATA_BITS));
            configFile.setProperty("STOP_BITS", Integer.toString(Config.SerialPort.STOP_BITS));
            configFile.setProperty("PARITY", Integer.toString(Config.SerialPort.PARITY));
            configFile.setProperty("DEBUG_SERIAL_PORT", Boolean.toString(Config.SerialPort.DEBUG_SERIAL_PORT));
            configFile.setProperty("DEVICE_START_COMMAND", Byte.toString(Config.Device.DEVICE_START_COMMAND));
            configFile.setProperty("DEVICE_STOP_COMMAND", Byte.toString(Config.Device.DEVICE_STOP_COMMAND));
            Logger.getLogger(Config.class.getName()).log(Level.INFO, "Saving ".concat(CONFIG_FILE_PATH));
            File file = new File(CONFIG_FILE_PATH);
            try (OutputStream out = new FileOutputStream(file)) {
                configFile.store(out, "OscilloscopeUI settings");
            }
        } catch (Exception e) {
            Logger.getLogger(Config.class.getName()).log(Level.WARNING, "Unable to save configuration file", e);
        }
    }
}
