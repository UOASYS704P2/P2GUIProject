package localization_GUI.org.compsys704.p2.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionWriter {
	/**
     * Encapsulate error information and send the information to the GUI
     * @param e Exception
     * @return 
     */
    public static String getErrorInfoFromException(Exception e) { 
            
            StringWriter sw = null;
            PrintWriter pw = null;
            
            try {  
                sw = new StringWriter();  
                pw = new PrintWriter(sw);  
                e.printStackTrace(pw);  
                return "\r\n" + sw.toString() + "\r\n";  
                
            } catch (Exception e2) {  
                return "Error, cannot catch exception! Please check!";  
            } finally {
                try {
                    if (pw != null) {
                        pw.close();
                    }
                    if (sw != null) {
                        sw.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
}
