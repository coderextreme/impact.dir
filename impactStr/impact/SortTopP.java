package impact;
import java.awt.*;

public class SortTopP extends Personality {
	String temp = null;
	public Object clone() {
		return new SortTopP();
	}
	public void step(int x, int y) {
		Personality bottomNeighbor = Common.personalities[x][y+1];
		Personality rightNeighbor = Common.personalities[x+1][y];
		if (rightNeighbor.getCellFull(Common.LEFT)) {
			try {
				temp = rightNeighbor.consumeLeftOutput();
				setBottomOutput(temp);
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 25");
			}
		}
		if (!getCellFull(Common.LEFT) &&
		    temp != null &&
		    bottomNeighbor.getCellFull(Common.TOP)) {
			try {
				String topdata = temp;
				temp = null;
				String bottomdata = bottomNeighbor.consumeTopOutput();
				int cmp = topdata.compareTo(bottomdata);
				if (cmp < 0) {
					setLeftOutput(topdata);
				} else {
					setLeftOutput(bottomdata);
				}
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 26");
			}
		}
	}
	public void paint(Graphics g) {
		g.drawString("ST", SX, 3*M-SY);
	}
}
