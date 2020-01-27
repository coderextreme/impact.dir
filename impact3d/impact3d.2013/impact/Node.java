package impact;

import java.util.*;

import com.jogamp.opengl.util.gl2.*;
import javax.media.opengl.*;

public class Node extends GraphObject {
  	private GLUT glut = new GLUT();
	Vect paths;
	Vect trans;
	float color[];
	int name;
	Node(int name, boolean useProxy) {
   	    paths = new Vect();
	    paths.x = Impact3D.random.nextFloat()*10-5;
	    paths.y = Impact3D.random.nextFloat()*10-5;
	    paths.z = Impact3D.random.nextFloat()*10-5;
            trans = new Vect();
    	    color = new float[] { Impact3D.random.nextFloat(), Impact3D.random.nextFloat(), Impact3D.random.nextFloat(), 1.0f };
	    this.name = name;
            // System.err.println("**** NAME = "+this.name);
	    if (useProxy) {
		Proxy.getProxy().insert(this);
	        Proxy.getProxy().update(this);
	    }
	}
        public int getName() {
		return this.name;
	}
	public void draw(GL2 gl) {
	   double x2 = paths.x+trans.x;
	   double y2 = paths.y+trans.y;
	   double z2 = paths.z+trans.z;
	   gl.glPushMatrix();
	   gl.glTranslated(x2, y2, z2);
	   gl.glPushName(name);
	   gl.glShadeModel(gl.GL_FLAT);
	   gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, color, 0);
	   if (Impact3D.nodes.selected.contains(this)) {
		    gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, Impact3D.white, 0);
	   }
	   glut.glutSolidCube(1.0f);
	   gl.glPopName();
	   gl.glPopMatrix();
	}
	public GraphObject clone() {
		Node node = new Node(Impact3D.name++, true);
		node.paths = (Vect)paths.clone();
		node.trans = (Vect)trans.clone();
		node.color[0] = color[0];
		node.color[1] = color[1];
		node.color[2] = color[2];
		node.color[3] = color[3];
	        Proxy.getProxy().update(node);
		return node;
	}
	public void untranslate() {
		trans.x = 0f;
		trans.y = 0f;
		trans.z = 0f;
	        Proxy.getProxy().update(this);
	}
       void translateSelection(float x, float y, float z) {
		trans.x += x;
		trans.y += y;
		trans.z += z;
	        Proxy.getProxy().update(this);
	}
	void save() {
		paths.x += trans.x;
		paths.y += trans.y;
		paths.z += trans.z;
		trans.x = 0f;
		trans.y = 0f;
		trans.z = 0f;
	        Proxy.getProxy().update(this);
	}
	public void insert(Proxy p) {
		p.send("NODE|"+name+"|INSERT");
	}
	public void remove(Proxy p) {
		p.send("NODE|"+name+"|DELETE");
	}
	public void update(Proxy p) {
		p.send("NODE|"+name+"|UPDATE|"+
			paths.x+"|"+paths.y+"|"+paths.z+"|"+
			trans.x+"|"+trans.y+"|"+trans.z+"|"+
			color[0]+"|"+color[1]+"|"+color[2]+"|"+color[3]
		);
	}
	static public void receive(String line) {
		// System.err.println("parsing "+line);
		String [] params = line.split("\\|");
		System.err.println("p0 "+params[0]);
		System.err.println("p1 "+params[1]);
		System.err.println("p2 "+params[2]);
		if (params[2].startsWith("INSERT")) {
			Impact3D.name = Integer.parseInt(params[1]);
			// System.err.println("From Server "+Impact3D.name);
			Node node = new Node(Impact3D.name++, false);
			Impact3D.nodes.add(node);
		} else if (params[2].startsWith("DELETE")) {
			int nm = Integer.parseInt(params[1]);
	   		Iterator<Node> n = Impact3D.nodes.shown.iterator();
			while (n.hasNext()) {
				Node node = n.next();
				if (node.name == nm) {
					System.err.println("Removing node "+node.name);
					n.remove();
				}
			}
		} else if (params[2].startsWith("UPDATE")) {
			int nm = Integer.parseInt(params[1]);
	   		Iterator<Node> n = Impact3D.nodes.shown.iterator();
			while (n.hasNext()) {
				Node node = n.next();
				if (node.name == nm) {
					node.paths.x = Float.parseFloat(params[3]);
					node.paths.y = Float.parseFloat(params[4]);
					node.paths.z = Float.parseFloat(params[5]);
					node.trans.x = Float.parseFloat(params[6]);
					node.trans.y = Float.parseFloat(params[7]);
					node.trans.z = Float.parseFloat(params[8]);
					node.color[0] = Float.parseFloat(params[9]);
					node.color[1] = Float.parseFloat(params[10]);
					node.color[2] = Float.parseFloat(params[11]);
					node.color[3] = Float.parseFloat(params[12]);
				}
			}
		}
	}
  }
