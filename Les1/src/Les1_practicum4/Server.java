package Les1_practicum4;

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
		OutputStream os = s.getOutputStream();
		String string = " ";
		PrintWriter pw = new PrintWriter(os);
		Scanner sc = new Scanner(is);
					
		while (true) {
			String regel = sc.nextLine();
			if(regel.isEmpty()) {
				break;
			}
			System.out.println(regel);
		}
		
		string = "HTTP/1.1 200 OK\n\n <h1>It works!</h1>\n\n";
		
		pw.write(string);
		pw.flush();
		pw.close();
		os.close();
		sc.close();
		ss.close();
	}
}