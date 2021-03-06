package impact;

import java.util.*;

import javax.media.opengl.*;

public class ObjectUtility<T extends GraphObject> {
	Vector<T> shown = new Vector<T>();
	Vector<T> selected = new Vector<T>();
	Vector<T> clipboard = new Vector<T>();
	void sendShown() {
		Iterator<T> i = shown.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			obj.insert(Proxy.getProxy());
			obj.update(Proxy.getProxy());
		}
        }
	void paste() {
		Iterator<T> i = clipboard.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			// if (!shown.contains(obj)) {
				shown.add((T)obj.clone());
				// selected.add(obj);
			// }
		}
	}
	void copy() {
		clipboard.clear();
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			clipboard.add((T)(obj.clone()));
		}
	}
	void cut() {
		clipboard.clear();
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			clipboard.add(obj);
			shown.remove(obj);
			obj.remove();
			i.remove();
			Proxy.getProxy().remove(obj);
		}
		selected.clear();
	}
	void selectAll() {
		Iterator<T> i = shown.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			selected.add(obj);
		}
	}
	boolean select(int nm) {
		boolean found = false;
		synchronized(shown) {
			Iterator<T> k = shown.iterator();
			while (k.hasNext()) {
				T obj = k.next();
				if (((T)obj).getName() == nm) {
					found = true;
					if (Impact3D.control) {
						if (selected.contains(obj)) {
							selected.remove(obj);
						} else {
							selected.add(obj);
						}
					} else {
						if (!selected.contains(obj)) {
							selected.add(obj);
						}
					}
				}

		      }
		}
		return found;
        }
	public void draw(GL2 gl) {
		synchronized(shown) {
			Iterator<T> i = shown.iterator();
			while (i.hasNext()) {
				T obj = i.next();
				((T)obj).draw(gl);
			}
		}
	}
	public void untranslate() {
		synchronized(shown) {
			Iterator<T> i = shown.iterator();
			while (i.hasNext()) {
				T obj = i.next();
				((T)obj).untranslate();
			}
		}
	}
  	void translateSelection(float x, float y, float z) {
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			((T)obj).translateSelection(x, y, z);
		}
	}
	void save() {
		Iterator<T> i = shown.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			((T)obj).save();
		}
	}
	void add(T obj) {
		shown.add(obj);
	}
	void clearSelected() {
		selected.clear();
	}
	int selectedSize() {
		return selected.size();
	}
  }
