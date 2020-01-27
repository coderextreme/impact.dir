package impact;

class Proxy implements LineHandler {
	static Proxy proxy = new Proxy();
	static HttpChat chat;
	private Proxy() {
		try {
			chat = new HttpChat(new String [] { Long.toString(System.currentTimeMillis())});
			chat.getSender().send("JOIN");
		} catch (Exception e) {
			System.err.println("Problems instantiating communications");
		}
	}
	static public Proxy getProxy() {
		return proxy;
	}
	public void insert(Arc arc) {
		arc.insert(proxy);
	}
	public void insert(Node node) {
		node.insert(proxy);
	}
	public void insert(GraphObject obj) {
		obj.insert(proxy);
	}
	public void remove(Arc arc) {
		arc.remove(proxy);
	}
	public void remove(Node node) {
		node.remove(proxy);
	}
	public void remove(GraphObject obj) {
		obj.remove(proxy);
	}
	public void update(Arc arc) {
		arc.update(proxy);
	}
	public void update(Node node) {
		node.update(proxy);
	}
	public void update(GraphObject obj) {
		obj.update(proxy);
	}
	public void send(String line) {
		try {
			System.err.println("Sending "+line);
			chat.getSender().send(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        public void close() {
		try {
			chat.getSender().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void receive(String line) {
		Impact3D.cmd = Impact3D.UPDATE;
		if (line.startsWith("ARC")) {
			// System.err.println("Received ARC");
			Arc.receive(line);
		} else if (line.startsWith("NODE")) {
			// System.err.println("Received NODE");
			Node.receive(line);
		} else if (line.startsWith("JOIN")) {
			System.err.println("Received JOIN");
			Impact3D.sendShown();
		}
	}
}
