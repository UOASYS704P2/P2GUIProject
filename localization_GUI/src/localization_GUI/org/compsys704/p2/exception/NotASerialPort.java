package localization_GUI.org.compsys704.p2.exception;

/**
 * @author KGL
 *
 */
public class NotASerialPort extends Exception {
	
	private static final long serialVersionUID = -4588440900339565032L;

	public NotASerialPort() {}

    @Override
    public String toString() {
        return "Exception: This is not a serial port!";
    }
    
}