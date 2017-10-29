package localization_GUI.org.compsys704.p2.gui.component;
import java.awt.*;
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

        this.setLayout(new BorderLayout());
        this.add(slider, BorderLayout.NORTH);
        this.add(scrollPane);
    }
}