package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//Thread for each client which listens constantly to what the server broadcasts
//we only need an input attribute since we won't be sending the server any messages with these threads
public class ServerConnection extends Thread{

	private BufferedReader in;
	private Socket clientSocket;

	public ServerConnection(Socket clientSocket) throws IOException {
		this.clientSocket=clientSocket;
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	public void run() {
		try {
			while(true) {
				String serverResponse = in.readLine();
				if (serverResponse==null) break;
				System.out.println("[SERVER]: "+serverResponse);
			} 
		}catch (IOException e){e.printStackTrace();}
		finally {
			try {
				in.close();
			} catch (IOException e){e.printStackTrace();
			}
		}
	}
}