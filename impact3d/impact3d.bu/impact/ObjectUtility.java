package impact;

import java.util.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import com.sun.opengl.impl.*;

public class ObjectUtility<T> {
	Vector<T> shown = new Vector<T>();
	Vector<T> selected = new Vector<T>();
	Vector<T> clipboard = new Vector<T>();
	void paste() {
		Iterator<T> i = clipboard.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			if (!shown.contains(obj)) {
				shown.add(obj);
				selected.add(obj);
			}
		}
	}
	void copy() {
		clipboard.clear();
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			clipboard.add((T)((GraphObject)obj).clone());
		}
	}
	void cut() {
		clipboard.clear();
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			clipboard.add(obj);
			shown.remove(obj);
			i.remove();
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
		Iterator<T> k = shown.iterator();
		while (k.hasNext()) {
			T obj = k.next();
			if (((GraphObject)obj).getName() == nm) {
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
	      return found;
        }
	public void draw(GL gl) {
		Iterator<T> i = shown.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			((GraphObject)obj).draw(gl);
		}
	}
	public void untranslate() {
		Iterator<T> i = shown.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			((GraphObject)obj).untranslate();
		}
	}
  	void translateSelection(float x, float y, float z) {
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			((GraphObject)obj).translateSelection(x, y, z);
		}
	}
	void save() {
		Iterator<T> i = shown.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			((GraphObject)obj).save();
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
