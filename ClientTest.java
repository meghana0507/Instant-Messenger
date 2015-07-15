package main.java.IM;

/**
 * Created by Meghana on 7/15/2015.
 */
import javax.swing.JFrame;

public class ClientTest {
    public static void main(String args[]){
        Client obj;
        obj = new Client("127.0.0.1");
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.startRunning();
    }
}
