package impact;

import java.util.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import com.sun.opengl.impl.*;

public  class Arc extends GraphObject {
	private Node begin;
	private Node end;
	private float color[];
	private int name;
	Arc(int name, Node begin, Node end, boolean useProxy) {
	    this.begin = begin;
	    this.end = end;
    	    color = new float[] { Impact3D.random.nextFloat(), Impact3D.random.nextFloat(), Impact3D.random.nextFloat(), 1.0f };
	    this.name = name;
	    if (useProxy) {
	        Proxy.insert(this);
	    }
	}
        public int getName() {
		return this.name;
	}
	public void draw(GL2 gl) {
	
	   gl.glPushName(name);
   	   double x1 = begin.paths.x+begin.trans.x;
   	   double y1 = begin.paths.y+begin.trans.y;
   	   double z1 = begin.paths.z+begin.trans.z;
   	   double x2 = end.paths.x+end.trans.x;
   	   double y2 = end.paths.y+end.trans.y;
   	   double z2 = end.paths.z+end.trans.z;
	   double radius = 0.5;
	   gl.glBegin(gl.GL_POLYGON);
	   gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, color, 0);
	   if (Impact3D.arcs.selected.contains(this)) {
		    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, Impact3D.white, 0);
	   }
	   gl.glVertex3d(x1-radius, y1-radius, z1-radius);
	   gl.glVertex3d(x1+radius, y1+radius, z1+radius);
	   gl.glVertex3d(x1, y1, z1);
	   gl.glVertex3d(x1+radius, y1+radius, z1+radius);
	   gl.glVertex3d(x2+radius, y2+radius, z2+radius);
	   gl.glVertex3d(x2, y2, z2);
	   gl.glVertex3d(x2-radius, y2-radius, z2-radius);
	   gl.glEnd();
	   gl.glPopName();
	}
	public Object clone() {
		Arc arc = new Arc(Impact3D.name++, begin, end, true);
		arc.color[0] = color[0];
		arc.color[1] = color[1];
		arc.color[2] = color[2];
		arc.color[3] = color[3];
		Proxy.update(arc);
		return arc;
	}
	public void insert(Proxy p) {
		p.send("ARC|"+name+"|INSERT|"+begin.name+"|"+end.name);
	}
	public void remove(Proxy p) {
		p.send("ARC|"+name+"|DELETE");
	}
	public void update(Proxy p) {
		p.send("ARC|"+name+"|UPDATE|"+
			color[0]+"|"+color[1]+"|"+color[2]+"|"+color[3]);
	}
	static public void receive(String line) {
		String [] params = line.split("\\|");
		if (params[2].startsWith("INSERT")) {
			int nm = Integer.parseInt(params[1]);
			int beginnm = Integer.parseInt(params[3]);
			int endnm = Integer.parseInt(params[4]);
			Node begin = null;
			Node end = null;
	   		Iterator<Node> n = Impact3D.nodes.shown.iterator();
			while (n.hasNext()) {
				Node node = n.next();
				if (node.name == beginnm) {
					begin = node;
				}
				if (node.name == endnm) {
					end = node;
				}
			}
			Impact3D.name = Integer.parseInt(params[1]);
			Arc arc = new Arc(Impact3D.name++, begin, end, false);
			Impact3D.arcs.add(arc);
		} else if (params[2].startsWith("DELETE")) {
			int nm = Integer.parseInt(params[1]);
	   		Iterator<Arc> a = Impact3D.arcs.shown.iterator();
			while (a.hasNext()) {
				Arc arc = a.next();
				if (arc.name == nm) {
					a.remove();
				}
			}
		} else if (params[2].startsWith("UPDATE")) {
			int nm = Integer.parseInt(params[1]);
	   		Iterator<Arc> a = Impact3D.arcs.shown.iterator();
			while (a.hasNext()) {
				Arc arc = a.next();
				if (arc.name == nm) {
					arc.color[0] = Float.parseFloat(params[3]);
					arc.color[1] = Float.parseFloat(params[4]);
					arc.color[2] = Float.parseFloat(params[5]);
					arc.color[3] = Float.parseFloat(params[6]);
				}
			}
		}
	}
  }
