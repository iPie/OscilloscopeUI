package oscilloscopeui;

/**
 *
 * @author Ajarax
 */
public class Config {

    public static class SerialPort {

        public static int BAUND_RATE = 115200;
        public static int DATA_BITS = 8;
        public static int STOP_BITS = 1;
        public static int PARITY = 0;
    }

    public static class DynamicChart {

        public static boolean AUTO_SCALE = false;
        public static double MIN_RANGE = 0;
        public static double MAX_RANGE = 2.56;
        public static double REFERENCE_VOLTAGE = 2.56;
        public static int ADC_RESOLUTION = 1024;
        public static double GAIN = 0;
        public static int BUFFER_OFFSET = 200;
        public static int ANTIALIAS_SAMPLES_COUNT = 64;

        public static void setAutoScaleState(boolean value) {
            AUTO_SCALE = value;
        }

        public static void setAntialiasSamplesCount(int value) {
            ANTIALIAS_SAMPLES_COUNT = value;
        }

        public static void setMinRange(double value) {
            MIN_RANGE = value;
        }

        public static void setMaxRange(double value) {
            MAX_RANGE = value;
        }

        public static void setBufferOffset(int value) {
            BUFFER_OFFSET = value;
        }
    }
}
