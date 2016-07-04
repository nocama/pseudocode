package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import expression.RGB;
import expression.Terminal;
import instruction.Block;

/**
 * An OutputPanel is a JPanel that draws the output of an algorithm.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 */
public class OutputPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	// The underlying algorithm being run by this output view.
	private Block block;
	// Flag for resetting the output view.
	private boolean reset = true;
	private boolean alwaysRepaint = true;
	private boolean printBlock = true;

	// BufferedImages for double buffering output
	private BufferedImage front;
	private BufferedImage back;
	private boolean frontBuffer = true;

	// Static values for storing the mouse and keyboard inputs
	public static Terminal mouseClicked = new Terminal();
	public static Terminal mouseX = new Terminal();
	public static Terminal mouseY = new Terminal();

	/**
	 * Constructs an OutputPanel object that will be displayed in the given PseudocodeFrame frame.
	 * @param frame - the frame that the output panel would be placed in
	 */
	public OutputPanel(Pseudocode frame) {

		// Sets the width and height of the panel
		setSize(frame.getWidth() / 2, frame.getHeight());

		// Create a timer that continuously repaints the window
		Timer t = new Timer();
		// Schedule repaints at a fixed rate.
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				repaint();
			}
		}, 0, 20);

		// 
		setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		repaint();
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		
		// Initialize the graphics buffers.
		front = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		back =  new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Resets the buffers and double buffering.
	 */
	public void reset() {
		// Reset the graphics buffers to white
		front.getGraphics().setColor(Color.WHITE);
		front.getGraphics().fillRect(0, 0, getWidth(), getHeight());
		back.getGraphics().setColor(Color.WHITE);
		back.getGraphics().fillRect(0, 0, getWidth(), getHeight());

		System.out.println(getWidth() + " x " + getHeight());

		frontBuffer = true;
	}

	public void paint(Graphics g) {
		if (reset) {
			reset = false;
			reset();
			//g.drawImage(front, 0, 0, null);
		}
		if (block != null) {
			((frontBuffer) ? back : front).getGraphics()
			.drawImage(((frontBuffer) ? front : back), 0, 0, null);

			block.execute(((frontBuffer) ? back : front).getGraphics(), block);
			g.drawImage(((frontBuffer) ? back : front), 0, 0, null);

			frontBuffer = ! frontBuffer;			
		}
	}

	public void update(Block block) {
		// Initialize color palette.
		RGB.initialize();

		// Cancel repaint for equivalent program
		if (! alwaysRepaint && this.block != null && this.block.equals(block))
			return;

		// Assign the width and height values to the block.
		block.assign("width", new Terminal(getWidth()));
		block.assign("height", new Terminal(getHeight()));
		block.assign("mousex", mouseX);
		block.assign("mousey", mouseY);
		block.assign("mouseclicked", mouseClicked);

		// Returns true if the block should be print out.
		if (printBlock) {
			print(block);
		}

		// Set the reset flag for the next repaint.
		block.reset();
		this.block = block;
		reset = true;
	}


	private void print(Block block) {
		System.out.println("public class Pseudocode {");
		for (String symbol : block.getSymbolTable().keySet()) {
			System.out.print("static double ");
			System.out.print(symbol);
			System.out.print(" = ");
			System.out.print(block.get(symbol));
			System.out.print(";\n");
		}
		System.out.print("public static void main(String[] args) ");
		System.out.println(block);
		System.out.println("}");
	}

	public void mouseMoved(MouseEvent e) {
		mouseX.setValue(e.getX());
		mouseY.setValue(e.getY());
		if (block != null) {
			block.assign("mousex", mouseX);
			block.assign("mousey", mouseY);
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		mouseClicked.setValue(1);
		if (block != null)
			block.assign("mouseclicked", mouseClicked);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("release");
		mouseClicked.setValue(0);
		if (block != null)
			block.assign("mouseclicked", mouseClicked);
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
}
