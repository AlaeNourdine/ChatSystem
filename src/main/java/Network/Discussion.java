/*package Network;

import Controller.Controller;
import Model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Discussion extends Thread {
	
	public Discussion(Socket link, Controller app) {
	    this.setLink(link);
	    this.setApp(app);
	    try {
	        this.setOut(new ObjectOutputStream(link.getOutputStream()));
	        this.setIn(new ObjectInputStream(link.getInputStream()));
	    }catch(Exception e) {
	        e.getStackTrace();
	    }
	    this.derniersMsg = new ArrayList<>();
	    this.pcs = new PropertyChangeSupport(this);
	    this.start();
	}
	
	
	public Discussion(User u2, Controller app ) {
		this.setU2(u2);
		this.setApp(app);
		try {
			socket s = new socket(u2.getIpAddress(),5000);
			this.setOut(new ObjectOutputStream)
		}
	}
	public static void main(String[] args) {
		
	}
}
*/