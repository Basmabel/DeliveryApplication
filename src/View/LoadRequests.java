package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import Controller.Controller;
import Ressources.FileDrop;

/**
 * LoadRequests is the Frame of the application that allows to load Requests
 * into the map. The LoadRequests extends a JFrame.
 * 
 * A LoadRequests characterises by :
 * <ul>
 * <li>A controller that links the model to the view.</li>
 * <li>A DrawnMap that represents the Map as a drawn entity</li>
 * </ul>
 * 
 * @author H4122
 *
 * @see Model.Map
 * @see Model.Request
 * @see Controller.Controller
 * @see View.DrawnMap
 * 
 */
public class LoadRequests extends JFrame {

	private static final long serialVersionUID = 1L;
	private Controller controller;
	private DrawnMap drawnMap;

	/**
	 * Class constructor. Calls the constructor of the super class (JFrame) When
	 * created, the LoadRequests frame initialises the attribute controller, with
	 * the controller given as a parameter, and the attribute drawnMap with the one
	 * given as a parameter to the constructor.
	 * 
	 * @param controller the controller that links the model to the view in our
	 *                   application.
	 * @param drawnMap DrawnMap that represents the Map as a drawn entity.
	 * 
	 * @see Model.Map
	 * @see Controller.Controller
	 * @see View.DrawnMap
	 */
	public LoadRequests(Controller controller, DrawnMap drawnMap) {
		super("Load Requests");
		this.controller = controller;
		this.drawnMap = drawnMap;
		this.setSize(550, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel g = new JPanel();
		BoxLayout box = new BoxLayout(g, BoxLayout.Y_AXIS);

		g.setLayout(box);
		g.add(LoadRequestsLabel());
		g.add(descriptiveLabel());
		g.add(dragAndDrop());
		add(g);
		setVisible(true);
	}

	/**
	 * Closes the frame of the LandingPage when another page appears
	 */
	public void closeLoadRequests() {
		LoadRequests.this.dispatchEvent(
				new java.awt.event.WindowEvent(LoadRequests.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Method that allows to create a LoadRequests label
	 * 
	 * @return JPanel
	 */
	public JLabel LoadRequestsLabel() {

		JLabel loadRequestsLabel = new JLabel("Load Your Request File here", JLabel.RIGHT);
		loadRequestsLabel.setAlignmentX(CENTER_ALIGNMENT);
		loadRequestsLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
		loadRequestsLabel.setPreferredSize(new Dimension(600, 300));
		loadRequestsLabel.setForeground(Color.black);

		return loadRequestsLabel;

	}
	
	/**
	 * Method that allows to create a descriptive Label
	 * 
	 * @return JPanel
	 */
	public JLabel descriptiveLabel() {

		JLabel dragDrop = new JLabel("Drag and drop your requests here");
		dragDrop.setAlignmentX(CENTER_ALIGNMENT);
		dragDrop.setFont(new Font("Verdana", Font.PLAIN, 20));
		dragDrop.setForeground(new Color(0, 158, 96));

		return dragDrop;

	}
	
	/**
	 * Method that allows to create and add the actionListener to the JButton Load
	 * Requests
	 * 
	 * @return JButton
	 */
	public JButton LoadRequest(String xmlFile) {

		JButton loadRequests = new JButton("Load Requests");
		loadRequests.setAlignmentX(CENTER_ALIGNMENT);
		loadRequests.setSize(400, 100);
		loadRequests.setPreferredSize(new Dimension(450, 30));
		// loadMap.setBounds(100, 100, 600, 200);
		loadRequests.setBackground(new Color(159, 226, 191));
		loadRequests.addActionListener(new ActionListener() {
			/**
			 * Loads the requests in the application
			 * 
			 * @param e is an ActionEvent. Here, a click on the button.
			 * 
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.loadRequests(xmlFile, drawnMap);

			}
		});

		return loadRequests;
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
		// Creation of a panel that contains the textArea
		JPanel textPanel = new JPanel();

		// Creation of the textArea where to drag and drop the file
		JTextArea text = new JTextArea(3, 40);
		text.setAlignmentX(CENTER_ALIGNMENT);
		text.setAlignmentY(CENTER_ALIGNMENT);
		text.setSize(600, 100);
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
						// fileChooser.addChoosableFileFilter(filter);

						int returnValue = fileChooser.showOpenDialog(null);

						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = fileChooser.getSelectedFile();
							text.append(selectedFile.getAbsolutePath());
							buttonPanel.add(LoadRequest(selectedFile.getAbsolutePath()));
							explore.setEnabled(false);
						}

					}
				});
				
		// implementing the file dropping system
		@SuppressWarnings("unused")
		FileDrop fileDrop = new FileDrop(myPanel, new FileDrop.Listener() {
			public void filesDropped(java.io.File[] files) {
				String file = "";
				if (text.getText().isBlank()) {
					try {
						file = files[0].getCanonicalPath();
						file = file.replaceAll("\\\\", "/");
						text.append(file);
						buttonPanel.add(LoadRequest(file));
						explore.setEnabled(false);
					} catch (java.io.IOException e) {
					}
				} else {
					JOptionPane.showMessageDialog(null, "You can't drag and drop multiple files");
				}

			}
		});

		
		textPanel.add(explore);

		return myPanel;
	}

}
