package main.java.IM;

/**
 * Created by Meghana on 7/15/2015.
 */
import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
    private String serverIP;
    private String message = "";
    private Socket connection;
    private JTextField text;
    private JTextArea window;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client(String host){
        super("Client");
        serverIP = host;
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
        add(new JScrollPane(window), BorderLayout.CENTER);
        setSize(300,200);
        setVisible(true);
    }

    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException e){
            showMessage("\n Client terminated connection..");
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            closeCrap();
        }
    }

    private void connectToServer() throws IOException{
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP),7780);
        showMessage("Connected to: "+ connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are setup! \n");
    }

    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }catch(ClassNotFoundException e){
                showMessage("\n Not Known..");
            }
        }while(!message.equals("SERVER - END"));
    }

    private void closeCrap(){
        showMessage("\n Closing everything...");
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
            output.writeObject("CLIENT - "+x);
            output.flush();
            showMessage("\nCLIENT - "+x);
        }catch(IOException e){
            e.printStackTrace();
        }
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
