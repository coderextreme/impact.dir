package impact;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.*;

public abstract class GraphObject {
	abstract void draw(GL2 gl);
	public abstract Object clone();
	abstract int getName();
	void untranslate() {
	}
	void save() {
	}
	void translateSelection(float x, float y, float z) {
	}
  }
