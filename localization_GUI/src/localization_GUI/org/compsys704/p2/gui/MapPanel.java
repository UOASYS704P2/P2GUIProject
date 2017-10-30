package localization_GUI.org.compsys704.p2.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import localization_GUI.org.compsys704.p2.entity.Position;

/**
 * @author KGL
 *
 */
public class MapPanel extends JPanel {
	
	public static List<Position> history = new ArrayList<>();
	BufferedImage mapIMG;
	
	public MapPanel() {
		
		try {
			mapIMG = ImageIO.read(new File("imgs/Map_small.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addPosition(Position position) {
		history.add(position);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.drawImage(mapIMG, 0, 0, null);
		
		for (int i = 0; i < history.size(); i++) {
			if(i == history.size() - 1) {
				drawArrow(graphics, history.get(i), Color.BLUE);
			}else {
				drawArrow(graphics, history.get(i), Color.RED);
			}
		}
	}
	
	private void drawArrow(Graphics graphics, Position position, Color color) {
		Path2D.Double arrow = createArrow();
		double theta = Math.toRadians(position.getOrientation());
		Graphics2D g2d = (Graphics2D)graphics;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
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
}
