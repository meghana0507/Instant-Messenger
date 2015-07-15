package main.java.IM;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by Meghana on 7/14/2015.
 */

public class Server extends JFrame{
    private ServerSocket server;
    private Socket connection;
    private JTextField text;
    private JTextArea window;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Server(){
        super("Instant Messenger");
        text = new JTextField();
        text.setEditable(false);
        text.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        text.setText("");
                    }
                }
        );
        add(text, BorderLayout.NORTH);
        window = new JTextArea();
        add(new JScrollPane(window));
        setSize(300,200);
        setVisible(true);
    }

    public void startRunning(){
        try{
            server = new ServerSocket(7780,100);
            while(true){
                try{
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }
                catch(EOFException e){
                    showMessage("\n Something went wrong! ");
                }finally {
                    closeCrap();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException{
        showMessage("Waiting to connect...\n");
        connection = server.accept();
        showMessage("Connected to"+connection.getInetAddress().getHostName());
    }

    private void whileChatting() throws IOException{
        String message = "You are connected!";
        sendMessage(message);
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n"+message);
            }catch(ClassNotFoundException e){
                showMessage("Unable to understand what user sent!");
            }
        }
        while(!message.equals("CLIENT - END"));
    }

    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are setup! \n");

    }

    private void showMessage(final String x){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        window.append(x);
                    }
                }
        );
    }

    private void closeCrap(){
        showMessage("\n Closing connection..\n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void sendMessage(String x){
        try{
            output.writeObject("SERVER - "+ x);
            output.flush();
            showMessage("\nSERVER - "+x);
        }
        catch(IOException e){
            window.append("Error sending the message");
        }
    }

    private void ableToType(final boolean bool){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        text.setEditable(bool);
                    }
                }
        );
    }
}
