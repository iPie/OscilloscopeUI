package oscilloscopeui;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.*;

/**
 *
 * @author Ajarax
 */
public class SerialPortManager {

    private SerialPort serialPort;
    private DynamicChart chart;

    public SerialPortManager() {
    }

    public void addDynamicChart(DynamicChart chart) {
        this.chart = chart;
    }

    public void initializeSerialPort(String portName) throws SerialPortException {
        this.serialPort = new SerialPort(portName);
    }

    public String getSerialPortName() {
        return this.serialPort.getPortName();
    }

    public String[] getSerialPortList() {
        return SerialPortList.getPortNames();
    }

    public void startListening() throws SerialPortException {
        if (portExists()) {
            if (!serialPort.isOpened()) {
                serialPort.openPort();
                serialPort.setParams(Config.SerialPort.BAUND_RATE, Config.SerialPort.DATA_BITS,
                        Config.SerialPort.STOP_BITS, Config.SerialPort.PARITY);
                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
                serialPort.setEventsMask(mask);
                serialPort.addEventListener(new SerialPortReader());
                serialPort.writeByte((byte) 'R');
            } else {
                throw new SerialPortException(this.getSerialPortName(), "stopListening()", SerialPortException.TYPE_PORT_ALREADY_OPENED);
            }
        } else {
            throw new SerialPortException(this.getSerialPortName(), "startListening()", SerialPortException.TYPE_PORT_NOT_FOUND);
        }
    }

    public void stopListening() throws SerialPortException {
        if (portExists()) {
            if (serialPort.isOpened()) {
                serialPort.writeByte((byte) 'S');
                serialPort.closePort();
            } else {
                throw new SerialPortException(this.getSerialPortName(), "stopListening()", SerialPortException.TYPE_PORT_NOT_OPENED);
            }
        } else {
            throw new SerialPortException(this.getSerialPortName(), "stopListening()", SerialPortException.TYPE_PORT_NOT_FOUND);
        }
    }

    private boolean portExists() {
        for (String port : SerialPortList.getPortNames()) {
            if (port.equals(this.serialPort.getPortName())) {
                return true;
            }
        }
        return false;
    }

    private class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                int bytesCount = event.getEventValue();
                try {
                    byte buffer[] = serialPort.readBytes(event.getEventValue());
                    parseBuffer(buffer, bytesCount);
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }

        private void parseBuffer(byte[] buffer, int length) {
            try {
                for (int i = 0; i < length; i += 2) {
                    int value = (buffer[i + 1] << 8 | (buffer[i] & 0xFF)); // AND with 0xFF to handle unsigned types in Java
                    if (Config.SerialPort.DEBUG_SERIAL_PORT == true) {
                        System.out.println(value);
                    }
                    chart.bufferizeValue(value, Config.DynamicChart.ANTIALIAS_SAMPLES_COUNT);
                }
            } catch (Exception ex) {
                Logger.getLogger(SerialPortManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
