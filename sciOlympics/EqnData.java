package ca.quarkphysics.harwood.sciOlympics;

import java.util.ArrayList;
import javax.swing.*;

class EqnData {

	//object variables
	String eqnText;
	String RPN;
	int x1, y1, x2, y2;

	//constructors. if no scale, then default to the standard scale
	EqnData(final String e1, final String e2) {
		this.eqnText = e1;
		this.RPN = e2;
	}

	EqnData(String e1, String e2, int x1, int y1, int x2, int y2) {
		this.eqnText = e1;
		this.RPN = e2;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}



	//this is set up to work for any of the equation types
	/*********************************************************************************
	Name:		listEquations()
	Purpose:	pop up a JOptionPane with correct info about equation type selected.
				User chooses which equation to display (by number). Invalid input or
				'Random' displays a random equation.
	Called by: 	MainMenuListener.actionPerformed()
	Parameters:	"source" is the arraylist of the chosen equation type
				"t" is the text string of the chosen equation type
	Calls: 		--
	**********************************************************************************/
	static void listEquations(ArrayList<EqnData> source, String t) {
		int maxEq = 0;
		String name = null;
		maxEq = source.size(); //how many equations of that type there are.
		name = JOptionPane.showInputDialog(null, "Enter 1-" + maxEq + " or Random", "Choose your " + t + " functions", JOptionPane.PLAIN_MESSAGE);
		// if (t.equals("Linear")) {
			// maxEq = linear.size();
			// name = JOptionPane.showInputDialog(null, "Enter 1-" + maxEq + " or Random","Choose your " + t + " functions",JOptionPane.PLAIN_MESSAGE);
		// }

		//handle CANCEL option
		if(name == null){
			System.out.println("Cancel pressed");	//this goes to standard output
			return;// null;
		}

		// convert name to integer. If it fails then choose random value
		int n = 0;
		try {
			n = Integer.parseInt(name);
			if (n > maxEq) n = maxEq;
		} catch (NumberFormatException e) {
			n = (int)(Math.random() * maxEq) + 1;
			// System.out.println("n=random (" + n + ")");
		}

		// System.out.println(source.get(n-1).eqnText);
		// if (Function.equation == null) Function.equation = new EqnData("","",0,0,0,0);
		Function.equation.eqnText = source.get(n-1).eqnText;
		Function.equation.RPN = source.get(n-1).RPN;
		Function.equation.x1 = source.get(n-1).x1;
		Function.equation.y1 = source.get(n-1).y1;
		Function.equation.x2 = source.get(n-1).x2;
		Function.equation.y2 = source.get(n-1).y2;
	}


	/*********************************************************************************
	Name:		evaluateEqn()
	Purpose:	takes the selected equation and evaluates it for the value of x passed in
				It returns the y value.
				The equation is written in RPN format (which is easier to code than postfix notation).
				The keys are laid out according to the HP-11C arrangement, mapped to the QWERTY keyboard.
	Called by: 	grpanel.paintComponent(), setValue()
	Calls: 		--
	**********************************************************************************/
	static double evaluateEqn(double xin) {
		/* RPN registers: x,y,z,t  */
		/* RPN function codes:
		*	# = Enter (not used)
		* 	Q = x^2		q = sqrt
		*	t = CHS
		*	W = ln		w = e^x
		*	E = log		e = 10^x		log10(x) = lnx / ln 10
		*	R = 1/x		r = y^x
		*	S = sin-1	s = sin
		*	D = cos-1	d = cos
		*	F = tan-1	f = tan
		*/

		double x = 0.0, y = 0.0, z = 0.0, t = 0.0;
		char c;
		String eqn = Function.equation.RPN;

		//parse each character in the RPN equation
		for (int i = 0; i < eqn.length(); i++) {
			c = eqn.charAt(i);
			// System.out.println("i=" + i);

			//This section added to process digits. Needed to handle possible multi-digit numbers and decimals.
			//It assumes that no number starts with a .
			//The resulting number is converted to a double and stored in x (after moving the stack up)
			if (c >= '0' && c <= '9') {
				String nn = "";	//only a few digits, so don't bother with StringBuilder
				do {
					nn = nn + c;
					i++;
					if (i == eqn.length()) {
						System.out.println("break. i=" + i);
						break;
					}
					c = eqn.charAt(i);
					// System.out.println("i=" + i);
					// System.out.println("c"+i+"="+c + " nn=" + nn);
				} while ((c >= '0' && c <= '9') || c == '.');

				t = z; z = y; y = x; //enter
				x = Double.parseDouble(nn);
				// System.out.println(x);
				// continue;
			}

			//Process every other symbol that is not a number
			switch (c) {
			case '.':
				//ERROR. numbers can't start with a period
				break;
			case 'x':
				t = z; z = y; y = x;
				x = xin;
				// System.out.println(x);
				break;
			case '#':
				t = z; z = y; y = x;
				break;
/*			case '0':	//this was originally used to process single digits
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				t = z;z = y;y = x; //enter
				x=Character.getNumericValue(c);
				// System.out.println("x="+x+" c="+c);
				//FIXME NEED TO HANDLE MULTIPLE DIGITS
				break;
*/
			//binary operators. Stack movement handled.
			case '*':
				x = y*x;
				y = z; z = t;
				break;
			case '+':
				x = y+x;
				y = z; z = t;
				break;
			case '/':
				x = y/x;
				y = z; z = t;
				break;
			case '-':
				x = y-x;
				y = z; z = t;
				break;
			case 'r':
				x = Math.pow(y, x);
				y = z; z = t;
				break;

			//unary operators. No stack movement happens.
			case 't':
				x = -x;
				break;
			case 'R':
				x = 1/x;
				break;
			case 'Q':  //x^2
				x = x*x;
				// System.out.println(x);
				break;
			case 'q':
				x = Math.sqrt(x);
				break;
			case 'W':
				x = Math.log(x);
				break;
			case 'w':
				x = Math.exp(x);
				break;
			case 'E':
				x = Math.log(x) / Math.log(10.0);
				break;
			case 'e':
				x = Math.pow(10, x);
				break;
			//(unary) trig functions
			case 's':
				x = Math.sin(x);
				break;
			case 'd':
				x = Math.cos(x);
				break;
			case 'f':
				x = Math.tan(x);
				break;
			}
			// System.out.println(x);
		}
		return x;
	} //end of evaluateEqn

	/*********************************************************************************
	Name:		loadData()
	Purpose:	Loads each equation into the correct arraylist (equation type)
	Called by: 	CreateAndShowGUI
	Calls: 		--

	Data format used in EqnData constructor:
		Fields: 	"equation","rpn formula",scale(if any)
		Examples:	"y=4x","x4*"	<-- no custom scale
					"y=x-2","x2-",-3,0,6,6	   <-- grid scale goes from (-3,0) to (6,6)
	**********************************************************************************/
	static void loadData() {
		Function.linear = new ArrayList<EqnData>();
		Function.linear.add(new EqnData("y=4","4"));
		Function.linear.add(new EqnData("y=x-2","x2-"));
		Function.linear.add(new EqnData("y=x+2","x2+",0,0,10,10));
		Function.linear.add(new EqnData("y=x+9","x9+"));
		Function.linear.add(new EqnData("y=2x","x2*"));
		 Function.linear.add(new EqnData("y=x/3","x3/"));
		Function.linear.add(new EqnData("y=-x-2","xt2-"));
		Function.linear.add(new EqnData("y=x+12","12x+",0,0,20,20));
		Function.linear.add(new EqnData("y=1/2x","x0.5*",0,0,20,20));
		Function.linear.add(new EqnData("y=-3x+1","xt3*1+"));

		Function.quadratic = new ArrayList<EqnData>();
		Function.quadratic.add(new EqnData("y=x^2","xQ"));
		Function.quadratic.add(new EqnData("y=1/2x^2","xQ2/"));
		Function.quadratic.add(new EqnData("y=-x^2-2x+3","xQtx2*-3+"));
		Function.quadratic.add(new EqnData("y=x^2-2x-3","xQx2*-3-"));
		Function.quadratic.add(new EqnData("y=-2x^2+3x-1","xQt2*x3*+1-"));
		 Function.quadratic.add(new EqnData("y=9x^2-16","xQ9*16-",-8,-20,8,5));
		Function.quadratic.add(new EqnData("y=sqrt(x)+3","xq3+"));
		Function.quadratic.add(new EqnData("y=sqrt(x+2)+3","x2+q3+"));
		Function.quadratic.add(new EqnData("y=sqrt(2x)","x2*q"));
		Function.quadratic.add(new EqnData("y=sqrt(2x+3)","x2*3+q"));

		Function.polynomial = new ArrayList<EqnData>();
		Function.polynomial.add(new EqnData("y=x^3","x3r"));
		Function.polynomial.add(new EqnData("y=x^3+1","x3r1+"));
		Function.polynomial.add(new EqnData("y=1/2x^3","x3r2/"));
		Function.polynomial.add(new EqnData("y=2x^3+1","x3r2*1+"));
		Function.polynomial.add(new EqnData("y=x^3+3x^2+3x+1","x3rxQ3*+x3*+1+"));
		Function.polynomial.add(new EqnData("y=x^3-3x^2+3x-1","x3rxQ3*-x3*+1-"));

		Function.rational = new ArrayList<EqnData>();
		Function.rational.add(new EqnData("y=1/x","xR"));
		Function.rational.add(new EqnData("y=1/x^2","xQR"));
		Function.rational.add(new EqnData("y=1/(x-2)^2","x2-QR"));
		Function.rational.add(new EqnData("y=2/x^2","xQR2*"));
		Function.rational.add(new EqnData("y=1/(x+1)","x1+R"));
		Function.rational.add(new EqnData("y=1/(x+1)^2","x1+QR"));
		Function.rational.add(new EqnData("y=(x+1)/(x-1)","x1+x1-/"));
		Function.rational.add(new EqnData("y=(x-1)/(x+1)","x1-x1+/"));

		Function.trigonometric = new ArrayList<EqnData>();
		Function.trigonometric.add(new EqnData("y=sin(x)","xs"));
		Function.trigonometric.add(new EqnData("y=sin(x)","xs",0,-2,10,+2));
		Function.trigonometric.add(new EqnData("y=cos(x)","xd"));
		Function.trigonometric.add(new EqnData("y=cos(x)","xd",0,-2,10,+2));
		Function.trigonometric.add(new EqnData("y=sin(2x)","x2*s"));
		Function.trigonometric.add(new EqnData("y=cos(2x)","x2*d"));
		Function.trigonometric.add(new EqnData("y=2sin(x)","xs2*"));
		Function.trigonometric.add(new EqnData("y=2cos(x)","xd2*"));
		Function.trigonometric.add(new EqnData("y=2sin(2x)","x2*s2*"));
		Function.trigonometric.add(new EqnData("y=2cos(2x)","x2*d2*"));
		Function.trigonometric.add(new EqnData("y=sin(x)+1","xs1+"));
		Function.trigonometric.add(new EqnData("y=cos(x)+1","xd1+"));
		Function.trigonometric.add(new EqnData("y=sin(x)+1","xs1+",0,-2,10,+2));
		Function.trigonometric.add(new EqnData("y=cos(x)+1","xd1+",0,-2,10,+2));
		Function.trigonometric.add(new EqnData("y=-sin(x)+1","xst1+",0,-2,10,+2));
		Function.trigonometric.add(new EqnData("y=-cos(x)+1","xdt1+",0,-2,10,+2));
		Function.trigonometric.add(new EqnData("y=sin(x)-1","xs1-"));
		Function.trigonometric.add(new EqnData("y=cos(x)-1","xd1-"));
		Function.trigonometric.add(new EqnData("y=sin(1/2x)","x2/s"));
		Function.trigonometric.add(new EqnData("y=cos(1/2x)","x2/d"));
		Function.trigonometric.add(new EqnData("y=tan(x)","xf"));
		Function.trigonometric.add(new EqnData("y=-tan(x)","xft"));
		Function.trigonometric.add(new EqnData("y=1/sin(x)","xsR"));
		Function.trigonometric.add(new EqnData("y=1/cos(x)","xdR"));
		Function.trigonometric.add(new EqnData("y=1/tan(x)","xfR"));

		Function.expon_log = new ArrayList<EqnData>();
		Function.expon_log.add(new EqnData("y=e^x","xw"));
		Function.expon_log.add(new EqnData("y=2^x","2xr"));
		Function.expon_log.add(new EqnData("y=2^(-x)","2xtr"));
		Function.expon_log.add(new EqnData("y=2^x+1","2xr1+"));
		Function.expon_log.add(new EqnData("y=1/(2^x+1)","2xr1+R"));
		Function.expon_log.add(new EqnData("y=ln(x)","xW"));
		Function.expon_log.add(new EqnData("y=ln(x)+1","xW1+"));
		Function.expon_log.add(new EqnData("y=ln(x+1)","x1+W"));
		Function.expon_log.add(new EqnData("y=log(x)","xE"));
		Function.expon_log.add(new EqnData("y=log(x)+1","xE1+"));
		Function.expon_log.add(new EqnData("y=log(x+1)","x1+E"));


	}

}
