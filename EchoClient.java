import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoClient {

	//this method gets a user's keyboard input on the command line, and returns it as a string.
	static String getUserInput(){
		BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter a string to echo.\n>>>");
		String s = null;
		
		try{
			s = userInputReader.readLine();
		}
		catch (IOException ioe) {
			System.out.println("An I/O error occurred with entering your input. Closing program.");
			return null;
		}
		try{
			userInputReader.close();
		}
		catch(IOException ioe){
			System.out.println("An I/O error occurred with closing the input reader. Closing program.");
			return null;
		}

		return s;
	}

	//this method sends the string parameter over the socket parameter
	//Returns a zero upon success and a 1 if it failed.
	static int send(Socket clientSocket, String toSend){
		OutputStream outstr = null;
		try{
			outstr = clientSocket.getOutputStream();
		}
		catch (IOException ioe) {
			System.out.println("An I/O error occurred with reading the socket's OutputStream. Closing program.");
			return 1;
		}
		PrintWriter pw = new PrintWriter(outstr);
		pw = new PrintWriter(outstr, true);
		pw.println(toSend); //where the magic happens
		return 0;
	}

	//this method listens to the socket's input stream for strings, and then prints one when it sees it. 
	//Returns a 0 upon success, 1 upon failure.
	static int receive(Socket clientSocket){
		InputStream inpstr;
		try{
			inpstr = clientSocket.getInputStream();
		}
		catch(IOException ioe){
			System.out.println("An I/O error occurred with reading the socket's InputStream. Closing program.");
			return 1;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(inpstr));
		
		String read_in = null;
		try{
			read_in = br.readLine(); //where the magic happens
		}
		catch(IOException ioe){
			System.out.println("An I/O error occurred with reading a line from the server. Closing program.");
			return 1;
		}
		System.out.println("Server's response: \n\t"+read_in);
		try{
			br.close();
		}
		catch (IOException ioe) {
			System.out.println("An I/O error occurred with closing the BufferedReader on the stream from the server. Closing program.");
			return 1;
		}
		return 0;
	}

	public static void main(String[] args){
		if(args.length < 2){ //poor input
			System.out.println("Program requires 2 command line inputs, host name and port number. Closing program.");
			return;
		}
		
		String hostname = args[0];
		int port;
		
		try{
			port = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException nfe){
			System.out.println("Second argument needs to be an integer. Closing program.");
			return;
		}
		
		if(port > 65535){ // port numbers dont go this high, it will never connect
			System.out.println("Port number needs to be less than 65536. Closing program.");
			return;
		}
		if(port < 0){ // port numbers dont go this low either.
			System.out.println("Port number must be greater than 0. Closing program.");
			return;
		}
		
		Socket clientSocket = null; //declare the socket to be initialized and used later 

		try{
			System.out.println("Searching...");
			clientSocket = new Socket(hostname, port);//initialize the socket to use.
		}
		catch(UnknownHostException uhe){ // this happens if the user-given hostname argument is invalid
			System.out.println("Client could not find the given host. Closing program.");
			return;
		}
		catch(IOException ioe){
			System.out.println("An I/O error occurred with the socket. Closing program.");
			return;
		}
		
		System.out.println("host found.");
		String userInput = getUserInput(); //at the command line, get a user's string to send and echo back
		if (userInput.equals(null)) return; //something went wrong with taking user input.

		int sendStatus = send(clientSocket, userInput); //send off the user's string to the server, to be echoed back
		if (sendStatus != 0) return; // this means that something went wrong with the sending and no receiving should be done.
		
		int receiveStatus = receive(clientSocket); //listen for echoes and print any we hear
		if (receiveStatus != 0) return; //something went wrong with the receiving.
		
		try{
			clientSocket.close(); //according to the docs on oracle.com, this will also automatically close the socket's InputStream and OutputStream
		}
		catch(IOException ioe){
			System.out.println("An I/O error occurred with closing the socket. Closing program.");
			return;
		}
	}
}