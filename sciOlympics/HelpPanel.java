package ca.quarkphysics.harwood.sciOlympics;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class HelpPanel extends JPanel
{
	private Function objFunct;

	/*********************************************************************************
	Name:		HelpPanel constructor
	Purpose:	Sets up the help screen. It makes a JEditorPane and puts it into a
				JScrollPane to add the scrollbar. It also adds a CLOSE button at the bottom.
	Parameters:	requires the Function object created when the program starts.
				This is needed so that Function.toggleHelp() can be called when it exits.
	Called by: 	createAndShowGUI
	Calls: 		toggleHelp
	**********************************************************************************/
	HelpPanel(final Function objFunct) {
		super();
		this.objFunct = objFunct;

		String text = loadText();

		setBackground(new Color(0, 200, 180));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JEditorPane textArea = new JEditorPane("text/html",text);
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textArea.setBackground(new Color(230, 230, 255));
		this.add(scrollPane);
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		JButton btnClose = new JButton("EXIT HELP");
		btnClose.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				objFunct.toggleHelp();
			}
		});
		this.add(btnClose);
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		this.validate();
	}

	//Load the text of the help page in HTML format.
	private String loadText() {
		String t = "<center><h1>How to use this program</h1></center>"
+ "This program will see whether you can identify polynomials, rationals, "
+ "trig functions, exponentials, and logs.  You choose the type of equation "
+ "and then can choose a specific function to display. The available numbers "
+ "are shown in the option box. If you type anything other than the available "
+ "numbers the computer will pick a random function for you."
+ "<ul><li>You can toggle the <b>grid</b> off and on and also toggle between two levels of <b>zoom</b>.</li>"
+ "<li> Viewing the <b>\"Table of values\"</b> will display (x,y) for -10&nbsp;<=&nbsp;x&nbsp;<=&nbsp;+10.</li>"
+ "<li> The program can be run as an application or as an applet in a webpage. "
+ "If it is an application you can resize the window to make the graph bigger.</li>"
+ "</ul>"
+ "<h2>Guessing the equation</h2>"
+ "To identify the equation, type the formula into the textbox at the bottom."
+ "The formula must be entered with specific syntax or it will be incorrect. Please read the following:"
+ "<ul><li>Formulas must be written as \"y=   \".</li>"
+ "<li>The the following symbols are used for mathematical operations:<br>"
+ "<ul> ( )  = brackets<br>"
+ " + -  = addition and subtraction<br>"
+ " / = division.<br>"
+ " <i>Multiplication is understood by writing symbols adjacent to each other<br>"
+ " e.g. y = 3x    y= 2sin(3x)    not: y = 3*x</i><br>"
+ " ^ = exponentiation<br>"
+ " e^x = exponential function<br>"
+ " sin( ), cos( ), tan( ) = trigonometric functions (in radians)<br>"
+ " log( ), ln( ) = logarithms, base 10 and base e<br>"
+ " sqrt( ) = square root<br>"
+ "</ul></li>"
+ "<li>Polynomials must be written with the highest order term first: y=ax^4 + bx^3 + cx^2 ..."
+ "<pre>Incorrect: y = 2-x  		Correct: y = -x+2</pre></li>"
+ "<li>Fractional coeffiecients must be written as fractions, not decimals.<br>"
+ "<pre>Incorrect: y = 0.5x^2		Correct: y = 1/2x^2</pre>"
+ "<i>This is to be consistent with things like y=2/3x which cannot be represented as an exact decimal.</i></li>"
+ "<li>Coefficients must be written before variables<br>"
+ "<pre>Incorrect: y = x/2			Correct: y = 1/2x</pre>"
+ "<i>which means y = (1/2) x . If you wanted y =1/(2x) you would have to use brackets.</i></li>"
+ "<li>The BEDMAS rule for order of operations is followed, so adding ( ) when not needed"
+ " will render the formula incorrect.<br>"
+ "<pre>Incorrect: y = 1/(x^2)		Correct:  y = 1/x^2</pre>"
+ "<i>These equations are the same, but since exponents have a higher precedence than division, brackets are not needed.</i><br>"
+ "As is expected, y = 1/x-2   is not the same as y = 1/(x-2)   Both of these formulas are correct, but they are different equations."
+ "<pre>Incorrect: y = x^3/2 		Correct: y = x^(3/2) 	or	y = 1/2x^3</pre>"
+ "<i>If you want y=x^(3/2) then brackets are required because of operator precedence (BEDMAS).<br>"
+ "If you want y = (x^3)/2  you have to write the coefficient first.</i></li>"
+ "<li>When there are two operators with the same precedence, evaluation proceeds left to right.<br>"
+ "Thus y = 1/2x  means y= 1/2 * x 	i.e. y = 0.5x and not y=1/(2*x)</li>"
+ "</ul>"
+ " If you get the formula correct, the textbar will turn green. You will not be able to edit the formula until you choose a new equation."
+ " If your formula is incorrect the textbar will flash red for a second."
+ "<p>"
+ " After 5 incorrect guesses, the formula will be displayed. This is done because the program "
+ " does not parse the equation you type in and there may be multiple ways of writing the same correct equation."
+ " The program just matches what you type with the stored equation. If it is not an exact match, it assumes that it is incorrect"
+ " when,  in fact, your equation may be correct, just in an incorrect form.<p>"
+ "If you want a program to graph equations, please download <b>Graphmatica</b>.";
		return t;
	}
}
