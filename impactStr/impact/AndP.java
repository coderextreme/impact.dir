package impact;
import javax.swing.*;
import java.awt.*;

public class AndP extends Personality {
	static private Image image = null;
	public AndP() {
		if (image == null) {	
			image = new ImageIcon(this.getClass().getClassLoader().getResource("andthing.gif")).getImage();
		}
	}
	public void step(int x, int y) {
		Personality rightNeighbor = Common.personalities[x+1][y];
		Personality topNeighbor = Common.personalities[x][y-1];
		boolean test = getCellFull(Common.TOP);
		boolean test2 = getCellFull(Common.BOTTOM);
		boolean test3 = getCellFull(Common.LEFT);
		if (rightNeighbor.getCellFull(Common.LEFT) && topNeighbor.getCellFull(Common.BOTTOM) && !test && !test2 && !test3) {
			String inData1 = step(rightNeighbor, Common.LEFT, Common.LEFT);
			String inData2 = step(topNeighbor, Common.BOTTOM, Common.BOTTOM);
			setOutputData(Common.TOP, (inData1.equals("1") && inData2.equals("1") ? "1" : "0"));
		}
	}
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, 5*M, 5*M, observer);
	}
	public Object clone() {
		return new AndP();
	}
}
