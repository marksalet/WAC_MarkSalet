package Les1_practicum1;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(4711);
		Socket s = ss.accept();
		InputStream is = s.getInputStream();
		int c = is.read();
		System.out.println(c);
		while (c != -1) {
			System.out.println((char) c);
			c = is.read();
		}
		
		s.close();
		ss.close();
	}

}