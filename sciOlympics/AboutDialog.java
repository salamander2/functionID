package ca.quarkphysics.harwood.sciOlympics;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/* This class makes a pop-up dialog box. It works as an application or JApplet.
   The code for this is based on http://www.jguru.com/faq/view.jsp?EID=27423
   and uses the findParentFrame method in the Function class.
*/

/* Text of About Program box
This program is free to use.
Written by Michael Harwood, 2012 for London District Science Olympics
Email: harwood@quarkphysics.ca
*/
class AboutDialog extends JDialog {

	//called by Function.startAbout(), which runs when you click the About help menu.
	//parameters are just the standard ones needed to make a JDialog object.
	public AboutDialog(final Frame f, final String s, final boolean b) {
		super(f, s, b);

		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		// this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setTitle("About Functions");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(300, 200);

		this.add(Box.createRigidArea(new Dimension(0, 10))); //vertical spacer

		// ImageIcon icon = new ImageIcon("notes.png");	//hard to load an image when used as a JApplet.
		// JLabel label = new JLabel(icon);
		// label.setAlignmentX(0.5f);
		// this.add(label);
		// this.add(Box.createRigidArea(new Dimension(0,10)));

		JLabel name = new JLabel("  Identify Functions v.1");
		name.setFont(new Font("Serif", Font.BOLD, 15));
		name.setAlignmentX(0.5f);
		this.add(name);
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.add(new JLabel("Written by Michael Harwood, 2012  "));
		this.add(new JLabel("for London District Science Olympics  "));
		JLabel email = new JLabel("Email: harwood@quarkphysics.ca  ");
		email.setFont(new Font("Arial", Font.ITALIC, 13));
		this.add(email);

		this.add(Box.createRigidArea(new Dimension(0, 30)));

		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		close.setAlignmentX(0.5f);
		this.add(close);
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.pack();
		this.setVisible(true);
	}
}
