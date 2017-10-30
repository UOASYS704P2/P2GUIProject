package localization_GUI.org.compsys704.p2.gui.component;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;

import localization_GUI.org.compsys704.p2.gui.MapFrame;

public class ImageViewer extends JComponent {
    final JSlider slider;
    public final ImageComponent image;
    final JScrollPane scrollPane;

    public ImageViewer(String path) throws IOException {
        slider = new JSlider(MapFrame.WIDTH, 1057, MapFrame.WIDTH);
        image = new ImageComponent(path);
        scrollPane = new JScrollPane(image);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                image.setZoom(2. * slider.getValue() / slider.getMaximum());
            }
        });
        
        MouseAdapter ma = new MouseAdapter() {

            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
            	setCursor(new Cursor(Cursor.HAND_CURSOR));
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, image);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        image.scrollRectToVisible(view);
                    }
                }
            }

        };

        image.addMouseListener(ma);
        image.addMouseMotionListener(ma);
        
        this.setLayout(new BorderLayout());
        this.add(slider, BorderLayout.NORTH);
        this.add(scrollPane);
    }
}