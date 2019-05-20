package Les1_practicum2;

import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Client {
	public static void main(String[] arg) throws Exception {
		Socket s = new Socket("localhost", 4711);
		OutputStream os = s.getOutputStream();
		PrintWriter pw = new PrintWriter(os);
		pw.write("Hello\n");
		pw.flush();
		pw.write("Hello");
		pw.flush();
		
		
		s.close();
		pw.close();
	}
}