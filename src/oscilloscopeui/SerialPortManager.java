/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oscilloscopeui;

import info.monitorenter.gui.chart.Chart2D;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import jssc.*;
import javax.swing.JTextArea;
import sun.text.normalizer.IntTrie;

/**
 *
 * @author Ajarax
 */
public class SerialPortManager {

    private SerialPort serialPort;
    //private String portName;
    private JTextArea jTextArea;
    private DynamicChart chart;
    private int baundRate;
    private int dataBits;
    private int stopBits;
    private int parity;

    public SerialPortManager() {
        //
    }

    public void setJTextArea(javax.swing.JTextArea area) {
        this.jTextArea = area;
    }

    public void addDynamicChart(DynamicChart chart) {
        this.chart = chart;
    }

    public void setSerialPort(String portName) {
        this.serialPort = new SerialPort(portName);
    }

    public String getSerialPort() {
        return this.serialPort.getPortName();
    }

    public String[] getSerialPortList() {
        return SerialPortList.getPortNames();
    }

    public void startListening() throws SerialPortException {
        serialPort.openPort();//Open port
        serialPort.setParams(115200, 8, 1, 0);//Set params
        int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
        serialPort.setEventsMask(mask);//Set mask
        serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener        
    }

    public void stopListening() throws SerialPortException {
        serialPort.closePort();
    }

    private class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {//If data is available
                int bytesCount = event.getEventValue();
                try {
                    byte buffer[] = serialPort.readBytes(event.getEventValue());
                    String result = parseBuffer(buffer, bytesCount);
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

        private String parseBuffer(byte[] buffer, int length) {
            String result;
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
                        chart.addY(res * 2.56 / 1024);
                    }
                }
                result = null;
            } catch (/*
                     * UnsupportedEncodingException ex
                     */Exception ex) {
                System.out.println("Unable to parse input");
                Logger.getLogger(SerialPortManager.class.getName()).log(Level.SEVERE, null, ex);
                result = null;
            }
            return result;
        }
    }
}
