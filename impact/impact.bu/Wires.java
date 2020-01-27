import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class UpWire extends JLabel {
	public UpWire() {
		super(new ImageIcon("up.gif"));
	}
}

class DownWire extends JLabel {
	public DownWire() {
		super(new ImageIcon("down.gif"));
	}
}

class LeftWire extends JLabel {
	public LeftWire() {
		super(new ImageIcon("left.gif"));
	}
}

class RightWire extends JLabel {
	public RightWire() {
		super(new ImageIcon("right.gif"));
	}
}

class VerticalWires extends JPanel {
	public VerticalWires() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(new JLabel("Load"));
		add(new DownWire());
		add(new JLabel("Ready"));
		add(new DownWire());
		add(new JLabel("1"));
		add(new DownWire());
		add(new JLabel("Resume"));
		add(new UpWire());

		add(new JLabel("Load"));
		add(new UpWire());
		add(new JLabel("Ready"));
		add(new UpWire());
		add(new JLabel("0"));
		add(new UpWire());
		add(new JLabel("Resume"));
		add(new DownWire());
	}
}

class HorizontalWires extends JPanel {
	public HorizontalWires() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("Load"));
		add(new LeftWire());
		add(new JLabel("Ready"));
		add(new LeftWire());
		add(new JLabel("1"));
		add(new LeftWire());
		add(new JLabel("Resume"));
		add(new RightWire());

		add(new JLabel("Load"));
		add(new RightWire());
		add(new JLabel("Ready"));
		add(new RightWire());
		add(new JLabel("0"));
		add(new RightWire());
		add(new JLabel("Resume"));
		add(new LeftWire());
	}
}

public class Wires extends JPanel {
	public static void main(String args[]) {
		JFrame jf = new JFrame();
		jf.getContentPane().setLayout(new GridLayout(2,2));
		Wires w0_0 = new Wires();
		jf.getContentPane().add(w0_0);
		Wires w0_1 = new Wires();
		jf.getContentPane().add(w0_1);
		Wires w1_0 = new Wires();
		jf.getContentPane().add(w1_0);
		Wires w1_1 = new Wires();
		jf.getContentPane().add(w1_1);
		jf.pack();
		jf.setVisible(true);
	}
	public Wires() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);

		JPanel jp = new JPanel();
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbl.setConstraints(jp, gbc);
		add(jp);

		VerticalWires vw = new VerticalWires();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(vw, gbc);
		add(vw);

		HorizontalWires hw = new HorizontalWires();
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbl.setConstraints(hw, gbc);
		add(hw);
		
		JLabel jl = new JLabel("Personality", SwingConstants.CENTER);
		jl.setBorder(new LineBorder(Color.BLACK, 2));
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(jl, gbc);
		add(jl);
	}	
}
