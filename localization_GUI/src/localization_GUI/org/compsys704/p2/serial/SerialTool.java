package localization_GUI.org.compsys704.p2.serial;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import localization_GUI.org.compsys704.p2.exception.NoSuchPort;
import localization_GUI.org.compsys704.p2.exception.NotASerialPort;
import localization_GUI.org.compsys704.p2.exception.PortInUse;
import localization_GUI.org.compsys704.p2.exception.ReadDataFromSerialPortFailure;
import localization_GUI.org.compsys704.p2.exception.SerialPortParameterFailure;

/**
 * @author KGL
 *
 */
public class SerialTool {
	
	private static SerialTool serialTool = null;
    
    static {
        //init an SerialTool Obj
        if (serialTool == null) {
            serialTool = new SerialTool();
        }
    }
    
    private SerialTool() {}
    
    /**
     * get singleton instance
     * @return serialTool
     */
    public static SerialTool getSerialTool() {
        if (serialTool == null) {
            serialTool = new SerialTool();
        }
        return serialTool;
    }
    
    /**
     * load available ports' names
     * @return available ports' names
     */
    public final ArrayList<String> findPort() {

        //load current available serial ports
        @SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();    
        
        ArrayList<String> portNameList = new ArrayList<>();

        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }

        return portNameList;
    }
    
    /**
     * open serial port by name
     * @param portName
     * @param baudrate
     * @return serial port obj
     * @throws SerialPortParameterFailure
     * @throws NotASerialPort
     * @throws NoSuchPort
     * @throws PortInUse
     */
    public final SerialPort openPort(String portName, int baudrate) throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse {

        try {

            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            CommPort commPort = portIdentifier.open(portName, 2000);

            if (commPort instanceof SerialPort) {
                
                SerialPort serialPort = (SerialPort) commPort;
                
                try {                        
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);                              
                } catch (UnsupportedCommOperationException e) {  
                    throw new SerialPortParameterFailure();
                }
                
                //System.out.println("Open " + portName + " successfully!");
                return serialPort;
            
            }
            else {
                throw new NotASerialPort();
            }
        } catch (NoSuchPortException e1) {
          throw new NoSuchPort();
        } catch (PortInUseException e2) {
            throw new PortInUse();
        }
    }
    
    /**
     * close serial port
     * @param serialport
     */
    public void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }
    
    /**
     * read data from serial port
     * @param serialPort
     * @return data
     * @throws ReadDataFromSerialPortFailure
     * @throws SerialPortInputStreamCloseFailure
     */
    public byte[] readFromPort(SerialPort serialPort) throws ReadDataFromSerialPortFailure {

        byte[] bytes = null;
        
        try (InputStream in = serialPort.getInputStream()){
            
            int bufflenth = in.available();
            
            while (bufflenth != 0) {                 
                bytes = new byte[bufflenth];
                in.read(bytes);
                bufflenth = in.available();
            } 
        } catch (IOException e) {
            throw new ReadDataFromSerialPortFailure();
        }
        return bytes;
    }
    
    /**
     * add event listener
     * @param port
     * @param listener
     * @throws TooManyListenersException 
     * @throws TooManyListeners
     */
    public void addListener(SerialPort port, SerialPortEventListener listener) throws TooManyListenersException {
        //add event listener
        port.addEventListener(listener);
        //notify the thread once data comes in
        port.notifyOnDataAvailable(true);
        //notify the interrupt thread once lost connection
        port.notifyOnBreakInterrupt(true);
    }
}
