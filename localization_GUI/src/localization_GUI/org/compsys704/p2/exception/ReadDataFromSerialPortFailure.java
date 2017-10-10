package localization_GUI.org.compsys704.p2.exception;

/**
 * @author KGL
 *
 */
public class ReadDataFromSerialPortFailure extends Exception {
	
	private static final long serialVersionUID = -4588440900339565032L;

	public ReadDataFromSerialPortFailure() {}

    @Override
    public String toString() {
        return "Exception: fail to read data from serial port!";
    }
    
}