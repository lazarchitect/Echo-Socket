import java.net.*;
import java.io.*;

public class EchoServer {
	public static void main(String[] args) throws IOException {

		try{

			System.out.println("\nWaiting for clients...\n");
			
			int port = Integer.parseInt(args[0]);

			ServerSocket ss = null;
			
			ss = new ServerSocket(port);
			
			Socket client = ss.accept();

			System.out.println("Client found. Location:" + client.getInetAddress());

			BufferedReader br = null;

			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String text = br.readLine();
			System.out.println(text);

			PrintWriter pw = new PrintWriter(client.getOutputStream(), true);

			pw.println(text);

			ss.close();
			client.close();
			br.close();
			pw.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}

	}
}