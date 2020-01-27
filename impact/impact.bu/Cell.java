import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class Common {
	public static int MAXX = 11;
	public static int MAXY = 10;
	public static Cell cells[][] = new Cell[MAXX][MAXY];
	public static Personality personalities[][] = new Personality[MAXX][MAXY];
	public static final int TOP = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
}

abstract class Personality implements Serializable {
	public abstract void step(int x, int y);
	public String step(int xn, int yn, int x, int y, int from, int to, String inData) {
		Cell neighbor = Common.cells[xn][yn];
		Cell self = Common.cells[x][y];
		boolean neighborFull = neighbor.getCellFull(from);
		boolean full = self.getCellFull(to);
		if (neighborFull && !full) {
			if (inData == null) {
				inData = neighbor.getInputData(from);
			}
			self.setOutputData(to, inData);
		}
		return inData;
	}
	public String step(int xn, int yn, int x, int y, int from, int to) {
		String inData = null;
		inData = step(xn, yn, x, y, from, to, inData);
		return inData;
	}
}

public class Cell extends JLabel implements ComponentListener, Runnable, Serializable {
	private boolean lOutput = false;
	private boolean rOutput = false;
	private boolean tOutput = false;
	private boolean bOutput = false;
	protected boolean lFull = false;
	protected boolean rFull = false;
	protected boolean tFull = false;
	protected boolean bFull = false;
	public static int M = 15; // size of subcell in pixels
					// multiply by 5 to get cell size
	public static final int SX = 4;
	public static final int SY = 3;
	public static boolean color = false;
	public static JTextField numTimes = new JTextField();
	public static JTextField interval = new JTextField();
	public static JPanel cellsView = new JPanel();
	public static boolean forever = false;
	public Cell() {
	}
	public Cell(int x, int y) {
		Common.cells[x][y] = this;
	}
	public String getOutput(boolean full, boolean out) {
		if (!full) {
			return " ";
		} else if (out) {
			return "1";
		} else {
			return "0";
		}
	}
	public String getTopOutput() {
		boolean tf = tFull;
		tFull = false;
		return getOutput(tf, tOutput);
	}
	public String getBottomOutput() {
		boolean bf = bFull;
		bFull = false;
		return getOutput(bf, bOutput);
	}
	public String getLeftOutput() {
		boolean lf = lFull;
		lFull = false;
		return getOutput(lf, lOutput);
	}
	public String getRightOutput() {
		boolean rf = rFull;
		rFull = false;
		return getOutput(rf, rOutput);
	}
	public String getTopDisplay() {
		return getOutput(tFull, tOutput);
	}
	public String getBottomDisplay() {
		return getOutput(bFull, bOutput);
	}
	public String getLeftDisplay() {
		return getOutput(lFull, lOutput);
	}
	public String getRightDisplay() {
		return getOutput(rFull, rOutput);
	}
	public void setTopOutput(String out) {
		if (out.equals(" ")) {
			tFull = false;
		} else {
			tFull = true;
			if (out.equals("1")) {
				tOutput = true;
			} else {
				tOutput = false;
			}
		}
	}
	public void setBottomOutput(String out) {
		if (out.equals(" ")) {
			bFull = false;
		} else {
			bFull = true;
			if (out.equals("1")) {
				bOutput = true;
			} else {
				bOutput = false;
			}
		}
	}
	public void setLeftOutput(String out) {
		if (out.equals(" ")) {
			lFull = false;
		} else {
			lFull = true;
			if (out.equals("1")) {
				lOutput = true;
			} else {
				lOutput = false;
			}
		}
	}
	public void setRightOutput(String out) {
		if (out.equals(" ")) {
			rFull = false;
		} else {
			rFull = true;
			if (out.equals("1")) {
				rOutput = true;
			} else {
				rOutput = false;
			}
		}
	}
	public void drawTile(Graphics g) {
	}
	public void paint(Graphics g) {
		super.paint(g);
		drawTile(g);
		g.setColor(Color.BLACK);
		g.drawLine(0,0,0,5*M);
		g.drawLine(0,5*M,5*M,5*M);
		g.drawLine(5*M,5*M,5*M,0);
		g.drawLine(5*M,0,0,0);

		g.drawLine(3*M,0,3*M,M);
		g.drawLine(3*M,M,4*M,M);
		g.drawLine(4*M,M,4*M,0);
		g.drawString(getTopDisplay(), 3*M+SX, M-SY);

		g.drawLine(M,5*M,M,4*M);
		g.drawLine(M,4*M,2*M,4*M);
		g.drawLine(2*M,4*M,2*M,5*M);
		g.drawString(getBottomDisplay(), M+SX, 5*M-SY);

		g.drawLine(0,M,M,M);
		g.drawLine(M,M,M,2*M);
		g.drawLine(M,2*M,0,2*M);
		g.drawString(getLeftDisplay(), 0+SX, 2*M-SY);

		g.drawLine(5*M,3*M,4*M,3*M);
		g.drawLine(4*M,3*M,4*M,4*M);
		g.drawLine(4*M,4*M,5*M,4*M);
		g.drawString(getRightDisplay(), 4*M+SX, 4*M-SY);
	}
	public Dimension getPreferredSize() {
		return new Dimension(5*M,5*M);
	}
	public static void main(String args[]) {
		String personalities_str = "4x4";
		if (args.length > 0) {
			personalities_str = args[0];
		}
		if (personalities_str.equals("cell")) {
			Common.MAXX = 3;
			Common.MAXY = 3;
		}
			
		JFrame jf = new JFrame();
		cellsView.setBackground(Color.WHITE);
		cellsView.setLayout(new GridLayout(Common.MAXY,Common.MAXX));
		JScrollPane jsp = new JScrollPane(cellsView);


		int y = 0;
		int x = 0;
		new Empty(x, y);
		for (x = 1; x < Common.MAXX-1; x++) {
			new Buffer(x, y);
		}
		new Empty(x, y);

		if (personalities_str.equals("cell")) {
			// simple adder
			new Buffer(0, 1);
			new MultAdder(1, 1);
			new Buffer(2, 1);
		} else if (personalities_str.equals("4x4")) {
			// 4 x 4 bit multiplier
			new Buffer(0, 1);
			new DontKnow(1, 1);
			new RightTurn(2, 1);
			new DontKnow(3, 1);
			new RightTurn(4, 1);
			new DontKnow(5, 1);
			new RightTurn(6, 1);
			new DontKnow(7, 1);
			new RightTurn(8, 1);
			new RightTurn(9, 1);
			new Buffer(10, 1);

			for (int z = 2; z < 8; z+=2) {
				new Buffer(0, z);
				new Pass(1, z);
				new And(2, z);
				new Pass(3, z);
				new And(4, z);
				new Pass(5, z);
				new And(6, z);
				new Pass(7, z);
				new And(8, z);
				new Pass(9, z);
				new Buffer(10, z);

				new Buffer(0, z+1);
				new LeftTurn(1, z+1);
				new MultAdder(2, z+1);
				new DontKnow(3, z+1);
				new MultAdder(4, z+1);
				new DontKnow(5, z+1);
				new MultAdder(6, z+1);
				new DontKnow(7, z+1);
				new MultAdder(8, z+1);
				new DontKnow(9, z+1);
				new Buffer(10, z+1);
			}
			new Buffer(0, 8);
			new Pass(1, 8);
			new And(2, 8);
			new Pass(3, 8);
			new And(4, 8);
			new Pass(5, 8);
			new And(6, 8);
			new Pass(7, 8);
			new And(8, 8);
			new Pass(9, 8);
			new Buffer(10, 8);
		} else {
			// random personalities
			Random r = new Random();
			for (y = 1; y < Common.MAXY-1; y++) {
				x = 0;
				new Buffer(x, y);
				for (x = 1; x < Common.MAXX-1; x++) {
					switch (r.nextInt(8)) {
					case 0:
						new RightTurn(x, y);
						break;
					case 1:
						new LeftTurn(x, y);
						break;
					case 2:
						new Pass(x, y);
						break;
					case 3:
						new LeftShift(x, y);
						break;
					case 4:
						new RightShift(x, y);
						break;
					case 5:
						new MultAdder(x, y);
						break;
					case 6:
						new And(x, y);
						break;
					default:
						new DontKnow(x, y);
						break;
					}
				}
				x = Common.MAXX-1;
				new Buffer(x, y);
			}
		}
		// continures here
		y = Common.MAXY-1;
		x = 0;
		new Empty(x, y);
		for (x = 1; x < Common.MAXX-1; x++) {
			new Buffer(x, y);
		}
		new Empty(x, y);

		for (y = 0; y < Common.MAXY; y++) {
			for (x = 0; x < Common.MAXX; x++) {
				cellsView.add(Common.cells[x][y]);
			}
		}
		JMenuBar jmb = new JMenuBar();
		jf.setJMenuBar(jmb);
		JMenu jm = new JMenu("File");
		jmb.add(jm);
		JMenuItem jmi = new JMenuItem("Open");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				open();
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Save");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Exit");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		jm.add(jmi);
		// jf.addComponentListener(Common.cells[0][0]);
		JPanel tools = new JPanel();
		JButton adv = new JButton("Advance");
		adv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new Thread(Common.cells[0][0]).start();
			}
		});
		JButton intr = new JButton("Interrupt");
		intr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				forever = false;
			}
		});
		tools.add(adv);
		tools.add(new JLabel("Number of half steps"));
		numTimes.setColumns(3);
		tools.add(numTimes);
		tools.add(intr);
		tools.add(new JLabel("Refresh interval"));
		interval.setColumns(6);
		tools.add(interval);
		jf.getContentPane().add("North", tools);
		jf.getContentPane().add("Center", jsp);
		jf.setSize(800,600);
		jf.setVisible(true);
	}
	public static void open() {
		try {
			FileInputStream fis = new FileInputStream("impact.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Common.MAXX = ois.readInt();
			Common.MAXY = ois.readInt();
			cellsView.removeAll();
			cellsView.setLayout(new GridLayout(Common.MAXY,Common.MAXX));
			Common.cells = new Cell[Common.MAXX][Common.MAXY];
			Common.personalities = new Personality[Common.MAXX][Common.MAXY];
			for (int y = 0; y < Common.MAXY; y++) {
				for (int x = 0; x < Common.MAXX; x++) {
					String cname = (String)ois.readObject();
					Common.cells[x][y] = (Cell)Class.forName(cname).newInstance();
					if (Common.cells[x][y] instanceof Buffer) {
						Buffer b = (Buffer)Common.cells[x][y];
						b.addMouseListener(b);
						b.setIn((String)ois.readObject());
						b.setOut((String)ois.readObject());
					}
					Common.cells[x][y].lOutput = ois.readBoolean();
					Common.cells[x][y].rOutput = ois.readBoolean();
					Common.cells[x][y].tOutput = ois.readBoolean();
					Common.cells[x][y].bOutput = ois.readBoolean();
					Common.cells[x][y].lFull = ois.readBoolean();
					Common.cells[x][y].rFull = ois.readBoolean();
					Common.cells[x][y].tFull = ois.readBoolean();
					Common.cells[x][y].bFull = ois.readBoolean();
					cellsView.add(Common.cells[x][y]);
					Common.cells[x][y].repaint();
					String pname = (String)ois.readObject();
					Common.personalities[x][y] = (Personality)Class.forName(pname).newInstance();
				}
			}
			ois.close();
			fis.close();
			cellsView.repaint();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Load");
		}
	}
	public static void save() {
		try {
			FileOutputStream fos = new FileOutputStream("impact.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeInt(Common.MAXX);
			oos.writeInt(Common.MAXY);
			for (int y = 0; y < Common.MAXY; y++) {
				for (int x = 0; x < Common.MAXX; x++) {
					oos.writeObject(Common.cells[x][y].getClass().getName());
					if (Common.cells[x][y] instanceof Buffer) {
						Buffer b = (Buffer)Common.cells[x][y];
						oos.writeObject(b.getIn());
						oos.writeObject(b.getOut());
					}
					oos.writeBoolean(Common.cells[x][y].lOutput);
					oos.writeBoolean(Common.cells[x][y].rOutput);
					oos.writeBoolean(Common.cells[x][y].tOutput);
					oos.writeBoolean(Common.cells[x][y].bOutput);
					oos.writeBoolean(Common.cells[x][y].lFull);
					oos.writeBoolean(Common.cells[x][y].rFull);
					oos.writeBoolean(Common.cells[x][y].tFull);
					oos.writeBoolean(Common.cells[x][y].bFull);
					oos.writeObject(Common.personalities[x][y].getClass().getName());
				}
			}
			oos.writeObject(Common.personalities);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public void drawHArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		if (x1 < x2) {
			g.drawLine(x1, y1, x1+2, y1+2);
			g.drawLine(x1, y1, x1+2, y1-2);
		} else {
			g.drawLine(x1, y1, x1-2, y1+2);
			g.drawLine(x1, y1, x1-2, y1-2);
		}
	}
	public void drawVArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		if (y1 < y2) {
			g.drawLine(x1, y1, x1+2, y1+2);
			g.drawLine(x1, y1, x1-2, y1+2);
		} else {
			g.drawLine(x1, y1, x1+2, y1-2);
			g.drawLine(x1, y1, x1-2, y1-2);
		}
	}
	public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		double th = Math.atan2(y1 - y2, x2 - x1);
		g.fillArc(x1-M/2, y1-M/2, M, M, (int)(th*180/3.1416)-20, 40);
	}
	public void componentHidden(ComponentEvent e) {
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentResized(ComponentEvent e) {
		Dimension d = e.getComponent().getSize();
		if (d.width < d.height) {
			M = d.height / (5 * Common.MAXY);
		 } else {
			M = d.width / (5 * Common.MAXX);
		}
		e.getComponent().repaint();
	}
	public void componentShown(ComponentEvent e) {
	}
	public void run() {
		long curtime = System.currentTimeMillis();
		int nt = 1;
		try {
			nt = Integer.parseInt(numTimes.getText());
		} catch (Exception e) {
			forever = true;
		}
		int intv = 0;
		try {
			intv = Integer.parseInt(interval.getText());
		} catch (Exception e) {
		}
		for(int i = 0; i < nt || forever; i++) {
			int y = 0;
			int x = 0;
			if (color) {
				// black squares in checker board
				for (y = 0; y < Common.MAXY; y+=2) {
					for (x = 0; x < Common.MAXX; x+=2) {
						Common.personalities[x][y].step(x, y);
					}
				}
				for (y = 1; y < Common.MAXY; y+=2) {
					for (x = 1; x < Common.MAXX; x+=2) {
						Common.personalities[x][y].step(x, y);
					}
				}
				color = false;
			} else {
				// red squares in checker board
				for (x = 1; x < Common.MAXX; x+=2) {
					for (y = 0; y < Common.MAXY; y+=2) {
						Common.personalities[x][y].step(x, y);
					}
				}
				for (x = 0; x < Common.MAXX; x+=2) {
					for (y = 1; y < Common.MAXY; y+=2) {
						Common.personalities[x][y].step(x, y);
					}
				}
				color = true;
			}
			long newtime = System.currentTimeMillis();
			if (newtime - curtime > intv * 1000) {
				// repaint all Common.cells
				for (y = 0; y < Common.MAXY; y++) {
					for (x = 0; x < Common.MAXX; x++) {
						Common.cells[x][y].repaint();
					}
				}
				curtime = newtime;
			}
		}
	}
	public boolean getCellFull(int direct) {
		boolean full = false;
		if (direct == Common.LEFT) {
			full = lFull;
		} else if (direct == Common.RIGHT) {
			full = rFull;
		} else if (direct == Common.TOP) {
			full = tFull;
		} else if (direct == Common.BOTTOM) {
			full = bFull;
		}
		return full;
	}
	public String getInputData(int direct) {
		String inData = null;
		if (direct == Common.LEFT) {
			inData = getLeftOutput();
		} else if (direct == Common.RIGHT) {
			inData = getRightOutput();
		} else if (direct == Common.TOP) {
			inData = getTopOutput();
		} else if (direct == Common.BOTTOM) {
			inData = getBottomOutput();
		}
		return inData;
	}
	public void setOutputData(int direct, String outData) {
		if (direct == Common.LEFT) {
			setLeftOutput(outData);
		} else if (direct == Common.RIGHT) {
			setRightOutput(outData);
		} else if (direct == Common.TOP) {
			setTopOutput(outData);
		} else if (direct == Common.BOTTOM) {
			setBottomOutput(outData);
		}
	}
}

class Pass extends Cell {
	public Pass() {
	}
	public Pass(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new PassP();
	}
	public void drawTile(Graphics g) {
		drawArrow(g, M,M+M/2,5*M,M+M/2);
		drawArrow(g, 4*M,3*M+M/2,0,3*M+M/2);
		drawArrow(g, 3*M+M/2,M,3*M+M/2,5*M);
		drawArrow(g, M+M/2,4*M,M+M/2,0);
		g.drawString("Pass", SX, 3*M-SY);
	}
}

class PassP extends Personality {
	public void step(int x, int y) {
		step(x+1, y, x, y, Common.LEFT, Common.LEFT);
		step(x-1, y, x, y, Common.RIGHT, Common.RIGHT);
		step(x, y+1, x, y, Common.TOP, Common.TOP);
		step(x, y-1, x, y, Common.BOTTOM, Common.BOTTOM);
	}
}

class RightTurn extends Cell {
	public RightTurn() {
	}
	public RightTurn(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new RightTurnP();
	}
	public void drawTile(Graphics g) {
		drawArrow(g, M,M+M/2,M+M/2,0);
		drawArrow(g, 3*M+M/2,M,5*M,M+M/2);
		drawArrow(g, 4*M,3*M+M/2,3*M+M/2,5*M);
		drawArrow(g, M+M/2,4*M,0,3*M+M/2);
		g.drawString("RightTurn", SX, 3*M-SY);
	}
}

class RightTurnP extends Personality {
	public void step(int x, int y) {
		step(x, y-1, x, y, Common.BOTTOM, Common.LEFT);
		step(x, y+1, x, y, Common.TOP, Common.RIGHT);
		step(x-1, y, x, y, Common.RIGHT, Common.BOTTOM);
		step(x+1, y, x, y, Common.LEFT, Common.TOP);
	}
}

class LeftTurn extends Cell {
	public LeftTurn() {
	}
	public LeftTurn(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new LeftTurnP();
	}
	public void drawTile(Graphics g) {
		drawArrow(g, M,M+M/2, 3*M+M/2,5*M);
		drawArrow(g, 3*M+M/2,M, 0,3*M+M/2);
		drawArrow(g, 4*M,3*M+M/2, M+M/2,0);
		drawArrow(g, M+M/2,4*M, 5*M,M+M/2);
		g.drawString("LeftTurn", SX, 3*M-SY);
	}
}
class LeftTurnP extends Personality {
	public void step(int x, int y) {
		step(x, y+1, x, y, Common.TOP, Common.LEFT);
		step(x, y-1, x, y, Common.BOTTOM, Common.RIGHT);
		step(x+1, y, x, y, Common.LEFT, Common.BOTTOM);
		step(x-1, y, x, y, Common.RIGHT, Common.TOP);
	}
}

class RightShift extends Cell {
	public RightShift() {
	}
	public RightShift(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new RightShiftP();
	}
	public void drawTile(Graphics g) {
		drawArrow(g, M,M+M/2,M+M/2,0);
		drawArrow(g, 3*M+M/2,M,0,3*M+M/2);
		drawArrow(g, 4*M,3*M+M/2,3*M+M/2,5*M);
		drawArrow(g, M+M/2,4*M,5*M,M+M/2);
		g.drawString("RightShift", SX, 3*M-SY);
	}
}

class RightShiftP extends Personality {
	public void step(int x, int y) {
		step(x, y-1, x, y, Common.BOTTOM, Common.LEFT);
		step(x, y+1, x, y, Common.TOP, Common.RIGHT);
		step(x+1, y, x, y, Common.LEFT, Common.BOTTOM);
		step(x-1, y, x, y, Common.RIGHT, Common.TOP);
	}
}

class LeftShift extends Cell {
	public LeftShift() {
	}
	public LeftShift(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new LeftShiftP();
	}
	public void drawTile(Graphics g) {
		drawArrow(g, M,M+M/2,3*M+M/2,5*M);
		drawArrow(g, 3*M+M/2,M,5*M,M+M/2);
		drawArrow(g, 4*M,3*M+M/2,M+M/2,0);
		drawArrow(g, M+M/2,4*M,0,3*M+M/2);
		g.drawString("LeftShift", SX, 3*M-SY);
	}
}

class LeftShiftP extends Personality {
	public void step(int x, int y) {
		step(x, y+1, x, y, Common.TOP, Common.LEFT);
		step(x, y-1, x, y, Common.BOTTOM, Common.RIGHT);
		step(x-1, y, x, y, Common.RIGHT, Common.BOTTOM);
		step(x+1, y, x, y, Common.LEFT, Common.TOP);
	}
}

class DontKnow extends Cell {
	public DontKnow() {
	}
	public DontKnow(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new DontKnowP();
	}
	public void drawTile(Graphics g) {
		drawArrow(g, M,M+M/2,M*5,M+M/2);
		drawArrow(g, 4*M,3*M+M/2,M+M/2,0);
		drawArrow(g, M+M/2,4*M,0,3*M+M/2);
		g.drawString("DontKnow", SX, 3*M-SY);
	}
}

class DontKnowP extends Personality {
	public void step(int x, int y) {
		step(x+1, y, x, y, Common.LEFT, Common.LEFT);
		step(x, y-1, x, y, Common.BOTTOM, Common.RIGHT);
		step(x-1, y, x, y, Common.RIGHT, Common.BOTTOM);
	}
}

/* TODO NEEDS WORK
class Copy extends Cell {
	public Copy() {
		setIcon(new ImageIcon("copy.gif"));
	}
	public Copy(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new CopyP();
		setIcon(new ImageIcon("copy.gif"));
	}
}
class CopyP extends Personality {
	public void step(int x, int y) {
		String inData = step(x+1, y, x, y, Common.RIGHT, Common.TOP);
		step(x, y-1, x, y, Common.RIGHT, Common.RIGHT, inData);
		step(x-1, y, x, y, Common.BOTTOM, Common.BOTTOM);
	}
}
*/

class MultAdder extends Cell {
	public MultAdder() {
		setIcon(new ImageIcon("adder.gif"));
	}
	public MultAdder(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new MultAdderP();
		setIcon(new ImageIcon("adder.gif"));
	}
}

class MultAdderP extends Personality {
	public void step(int x, int y) {
		step(x, y-1, x, y, Common.BOTTOM, Common.BOTTOM);
		Cell topNeighbor = Common.cells[x][y-1];
		Cell bottomNeighbor = Common.cells[x][y+1];
		Cell rightNeighbor = Common.cells[x+1][y];
		Cell leftNeighbor = Common.cells[x-1][y];
		if (leftNeighbor.getCellFull(Common.RIGHT) &&
		    bottomNeighbor.getCellFull(Common.TOP) &&
		    rightNeighbor.getCellFull(Common.LEFT)&&
		    !Common.cells[x][y].getCellFull(Common.LEFT) &&
		    !Common.cells[x][y].getCellFull(Common.RIGHT)
		    ) {
			String in1 = leftNeighbor.getInputData(Common.RIGHT);
			String in2 = bottomNeighbor.getInputData(Common.TOP);
			String in3 = rightNeighbor.getInputData(Common.LEFT);
			int sum = 0;
			if (in1.equals("1")) {
				sum++;
			}
			if (in2.equals("1")) {
				sum++;
			}
			if (in3.equals("1")) {
				sum++;
			}
			switch (sum) {
			case 0:
				Common.cells[x][y].setOutputData(Common.LEFT, "0");
				Common.cells[x][y].setOutputData(Common.RIGHT, "0");
				break;
			case 1:
				Common.cells[x][y].setOutputData(Common.LEFT, "0");
				Common.cells[x][y].setOutputData(Common.RIGHT, "1");
				break;
			case 2:
				Common.cells[x][y].setOutputData(Common.LEFT, "1");
				Common.cells[x][y].setOutputData(Common.RIGHT, "0");
				break;
			case 3:
				Common.cells[x][y].setOutputData(Common.LEFT, "1");
				Common.cells[x][y].setOutputData(Common.RIGHT, "1");
				break;
			}	
		}
	}
}

class BitAdder extends Cell {
	public BitAdder() {
	}
	public BitAdder(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new BitAdderP();
		// setIcon(new ImageIcon("adder.gif"));
	}
}

class BitAdderP extends Personality {
	public void step(int x, int y) {
		Cell topNeighbor = Common.cells[x][y-1];
		Cell bottomNeighbor = Common.cells[x][y+1];
		Cell rightNeighbor = Common.cells[x+1][y];
		Cell leftNeighbor = Common.cells[x-1][y];
		if (topNeighbor.getCellFull(Common.BOTTOM) &&
		    bottomNeighbor.getCellFull(Common.TOP) &&
		    rightNeighbor.getCellFull(Common.LEFT)&&
		    !Common.cells[x][y].getCellFull(Common.LEFT) &&
		    !Common.cells[x][y].getCellFull(Common.RIGHT)
		    ) {
			String in1 = topNeighbor.getInputData(Common.BOTTOM);
			String in2 = bottomNeighbor.getInputData(Common.TOP);
			String in3 = rightNeighbor.getInputData(Common.LEFT);
			int sum = 0;
			if (in1.equals("1")) {
				sum++;
			}
			if (in2.equals("1")) {
				sum++;
			}
			if (in3.equals("1")) {
				sum++;
			}
			switch (sum) {
			case 0:
				Common.cells[x][y].setOutputData(Common.LEFT, "0");
				Common.cells[x][y].setOutputData(Common.RIGHT, "0");
				break;
			case 1:
				Common.cells[x][y].setOutputData(Common.LEFT, "0");
				Common.cells[x][y].setOutputData(Common.RIGHT, "1");
				break;
			case 2:
				Common.cells[x][y].setOutputData(Common.LEFT, "1");
				Common.cells[x][y].setOutputData(Common.RIGHT, "0");
				break;
			case 3:
				Common.cells[x][y].setOutputData(Common.LEFT, "1");
				Common.cells[x][y].setOutputData(Common.RIGHT, "1");
				break;
			}	
			step(x-1, y, x, y, Common.RIGHT, Common.BOTTOM);
		}
	}
}

class And extends Cell {
	public And() {
		setIcon(new ImageIcon("andthing.gif"));
	}
	public And(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new AndP();
		setIcon(new ImageIcon("andthing.gif"));
	}
}

class AndP extends Personality {
	public void step(int x, int y) {
		Cell rightNeighbor = Common.cells[x+1][y];
		Cell topNeighbor = Common.cells[x][y-1];
		String inData1 = rightNeighbor.getLeftDisplay(); // peek at values
		String inData2 = topNeighbor.getBottomDisplay();
		String test = Common.cells[x][y].getTopDisplay();
		if (!inData1.equals(" ") && !inData2.equals(" ") && test.equals(" ")) {
			step(x+1, y, x, y, Common.LEFT, Common.LEFT);
			step(x, y-1, x, y, Common.BOTTOM, Common.BOTTOM);
			if (inData1.equals("1") && inData2.equals("1")) {
				Common.cells[x][y].setTopOutput("1");
			} else {
				Common.cells[x][y].setTopOutput("0");
			}
		}
	}
}

class Buffer extends Cell implements MouseListener {
	private StringBuffer in = new StringBuffer();
	private StringBuffer out = new StringBuffer();
	public Buffer() {
	}
	public Buffer(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new BufferP();
		addMouseListener(this);
	}
	public String getIn() {
		return in.toString();
	}
	public String getOut() {
		return out.toString();
	}
	public void setIn(String s) {
		in = new StringBuffer(s);
	}
	public void setOut(String s) {
		out = new StringBuffer(s);
	}
	public void addToInBuffer(String value) {
		in.append(value);
		tFull = true;
		bFull = true;
		rFull = true;
		lFull = true;
		repaint();
	}
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			addToInBuffer("1");
		} else {
			addToInBuffer("0");
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(0,0,0,5*M);
		g.drawLine(0,5*M,5*M,5*M);
		g.drawLine(5*M,5*M,5*M,0);
		g.drawLine(5*M,0,0,0);
		g.drawString(in.toString(), SX, 2*M-SY);
		g.drawString(out.toString(), SX, 4*M-SY);
	}
	public String getOutput(boolean empty) {
		if (in.length() > 0) {
			char ch = in.charAt(0);
			if (empty) {
				in.deleteCharAt(0);
			}
			tFull = true;
			bFull = true;
			rFull = true;
			lFull = true;
			return Character.toString(ch);
		} else {
			tFull = false;
			bFull = false;
			rFull = false;
			lFull = false;
			return " ";
		}
	}
	public String getTopOutput() {
		return getOutput(true);
	}
	public String getBottomOutput() {
		return getOutput(true);
	}
	public String getLeftOutput() {
		return getOutput(true);
	}
	public String getRightOutput() {
		return getOutput(true);
	}
	public String getTopDisplay() {
		return getOutput(false);
	}
	public String getBottomDisplay() {
		return getOutput(false);
	}
	public String getLeftDisplay() {
		return getOutput(false);
	}
	public String getRightDisplay() {
		return getOutput(false);
	}
	public void setOutputData(int direct, String outData) {
		out.append(outData);
	}
}

class BufferP extends Personality {
	public void step(int x, int y) {
		if (x ==  0) {
			step(x+1, y, x, y, Common.LEFT, Common.LEFT);
		}
		if (x == Common.MAXX-1) {
			step(x-1, y, x, y, Common.RIGHT, Common.RIGHT);
		}
		if (y == 0) {
			step(x, y+1, x, y, Common.TOP, Common.TOP);
		}
		if (y == Common.MAXY-1) {
			step(x, y-1, x, y, Common.BOTTOM, Common.BOTTOM);
		}
	}
}

class Empty extends Cell {
	public Empty() {
	}
	public Empty(int x, int y) {
		super(x, y);
		Common.personalities[x][y] = new EmptyP();
	}
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(0,0,0,5*M);
		g.drawLine(0,5*M,5*M,5*M);
		g.drawLine(5*M,5*M,5*M,0);
		g.drawLine(5*M,0,0,0);
	}
}

class EmptyP extends Personality {
	public void step(int x, int y) {
	}
}
