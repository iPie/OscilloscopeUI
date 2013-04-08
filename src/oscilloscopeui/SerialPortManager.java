package oscilloscopeui;

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
                        Config.SerialPort.STOP_BITS, Config.SerialPort.PARITY);//Set params
                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
                serialPort.setEventsMask(mask);//Set mask   
                serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener 
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
            } else if (event.isCTS()) {//If CTS line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) {///If DSR line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }
        }

        private void parseBuffer(byte[] buffer, int length) {
            try {
                for (int i = 0; i < length - 2; i += 2) {
                    if (buffer[i] == '\0' || buffer[i + 1] == '\0') {
                        if (i > length - 3) {
                            break;
                        }
                        i++;
                    }
                    int res = buffer[i + 1] << 8 | buffer[i];
                    if (res > 1023) {
                        i++;
                    } else {
                        chart.bufferizeValue(res, Config.DynamicChart.ANTIALIAS_SAMPLES_COUNT);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(SerialPortManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
