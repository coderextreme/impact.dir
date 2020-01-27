package impact;
import java.awt.*;

public class SortBottomP extends Personality {
	String temp = null;
	public Object clone() {
		return new SortBottomP();
	}
	public void step(int x, int y) {
		Personality topNeighbor = Common.personalities[x][y-1];
		Personality rightNeighbor = Common.personalities[x+1][y];
		if (rightNeighbor.getCellFull(Common.LEFT)) {
			try {
				// let top neighbor analyze it
				temp = rightNeighbor.consumeLeftOutput();
				setTopOutput(temp);
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 15");
			}
		}
		if (!getCellFull(Common.LEFT) &&
		    temp != null &&
		    topNeighbor.getCellFull(Common.BOTTOM)) {
			try {
				String topdata = topNeighbor.consumeBottomOutput();
				String bottomdata = temp;
				temp = null;
				int cmp = topdata.compareTo(bottomdata);
				if (cmp < 0) {
					setLeftOutput(bottomdata);
				} else {
					setLeftOutput(topdata);
				}
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 16");
			}
		}
	}
	public void paint(Graphics g) {
		g.drawString("SB", SX, 3*M-SY);
	}
}
