package main.java.IM;

/**
 * Created by Meghana on 7/15/2015.
 */

import javax.swing.JFrame;
public class ServerTest {
    public static void main(String args[]){
        Server obj = new Server();
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.startRunning();
    }
}
