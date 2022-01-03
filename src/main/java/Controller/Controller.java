package Controller;

import java.io.*;
import java.util.Scanner;

public class Controller {

	public boolean NicknameCheck(String nickname) {
		
	
		
		return false;
	}
	
	
	public String editNickname() throws IOException {
		String nickname;
		Scanner in = new Scanner(System.in);
	    System.out.println("Choisis ton pseudonyme :");
	    nickname=in.nextLine();
		while(!(this.NicknameCheck(nickname))) { 
			System.out.println("Désolé, ce pseudonyme est déjà utilisé. Choisis un autre pseudonyme :");
			nickname=in.nextLine();  
	
		System.out.println("Bienvenue !");
		in.close();
		}
		return nickname;

	} 
}

