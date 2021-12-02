package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import Controller.Controller;
import Ressources.FileDrop;

/**
 * LandingPage is the landing page of the application. It allows us to load an
 * XML file that corresponds to the Map. The LandingPage extends a JFrame.
 * 
 * A LandingPage characterises by :
 * <ul>
 * <li>A controller that links the model to the view.</li>
 * </ul>
 * 
 * @author H4122
 *
 * @see Model.Map
 * @see Controller.Controller
 */
public class LandingPage extends JFrame {

	private Controller controller;
	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor. Calls the constructor of the super class (JFrame) When
	 * created, the landingPage initialises the attribute controller, with the
	 * controller given as a parameter
	 * 
	 * @param controller the controller that links the model to the view in our
	 *                   application.
	 * 
	 * @see Controller.Controller
	 */
	public LandingPage(Controller controller) {
		super("Welcome");
		this.controller = controller;
		this.setSize(1100, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel g = new JPanel();
		BoxLayout box = new BoxLayout(g, BoxLayout.Y_AXIS);

		g.setLayout(box);
		g.add(welcomeLabel());
		g.add(descriptiveLabel());
		g.add(dragAndDrop());
		add(g);
		setVisible(true);
	}

	/**
	 * Closes the frame of the LandingPage when another page appears
	 */
	public void closeLandingPage() {
		LandingPage.this.dispatchEvent(
				new java.awt.event.WindowEvent(LandingPage.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Method that allows to create a welcome Label
	 * 
	 * @return JPanel
	 */
	public JLabel welcomeLabel() {

		JLabel welcome = new JLabel("Welcome to Deliver'IF", JLabel.RIGHT);
		welcome.setAlignmentX(CENTER_ALIGNMENT);
		welcome.setFont(new Font("Verdana", Font.PLAIN, 50));
		welcome.setPreferredSize(new Dimension(600, 300));
		welcome.setForeground(Color.black);

		return welcome;

	}

	/**
	 * Method that allows to create a descriptive Label
	 * 
	 * @return JPanel
	 */
	public JLabel descriptiveLabel() {

		JLabel dragDrop = new JLabel("Drag and drop your Map here");
		dragDrop.setAlignmentX(CENTER_ALIGNMENT);
		dragDrop.setFont(new Font("Verdana", Font.PLAIN, 20));
		dragDrop.setForeground(new Color(0, 158, 96));

		return dragDrop;

	}

	/**
	 * Method that allows to create and add the actionListener to the JButton Load
	 * Map
	 * 
	 * @return JButton
	 */
	public JButton LoadMap(String xmlFile) {

		JButton loadMap = new JButton("Load Map");
		loadMap.setAlignmentX(CENTER_ALIGNMENT);
		loadMap.setSize(600, 200);
		loadMap.setPreferredSize(new Dimension(450, 60));
		loadMap.setBackground(new Color(159, 226, 191));
		loadMap.addActionListener(new ActionListener() {
			/**
			 * Loads the map of the application
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.loadMap(xmlFile);
			}
		});

		return loadMap;
	}

	/**
	 * Method that allows to create the JPanel that contains the JtextArea assigned for drag and Drop.
	 * this Method also allows to get the path of the file dropped in the designed area.
	 * 
	 * The class used and called inside this method is a library found online
	 * 
	 * @return JPanel
	 */
	public JPanel dragAndDrop() {
		// Creation of a panel that contains the textArea and the explore button
		JPanel textPanel = new JPanel();

		// Creation of the textArea where to drag and drop the file
		JTextArea text = new JTextArea(4, 40);
		text.setAlignmentX(CENTER_ALIGNMENT);
		text.setAlignmentY(CENTER_ALIGNMENT);
		text.setSize(600, 200);
		textPanel.add(text);

		// Creation of the panel that holds the textArea
		JPanel myPanel = new JPanel();
		myPanel.setSize(600, 200);
		myPanel.setAlignmentX(CENTER_ALIGNMENT);
		BoxLayout box = new BoxLayout(myPanel, BoxLayout.Y_AXIS);
		myPanel.setLayout(box);

		// Creation of a panel for button
		JPanel buttonPanel = new JPanel();

		// Adding the JTextArea & the emptyPanel to the myPanel
		myPanel.add(textPanel);
		myPanel.add(buttonPanel);
		
		// Creation of the explore button
				JButton explore = new JButton("Explore");
				explore.setAlignmentX(CENTER_ALIGNMENT);
				explore.setBackground(new Color(159, 226, 191));
				explore.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// Implementing the FileChooser

						JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
						FileFilter filter = new FileNameExtensionFilter("XML files", "xml");
						fileChooser.setFileFilter(filter);

						int returnValue = fileChooser.showOpenDialog(null);

						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = fileChooser.getSelectedFile();
							text.append(selectedFile.getAbsolutePath());
							buttonPanel.add(LoadMap(selectedFile.getAbsolutePath()));
							explore.setEnabled(false);
						}

					}
				});
				textPanel.add(explore);
				
		// implementing the file dropping system
		FileDrop fileDrop = new FileDrop(myPanel, new FileDrop.Listener() {
			public void filesDropped(java.io.File[] files) {
				String file = "";
				if (text.getText().isBlank()) {
					try {
						file = files[0].getCanonicalPath();
						file = file.replaceAll("\\\\", "/");
						text.append(file);
						buttonPanel.add(LoadMap(file));
						explore.setEnabled(false);
						
					} catch (java.io.IOException e) {
					}
				} else {
					JOptionPane.showMessageDialog(null, "You can't drag and drop multiple files");
				}
			}

		});

		

		return myPanel;
	}

}
