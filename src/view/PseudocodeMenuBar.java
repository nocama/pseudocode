package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class PseudocodeMenuBar extends JMenuBar implements ActionListener {
	
	private Pseudocode pseudocode;
	private FileFilter pseudocodeFilter = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.getAbsolutePath().endsWith(".pseudocode");
		}

		@Override
		public String getDescription() {
			return "Pseudocode (.pseudocode)";
		}
	};
	
	public PseudocodeMenuBar(Pseudocode pseudocode) {
		super();
		this.pseudocode = pseudocode;
		initialize();
	}
	
	private void initialize() {
		JMenu fileMenu = createMenu("File");
		fileMenu.add(createMenuItem("New"));
		fileMenu.add(createMenuItem("Open"));
		fileMenu.add(createMenuItem("Save"));
		fileMenu.add(createMenuItem("Save As"));
		fileMenu.add(createMenuItem("Quit"));
		add(fileMenu);
		
		JMenu examplesMenu = createMenu("Examples");
		add( createMenu("Examples") );
	}
	
	private JMenu createMenu(String name) {
		JMenu menu = new JMenu(name);
		return menu;
	}
	
	private JMenuItem createMenuItem(String name) {
		return createMenuItem(name, name.toLowerCase());
	}
	
	private JMenuItem createMenuItem(String name, String action) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(this);
		item.setActionCommand(action);
		return item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (e.equals("open")) openFile();
		if (e.equals("save")) saveFile();
		if (e.equals("quit")) quit();
	}

	private void quit() {
		pseudocode.dispatchEvent(new WindowEvent(pseudocode, WindowEvent.WINDOW_CLOSING));
	}

	private void openFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(pseudocodeFilter);
		
		int choice = chooser.showOpenDialog(pseudocode);
		
		if (choice == JFileChooser.APPROVE_OPTION) {
			File open = chooser.getSelectedFile();
			
			if (open.exists()) {
				try {
					Scanner fileScanner = new Scanner(open);
					StringBuilder content = new StringBuilder();
					
					while (fileScanner.hasNextLine()) {
						content.append(fileScanner.nextLine()).append("\n");
					}
					
					pseudocode.update(content.toString());
					
				} catch (FileNotFoundException e) {}
			}
		}
	}

	private void saveFile() {
		// TODO Auto-generated method stub
		
	}
}
