package localization_GUI.org.compsys704.p2.gui;

import java.awt.Color;
import java.awt.Graphics;
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
	
	private static List<Position> history = new ArrayList<>();
	BufferedImage mapIMG;
	
	public MapPanel() {
		
		try {
			mapIMG = ImageIO.read(new File("imgs/1.png"));
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
		
		for (Position position : history) {
			drawDot(graphics, position);
		}
		
		for (int i = 0; i < history.size(); i++) {
			if(i == history.size() - 1) {
				drawCurrentDot(graphics, history.get(i));
			}else {
				drawDot(graphics, history.get(i));
			}
		}
	}
	
	private void drawDot(Graphics graphics, Position position) {
		graphics.setColor(Color.RED);
		graphics.fillOval(position.getX(), position.getY(), 3, 3);
	}
	
	private void drawCurrentDot(Graphics graphics, Position position) {
		graphics.setColor(Color.BLUE);
		graphics.fillOval(position.getX(), position.getY(), 6, 6);
	}
}
