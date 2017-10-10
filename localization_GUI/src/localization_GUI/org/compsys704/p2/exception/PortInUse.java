package localization_GUI.org.compsys704.p2.exception;

/**
 * @author KGL
 *
 */
public class PortInUse extends Exception {
	
	private static final long serialVersionUID = -4588440900339565032L;

	public PortInUse() {}

    @Override
    public String toString() {
        return "Exception: port in use!";
    }
    
}