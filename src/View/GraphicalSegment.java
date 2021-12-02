package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.*;
import java.awt.geom.Line2D;

import Model.Segment;

/**
 * GraphicalSegment is the class that displays graphically on the drawn map.
 * 
 * A graphical segment is composed of:
 * <ul>
 * <li>A segment.</li>
 * <li>A point (coordinates of the position on screen of the beginning of the segment).</li>
 * <li>A point (coordinates of the position on screen of the end of segment).</li>
 * <li>An indication about if it is selected or not. If applicable, it shows the name of the road.</li>
 * </ul>
 * 
 * Graphical Segments are created to display the segment (road of a map).<br/>
 * 
 * It can be red when there is an interaction with the mouse (over).
 * 
 * @author H4122
 * 
 * @see Model.Segment
 * @see View.DrawnMap
 */
public class GraphicalSegment {

	@SuppressWarnings("unused")
	private Segment segment;
	private Point coordinatesDeparture;
	private Point coordinatesArrival;
	private boolean selected;

	/**
	 * Class constructor.
	 * Creates a graphical segment with the segment, the coordinates departure, the coordinates arrival and selected in parameters.
	 * 
	 * @param segment The segment associate to the graphical segment.
	 * @param coordinatesDeparture It determines of the position on screen of the departure of the segment.
	 * @param coordinatesArrival It determines the position on screen of the arrival of the segment.
	 * @param selected It determines whether a mouse is over the segment or not.
	 * 
	 */
	public GraphicalSegment(Segment segment, Point coordinatesDeparture, Point coordinatesArrival, boolean selected) {
		this.segment = segment;
		this.coordinatesDeparture = coordinatesDeparture;
		this.coordinatesArrival = coordinatesArrival;
		this.selected = selected;
	}

	public Segment getSegment() {
		return segment;
	}

	public Point getCoordinatesDeparture() {
		return coordinatesDeparture;
	}


	public Point getCoordinatesArrival() {
		return coordinatesArrival;
	}


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Represents graphically the segment on a map.
	 * It is a line. When selected, it is red and the name of the road is displayed.
	 * 
	 * @param g an object of the graphics class.
	 * 
	 */
	
	public void draw(Graphics g) {
		if (this.selected) {
			g.setColor(Color.red);
			g.drawString(segment.getName(), (coordinatesArrival.x+coordinatesDeparture.x)/2, (coordinatesArrival.y+coordinatesDeparture.y)/2);
		}else {
			g.setColor(Color.black);
		}	
		g.drawLine(coordinatesDeparture.x, coordinatesDeparture.y, coordinatesArrival.x, coordinatesArrival.y);
	}

	/**
	 * Sources : https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
	 * 
	 * When a segment is used on a tour, the segment is drawn differently to show the path.
	 * 
	 * @param g an object of the graphics class.
	 */
	
	public void drawPath(Graphics g) {
		g.setColor(Color.blue);
		int x1 = coordinatesDeparture.x;
		int y1 = coordinatesDeparture.y;
		int x2 = coordinatesArrival.x;
		int y2 = coordinatesArrival.y;
		int d = 6;
		int h = 6;
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;

	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};

	    Graphics2D g2= (Graphics2D) g;
	    g2.setStroke(new BasicStroke(3));
	    g2.draw(new Line2D.Float(x1, y1, x2, y2));
	    g.fillPolygon(xpoints, ypoints, 3);
	}


}
