package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.net.URL;
import javax.swing.ImageIcon;
import Model.Intersection;
import Ressources.FileDrop;

/**
 * GraphicalIntersection is the class that displays graphically a location on the drawn map.
 * 
 * A graphical intersection is composed of:
 * <ul>
 * <li>An intersection.</li>
 * <li>A point (coordinates of the position on screen).</li>
 * <li>An indication about if it is selected or not (used when adding a request).</li>
 * <li>An indication about if a mouse is over, when the intersection is a stop in a tour (used when a tour is computed).</li>
 * <li>A number that indicates its position if the intersection is a stop in a tour. </li>
 * </ul>
 * 
 * Graphical Intersections are created to display the intersection.<br/>
 * 
 * It can be style differently for particular intersections like stops or tour departure.
 * It can also be bigger or in red when there is an interaction with the mouse (click or over).
 * 
 * @author H4122
 * 
 * @see Model.Intersection
 * @see Model.Stop
 * @see View.DrawnMap
 */

public class GraphicalIntersection {

	@SuppressWarnings("unused")
	private Intersection intersection;
	private Point coordinates;
	private boolean selected;
	private boolean over;
	private int number;

	/**
	 * Class constructor.
	 * Creates a graphical intersection with the intersection and the coordinates in parameters.
	 * The graphical intersection is not selected and the mouse is not over it.
	 * The visiting number is 0 by default. It won't appear if the intersection is not a stop in a tour.
	 * 
	 * @param intersection The intersection associate to the graphical intersection.
	 * @param coordinates The coordinates of the position on screen.	 * 
	 */
	public GraphicalIntersection(Intersection intersection, Point coordinates) {
		this.intersection = intersection;
		this.coordinates = coordinates;
		this.selected = false;
		this.over = false;
		this.number=0;
	}
	
	public Point getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isOver() {
		return selected;
	}

	public void setOver(boolean over) {
		this.over = over;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Represents graphically the intersection on a map.
	 * It is a black oval. It is red when selected.
	 * 
	 * @param g an object of the graphics class.
	 * 
	 */

	public void draw(Graphics g) {
		if (this.selected) {
			g.setColor(Color.red);
			g.fillOval(coordinates.x-1, coordinates.y-1, 5, 5);
		}else {
			g.setColor(Color.black);
			g.fillOval(coordinates.x-1, coordinates.y-1, 3, 3);
		}

	}
	
	/**
	 * Represents graphically a particular intersection which is the tourDeparture with an icon.
	 * 
	 * @param g an object of the graphics class.
	 * @param drawnMap the graphical map containing the tourDeparture.
	 * 
	 * @see Model.Tour#tourDeparture
	 */

	public void drawDeparture(Graphics g, DrawnMap drawnMap) {
		g.setColor(Color.RED);
		URL path = FileDrop.class.getResource("Map_pin_icon.svg.png");
		ImageIcon icon = new ImageIcon(path);
		icon.paintIcon(drawnMap, g, coordinates.x-17, coordinates.y-12);
	
	}
	
	/**
	 * Represents graphically a particular intersection which is a stop in a tour.
	 * A different color is determined for every stop.
	 * The shape will be different depending on the type.
	 * 
	 * @param g an object of the graphics class.
	 * @param type defined whether it's a delivery or a pickup stop.
	 * @param color defined the color of the graphical stop.
	 * 
	 * @see Model.Stop
	 */

	public void drawStopRequest(Graphics g, String type, Color color) {

		g.setColor(color);
		if (type == "Pickup")
			g.fillOval(coordinates.x-6, coordinates.y-6, 13, 13);
		else if (type == "Delivery")
			g.fillRect(coordinates.x-6, coordinates.y-6, 13, 13);
	}
	
	/**
	 * Represents graphically a stop when a user clicks on it.
	 * The oval is bigger and red.
	 * The shape will be different depending on the type.
	 * 
	 * @param g an object of the graphics class.
	 * @param type defined whether it's a delivery or a pickup stop.
	 * 
	 * @see Model.Stop
	 */
	
	public void drawBiggerPoint(Graphics g, String type) {

		g.setColor(Color.red);
		if (type == "Pickup")
			g.fillOval(coordinates.x-10, coordinates.y-10, 20, 20);
		else if (type == "Delivery")
			g.fillRect(coordinates.x-10, coordinates.y-10, 20, 20);
	}
	
	/**
	 * Draws a number that indicates the visiting order of the stop in a tour.
	 * 
	 * @param g an object of the graphics class.
	 * 
	 * @see Model.Stop
	 * @see Model.Tour
	 */
	
	public void drawOver(Graphics g) {
		g.drawString(Integer.toString(this.number), coordinates.x+10,coordinates.y+10);
	}

}
