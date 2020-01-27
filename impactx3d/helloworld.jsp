<%@ page import="java.io.*,java.util.*,javax.servlet.http.*"%><%
Hashtable sessions = (Hashtable)application.getAttribute("sessions");
if (sessions == null) {
	out.println("sessions is null");
	sessions = new Hashtable();
} else {
	// sessions.clear();
}
String nick = request.getParameter("nick");
if (nick == null) {
	return;
}
String text = request.getParameter("text");
if (text == null) {
	text = "";
}
StringBuffer sb = (StringBuffer)sessions.get(nick);
if (sb == null) {
	out.println("can't get "+nick+" from sessions");
	sb = new StringBuffer();
	sessions.put(nick, sb);
}
Iterator i = sessions.keySet().iterator();	
byte buffer[] = new byte[1024];
while (i.hasNext()) {
	Object o = i.next();
	if (o instanceof HttpSession) {
		continue;
	}
	String hts = (String)o;
	if (hts.equals(nick)) {
		continue;
	}
	sb = (StringBuffer)sessions.get(hts);
	if (text != null) {
		sb.append(text);
		sb.append('\n');
	}
	/*
	BufferedReader br = request.getReader();
	String line = null;
	
	while ((line = br.readLine()) != null) {
		out.println("\nAPPENDING\n");
		sb.append(line);
	}
	*/
	sessions.put(hts, sb);
}
out.print(sessions.get(nick));
out.flush();
sessions.put(nick, new StringBuffer());
application.setAttribute("sessions", sessions);
%>
