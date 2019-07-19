package Les1_practicum4;

import java.net.Socket;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Client {
	public static void main(String[] arg) throws Exception {
		Socket s = new Socket("localhost", 4711);
		OutputStream os = s.getOutputStream();

		Scanner scanner = new Scanner(System.in);
		String gebruikersInvoer = "";

		PrintWriter pw = new PrintWriter(os);
		while (!gebruikersInvoer.equals("stop")) {
			System.out.print("Invoer: ");
			gebruikersInvoer = scanner.nextLine();

			pw.write(gebruikersInvoer+"\n");
			pw.flush();	

		}
		scanner.close();
	}
}