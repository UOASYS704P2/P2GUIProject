package localization_GUI.org.compsys704.p2.serial;

import java.util.TooManyListenersException;

import javax.swing.JOptionPane;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import localization_GUI.org.compsys704.p2.exception.NoSuchPort;
import localization_GUI.org.compsys704.p2.exception.NotASerialPort;
import localization_GUI.org.compsys704.p2.exception.PortInUse;
import localization_GUI.org.compsys704.p2.exception.ReadDataFromSerialPortFailure;
import localization_GUI.org.compsys704.p2.exception.SerialPortParameterFailure;

/**
 * @author KGL
 *
 */
public class ReadingTest {
	
	private static SerialTool SERIAL_TOOL = SerialTool.getSerialTool();
	
	private SerialPort serialPort = null;
	
	public ReadingTest() throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse {
		serialPort = SERIAL_TOOL.openPort("COM5", 115200);
	}
	
	public SerialPort getSerialPort() {
		return serialPort;
	}

	public static void main(String[] args) throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse, TooManyListenersException {
		ReadingTest awesomeTest = new ReadingTest();
		SERIAL_TOOL.addListener(awesomeTest.getSerialPort(), awesomeTest.new SerialListener());
	}
	
private class SerialListener implements SerialPortEventListener {
	
    public void serialEvent(SerialPortEvent serialPortEvent) {
        
        switch (serialPortEvent.getEventType()) {

            case SerialPortEvent.BI: // 10 lose connection
                JOptionPane.showMessageDialog(null, "lose connection!", "Erro", JOptionPane.INFORMATION_MESSAGE);
                break;
            case SerialPortEvent.OE: // 7 The OE port event signals an overrun error

            case SerialPortEvent.FE: // 9 The FE port event signals a framing error.

            case SerialPortEvent.PE: // 8 The PE port event signals a parity error
            
            case SerialPortEvent.CD: // 6 The CD port event is triggered when the Data Carrier Detect line on the port changes its logic level.

            case SerialPortEvent.CTS: // 3 clear buffered data

            case SerialPortEvent.DSR: // 4 ready to send data

            case SerialPortEvent.RI: // 5 The RI port event is triggered when the Ring Indicator line on the port changes its logic level.

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 output buffer has been cleared
                break;
            
            case SerialPortEvent.DATA_AVAILABLE: // 1 data exists in serial port
                
//                System.out.println("found data");
                byte[] data = null;
                
                try {
                    if (serialPort == null) {
                        JOptionPane.showMessageDialog(null, "serialPort obj is null, listen failed!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        data = SERIAL_TOOL.readFromPort(serialPort);    //read data and store in a byte[]
                        System.out.println(data);
                        
                        // parse customized receiving data
                        if (data == null || data.length < 1) {
                            JOptionPane.showMessageDialog(null, "didn't receive valid data!", "Error", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        } else {
                            String dataOriginal = new String(data);
                            System.out.println(dataOriginal);
                        }
                    }
                } catch (ReadDataFromSerialPortFailure e) {
                    JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                break;
            }
        }
    }
}
