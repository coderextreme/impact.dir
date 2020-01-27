import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Cell extends Component implements MouseMotionListener, MouseListener, KeyListener, Runnable {
	public static int M = Common.M;
	public static int SX = Common.SX;
	public static int SY = Common.SY;
	public static JTextField numTimes = new JTextField();
	public static JTextField interval = new JTextField();
	public static JPanel cellsView = new JPanel();
	public static JFrame ask = new JFrame("Enter number of cells in x and y directions");
	public static JFrame stopdlg = null;
	public static JPanel jp = new JPanel();
	public static JLabel jlx = new JLabel("Width w/o buffers");
	public static JTextField jtfx = new JTextField(" 9");
	public static JLabel jly = new JLabel("Height w/o buffers");
	public static JTextField jtfy = new JTextField(" 8");
	public static JButton ok = new JButton("OK");
	public static JButton cancel = new JButton("Cancel");
	Personality p = null;
	public static String pClass = "EmptyP";
	int x = 0;
	int y = 0;

	public Cell(int x, int y) {
		super.setBackground(Color.white);
		this.x = x;
		this.y = y;
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	public void setPersonality(Personality p) {
		this.p = p;
		if (p != null) {
			p.setObserver(this);
		}
	}
	public Personality getPersonality() {
		return p;
	}
	public void setXY(int x, int y) {
	}
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (Common.starty > 0)
				Common.starty--;
			movePersonalities();
			break;
		case KeyEvent.VK_DOWN:
			if (Common.starty + Common.PMAXY < Common.PMAXY) 
				Common.starty++;
			movePersonalities();
			break;
		case KeyEvent.VK_LEFT:
			if (Common.startx > 0)
				Common.startx--;
			movePersonalities();
			break;
		case KeyEvent.VK_RIGHT:
			if (Common.startx + Common.PMAXX < Common.PMAXX) 
				Common.startx++;
			movePersonalities();
			break;
		default:
			if (p instanceof BufferP) {
				((BufferP)p).keyPressed(e);
			}
			break;
		}
	}
	static public void movePersonalities() {
		for (int y = 0; y < Common.PMAXY; y++) {
			for (int x = 0; x < Common.PMAXX; x++) {
				if (Common.cells[x][y] != null) {
					if (x+Common.startx < Common.PMAXX &&
					    y+Common.starty < Common.PMAXY) {
						Common.cells[x][y].setPersonality(Common.personalities[x+Common.startx][y+Common.starty]);
					} else {
						Common.cells[x][y].setPersonality(null);
					}
					Common.cells[x][y].repaint();
				}
			}
		}
		cellsView.invalidate();
		cellsView.validate();
		cellsView.repaint();
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
	public void mouseDragged(MouseEvent me) {
		requestFocus();
	}
	public void mouseMoved(MouseEvent me) {
		requestFocus();
	}

	public void mouseClicked(MouseEvent e) {
		try {
			if (pClass.equals("Module")) {
				if (Common.modulePersonalities != null) {
					for (int mx = 0; mx < Common.modulePersonalities.length; mx++) {
						for (int my = 0; my < Common.modulePersonalities[mx].length; my++) {
							Personality p = Common.personalities[x+mx][y+my] = (Personality)Common.modulePersonalities[mx][my].clone();
							Cell c = Common.cells[x+mx][y+my];
							c.setPersonality(p);
							c.repaint();
						}
					}
				}
			} else if (x+Common.startx > 0 && y+Common.starty > 0 && x+Common.startx < Common.PMAXX-1 && y+Common.starty < Common.PMAXY-1) {
				Personality p = (Personality)(Class.forName(pClass).newInstance());
				Common.personalities[x+Common.startx][y+Common.starty] = p;
				setPersonality(p);
				repaint();
			} else {
				if (p instanceof BufferP) {
					int b = e.getButton();
					if (b == e.BUTTON1) {
						((BufferP)p).addToInBuffer('1');
					} else if (b == e.BUTTON3) {
						((BufferP)p).addToInBuffer('0');
					}
					repaint();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
	{
		if((infoflags & ImageObserver.ALLBITS) != 0) {
			return false;
		}
		return true;
	}
	public void paint(Graphics g) {
		g.setFont(new Font("Helvetica", Font.PLAIN, M - 2));
		g.setColor(Color.white);
		super.paint(g);
		g.fillRect(0,0,5*M,5*M);
		g.setColor(Color.black);
		if (p != null) {
			p.paint(g);
		}
		g.drawLine(0,0,0,5*M);
		g.drawLine(0,5*M,5*M,5*M);
		g.drawLine(5*M,5*M,5*M,0);
		g.drawLine(5*M,0,0,0);

		if (p == null || p instanceof BufferP || p instanceof EmptyP) {
		} else {
			g.drawLine(3*M,0,3*M,M);
			g.drawLine(3*M,M,4*M,M);
			g.drawLine(4*M,M,4*M,0);
			if (p != null && p.getCellFull(Common.TOP)) 
				g.drawString(p.getTopDisplay(), 3*M+SX, M-SY);

			g.drawLine(M,5*M,M,4*M);
			g.drawLine(M,4*M,2*M,4*M);
			g.drawLine(2*M,4*M,2*M,5*M);
			if (p != null && p.getCellFull(Common.BOTTOM))
				g.drawString(p.getBottomDisplay(), M+SX, 5*M-SY);

			g.drawLine(0,M,M,M);
			g.drawLine(M,M,M,2*M);
			g.drawLine(M,2*M,0,2*M);
			if (p != null && p.getCellFull(Common.LEFT))
				g.drawString(p.getLeftDisplay(), 0+SX, 2*M-SY);

			g.drawLine(5*M,3*M,4*M,3*M);
			g.drawLine(4*M,3*M,4*M,4*M);
			g.drawLine(4*M,4*M,5*M,4*M);
			if (p != null && p.getCellFull(Common.RIGHT))
				g.drawString(p.getRightDisplay(), 4*M+SX, 4*M-SY);
		}
	}
	public Dimension getPreferredSize() {
		return new Dimension(5*M,5*M);
	}
	public static void main(String args[]) {
		String personalities_str = "division";
		if (args.length > 0) {
			personalities_str = args[0];
		}
		if (personalities_str.equals("cell")) {
			Common.PMAXX = 3;
			Common.PMAXY = 3;
		} else if (personalities_str.equals("division")) {
			Common.PMAXX = 3;
			Common.PMAXY = 10;
		}
			
		class ClosingFrame extends JFrame implements WindowListener {
			public ClosingFrame() {
				addWindowListener(this);
			}
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				System.err.println("Closing");
				System.exit(0);
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}

		}
		JFrame jf = new ClosingFrame();
		cellsView.setLayout(new GridLayout(Common.PMAXY,Common.PMAXX));
		JScrollPane jsp = new JScrollPane(cellsView);

		int y = 0;
		int x = 0;
		Common.personalities[x][y] = new EmptyP();
		for (x = 1; x < Common.PMAXX-1; x++) {
			Common.personalities[x][y] = new BufferP();
		}
		Common.personalities[x][y] = new EmptyP();

		if (personalities_str.equals("cell")) {
			// simple adder
			Common.personalities[0][1] = new BufferP();
			Common.personalities[1][1] = new MultAdderP();
			Common.personalities[2][1] = new BufferP();
		} else if (personalities_str.equals("4x4")) {
			// 4 x 4 bit multiplier
			Common.personalities[0][1] = new BufferP();
			Common.personalities[1][1] = new DontKnowP();
			Common.personalities[2][1] = new RightTurnP();
			Common.personalities[3][1] = new DontKnowP();
			Common.personalities[4][1] = new RightTurnP();
			Common.personalities[5][1] = new DontKnowP();
			Common.personalities[6][1] = new RightTurnP();
			Common.personalities[7][1] = new DontKnowP();
			Common.personalities[8][1] = new RightTurnP();
			Common.personalities[9][1] = new RightTurnP();
			Common.personalities[10][1] = new BufferP();

			for (int z = 2; z < 8; z+=2) {
				Common.personalities[0][z] = new BufferP();
				Common.personalities[1][z] = new PassP();
				Common.personalities[2][z] = new AndP();
				Common.personalities[3][z] = new PassP();
				Common.personalities[4][z] = new AndP();
				Common.personalities[5][z] = new PassP();
				Common.personalities[6][z] = new AndP();
				Common.personalities[7][z] = new PassP();
				Common.personalities[8][z] = new AndP();
				Common.personalities[9][z] = new PassP();
				Common.personalities[10][z] = new BufferP();

				Common.personalities[0][z+1] = new BufferP();
				Common.personalities[1][z+1] = new LeftTurnP();
				Common.personalities[2][z+1] = new MultAdderP();
				Common.personalities[3][z+1] = new DontKnowP();
				Common.personalities[4][z+1] = new MultAdderP();
				Common.personalities[5][z+1] = new DontKnowP();
				Common.personalities[6][z+1] = new MultAdderP();
				Common.personalities[7][z+1] = new DontKnowP();
				Common.personalities[8][z+1] = new MultAdderP();
				Common.personalities[9][z+1] = new DontKnowP();
				Common.personalities[10][z+1] = new BufferP();
			}
			Common.personalities[0][8] = new BufferP();
			Common.personalities[1][8] = new PassP();
			Common.personalities[2][8] = new AndP();
			Common.personalities[3][8] = new PassP();
			Common.personalities[4][8] = new AndP();
			Common.personalities[5][8] = new PassP();
			Common.personalities[6][8] = new AndP();
			Common.personalities[7][8] = new PassP();
			Common.personalities[8][8] = new AndP();
			Common.personalities[9][8] = new PassP();
			Common.personalities[10][8] = new BufferP();
		} else if (personalities_str.equals("division")) {
			for (y = 1; y < Common.PMAXY-1; y++) {
				Common.personalities[0][y] = new BufferP();
				Common.personalities[1][y] = new DivisionP();
				Common.personalities[2][y] = new BufferP();
				((BufferP)Common.personalities[2][y]).addToInBuffer('1');
				((BufferP)Common.personalities[2][y]).addToInBuffer('0');
				((BufferP)Common.personalities[2][y]).addToInBuffer('0');
				((BufferP)Common.personalities[2][y]).addToInBuffer('0');
				((BufferP)Common.personalities[2][y]).addToInBuffer('0');
				((BufferP)Common.personalities[2][y]).addToInBuffer('0');
				((BufferP)Common.personalities[2][y]).addToInBuffer('0');
				((BufferP)Common.personalities[2][y]).addToInBuffer('1');
			}
			((BufferP)Common.personalities[1][0]).addToInBuffer('1');
			((BufferP)Common.personalities[1][0]).addToInBuffer('1');
		} else {
			// random personalities
			Random r = new Random();
			for (y = 1; y < Common.PMAXY-1; y++) {
				x = 0;
				Common.personalities[x][y] = new BufferP();
				for (x = 1; x < Common.PMAXX-1; x++) {
					switch (r.nextInt(8)) {
					case 0:
						Common.personalities[x][y] = new RightTurnP();
						break;
					case 1:
						Common.personalities[x][y] = new LeftTurnP();
						break;
					case 2:
						Common.personalities[x][y] = new PassP();
						break;
					case 3:
						Common.personalities[x][y] = new LeftShiftP();
						break;
					case 4:
						Common.personalities[x][y] = new RightShiftP();
						break;
					case 5:
						Common.personalities[x][y] = new MultAdderP();
						break;
					case 6:
						Common.personalities[x][y] = new AndP();
						break;
					default:
						Common.personalities[x][y] = new DontKnowP();
						break;
					}
				}
				x = Common.PMAXX-1;
				Common.personalities[x][y] = new BufferP();
			}
		}
		// continures here
		y = Common.PMAXY-1;
		x = 0;
		Common.personalities[x][y] = new EmptyP();
		for (x = 1; x < Common.PMAXX-1; x++) {
			Common.personalities[x][y] = new BufferP();
		}
		Common.personalities[x][y] = new EmptyP();
		addCells();

		JMenuBar jmb = new JMenuBar();
		jf.setJMenuBar(jmb);
		JMenu jm = new JMenu("File");
		jmb.add(jm);
		JMenuItem jmi = new JMenuItem("New");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ok.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						clear();
					}
				});
				cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						ask.setVisible(false);
					}
				});
				jp.add(jlx);
				jp.add(jtfx);
				jp.add(jly);
				jp.add(jtfy);
				jp.add(ok);
				jp.add(cancel);
				ask.getContentPane().add(jp);
				ask.pack();
				ask.setVisible(true);
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Open Module...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				int rv = jfc.showOpenDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					FileInputStream fis = new FileInputStream(jfc.getSelectedFile()); 
					ObjectInputStream ois = new ObjectInputStream(fis);
					openModule(ois);
					ois.close();
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Open Inputs...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				int rv = jfc.showOpenDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					FileInputStream fis = new FileInputStream(jfc.getSelectedFile()); 
					ObjectInputStream ois = new ObjectInputStream(fis);
					openInputs(ois);
					ois.close();
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Open Machine...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				int rv = jfc.showOpenDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					FileInputStream fis = new FileInputStream(jfc.getSelectedFile()); 
					ObjectInputStream ois = new ObjectInputStream(fis);
					openMachine(ois);
					ois.close();
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Save Module As...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				int rv = jfc.showSaveDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					saveModule(oos);
					oos.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Save Inputs As...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				int rv = jfc.showSaveDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				try {
					FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					saveInputs(oos);
					oos.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Save Machine As...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				int rv = jfc.showSaveDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				try {
					FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					saveMachine(oos);
					oos.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
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








		ButtonGroup bg = new ButtonGroup();

		jm = new JMenu("Create");
		jmb.add(jm);
		jmi = new JRadioButtonMenuItem("Module");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "Module";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Pass");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "PassP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Left Turn");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "LeftTurnP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Right Turn");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "RightTurnP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Left Shift");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "LeftShiftP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Right Shift");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "RightShiftP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Copy");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "CopyP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("And");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "AndP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Multiply Adder");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "MultAdderP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Bit Adder");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "BitAdderP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Empty/Flaw");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "EmptyP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Don't Know");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "DontKnowP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Buffer");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "BufferP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Sort Top");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "SortTopP";
			}
		});
		jm.add(jmi);

		jmi = new JRadioButtonMenuItem("Sort Bottom");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "SortBottomP";
			}
		});
		jm.add(jmi);
		jmi = new JRadioButtonMenuItem("Division");
		bg.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = "DivisionP";
			}
		});
		jm.add(jmi);

		JPanel tools = new JPanel();
		JButton adv = new JButton("Advance");
		adv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Common.cells[0][0].step();
				// new Thread(Common.cells[0][0]).start();
			}
		});
		JButton intr = new JButton("Interrupt");
		intr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Common.forever = false;
			}
		});
		tools.add(adv);
		tools.add(new JLabel("Number of steps"));
		numTimes.setColumns(3);
		tools.add(numTimes);
		tools.add(intr);
		tools.add(new JLabel("Pause interval"));
		interval.setColumns(6);
		tools.add(interval);
		jf.getContentPane().add("North", tools);
		jf.getContentPane().add("Center", jsp);
		//jf.setSize(800,600);
		jf.pack();
		jf.setVisible(true);
	}
	public static void openModule(ObjectInputStream ois) {
		try {
			pClass = "Module";
			Common.MMAXX = ois.readInt();
			Common.MMAXY = ois.readInt();
			Common.modulePersonalities = new Personality[Common.MMAXX][Common.MMAXY];
			for (int y = 0; y < Common.MMAXY; y++) {
				for (int x = 0; x < Common.MMAXX; x++) {
					String pname = (String)ois.readObject();
					Common.modulePersonalities[x][y] = (Personality)Class.forName(pname).newInstance();
					Common.modulePersonalities[x][y].lOutput = ois.readBoolean();
					Common.modulePersonalities[x][y].rOutput = ois.readBoolean();
					Common.modulePersonalities[x][y].tOutput = ois.readBoolean();
					Common.modulePersonalities[x][y].bOutput = ois.readBoolean();
					Common.modulePersonalities[x][y].lFull = ois.readBoolean();
					Common.modulePersonalities[x][y].rFull = ois.readBoolean();
					Common.modulePersonalities[x][y].tFull = ois.readBoolean();
					Common.modulePersonalities[x][y].bFull = ois.readBoolean();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Load");
		}
	}
	public static void openInputs(ObjectInputStream ois) {
		try {
			Common.PMAXX = ois.readInt();
			Common.PMAXY = ois.readInt();
			// Common.personalities = new Personality[Common.PMAXX][Common.PMAXY];
			int n = 0;
			try {
				do {
					int x = ois.readInt();
					int y = ois.readInt();
					BufferP bp = new BufferP();
					Common.personalities[x][y] = bp;
					if (x < Common.PMAXX && y < Common.PMAXY) {
						Common.cells[x][y].setPersonality(bp);
					}
					bp.setIn((String)ois.readObject());
					bp.setOut((String)ois.readObject());
					n++;
				} while (true);
			} catch (Exception e)  {
				System.err.println("Read "+n+" buffers");
			}
			cellsView.invalidate();
			cellsView.validate();
			cellsView.repaint();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Load");
		}
	}
	public static void openMachine(ObjectInputStream ois) {
		try {
			Common.PMAXX = ois.readInt();
			Common.PMAXY = ois.readInt();
			Common.MMAXX = Common.PMAXX-2;
			Common.MMAXY = Common.PMAXY-2;

			cellsView.setLayout(new GridLayout(Common.PMAXY,Common.PMAXX));
			Common.cells = new Cell[Common.PMAXX][Common.PMAXY];
			Common.personalities = new Personality[Common.PMAXX][Common.PMAXY];
			for (int y = 0; y < Common.PMAXY; y++) {
				for (int x = 0; x < Common.PMAXX; x++) {
					Common.cells[x][y] = new Cell(x, y);
					String pname = (String)ois.readObject();
					Personality p = (Personality)Class.forName(pname).newInstance();
					if (p == null) {
						p = new EmptyP();
					}
					Common.personalities[x][y] = p;
					p.lOutput = ois.readBoolean();
					p.rOutput = ois.readBoolean();
					p.tOutput = ois.readBoolean();
					p.bOutput = ois.readBoolean();
					p.lFull = ois.readBoolean();
					p.rFull = ois.readBoolean();
					p.tFull = ois.readBoolean();
					p.bFull = ois.readBoolean();
				}
			}
			int n = 0;
			try {
				do {
					int x = ois.readInt();
					int y = ois.readInt();
					BufferP bp = new BufferP();
					if (x < Common.PMAXX && y < Common.PMAXY) {
						Common.personalities[x][y] = bp;
					}
					bp.setIn((String)ois.readObject());
					bp.setOut((String)ois.readObject());
					n++;
				} while (true);
			} catch (Exception e)  {
				System.err.println("Read "+n+" buffers");
			}
			cellsView.removeAll();
			addCells();
			cellsView.invalidate();
			cellsView.validate();
			cellsView.repaint();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Load");
		}
	}
	public static void clear() {
		try {
			Common.startx = 0;
			Common.starty = 0;
			int newx = Integer.parseInt(jtfx.getText().trim());
			int newy = Integer.parseInt(jtfy.getText().trim());
			Common.PMAXX = newx + 2;
			Common.PMAXY = newy + 2;
			Common.MMAXX = newx;
			Common.MMAXY = newx;

			cellsView.setLayout(new GridLayout(Common.PMAXY,Common.PMAXX));
			Common.cells = new Cell[Common.PMAXX][Common.PMAXY];
			Common.personalities = new Personality[Common.PMAXX][Common.PMAXY];
			System.err.println("pwidth = "+Common.PMAXX+" pheight = "+Common.PMAXY);
			int y = 0;
			int x = 0;
			Common.personalities[x][y] = new EmptyP();
			for (x = 1; x < Common.PMAXX-1; x++) {
				Common.personalities[x][y] = new BufferP();
			}
			Common.personalities[x][y] = new EmptyP();
			for (y = 1; y < Common.PMAXY-1; y++) {
				x = 0;
				Common.personalities[x][y] = new BufferP();
				for (x = 1; x < Common.PMAXX-1; x++) {

					Common.personalities[x][y] = new EmptyP();
				}
				x = Common.PMAXX-1;
				Common.personalities[x][y] = new BufferP();
			}
			y = Common.PMAXY-1;
			x = 0;
			Common.personalities[x][y] = new EmptyP();
			for (x = 1; x < Common.PMAXX-1; x++) {
				Common.personalities[x][y] = new BufferP();
			}
			Common.personalities[x][y] = new EmptyP();
			// movePersonalities();
			cellsView.removeAll();
			addCells();
			cellsView.invalidate();
			cellsView.validate();
			cellsView.repaint();
			ask.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
			ask.setVisible(false);
		}
	}
	public static void saveModule(ObjectOutputStream oos) {
		try {
			oos.writeInt(Common.PMAXX);
			oos.writeInt(Common.PMAXY);
			for (int y = 1; y < Common.PMAXY-1; y++) {
				for (int x = 1; x < Common.PMAXX-1; x++) {
					oos.writeObject(Common.personalities[x][y].getClass().getName());
					System.err.println(Common.personalities[x][y].getClass().getName());
					oos.writeBoolean(Common.personalities[x][y].lOutput);
					oos.writeBoolean(Common.personalities[x][y].rOutput);
					oos.writeBoolean(Common.personalities[x][y].tOutput);
					oos.writeBoolean(Common.personalities[x][y].bOutput);
					oos.writeBoolean(Common.personalities[x][y].lFull);
					oos.writeBoolean(Common.personalities[x][y].rFull);
					oos.writeBoolean(Common.personalities[x][y].tFull);
					oos.writeBoolean(Common.personalities[x][y].bFull);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public static void saveInputs(ObjectOutputStream oos) {
		try {
			oos.writeInt(Common.PMAXX);
			oos.writeInt(Common.PMAXY);
			for (int y = 0; y < Common.PMAXY; y++) {
				for (int x = 0; x < Common.PMAXX; x++) {
					if (Common.personalities[x][y] instanceof BufferP) {
						BufferP bp = (BufferP)Common.personalities[x][y];
						oos.writeInt(x);
						oos.writeInt(y);
						oos.writeObject(bp.getIn());
						oos.writeObject(bp.getOut());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public static void saveMachine(ObjectOutputStream oos) {
		try {
			oos.writeInt(Common.PMAXX);
			oos.writeInt(Common.PMAXY);
			for (int y = 0; y < Common.PMAXY; y++) {
				for (int x = 0; x < Common.PMAXX; x++) {
					oos.writeObject(Common.personalities[x][y].getClass().getName());
					System.err.println(Common.personalities[x][y].getClass().getName());
					oos.writeBoolean(Common.personalities[x][y].lOutput);
					oos.writeBoolean(Common.personalities[x][y].rOutput);
					oos.writeBoolean(Common.personalities[x][y].tOutput);
					oos.writeBoolean(Common.personalities[x][y].bOutput);
					oos.writeBoolean(Common.personalities[x][y].lFull);
					oos.writeBoolean(Common.personalities[x][y].rFull);
					oos.writeBoolean(Common.personalities[x][y].tFull);
					oos.writeBoolean(Common.personalities[x][y].bFull);
				}
			}
			for (int y = 0; y < Common.PMAXY; y++) {
				for (int x = 0; x < Common.PMAXX; x++) {
					if (Common.personalities[x][y] instanceof BufferP) {
						BufferP bp = (BufferP)Common.personalities[x][y];
						oos.writeInt(x);
						oos.writeInt(y);
						oos.writeObject(bp.getIn());
						oos.writeObject(bp.getOut());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public void run() {
		Common.changed = false;
		for(int i = 0; Common.forever; i++) {
			step();
			if (!Common.changed) {
				break;
			}
		}
	}
	public void step() {
		int nt = 1;
		try {
			nt = Integer.parseInt(numTimes.getText());
		} catch (Exception e) {
			Common.forever = true;
		}
		int intv = 0;
		try {
			intv = Integer.parseInt(interval.getText());
		} catch (Exception e) {
		}
		int y = 0;
		int x = 0;
		Common.changed = false;
		for(int i = 0; i < nt; i++) {
			if (Common.color) {
				// black squares in checker board
				for (y = 0; y < Common.PMAXY; y+=2) {
					for (x = 0; x < Common.PMAXX; x+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				for (y = 1; y < Common.PMAXY; y+=2) {
					for (x = 1; x < Common.PMAXX; x+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				Common.color = false;
			} else {
				// red squares in checker board
				for (x = 1; x < Common.PMAXX; x+=2) {
					for (y = 0; y < Common.PMAXY; y+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				for (x = 0; x < Common.PMAXX; x+=2) {
					for (y = 1; y < Common.PMAXY; y+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				Common.color = true;
			}
			if (!Common.changed) {
				break;
			}
			try {
				for (y = 0; y < Common.PMAXY; y++) {
					for (x = 0; x < Common.PMAXX; x++) {
						Common.cells[x][y].repaint();
					}
				}
				Thread.sleep(intv * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (y = 0; y < Common.PMAXY; y++) {
			for (x = 0; x < Common.PMAXX; x++) {
				Common.cells[x][y].repaint();
			}
		}
		if (!Common.changed) {
			if (stopdlg == null) {
				stopdlg = new JFrame("Simulation stopped");
				JPanel jp = new JPanel();
				JLabel jl = new JLabel("Simulation stopped");
				JButton jb = new JButton("OK");
				class Confirm implements ActionListener {
					JFrame stopdlg = null;
					public Confirm(JFrame stopdlg) {
						this.stopdlg = stopdlg;
					}
					public void actionPerformed(ActionEvent ae) {
						stopdlg.setVisible(false);
					}
				}
				jb.addActionListener(new Confirm(stopdlg));
				jp.add(jl);
				jp.add(jb);
				stopdlg.getContentPane().add("Center", jp);
				stopdlg.pack();
			}
			stopdlg.setVisible(true);
		}
	}
	static public void addCells() {
		for (int y = 0; y < Common.PMAXY; y++) {
			for (int x = 0; x < Common.PMAXX; x++) {
				if (Common.cells[x][y] == null) {
					Common.cells[x][y] = new Cell(x, y);
				}
				Cell c = Common.cells[x][y];
				c.setPersonality(Common.personalities[x][y]);
				c.repaint();
				cellsView.add(c);
			}
		}
	}
}
