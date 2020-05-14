package ca.quarkphysics.harwood.sciOlympics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

// Version: 1.0
/* This program is to help students identify functions
based on graphs or table of values. It was written for
the London District Science Olympics at UWO. */

/* ****************************** Program flow *****************************
(1) Application: main --> constructor --> createAndShowGUI()
(2) Applet: constructor (does nothing) --> init() --> createAndShowGUI()
    Applet documentation says that the program should be run from init() not the constructor.
***************************************************************************/
public class Function extends JApplet
{
	// Start as Application
	public static void main(String[] args){
		ISAPPLET = false; //important!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Function progObject = new Function();
			}});
	}

	//constructor
	public Function() {		// Java documentation: "applet should avoid calling methods in java.applet.Applet in the constructor."
		if (!ISAPPLET) createAndShowGUI();
	}

	public void init() {
		createAndShowGUI();
	}

	//static variables
	static boolean ISAPPLET = true;
	static boolean HELPON = false;	//needs to be static so that HELPPANEL can access it.
	static final int NVALUES = 21;
	static ArrayList<EqnData> linear;
	static ArrayList<EqnData> quadratic;
	static ArrayList<EqnData> polynomial;
	static ArrayList<EqnData> rational;
	static ArrayList<EqnData> trigonometric;
	static ArrayList<EqnData> expon_log;
	// static EqnData equation = null;
	static EqnData equation = new EqnData("","",0,0,0,0);

	//instance variables - these can't be used in the GraphPanel class (in other file)
	private Container content;
	private GraphPanel grpanel;
	private HelpPanel helppanel;
	private JPanel vpanel;
	private JTextField txtInput;
	private JLabel[] xvalues;
	private JLabel[] yvalues;
	private MainMenuListener mml;
	private MyMenuItem aboutItem, helpItem;
	private MyMenuItem zoomItem, gridItem, valueItem;
	private MyMenuItem linearItem, quadItem, polyItem, rationItem, trigItem, expItem;
	private boolean VALUEON = false;
	private boolean eqnCorrect = false;

	/*********************************************************************************
	Name:		createAndShowGUI()
	Purpose:	This function sets the new values for the zoom.
				It alternates between (-10,-10)-(10,10) and (-5,-5)-(5,5) or a custom zoom
	Called by: 	constructor (if application) or init() (if applet)
	Calls: 		EqnData.loadData(),
	**********************************************************************************/
	private void createAndShowGUI() {
		//local variables
		JFrame frame = null;

		EqnData.loadData();
		if (ISAPPLET) {
			this.setSize(800, 600);
			content = getContentPane();
		} else {
			//JFrame.setDefaultLookAndFeelDecorated(true);
			frame = new JFrame("Identifying Equations");
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			frame.setSize(800, 600);
			frame.setResizable(true);
			frame.setLocationRelativeTo(null);
			content = frame.getContentPane();
		}

		content.setBackground(Color.BLACK);
		content.setLayout(new BorderLayout(3,3));

		//make menus
		JMenuBar menuBar = new JMenuBar();
		mml = new MainMenuListener();
		if (ISAPPLET)
			setJMenuBar(menuBar);
		else
			frame.setJMenuBar(menuBar);	//not on contentpane

		//View menu
		JMenu mnuView = new JMenu("View");					mnuView.setMnemonic('V');
		zoomItem = new MyMenuItem("Zoom in", 'Z');			mnuView.add(zoomItem);
		gridItem = new MyMenuItem("Grid", 'G');				mnuView.add(gridItem);
		valueItem = new MyMenuItem("Table of Values", 'V');	mnuView.add(valueItem);
		menuBar.add(mnuView);

		//Equation menu
		JMenu mnuEqn = new JMenu("Equation type");			mnuEqn.setMnemonic('E');
		linearItem = new MyMenuItem("Linear", 'L');			mnuEqn.add(linearItem);
		quadItem = new MyMenuItem("Quadratic", 'Q');		mnuEqn.add(quadItem);
		polyItem = new MyMenuItem("Polynomial", 'P');		mnuEqn.add(polyItem);
		rationItem = new MyMenuItem("Rational", 'R');		mnuEqn.add(rationItem);
		trigItem = new MyMenuItem("Trigonometric", 'T');	mnuEqn.add(trigItem);
		expItem = new MyMenuItem("Exp & Log", 'E');			mnuEqn.add(expItem);
		menuBar.add(mnuEqn);

		//Move help menu to right side
		menuBar.add(Box.createHorizontalGlue());

		//Help Menu
		JMenu mnuHelp = new JMenu("Help");				mnuHelp.setMnemonic('H');
		helpItem = new MyMenuItem("General Help", 'H');	mnuHelp.add(helpItem);
		aboutItem = new MyMenuItem("About", 'A');		mnuHelp.add(aboutItem);
		menuBar.add(mnuHelp);

		grpanel = new GraphPanel();	//must be AFTER scrW and scrH are set (huh? they're set in the constructor)
		content.add(grpanel, BorderLayout.CENTER);
		helppanel = new HelpPanel(this);

		//panel for textfield
		JPanel txtpanel = new JPanel();
		txtpanel.add(new JLabel("Guess equation:"));
		txtInput = new JTextField(40);
		txtpanel.add(txtInput);
		//TextFieldListener myTFL = new TextFieldListener();
		txtInput.addActionListener(new TextFieldListener());
		txtInput.addKeyListener(new TextKeyListener());

		content.add(txtpanel, BorderLayout.SOUTH);

		//panel for values
		vpanel = new JPanel();
		vpanel.setBackground(new Color(120,120,120));
		vpanel.setLayout(new GridLayout(NVALUES+1,2,1,1));
		//header buttons
		JButton xbutton = new JButton("X");
		xbutton.setPreferredSize(new Dimension(80,30));
		vpanel.add(xbutton);
		vpanel.add(new JButton("Y"));
		//set up grid of JLabels for (x,y) values
		xvalues = new JLabel[NVALUES];
		yvalues = new JLabel[NVALUES];
		for (int i = 0; i < NVALUES; i++) {
			xvalues[i] = new JLabel(" 0");
			yvalues[i] = new JLabel(" 0");
			xvalues[i].setOpaque(true);
			yvalues[i].setOpaque(true);
			xvalues[i].setHorizontalAlignment(SwingConstants.CENTER);
			yvalues[i].setHorizontalAlignment(SwingConstants.CENTER);
			vpanel.add(xvalues[i]);
			vpanel.add(yvalues[i]);
		}
		content.add(vpanel, BorderLayout.EAST);

		content.validate();

		if (!ISAPPLET) frame.setVisible(true);
		if (!VALUEON)  vpanel.setVisible(false);
	} // end of createAndShowGUI

	/*********************************************************************************
	Name:		setZoom()
	Purpose:	This function sets the new values for the zoom.
				It alternates between (-10,-10)-(10,10) and (-5,-5)-(5,5) or a custom zoom
	Called by: 	actionPerformed() in MainMenuListener class
	Calls: 		---
	**********************************************************************************/
	private void setZoom() {
		//set to default scale
		if (!grpanel.ZOOMON) {
			grpanel.xMin = -10.0; grpanel.xMax = 10.0; grpanel.yMin = -10.0; grpanel.yMax = 10.0;
			zoomItem.setText("Zoom in");
		} else {
			//zoom in to 2X
			grpanel.xMin = -5.0; grpanel.xMax = 5.0; grpanel.yMin = -5.0; grpanel.yMax = 5.0;
			zoomItem.setText("Zoom out");
			//if there is a custom zoom, zoom to that instead
			if (equation.x1 != equation.x2) {
				grpanel.xMin = equation.x1; grpanel.xMax = equation.x2; grpanel.yMin = equation.y1; grpanel.yMax = equation.y2;
			}
		}
	}


	/*********************************************************************************
	Name:		setValues()
	Purpose:	This function sets the values in the table of values based on the current equation.
	Called by: 	actionPerformed() in MainMenuListener class
	Calls: 		EqnData.evaluateEqn()
	**********************************************************************************/
	private void setValues() {
		if (equation.RPN == "") return;
		double yval;
		String yStr;
		for (int i = 0; i < NVALUES; i++) {
			xvalues[i].setText(" " + (i - NVALUES/2));
			yval = EqnData.evaluateEqn(i - NVALUES/2);
			// yStr = String.format(" %.4f",yval);
			yStr = new java.text.DecimalFormat("#.####").format(yval); //this is better because it doesn't have trailing zeros
			yvalues[i].setText(" " + yStr);
		}
	}

	/*********************************************************************************
	Name:		toggleHelp()
	Purpose:	Toggles the help screen on and off (alternating helppanel with grpanel)
	Called by: 	MainMenuListener.actionPerformed(), close button in helppanel object
	Calls: 		helppanel.repaint(), grpanel.repaint()
	**********************************************************************************/
	void toggleHelp() {
		HELPON = !HELPON;
		if(HELPON) {
			content.remove(grpanel);
			content.add(helppanel, BorderLayout.CENTER);
			helppanel.repaint();
		} else {
			content.remove(helppanel);
			content.add(grpanel, BorderLayout.CENTER);
			grpanel.repaint();
		}
		content.validate();
	}

	/*********************************************************************************
	Name:		startAbout()
	Purpose:	Starts the "About" dialog.
	Called by: 	actionPerformed() in MainMenuListener class
	Calls: 		findParentFrame()
	**********************************************************************************/
	private void startAbout() {
		Frame f = findParentFrame();
		AboutDialog ad = new AboutDialog(f,"hello",false);
		//f == null if it is an application - for some reason.
		//f != null for applets!
	}
	/*********************************************************************************
	Name:		findParentFrame()
	Purpose:	When run from an applet, we must work our way up to find the parent frame
				before I can show the dialog.
				This parentFrame needs to be passed to the constructor.
				It would be really nice to put this in the AboutDialog class, but it can't
				find the parent there!
	Called by: 	startAbout()
	Calls: 		--
	**********************************************************************************/
	private Frame findParentFrame(){
		Container c = this;
		while(c != null){
			if (c instanceof Frame) return (Frame)c;
			c = c.getParent();
		}
		return (Frame)null;
	}


	// *******INNER CLASSES***********

	/* This class handles all clicks on menus */
	class MainMenuListener implements ActionListener {

		/************************************************************
		* This method does the following:
		* Clicking on:
			About --> pop up About dialog box
			Zoom  --> toggle zoom variable, call zoom function and repaint.
			Grid  --> toggle grid variable and repaint.
			Table of Values --> toggle variable, show or hide value panel
			Equation -->
						* get equation type
						* call function to choose and load data for that equation
						* if there is a custom scale, zoom to that scale (call function and repaint)
						* call function to set data in table of values
						* clear and reset txtInput
		Calls: startAbout, setValues, setZoom, EqnData.listEquations
		**************************************************************/
		public void actionPerformed(ActionEvent e)	{

			if (e.getSource() == helpItem) {
				toggleHelp();
				return;
			}
			if (HELPON) return; // can't click on any other menus until the help screen is hidden.

			if (e.getSource() == aboutItem) {
				startAbout();
				// AboutDialog ad = new AboutDialog();
				// Frame f = findParentFrame();
				// if (f != null) {
					// AboutDialog ad = new AboutDialog(f,"hello",false);
				// }
                // ad.setVisible(true);
			}

			//handle zooming
			// if (e.getActionCommand().startsWith("Zoom")) {
			if (e.getSource() == zoomItem) {	//zoom between -10,-10,10,10 and custom zoom.
				grpanel.ZOOMON = !grpanel.ZOOMON;
				setZoom();
				grpanel.repaint();
			}

			if (e.getActionCommand().equals("Grid")) {
				grpanel.GRIDON = !grpanel.GRIDON;
				grpanel.repaint();
			}

			if (e.getSource() == valueItem) {
				VALUEON = !VALUEON;
				if (VALUEON)
					vpanel.setVisible(true);
				else
					vpanel.setVisible(false);
			}

			//all equation menus
			if (e.getSource() == linearItem || e.getSource() == quadItem
					|| e.getSource() == polyItem || e.getSource() == rationItem
					|| e.getSource() == trigItem || e.getSource() == expItem) {
				String type = e.getActionCommand();

				//no need for a return value. Global equation is set. Cancel works and doesn't need to return anything.
				if (type.equals("Linear")) 			EqnData.listEquations(linear, type);
				if (type.equals("Quadratic"))	 	EqnData.listEquations(quadratic, type);
				if (type.equals("Polynomial")) 		EqnData.listEquations(polynomial, type);
				if (type.equals("Rational")) 		EqnData.listEquations(rational, type);
				if (type.equals("Trigonometric")) 	EqnData.listEquations(trigonometric, type);
				if (type.equals("Exp & Log")) 		EqnData.listEquations(expon_log, type);

				if (equation.x1 == equation.x2) {
					grpanel.ZOOMON = false;
				} else {
					grpanel.ZOOMON = true;
				}
				setZoom();
				setValues();
				eqnCorrect = false;
				// txtInput.setEnabled(true);
				txtInput.setBackground(Color.WHITE);
				txtInput.setText("y=");
				grpanel.repaint();
			}
		} //end of actionPerformed()
	} // end of MainMenuListener class

	/* do some simple repeated things to make menu items. */
	class MyMenuItem extends JMenuItem {
		MyMenuItem(String s, char c) {
			super(s);
			if (c != '\0') this.setMnemonic(c);
			this.addActionListener(mml);
		}
	}

	/* This class is used to check each keystroke for the textbox txtInput */
	class TextKeyListener implements KeyListener {

		public void keyReleased (KeyEvent e) {}

		/* This is neeeded to stop the backspace from being executed
		   keyTyped can't stop it.
	   */
		public void keyPressed (KeyEvent e) {
			if (eqnCorrect && (int)e.getKeyChar() == 8) e.consume();
		}


		/*******************************************************************
		*  This function does the following:
			* if the equation is correct (eqnCorrec) then it doesn't allow any more typing
			* if the key is a backspace or <ENTER> do nothing,
			  just pass it on to the next event handler
			* if it is a letter, make it lowercase
			* check that the key is in the list of valid letters
			  if it is not, consume the event so that the JTextField doesn't get updated
			* valid letters will be displayed in txtInput.
			The list of valid letters is: numbers: 0-9, symbols:  =.*+-/()^
			letters in the following words: xy sin cos tan e log ln sqr
			No spaces.
		********************************************************************/
		public void keyTyped(KeyEvent e) {
			if (eqnCorrect) {	//if they have correctly guessed the equation, don't allow more typing
				e.consume();
				e.consume(); //second consume required for? <ENTER> ?
				return;
			}

			char key = e.getKeyChar();
			if ((int)key == 8) 	return;	//backspace
			if ((int)key == 10)	return; //enter

			//System.out.print ("~" + (int)key);
			String valid = "0123456789.+-*/^()=xyaeiocglnqrst";
			if (key >='A' && key <='Z') key = Character.toLowerCase(key);

			if (valid.indexOf(key) == -1) {
				e.consume();
				return;
			}
		} // end of keyTyped()
	} //end of class TextKeyListener


	/*****************************************************************************************
	This class is used when <ENTER> is pressed in the JTextField
	If there is text in txtInput it checks to see if it matches the stored equation.
	If it matches, then it sets eqnCorrect to TRUE and sets the background to green.
	If it does not match, then it makes the background red for 1 second.
	If you get 5 wrong guesses, it will tell you the equation. This is done because it may
	simply be the user having trouble formatting the equation correctly to match it.
	********************************************************************************************/
	class TextFieldListener implements ActionListener {

		private int numTries = 0;

		public void actionPerformed(ActionEvent e)	{
			String s = txtInput.getText();
			s = s.toLowerCase();
			if (s.length() == 0) return;
			if (equation.eqnText.equals(s)) {
				eqnCorrect = true;
				numTries = 0;
				txtInput.setBackground(Color.GREEN);
				// txtInput.setEnabled(false); //NO: this makes the colour of the text to be gray!!! Use a boolean instead.
				// System.out.println("YES match");
			} else {
				eqnCorrect = false;
				txtInput.setBackground(Color.RED);
				//start a thread to make it back to white in 1 second
				new Thread() {
				public void run() {
					try {
						Thread.sleep(1000);
						txtInput.setBackground(Color.WHITE);
						numTries++;
						if (numTries >= 5) {
							txtInput.setText("Answer is: " + equation.eqnText);
							eqnCorrect = true;
							numTries = 0;
							txtInput.setBackground(Color.GREEN);
						}
					} catch (InterruptedException xxx) {}
				}}.start();
				// System.out.println("NO match " + numTries);
			}
		} // end of actionPerformed()
	} //end of class TextFieldListener

}
