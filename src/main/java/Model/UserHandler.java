package Model ;

import java.util.ArrayList;
import java.util.HashMap;

public class UserHandler {
    HashMap<String,User> ActiveUser; 

    public UserHandler(){
        ActiveUser = new HashMap<>();
    }

    
    public ArrayList<User> getUserList(){
        ArrayList<User> result = new ArrayList<>();
        for (User user : ActiveUser.values()){
            result.add(user);
        }
        return result;
    }
    public ArrayList<String> getNameList(){
        ArrayList<String> result = new ArrayList<>();
        for (User user : ActiveUser.values()){
            result.add(user.getNickname());
        }
        return result;
    }
    public void addUser(User u){

    }
}