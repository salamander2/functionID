package ca.quarkphysics.harwood.sciOlympics;

import java.awt.*;
import javax.swing.*;

class GraphPanel extends JPanel
{

	//GLOBAL VARIABLES used in GraphPanel
	//These are the co-ordinates to specify the size of the screen (x and y axes).
	static double xMin = -10.0, xMax = 10.0, yMin = -10.0, yMax = 10.0;
	//Used to find the size of screen (panel or canvas) in pixels.
	static int scrW, scrH;
	//The size of the steps used in drawing pixel by pixel.
	static double xStep, yStep;

	static Color COLOURGRID = new Color(160, 160, 160, 128);	//the colour for the grid lines
	static Color COLOURPOINT = Color.BLUE;								//the colour for the points that make up the graph

	//INSTANCE VARIABLES for GraphPanel
	boolean GRIDON = true;	//is the grid displayed?
	boolean ZOOMON = false;	//is the graph zoomed in?

	//constructor
	GraphPanel() {
		super(); //is this needed?
		setBackground(Color.WHITE);
		initGraphics();
	}

	/*********************************************************************************
			initGraphics()
	This function sets up the screen size variables.
	It needs to be recalculated if the screen is resized, if the zoom changes,
	or if the panel size changes due to Table of Values being shown.
	Called from: GraphPanel constructor, paintComponent
	**********************************************************************************/
	void initGraphics() {
		scrW = super.getSize().width;
		scrH = super.getSize().height;
		xStep = (xMax - xMin) / scrW;
		yStep = (yMax - yMin) / scrH;
	}

	/*********************************************************************************
	Name:		paintComponent()
	Purpose:	This function paints the graph on the GraphPanel.
	Called automatically by: repaint()
				repaint() is called from: toggleHelp, MainMenuListener.actionPerformed(),
	Calls: 		initGraphics, plotLine, plotPoint, EqnData.evaluateEqn
	**********************************************************************************/
	public void paintComponent(Graphics g) 	{
		super.paintComponent(g);
		initGraphics();

		//draw labels and draw Grid (if needed)
		for (int i=(int)xMin; i < (int)xMax; i++) {
			if (GRIDON) plotLine(i, yMin, i, yMax, COLOURGRID, g);	// draw grid?

			//labels on axes. Calculation of the location is copied from plotPoint
			int px1 = (int) (scrW * (i - xMin) / (xMax - xMin));
			int y1 = 0;
			int py1 = scrH - (int) (scrH * (y1 - yMin) / (yMax - yMin));
			g.setColor(Color.BLACK);
			g.drawString(i+"", px1+1, py1-1);
		}
		for (int j=(int)yMin; j < (int)yMax; j++) {
			if (GRIDON)  plotLine(xMin,j,xMax,j,COLOURGRID,g);
			int x1 = 0;
			int px1 = (int) (scrW * (x1 - xMin) / (xMax - xMin));
			int py1 = scrH - (int) (scrH * (j - yMin) / (yMax - yMin));
			g.setColor(Color.BLACK);
			g.drawString(j+"", px1+2, py1-1);
		}

		//draw Axes
		plotLine(xMin, 0, xMax, 0, Color.BLACK, g);
		plotLine(0, yMin, 0, yMax, Color.BLACK, g);


		if (!Function.equation.RPN.equals("")) {
			for (double d = xMin; d < xMax; d += xStep) {
				plotPoint(d, EqnData.evaluateEqn(d), COLOURPOINT, g);
			}
		}
	} //end of paintComponent

	/*********************************************************************************
	Name:		plotPoint()
	Purpose:	This function draws a point at the correct pixel based on the screen size
				and the scale of the graph.
	Parameters: x1 and y1 are the point coordinates in scale space, not screen pixel locations.
	Called from: paintComponent()
	Calls: --
	**********************************************************************************/
	public void plotPoint(double x1, double y1, Color col, Graphics g) {
		// plotLine(x,y,x,y,col,g);
		int px1 = (int) (scrW * (x1 - xMin) / (xMax - xMin));
		int py1 = scrH - (int) (scrH * (y1 - yMin) / (yMax - yMin));
		g.setColor (col);
		g.drawLine (px1, py1, px1, py1);
		// System.out.println(px1 + " " + py1);
	}


	/*********************************************************************************
	Name:		plotLine()
	Purpose:	This function draws lines on the screen based on the screen size
				and the scale of the graph.	This method screws up if the points are are too large or too small:
				strange lines appear on the screen. I haven't bothered to try and duplicate it
				in Java 1.6. It was a problem in Java 1.2.
	Parameters: x1 and y1 are the point coordinates in scale space, not screen pixel locations.
	Called from: paintComponent()
	Calls: --
	**********************************************************************************/
	public void plotLine (double x1, double y1, double x2, double y2, Color col, Graphics g) {
		/* This method screws up when a number results in an int that it too big or two small so that it wraps around.
		e.g. plotLine(-9.46875,-1295.2,-9.5,-1306.625,Color.red);
			c.print("\n\tpx1=" + px1 + "\tpy1=" + py1 + "\tpx2=" + px2 + "\tpy2=" + py2);
		This results in py1 = 32630  py2 = 32915, px1 = 17, px2 = 16. A vertical line is drawn down the screen!!!
		It looks like the real drawLine converts everything to a short, but I can't find any reference to this in the documentation.
		*/

		int px1 = (int) (scrW * (x1 - xMin) / (xMax - xMin));
		int px2 = (int) (scrW * (x2 - xMin) / (xMax - xMin));
		int py1 = scrH - (int) (scrH * (y1 - yMin) / (yMax - yMin));
		int py2 = scrH - (int) (scrH * (y2 - yMin) / (yMax - yMin));

		g.setColor (col);
		//Test for integer wrapping.
		/*if (px1 > Short.MAX_VALUE)
			return;
			if (px2 > Short.MAX_VALUE)
			return;
			if (py1 > Short.MAX_VALUE)
			return;
			if (py2 > Short.MAX_VALUE)
			return; */
		g.drawLine (px1, py1, px2, py2);
		return;
	} //end of PlotLine


} //end of class
