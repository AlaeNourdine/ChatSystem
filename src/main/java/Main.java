import Controller.ControllerChat;

public class Main {
	ControllerChat chatsystem;
	
    public void start() throws Exception {
        this.chatsystem = ControllerChat.getInstance("Null",1234);
        chatsystem.activerEcouteTCP();
    }
    
    public static void main(String[] args) {
      
    }
}
