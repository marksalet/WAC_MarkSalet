package Les1_practicum3;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(4711);
		Socket s = ss.accept();
		InputStream is = s.getInputStream();
		Scanner sc = new Scanner(is);
			
		OutputStream os = s.getOutputStream();
		PrintWriter pw = new PrintWriter(os);
		
		while (!sc.hasNextByte()) {
			System.out.println(sc.nextLine());
		}
		s.close();
		ss.close();
		sc.close();
	}

}