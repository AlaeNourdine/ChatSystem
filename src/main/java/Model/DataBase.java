package Model;

import Controller.ControllerChat;

import java.sql.*;
import java.net.InetAddress;

/**
 * <p>
 * Classe représentant la Base de données.
 * Celle-ci stocke la liste des utilisateurs ainsi que les échanges entre eux
 * </p>
 * Format de la table Utilisateurs
 * <br> +--------+-------------+---------+---------------------+
 * <br> |   ID   |	 Pseudo	   |  Actif	 | Date d'inscription  |
 * <br> +--------+-------------+---------+---------------------+
 * <br> - Id : Correspond à l'ID de l'utilisateur
 * <br> - Pseudo :  Dernier Pseudo utilisé pour cet ID, va etre remplacé par le pseudo actuel avec majPseudo
 * <br> - Date d'inscription : Comme son nom l'indique le jour de l'inscription de cet utilisateur
 * <br> - Actif :  Boolean indiquant si l'utilisateur est actif
 * <br>
 * <br>  Chaque communication entres utilisateurs sera stockée dans une table de la forme
 * <br> +--------------+--------+---------+--------------+
 * <br> | Destinataire | Source | Message | Date d'envoi |
 * <br> +--------------+--------+---------+--------------+
 * <br>  - Destinataire : Id du destinataire du message
 * <br>  - Source : Id de celui qui envoie le message
 * <br>  - Message : Msg envoyé
 * <br>  - Date d'envoi : Date a laquelle Source a envoyé le message à destinataire
 */
public class DataBase {

    /* URL pour accéder à la BDD */
	//C'est une concaténation des éléments suivants : 
	//jdbc:mysql : qui indique le protocole utilisé pour accéder à la BDD
	//localhost::3306 : dans notre cas srv-bdens.insa-toulouse.fr:3306 , indique le nom de l'hôte qui héberge la bdd, ainsi que le port d'accès
	//nom_de_la_bdd : dans notre cas tp_servlet_020 
	private String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_020?" ;

    /* Connection avec la BDD */
    private Connection connection = null;

    /* Login pour se connecter à la BDD */
    private String login = "tp_servlet_020";

    /* Mdp pour se connecter à la BDD */
    private String mdp = "Or4Xaigh";

    private ControllerChat chatapp;

    /* Singleton */
    private static final DataBase instance = null;

    /**
     * Constructeur de la database
     * <br> On installe le driver et on établit la connection.
     */
    public DataBase(ControllerChat chatapp) {
        //this.chatapp = chatapp;
        //this.login = (String) chatapp.getConfigJSON().get("BaseDeDonnesLogin");
        //this.mdp = (String) chatapp.getConfigJSON().get("BaseDeDonneesMDP");
        //this.DB_URL = (String) chatapp.getConfigJSON().get("BaseDeDonnesURL");
        try {
            //Besoin d'installer le driver JDBC entre java IDE et le system DBMS pour faire un pont entre les deux
            Class.forName("com.mysql.cj.jdbc.Driver"); //Chargement du pilote MySQL
            System.out.println("Driver Installe");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Echec installation Driver");
        }
        try {
            //Etablir une connexion , forme : (url, "myLogin", "myPassword");            
        	System.out.println(this.DB_URL);
            System.out.println(this.mdp);
            System.out.println(this.login);
            this.connection = DriverManager.getConnection(this.DB_URL,this.login,this.mdp); //Ouverture de la connexion
            System.out.println("Connexion Etablie");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Echec d'etablissement de la connexion");
        }
    }

    /**
     * Méthode permettant de renvoyer une instance de la classe DataBase
     * @return L'instance du singleton DataBase.
     */
    public static DataBase getInstance(ControllerChat chatapp) {
        synchronized (DataBase.class) {
            return new DataBase(chatapp);
        }
    }

    /**
     * Methode permettant de créer une table pour stocker les messages entre deux utilisateurs
     * @param ip1 IP du premier utilisateur
     * @param ip2 IP du second utilisateur
     */
    public void HistoriqueTable (String id1, String id2) {
        // Pour eviter d'avoir les tables en double
    	String p ;
    	String g;
        int comparaison = id1.compareTo(id2);
        
        if (comparaison < 0) {
            p = id1 ;
            g = id2 ;
        }
        else {
            p = id2;
            g = id1 ;
        }
        String nomTable = "Chat_" + p + "_" + g ;
        String requete = "CREATE TABLE IF NOT EXISTS`" +  nomTable +"` (\n" 
        		+ "`Destinataire` varchar(100) NOT NULL,\n" 
        		+ "`Emetteur` varchar(100) NOT NULL,\n" 
        		+ "`Envoi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" 
        		+ "`Message` text NOT NULL\n " ;
        System.out.println(requete);
        PreparedStatement ps = null ;
        //Statement est utilisé pour envoyer une requete SQL à la base de donnee
        try {
            ps = connection.prepareStatement(requete);
            System.out.println("Statement cree");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Echec creation Statement");
        }
        //Execute la donnée SQL statement passe en parametre
        try {
            assert ps != null;
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Echec executeUpdate ");
        }
    }

    /**
     * Méthode permettant de mettre a jour le pseudo d'un utilisateur en fonction de son ID dans la base de donnee
     * @param iD Id de l'utilisateur qui vient de changer de pseudo
     * @param pseudo Nouveau Pseudo de l'utilisateur
     */
    public void majNickname( String id , String Nickname) {
        String requete= "UPDATE `Utilisateurs` SET `Pseudo`=? WHERE id=?";
        PreparedStatement ps = null ;
        //Statement est utilisé pour envoyer une requete SQL à la base de donnee
        try {
            ps = connection.prepareStatement(requete);
            ps.setString(1, Nickname); // ? n°1 devient la valeur contenu dans Pseudo
            ps.setString(2, id); // ? n°2 devient la valeur contenu dans ID
            System.out.println("Statement cree");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Echec creation Statement");
        }
        //Execute la donnée SQL statement passe en parametre
        try {
            assert ps != null;
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Echec executeUpdate ");
        }
    }



    /**
     * Méthode permettant de mettre a jour le pseudo d'un utilisateur en fonction de son ID dans la base de donnée
     * @param iDdest Id de l'utilisateur Destinataire du message
     * @param iDsrc Id de celui qui envoi le message
     * @param msg Message envoye entre les deux utilisateurs
     */
    public void addHistory(String id_src , String id_dest, String msg) {
        String petit;
        String grand;
        int comparaison = id_src.compareTo(id_dest);
        if (comparaison < 0) {
            petit = id_dest;
            grand = id_src ;
        }
        else {
            petit = id_src;
            grand = id_dest ;
        }
        String nomTable = "Chat_" + petit + "_" + grand ;
        String requete= "INSERT INTO `"+ nomTable +"`(`Destinataire`, `Source`, `Message`)  VALUES ( ? , ? , ?)";
        PreparedStatement ps = null ;
        //Statement est utilisé pour envoyer une requete SQL à la base de donnee
        try {
            ps = connection.prepareStatement(requete);
            ps.setString(1, id_dest);  // ? n°1 devient la valeur contenu dans IDdest
            ps.setString(2, id_src);  // ? n°2 devient la valeur contenu dans IDsrc
            ps.setString(3, msg);  // ? n°3 devient la valeur contenu dans Msg
            //La date de l'envoi n'a pas besoin d'être renseigné, elle est automatique
            System.out.println("Statement cree");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Echec creation Statement");
        }
        //Execute la donnée SQL statement passe en parametre
        try {
            assert ps != null;
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Echec executeUpdate ");
        }
    }


    /**
     * Méthode permettant d'ajouter un utilisateur dans la base de donnée si celui-ci n'existe pas déjà
     * @param iD Id de l'utilisateur que l'on veut rajouter
     * @param pseudo Pseudo actuelle de l'utilisateur
     */
    public void addUser (String id, String nickname ) {
        if(this.idExist(id)){
            this.majNickname(id, nickname);
        }
        else {
            // L'utilisateur n'existe pas , on va le rajouter.
            System.out.println("On rajoute l'utilisateur " + nickname);
            String requete = "INSERT INTO `User` (`ID`, `Nickname`, `Actif`)  VALUES ( ? , ? , '1')";
            PreparedStatement ps = null;
            //Statement est utilisé pour envoyer une requete SQL à la base de donnee
            try {
                ps = connection.prepareStatement(requete);
                ps.setString(1, id); // ? n°1 devient la valeur contenu dans IP
                ps.setString(2, nickname); // ? n°2 devient la valeur contenu dans Pseudo
                System.out.println("Statement cree");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Echec creation Statement");
            }
            //Execute la donnée SQL statement passe en parametre
            try {
                assert ps != null;
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(" Echec executeUpdate ");
            }
        }
    }

    /**
     * Méthode permettant de mettre a jour les utilisateurs actuellement actif
     * @param Id Id de l'utilisateur qui vient de se connecter ou déconnecter
     * @param Connecte Boolean true si il est connecté , false sinon
     */
    public void updateActifUsers(Boolean Connecte, String id) {
        String requete= "UPDATE `Users` SET `Actif`=? WHERE id=?";
        PreparedStatement Ps = null ;
        int Actif = Connecte ? 1 : 0; // True -> 1 , False -> 0
        //Statement est utilisé pour envoyer une requete SQL à la base de donnee
        try {
            Ps = connection.prepareStatement(requete);
            Ps.setInt(1, Actif); // ? n°1 devient la valeur contenu dans Actif (1 ou 0)
            Ps.setString(2, id); // // ? n°2 devient la valeur contenu dans Id
            System.out.println("Statement cree");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Echec creation Statement");
        }
        //Execute la donnée SQL statement passe en parametre
        try {
            assert Ps != null;
            Ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Echec executeUpdate ");
        }
    }

    /**
     * Méthode permettant de récupérer les N derniers messages envoyés entre 2 utilisateurs
     * @param iD Utilisateur associé a ChatApp
     * @param iDdestinataire Celui avec qui correspond ID
     * @param n Le nombre de messages souhaités
     * @return Les N derniers Messages
     */
    public String restoreMsg(String id, String id_dest, int n) {
        StringBuilder msg = new StringBuilder();
        String petit;
        String grand;
        int comparaison = id.compareTo(id_dest);
        if (comparaison < 0) {
            petit = id;
            grand = id_dest;
        }
        else {
            petit = id_dest;
            grand = id ;
        }
        String nomTable = "Chat_" + petit + "_" + grand ;
        String requete = "SELECT * FROM `"+ nomTable +"`";
        Statement stmt = null;
        ResultSet rs = null ;
        try {
            stmt = this.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert stmt != null;
            rs = stmt.executeQuery(requete);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Integer taille = tailleBDD(nomTable);
        if(taille < n ){
            n = taille ;
        }
        for(int i = 0 ; i < n; i++){
            try {
                assert rs != null;
                if( rs.next()){
                    String idEmetteur = rs.getString("Emetteur");
                    String envoi = rs.getTimestamp("Envoi").toString();
                    String message = rs.getString("Message");
                    if (idEmetteur.equals(id)) msg.append("Moi (").append(envoi).append(") : ").append(message);
                    else {
                        String pseudoSource = getNickname(idEmetteur) ;
                        msg.append(pseudoSource).append(" (").append(envoi).append(") : ").append(message);
                    }
                msg.append('\n');
                }
                // MSG de la forme :
                // Source (Date) : Texte
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return msg.toString();
    }

    /**
     * Méthode permettant de récupérer le pseudo actuel d'un utilisateur en fonction de son ID
     * @param idSource Id de l'utilsateur dont on veut connaitre le pseudo
     * @return Pseudo actuel
     */
    private String getNickname(String id_src) {
        String nickname  = "" ;
        String requete= "SELECT * FROM `Utilisateurs` WHERE `ID` LIKE '" + id_src + "'";
        Statement stmt = null;
        ResultSet rs = null ;
        try {
            stmt = this.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert stmt != null;
            rs = stmt.executeQuery(requete);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert rs != null;
            if (rs.next()){
                    nickname = rs.getString("Nickname") ;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
        }
            return nickname ;
    }

    /**
     * Méthode permettant de récupérer les messages d'une plage de donnée [deb,fin] envoyés entre 2 utilisateurs
     * @param iD Utilisateur associé a ChatApp
     * @param iDdestinataire Celui avec qui correspond ID
     * @param deb On veut les messages à partir de l'indice deb
     * @param fin On veut les messages jusqu'a l'indice fin
     * @return Les messages d'une plage de donnée [deb,fin]
     */
    public String restoreMsg(String id, String id_dest, int deb , int fin) {
        StringBuilder msg = new StringBuilder();
        String petit;
        String grand;
        int comparaison = id.compareTo(id_dest);
        if (comparaison < 0) {
            petit = id;
            grand = id_dest;
        }
        else {
            petit = id_dest;
            grand = id ;
        }
        String nomTable = "Chat_" + petit + "_" + grand ;
        String requete = "SELECT * FROM `"+ nomTable+"`";
        Statement stmt = null;
        ResultSet rs = null ;
        try {
            stmt = this.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert stmt != null;
            rs = stmt.executeQuery(requete);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Integer taille = tailleBDD(nomTable);
        if(taille < fin ){
            fin = taille ;
        }
        for(int i = 0 ; i < fin; i++){
            try {
                assert rs != null;
                if( rs.next()){
                    String idEmetteur = rs.getString("Emetteur");
                    String envoi = rs.getTimestamp("Envoi").toString();
                    String message = rs.getString("Message");
                    if (i >= deb) {
                        if (idEmetteur.equals(id)) {
                            msg.append("Moi (").append(envoi).append(") : ").append(message);
                        } else {
                            String PseudoSource = getNickname(idEmetteur);
                            msg.append(PseudoSource).append(" (").append(envoi).append(") : ").append(message);
                        }
                        msg.append('\n');
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return msg.toString();
    }

    /**
     * Cette méthode sert à récupérer la taille d'un base de donnée
     * @param nomTable Nom de la table de donnée dont on veut récuperer le nom
     * @return Taille de la BDD
     */
    public Integer tailleBDD(String nomTable) {
        Integer taille = 0 ;
        String requete = "SELECT * FROM `"+nomTable+ "`";
        Statement stmt = null;
        ResultSet rs = null ;
        try {
            stmt = this.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            assert stmt != null;
            rs = stmt.executeQuery(requete);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while(true){
            try {
                assert rs != null;
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            taille++; // on incremente Taille pour chaque solution trouvée
        }
        return taille;
    }

    /**
     * Methode permettant de savoir si un utilisateur existe dans la base de donnée 'Utilisateurs'
     * @param iD On recherche l'utilisateur dont l'ID est ID
     * @return True si l'utilisateur existe , False sinon
     */
    public boolean idExist(String id){
        boolean existe = false ;
        // Verification que l'utilisateur n'existe pas
        String requete = "SELECT * FROM `Utilisateurs` WHERE `ID` LIKE '" + id +"'";
        Statement stmt;
        ResultSet rs = null ;
        try {
            stmt = this.connection.createStatement();
            rs = stmt.executeQuery(requete);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if(rs.next()){
                existe = (!rs.equals(null));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return existe;
    }

    /**
     * Méthode permettant de récupérer le nom de la table où sont stockés les messages de 2 utilisateurs.
     * @param u1 Un des 2 utilisateurs
     * @param u2 Le second utilisateur
     * @return Le nom de la table des historiques
     */
    public String getTableName(User u1, User u2){
    	String petit;
    	String grand;
        int comparaison;
        comparaison = u1.getId().compareTo(u2.getId()) ; //Compare Adresse IP de u1 à celle de u2
        if (comparaison < 0) {
            petit = u1.getId();
            grand = u2.getId();
        }
        else {
            petit = u2.getId() ;
            grand = u1.getId();
        }
        return "Chat_" + petit + "_" + grand ;
    }
}
