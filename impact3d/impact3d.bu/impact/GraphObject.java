package impact;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import com.sun.opengl.impl.*;
public abstract class GraphObject {
	abstract void draw(GL gl);
	public abstract Object clone();
	abstract int getName();
	void untranslate() {
	}
	void save() {
	}
	void translateSelection(float x, float y, float z) {
	}
  }
