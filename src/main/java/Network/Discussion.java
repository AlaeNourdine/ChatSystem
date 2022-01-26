package Network;

import Controller.ControllerChat ;
import Model.Messages;
import Model.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Discussion extends Thread {
	private Socket link ;
	private ControllerChat agent ;
	private User user ;
	private ObjectOutputStream out ;
	private ObjectInputStream in ;
	private PropertyChangeSupport pcs ;
	private ArrayList<Messages> dernierMsg;
	
    public Discussion(Socket link, ControllerChat agent) {
        this.setLink(link);
        this.setAgent(agent);
        try {
            this.setOut(new ObjectOutputStream(link.getOutputStream()));
            this.setIn(new ObjectInputStream(link.getInputStream()));
        }catch(Exception e) {
            e.getStackTrace();
        }
        this.dernierMsg = new ArrayList<>();
        this.pcs = new PropertyChangeSupport(this);
        this.start();
    }
    
    public Discussion(User user, ControllerChat agent) {
        this.setUser(user);
        this.setAgent(agent);
        try {
            Socket s = new Socket(user.getIpAddress(),5000);
            this.setOut(new ObjectOutputStream(s.getOutputStream()));
            this.setIn(new ObjectInputStream(s.getInputStream()));
            this.setLink(s);
        } catch (IOException e) {

            e.printStackTrace();
        }
        this.dernierMsg = new ArrayList<>();
        this.pcs = new PropertyChangeSupport(this);
        this.start();
        this.startSession();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl){
        this.pcs.addPropertyChangeListener("Message Reçu",pcl);
        this.pcs.addPropertyChangeListener("Fin de la discussion",pcl);
    }
    
    public void startSession(){
        System.out.println("Ajout d'un utilisateur dans communication");
        agent.listCommunication.add(getUser().getIpAddress());
        System.out.println(agent.listCommunication.size());
        Messages msgh = new Messages("new session", getUser(),getAgent().getMe(),3);
        try {
            getOut().writeObject(msgh.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopSession() {
        Messages msg = new Messages(".", getUser(),getAgent().getMe(), 2);
        try {
            getOut().writeObject(msg.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                link.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(String message) {
        Messages msg = new Messages(message ,getUser(),getAgent().getMe(),1);
        try {
            getOut().writeObject(msg.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public Messages getDernierMsg() {
        return this.dernierMsg.remove(0);
    }
    
    public void run() {
        String plaintext;
        Messages msg = null;
        System.out.println("Session demarre");
        while(true) {
            try {
                plaintext = (String) getIn().readObject();
                System.out.println("Message reÃ§u");
                msg = Messages.stringToMessageHorodated(plaintext);
                System.out.println(msg.getMsg());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                pcs.firePropertyChange("FinDeLaSession", false, true);
                agent.listCommunication.remove(user.getIpAddress());
                break;
            }
            if(msg.getType() == 2) {
                pcs.firePropertyChange("FinDeLaSession", false, true);
                try {
                    link.close();
                } catch (IOException e) {
                    break;
                }
                agent.listCommunication.remove(user.getIpAddress());
                break;
            }
            else if(msg.getType() == 3) {
                System.out.println("Session Initiee");
                this.user = msg.getEmetteur() ;
                agent.listCommunication.add(user.getIpAddress());
            }
            else{
            dernierMsg.add(msg);
            pcs.firePropertyChange("MessageRecu",false,true);}
        }
    }
    private ControllerChat getAgent() {
		return agent;
	}


	public ObjectInputStream getIn() {
        return in;
    }
	public void setIn(ObjectInputStream in) {
		this.in = in;
		
	}

	public ObjectOutputStream getOut() {
		return out;
	}
	public void setOut(ObjectOutputStream out) {
		this.out = out ;
		
	}

	public void setAgent(ControllerChat agent) {
		this.agent = agent ;
	}

	public void setLink(Socket link) {
		this.link = link;
	}
	
	public ControllerChat chatsystem ;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user=user;
	}
}

