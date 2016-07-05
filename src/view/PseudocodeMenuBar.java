package view;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * 
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 *
 * @license MIT
 */
@SuppressWarnings("serial")
public class PseudocodeMenuBar extends JMenuBar implements ActionListener {
	
	// Refers to the pseudocode frame that is containing this menu bar.
	private Pseudocode pseudocode;
	
	private static String[] exampleCategories = { "Basics", "Physics", "Games" };
	private static HashMap <String, String> example;
	
	// The FileFilter object that is used to filter non-pseudocode files from being opened.
	private FileFilter pseudocodeFilter = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory() ||
				   pathname.getAbsolutePath().endsWith(".pseudo") ||
				   pathname.getAbsolutePath().endsWith(".txt");
		}

		@Override
		public String getDescription() {
			return "Pseudocode (.pseudo, .txt)";
		}
	};
	private Font font = new Font("Monaco", Font.BOLD, 18);
	
	public PseudocodeMenuBar(Pseudocode pseudocode) {
		super();
		
		this.pseudocode = pseudocode;
		this.setFont(font);
		if (example == null)
			initializeExamples();
		initialize();
	}
	
	private void initialize() {
		JMenu fileMenu = createMenu("File");
		fileMenu.add(createMenuItem("New", 'N'));
		fileMenu.add(createMenuItem("Open", 'O'));
		fileMenu.add(createMenuItem("Save", 'S'));
		fileMenu.add(createMenuItem("Save As"));
		fileMenu.add(createMenuItem("Quit", 'Q'));
		add(fileMenu);
		
		// Create a menu for each example category
		for (String category : exampleCategories) {
			JMenu exampleMenu = createMenu(category);
			
			// Add a menu item for all example category additions
			for (String fileName : example.keySet()) {
				
				if (fileName.startsWith(category + "-")) {
					String name = fileName.substring(category.length() + 1);
					
					// Remove the .pseudo type from the name 
					if (name.endsWith(".pseudo"))
						name = name.substring(0, name.indexOf(".pseudo"));
					
					// Replace underscores with spaces
					name = name.replace('_', ' ');
					
					// Add a menu item for opening this example file
					exampleMenu.add(createMenuItem(name, fileName));
				}
				
			}
				
			add(exampleMenu);
		}
	}
	
	/**
	 * Initialize all the example files.
	 */
	private void initializeExamples() {
		example = new HashMap <String, String> ();
		
		final String path = "examples/Physics-Bouncing_Ball.pseudo";
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		File file = new File(classLoader.getResource(path).getFile());
		
		System.out.println("Got example: " + file.exists());
		
		// Run with jar file
		
		// Get the example folder resource 
		// Get every category from the examples folder
		for (String category : exampleCategories) {

			
//			// Read all example files from the directory
//			if (exampleCategory.isDirectory()) {
//				ArrayList <String> category = new ArrayList <String> ();
//				// Add the name of the category to the list
//				category.add(exampleCategory.getName());
//			
//				// Add the name of every example file
//				for (File exampleFile : exampleCategory.listFiles()) {
//					category.add(exampleFile.getName());
//					example.put(exampleFile.getName(), readFile(exampleFile));
//				}
//				
//				// Add the menu descriptor 
//				exampleCategories.add(category);
//			}
		}
	}
	
	/**
	 * Reads the given File object and returns a String representing its contents.
	 * @param file - a reference to the File
	 * @return the file's contents as a String
	 */
	private String readFile(File file) {
		try {
			// Create a Scanner and StringBuilder object to read from this file.
			Scanner scanner = new Scanner(file);
			StringBuilder builder = new StringBuilder();
			
			// Read each line from the file
			while (scanner.hasNextLine()) {
				builder.append(scanner.nextLine());
				
				// Append new line characters between lines
				if (scanner.hasNextLine())
					builder.append("\n");
			}
			
			// Clean up resources and return the resulting String.
			scanner.close();
			return builder.toString();
			
		}
		// If the file wasn't found, return the empty string.
		catch (FileNotFoundException e) {
			return "";
		}
	}
	
	/**
	 * Creates a JMenu with the given name.
	 * @param name - the name of the menu
	 * @return a JMenu object
	 */
	private JMenu createMenu(String name) {
		JMenu menu = new JMenu(name);
		menu.setFont(font);
		return menu;
	}
	
	/**
	 * Returns a JMenuItem with the given name and a default action of the lowercase version 
	 * of the name.
	 * @param name - the name of the item
	 * @return the JMenuItem object representing this item
	 */
	private JMenuItem createMenuItem(String name) {
		return createMenuItem(name, name.toLowerCase(), (char) 0);
	}
	
	/**
	 * Returns a JMenuItem with the given name and action command.
	 * @param name - the name of the item
	 * @param action - the action command send to this ActionListener event
	 * @return the JMenuItem object representing this item
	 */
	private JMenuItem createMenuItem(String name, String action) {
		return createMenuItem(name, action, (char) 0);
	}
	
	/**
	 * Returns a JMenuItem with the given name and keyboard shortcut.
	 * @param name - the name of the item
	 * @param shortcut - the keyboard shortcut for triggering this item
	 * @return the JMenuItem object representing this item
	 */
	private JMenuItem createMenuItem(String name, char shortcut) {
		return createMenuItem(name, name.toLowerCase(), shortcut);
	}
	
	/**
	 * Returns a JMenuItem with the given name, action command, and keyboard shortcut.
	 * @param name - the name of the item
	 * @param action - the action command send to this ActionListener event
	 * @param shortcut - the keyboard shortcut for triggering this item
	 * @return the JMenuItem object representing this item
	 */
	private JMenuItem createMenuItem(String name, String action, char shortcut) {
		JMenuItem item = new JMenuItem(name);
		
		// Set up action listener
		item.addActionListener(this);
		item.setActionCommand(action);
		
		// Set the menu font as the font of this object
		item.setFont(font);
		
		// Set the keyboard shortcut for this item if there is one.
		if (shortcut > 0)
			item.setAccelerator(KeyStroke.getKeyStroke(shortcut, 
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		// Return the menu item.
		return item;
	}
	
	/**
	 * Called when a menu item is clicked.
	 * @param event - the ActionEvent object representing the item that was clicked.
	 */
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		// Standard file commands
		if (command.equals("new")) newFile();
		else if (command.equals("open")) openFile();
		else if (command.equals("save")) saveFile();
		else if (command.equals("save as")) saveFileAs();
		else if (command.equals("quit")) quit();
		
		// Command to open an example
		else if (example.containsKey(command)) {
			openExample(command);
		}
	}
	
	/**
	 * Opens a new instance of the Pseudocode editor.
	 */
	private void newFile() {
		new Pseudocode();
	}

	/**
	 * Quits the current pseudocode editor.
	 */
	private void quit() {
		// TODO: check if work is unsaved
		pseudocode.dispatchEvent(new WindowEvent(pseudocode, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Use a JFileChooser object to select the file that should be opened.
	 */
	private void openFile() {
		// Create a JFileChooser with the file filter defined above.
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(pseudocodeFilter);
		
		// Get the result of an open dialog.
		int choice = chooser.showOpenDialog(pseudocode);
		
		// If the user selected a file to open, get the File object
		if (choice == JFileChooser.APPROVE_OPTION) {
			File open = chooser.getSelectedFile();
			
			// Check if this is a valid file, and if it is, update the pseudocode editor
			// with the String contents of the file.
			if (open.exists()) {
				pseudocode.updateText(readFile(open));
			}
		}
	}
	
	/**
	 * Saves the current file.
	 */
	private void saveFile() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Saves the current file by asking the user to select a new file target.
	 */
	private void saveFileAs() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Opens an example file.
	 * @param exampleName
	 */
	private void openExample(String exampleName) {
		// TODO: check for unsaved changes
		if (example != null)
			pseudocode.updateText(example.get(exampleName));
	}
}
 