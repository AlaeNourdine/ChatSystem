package Network;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public Discussion(Socket link, ChatApp app) {
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
