package IMServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server extends JFrame {

    private JTextField userMessage;
    private JTextArea chatBox;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    
    //JFrame created and edit
    public Server() {
        super("Instant Messenger");
        userMessage = new JTextField();
        userMessage.setEditable(false);
        userMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendMessage(event.getActionCommand());
                userMessage.setText("");
            }
        });
        add(userMessage, BorderLayout.SOUTH);
        chatBox = new JTextArea();
        add(new JScrollPane(chatBox));
        chatBox.setBackground(Color.lightGray);
        setSize(500, 500);
        setVisible(true);

    }

    //starts server and checks for client
    public void startRunning() {
        try {
            server = new ServerSocket(6789);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eofException) {
                    showMessage("\n Server ended the connection!");
                } finally {
                    closeCrap();
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();

        }
    }

    //waits for client to start/connect
    public void waitForConnection() throws IOException {
        showMessage("Waiting for someone to connect!");
        MyIpAddress();
        connection = server.accept();
        showMessage("\nNow connected to"
                + connection.getInetAddress().getHostName() + " !");

    }
    
    //lets you know everything is set up
    public void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are setup! \n");
    }
    
    //checks if its able to type and is still in chat
    public void whileChatting() throws IOException {
        String message = "\nYou are now connected!";
        sendMessage(message);
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\n I don't know what user send!");
            }
        } while (!message.equals("\nUSER-END"));
    }

    //once the JFrame is closed, the server shuts down
    public void closeCrap() {
        showMessage("\n Closing connections \n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
    
    //sends message when you push enter
    public void sendMessage(String message) {
        try {
            output.writeObject("ADMIN- " + message);
            output.flush();
            showMessage("\nADMIN- " + message);

        } catch (IOException ioException) {
            chatBox.append("\nERROR: Can't send that message");
        }
    }

    //gets message from other sender
    public void showMessage(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatBox.append(text);
            }
        });
    }

    public void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userMessage.setEditable(tof);
            }
        });
    }
    
    public void MyIpAddress() {
   	 try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            System.out.println(ipAddr.getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
   }
}
