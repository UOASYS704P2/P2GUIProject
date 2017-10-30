package localization_GUI.org.compsys704.p2.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import localization_GUI.org.compsys704.p2.entity.Position;
import localization_GUI.org.compsys704.p2.gui.MapPanel;

/**
 * Image component (showing the map in the frame)
 * @author KGL
 *
 */
public class ImageComponent extends JComponent {
	
	/** pointing to the MapPanel.history (this container includes all locations history) */
	private static List<Position> history = MapPanel.history;

    final BufferedImage img;

    public ImageComponent(String path) throws IOException {
        img = ImageIO.read(new File(path));
        setZoom(1);
    }
    
    public void addPosition(Position position) {
		history.add(position);
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = getPreferredSize();
        g.drawImage(img, 0, 0, dim.width, dim.height, this);
        
//        System.out.println(dim.getWidth() + "========================" + dim.getHeight());
        
        for (int i = 0; i < history.size(); i++) {
			if(i == history.size() - 1) {
				drawArrow(g, history.get(i), Color.BLUE, dim);
			}else {
				drawArrow(g, history.get(i), Color.RED, dim);
			}
		}
    }
    
    private void drawArrow(Graphics graphics, Position position, Color color, Dimension dim) {
		Path2D.Double arrow = createArrow();
		double theta = Math.toRadians(position.getOrientation());
		Graphics2D g2d = (Graphics2D)graphics;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX()*3.55*dim.getWidth()/1057, position.getY()*3.55*dim.getHeight()/1900);
        at.rotate(theta);
        at.scale(2.0, 2.0);
        Shape shape = at.createTransformedShape(arrow);
        g2d.setPaint(color);
        g2d.draw(shape);
	}
	
	private Path2D.Double createArrow() {
        int length = 5;
        int barb = 3;
        double angle = Math.toRadians(20);
        Path2D.Double path = new Path2D.Double();
        path.moveTo(-length/2, 0);
        path.lineTo(length/2, 0);
        double x = length/2 - barb*Math.cos(angle);
        double y = barb*Math.sin(angle);
        path.lineTo(x, y);
        x = length/2 - barb*Math.cos(-angle);
        y = barb*Math.sin(-angle);
        path.moveTo(length/2, 0);
        path.lineTo(x, y);
        return path;
    }

    public void setZoom(double zoom) {
        int w = (int) (zoom * img.getWidth());
        int h = (int) (zoom * img.getHeight());
        setPreferredSize(new Dimension(w, h));
        revalidate();
        repaint();
    }
}