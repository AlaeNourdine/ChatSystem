package Network ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private int num;
	private boolean b=true;
	private String nickname;

	public ServerThread(Socket clientsocket, int num) {
		this.socket=clientsocket;
		this.num=num;
	}
	public void run() {
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			e.printStackTrace();
		}



		try {
			String resp = input.readLine();
			while(resp!=null) {
				System.out.println(nickname + " " + resp);

				if (b) {
					nickname=resp;
					System.out.println("[Server] Client "+num+" is connected");
					resp = input.readLine();
					b=false;
				}


				else if (resp.contains("@")) {
					int spaceIndex=resp.indexOf(" ");
					String remoteUser=resp.substring(1,spaceIndex);
					String message=resp.substring(spaceIndex);
					ServerThread remoteClientHandler=find(remoteUser);
					if (remoteClientHandler!=null) {
					remoteClientHandler.output.println(this.nickname+": "+message);
					} else {
						System.out.println("This user doesn't exist");
					}
					resp = input.readLine();

				} 
				
				else if (resp.equals("userslist")){
					for (ServerThread client : TCPReceive.getThreadslist()) {
						output.println(client.getNickname());
					}
					resp = input.readLine();
				}
				else {
					output.println("request unknown");				
					resp = input.readLine();

				

				}

			}



		}catch (IOException e){e.printStackTrace();}

	}
	private ServerThread find(String remoteUser) {
		ServerThread thread1=null;
		for (ServerThread thread : TCPReceive.getThreadslist()) {
			if (remoteUser.equals(thread.nickname)) {
				thread1=thread;
				break;
			}
		}
		return thread1;
	}
	
	public String getNickname() {
		return nickname;
	}
}