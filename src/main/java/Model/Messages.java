package Model;

import java.io.Serializable;
import java.util.Date;

public class Messages implements Serializable{



	private static final long serialVersionUID = 1L;
	private final String msg;
    private final Date horodatation;
    private final User emetteur;

    public Messages(String message, User emetteur)
    {
        this.msg = message;
        this.horodatation = new Date();
        this.emetteur = emetteur;

    }

    public String getMessage()
    {
        return this.msg;

    }

    public Date getHorodatation()
    {
        return this.horodatation;

    }

    public User getEmetteur() {
        return emetteur;
    }
}
