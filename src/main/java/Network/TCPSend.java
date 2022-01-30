package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Controller.Controller;
import Model.Messages;
import Model.User;

public class TCPSend {
	
	private Controller app;
	private User them;
    private ObjectOutputStream out;
    private ObjectInputStream in;
	private static Socket socket;

	//Constructor pour envoyer
	public TCPSend (Controller app, Socket sock) {
    	setApp(app);
    	setSocket(sock);
    	try {
			setOut(new ObjectOutputStream(sock.getOutputStream()));
			setIn(new ObjectInputStream(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	//Constructor pour recevoir
	 public TCPSend (Controller app, User user) {
	    	setApp(app);
	    	setThem(user);
	    	try {
				setSocket(new Socket(user.getIP(),2000));
				setOut(new ObjectOutputStream(socket.getOutputStream()));
				setIn(new ObjectInputStream(socket.getInputStream()));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 

	public void SendMessage(String data) {
		Messages msg = new Messages(getApp().getMe(),getThem(), data);
		try {
		    getOut().writeObject(msg.toString());
		    getApp().getDb().addMessage(getThem().getIP(), msg);
		    socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//-------------------- GETTEURS & SETTEURS -----------------------------//
	
	public Controller getApp() {
		return app;
	}
	
	public void setApp(Controller app) {
		this.app = app;
	}
	
	public User getThem() {
		return them;
	}
	
	public void setThem(User them) {
		this.them = them;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}
	
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	
	public ObjectInputStream getIn() {
		return in;
	}
	
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	
	
	}

