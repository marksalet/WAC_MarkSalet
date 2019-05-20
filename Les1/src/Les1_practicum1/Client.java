package Les1_practicum1;

import java.net.Socket;
import java.io.OutputStream;

public class Client {
	public static void main(String[] arg) throws Exception {
		Socket s = new Socket("localhost", 4711);
		OutputStream os = s.getOutputStream();
		os.write("hello\n".getBytes());
		s.close();
	}
}
