package Network;

import Controller.ControllerChat;
import Model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


public class broadcastUDP {
	
	private static Boolean online = true;
	
    public static Boolean getConnexion() {
        return online;
    }
	
    public static void setConnexion(Boolean value) {
        online = value ;
    }
	
    private static Boolean validNickname = true;
    
    public static Boolean getvalidNickname() {
        return validNickname;
    }
    
    public static void setvalidNickname(Boolean value) {
        validNickname = value ;
    }
    
    public static InetAddress getCurrentIp() {
        try {
        	// on recupere toutes les addresses de broadcast
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while(nias.hasMoreElements()) {
                    InetAddress ia= (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress()
                            && !ia.isLoopbackAddress()
                            && ia instanceof InetAddress) {
                        return ia;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("unable to get current IP " + e.getMessage());
        }
        return null;
    }
    
    
    public static void envoiBroadcast(String broadcastMessage) throws IOException {
        int port = 1234 ;
        for (InetAddress broadcastAddr : AllAddresses()) {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[]buffer = broadcastMessage.getBytes();
            DatagramPacket packet = new DatagramPacket( buffer, buffer.length,  broadcastAddr,port);
            socket.send(packet);
            socket.close();
            System.out.println("Broadcast sent with address " + broadcastAddr.toString());
            System.out.println("***********Message envoye***********");
            System.out.println("Dest Ip: " + broadcastAddr.toString());
            System.out.println("Dest port: " + String.valueOf(port));
            System.out.println("Contenu: ");
            System.out.println(broadcastMessage);
            System.out.println("************************************");
        }
    }
    
    public static void envoiUnicast( InetAddress address , String message ) throws IOException {
        if (ControllerChat.getInstance().isOnline()){
            DatagramSocket socket = new DatagramSocket();
            byte[]buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket( buffer, buffer.length,  address, 1234 );
            socket.send(packet);
            socket.close();
            System.out.println("***********Message envoye***********");
            System.out.println("Dest Ip: " + address.toString());
            System.out.println("Dest port: " + String.valueOf(1234));
            System.out.println("Contenu: ");
            System.out.println(message);
            System.out.println("************************************");
        }
    }
    
class UDPrun implements Runnable {
	
	final DatagramPacket data ;
	ControllerChat agent ;
	
	public UDPrun (DatagramPacket data, ControllerChat agent) {
		this.data = data ;
		this.agent = agent ;
	}
	
	public void run() {
        System.out.println("Thread started");
        String received = new String(data.getData(), 0, data.getLength());
        System.out.println("***********Message re�u***********");
        System.out.println(received);
        System.out.println("**********************************");
        String Type = received.split("\n")[0];

        
        if (Type.equals("Connexion")) { 
            System.out.println("Reception d'une demande de connexion");
            User user =  User.stringToUser(received.split("\n")[1]);
            if (! user.equals(this.agent.getMe())) { 
                String reponse = "Reponse Connexion\n";
                if (!( agent.getActifUsers() ).checkNicknameUnicity(user.getNickname())) {
                    System.out.println("Pseudo deja present dans la liste");
                    reponse += "false\n";
                }
                else {
                    if(!user.getNickname().equals("root")){
                        System.out.println("Ajout d'un nouvel utilisateur dans la liste des Utilisateurs");
                        ( agent.getActifUsers() ).ajouterUser(user);
                        reponse += "true\n";
                    }
                    else{
                        System.out.println("Non ajout de root dans la liste");
                    }
                }
                reponse += agent.getMe().toString();

                try {
                    broadcastUDP.envoiUnicast(user.getIpAddress(),reponse);
                }catch(IOException e)
                {
                    System.out.println("Echec de l'envoi du message");
                }

                ( agent.getActifUsers() ).showActifUsers();
            }
        }

        if (Type.equals("Reponse Connexion")) { 
            if((received.split("\n")[1]).equals("true")) {
                User user = User.stringToUser(received.split("\n")[2]);
                if(!user.getNickname().equals("root")) { 
                    agent.getActifUsers().ajouterUser(user);
                    agent.getActifUsers().showActifUsers();
                }
                else{
                    System.out.println("Le serveur nous a repondu");
                    int max = received.split("\n").length ;
                    if (max > 3 ){
                        for (int i = 3 ; i < max ; i++){
                            String suite = received.split("\n")[i];                       
                            if (!suite.equals("")){
                                agent.getActifUsers().ajouterUser(User.stringToUser(suite));
                                System.out.println("root m'a transmis les infos de l'utilisateur " + User.stringToUser(suite).toString() );
                            }
                        }
                    }
                }

            }
            else {
                System.out.println("Pseudo deja pris");
                broadcastUDP.setConnexion(false);
            }
        }
        
        if (Type.equals("Demande Modification Pseudo")) {
            User emetteur = User.stringToUser(received.split("\n")[1]);
            if (! emetteur.equals(this.agent.getMe())) {
                String nouveau = received.split("\n")[2] ;
                String message = "";
                if(( agent.getActifUsers() ).checkNicknameUnicity(nouveau)) {
                    message = "Bon Choix Pseudo\n" + nouveau ;
                }
                else {
                    message = "Mauvais Choix Pseudo\n" ;
                }
                System.out.println(message);
                try {
                    broadcastUDP.envoiUnicast(emetteur.getIpAddress(),message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (Type.equals("Modification pseudo reussi")) {
            User emetteur = User.stringToUser(received.split("\n")[1]);
            if (! emetteur.equals(this.agent.getMe())) { 
                String nouveau = received.split("\n")[2] ;
                System.out.println("Je rentre ici");
                if(agent.getActifUsers().appartient(emetteur)) { 
                    System.out.println(emetteur.getNickname() + " va etre changer en : " + nouveau);
                    agent.getActifUsers().editList(emetteur.getNickname(), nouveau);
                } else {
                    System.out.println(emetteur.getNickname() + " va etre ajouter en : " + nouveau);
                    
                    emetteur.setNickname(nouveau);
                    agent.getActifUsers().ajouterUser(emetteur);
                }
            }
        }
       
        if (Type.equals("Mauvais Choix Pseudo")) {
            System.out.println("Ce choix de pseudo est dÃ©jÃ  pris il te faut en choisir un autre");
            broadcastUDP.setvalidNickname(false);
        }

        if (Type.equals("Bon Choix Pseudo")) {
            
        }


        if (Type.equals("Deconnexion")) {
            ( agent.getActifUsers() ).deleteUser(User.stringToUser(received.split("\n")[1]));
        }

    }
}
    
	private static List<InetAddress> AllAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
	}
    
	
}
