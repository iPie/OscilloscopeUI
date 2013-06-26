/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oscilloscopeui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ajarax
 */
public class SerialPortManagerTest {
    
    public SerialPortManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addDynamicChart method, of class SerialPortManager.
     */
    @Test
    public void testAddDynamicChart() {
        System.out.println("addDynamicChart");
        DynamicChart chart = null;
        SerialPortManager instance = new SerialPortManager();
        instance.addDynamicChart(chart);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initializeSerialPort method, of class SerialPortManager.
     */
    @Test
    public void testInitializeSerialPort() throws Exception {
        System.out.println("initializeSerialPort");
        String portName = "";
        SerialPortManager instance = new SerialPortManager();
        instance.initializeSerialPort(portName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSerialPortName method, of class SerialPortManager.
     */
    @Test
    public void testGetSerialPortName() {
        System.out.println("getSerialPortName");
        SerialPortManager instance = new SerialPortManager();
        String expResult = "";
        String result = instance.getSerialPortName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSerialPortList method, of class SerialPortManager.
     */
    @Test
    public void testGetSerialPortList() {
        System.out.println("getSerialPortList");
        SerialPortManager instance = new SerialPortManager();
        String[] expResult = null;
        String[] result = instance.getSerialPortList();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startListening method, of class SerialPortManager.
     */
    @Test
    public void testStartListening() throws Exception {
        System.out.println("startListening");
        SerialPortManager instance = new SerialPortManager();
        instance.startListening();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stopListening method, of class SerialPortManager.
     */
    @Test
    public void testStopListening() throws Exception {
        System.out.println("stopListening");
        SerialPortManager instance = new SerialPortManager();
        instance.stopListening();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}