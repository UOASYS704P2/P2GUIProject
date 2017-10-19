package localization_GUI.org.compsys704.p2.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import localization_GUI.org.compsys704.p2.entity.Position;

/**
 * @author KGL
 *
 */
public class MapPanel extends JPanel {
	
	private static List<Position> history = new ArrayList<>();
	
	public MapPanel() {
		
	}
	
	public void addPosition(Position position) {
		history.add(position);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		for (Position position : history) {
			drawDot(graphics, position);
		}
	}
	
	private void drawDot(Graphics graphics, Position position) {
		graphics.setColor(Color.RED);
		graphics.fillOval(position.getX(), position.getY(), 3, 3);
	}
}
