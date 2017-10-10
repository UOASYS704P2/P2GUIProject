package localization_GUI.org.compsys704.p2.exception;

/**
 * @author KGL
 *
 */
public class SerialPortParameterFailure extends Exception {
	
	private static final long serialVersionUID = -4588440900339565032L;

	public SerialPortParameterFailure() {}

    @Override
    public String toString() {
        return "Exception: Fail to set serial parameters, cannot open serial port!";
    }
    
}