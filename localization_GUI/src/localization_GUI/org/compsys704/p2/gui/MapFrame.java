package localization_GUI.org.compsys704.p2.gui;

import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.JFrame;

import localization_GUI.org.compsys704.p2.gui.component.ImageViewer;

/**
 * @author KGL
 *
 */
public class MapFrame extends JFrame{
	
	private static final MapFrame INSTANCE = new MapFrame();
	
	private static final int X = 750;
	private static final int Y = 0;
    public static final int WIDTH = 473;
    public static final int HEIGHT = 850;
    
    public ImageViewer imageViewer;
    
    public static MapFrame getInstance() {
    	return INSTANCE;
    }

	private MapFrame() {
		setBounds(new Rectangle(X, Y, WIDTH, HEIGHT));
        
        try {
			imageViewer = new ImageViewer("imgs/Map_full.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
        getContentPane().add(imageViewer);
        
        setResizable(false);
        new Thread(new RepaintThread()).start();
	}
	
	private class RepaintThread implements Runnable {

		@Override
		public void run() {
			while(true) {
                repaint();
			}
		}
		
	}
}
