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
	static public void insert(Arc arc) {
		arc.insert(proxy);
	}
	static public void insert(Node node) {
		node.insert(proxy);
	}
	static public void remove(Arc arc) {
		arc.remove(proxy);
	}
	static public void remove(Node node) {
		node.remove(proxy);
	}
	static public void update(Arc arc) {
		arc.update(proxy);
	}
	static public void update(Node node) {
		node.update(proxy);
	}
	static public void send(String line) {
		try {
			chat.getSender().send(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        static public void close() {
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
		}
	}
}
