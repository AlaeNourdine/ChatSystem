package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import View.*;
import Controller.*;
import Model.*;

/**
 * 
 * Classe TCPSend
 *
 */

public class TCPSend extends Thread{
	private Socket socket;
	private User them;
	private ObjectOutputStream out;
    private ObjectInputStream in;
	private Controller app;
    
    
    /**
     * Constructeur a utiliser lorsque quelqu'un veut nous envoyer un message
     * 
     * @param app Controller 
     * @param sock socket 
     */
    public TCPSend (Controller app, Socket sock) {
    	setApp(app);
    	setSocket(sock);
    	try {
			setOut(new ObjectOutputStream(sock.getOutputStream()));
			setIn(new ObjectInputStream(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	start();
    }
    
    /**
     * Constructeur a utiliser lorsqu'on veut envoyer un message a quelqu'un
     * 
     * @param app Controller
     * @param u2 User avec qui on veut discuter
     */
    public TCPSend (Controller app, User u2) {
    	setApp(app);
    	setThem(u2);
    	try {
			setSocket(new Socket(u2.getIP(),2000));
			setOut(new ObjectOutputStream(socket.getOutputStream()));
			setIn(new ObjectInputStream(socket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
    
	//-------------------- Methodes -----------------------------//
	
    /**
     * Methode pour envoyer un message
     * @param data String (message a envoyer)
     */
    public void SendMessage(String data) {
        Message msg = new Message(getApp().getMe(),getThem(), data);
        try {
            getOut().writeObject(msg.toString());
            getApp().getDb().addMessage(getThem().getIP(), msg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Metode pour recevoir les messages
     */
    public void run() {
        String data = null;
        Message msg = null;
        try {
        	data = (String) getIn().readObject();
        	msg = Message.toMessage(data);
        }
        catch (ClassNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        getApp().getDb().addMessage(socket.getInetAddress().getHostAddress(), msg);
    	General.displayNotification(" vous a envoy?? un message.",socket.getInetAddress().getHostAddress());
    	General.display(msg.getEmetteur().getNickname());
    	try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
   	}
   
    



}
