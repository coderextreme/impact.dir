package impact;

import java.util.List;

class Link {
	enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST };
	Cell cell;
	Direction dir;
	boolean input = false;
	boolean output = false;
}
class Module {
	int width;
	int height;
	List<Link> links;
}
public class ModuleLinker {
	Module m1;
	Module m2;
}
