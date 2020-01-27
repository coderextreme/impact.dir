package impact;

import java.net.*;
import java.io.*;

public class HttpChat implements LineHandler {
	String sendurl;
	String receiveurl;
	Sender s;
	Receiver r;
	public void start(String sendurl, String receiveurl) throws Exception {
		r = new Receiver(receiveurl);
		r.start();
		s = new Sender(sendurl, r);
		s.start();
	}
	public Sender getSender() throws NullPointerException {
		if (s == null) {
			throw new NullPointerException("Error getting sender");
		}
		return s;
	}
	public void receive(String line) {
		System.out.print(line);
		System.out.flush();
	}
	public HttpChat(String args[]) throws Exception {
		start(args.length > 1 ? args[1] : "http://coderextreme.net/helloworld.php?nick="+args[0],
		      args.length > 2 ? args[2] : "http://coderextreme.net/helloworld.php?nick="+args[0]);
	}
	static public void main(String args[]) throws Exception {
		HttpChat v = new HttpChat(args);
	}
}
