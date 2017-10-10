package localization_GUI.org.compsys704.p2.exception;

/**
 * @author KGL
 *
 */
public class NoSuchPort extends Exception {
	
	private static final long serialVersionUID = -4588440900339565032L;

	public NoSuchPort() {}

    @Override
    public String toString() {
        return "Exception: No such port exists!";
    }
    
}