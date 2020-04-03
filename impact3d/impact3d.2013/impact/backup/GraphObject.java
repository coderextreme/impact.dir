package impact;

import javax.media.opengl.*;

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
	public void remove() {
		remove(Proxy.getProxy());
	}
	abstract public void remove(Proxy p);
	public void update() {
		update(Proxy.getProxy());
	}
	abstract public void update(Proxy p);
	public void insert() {
		insert(Proxy.getProxy());
	}
	abstract public void insert(Proxy p);
  }
