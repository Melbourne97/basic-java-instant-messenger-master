package IMClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {

    private JTextField userMessage;
    private JTextArea chatBox;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    
    
    //JFrame created and edit
    public Client(String host) {
        super("Client!");
        serverIP = host;
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
        add(new JScrollPane(chatBox), BorderLayout.CENTER);
        chatBox.setBackground(Color.lightGray);
        setSize(500, 500);
        setVisible(true);

    }

    //checks for server
    public void startRunning() {

        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException eofException) {
            showMessage("\n Client terminated the connection");

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeCrap();
        }
    }

    //once server is found it connects
    private void connectToServer() throws IOException {
        showMessage("Attempting connection");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("\nConnected to" + connection.getInetAddress().getHostName());

    }

    //lets you know everything is set up
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are now good to go!");

    }

    //checks if its able to type and is still in chat
    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);

            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\nDont know ObjectType!");
            }
        } while (!message.equals("\nADMIN - END"));
    }

    //once the JFrame is closed, the server shuts down
    private void closeCrap() {
        showMessage("\nClosing down!");
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
    private void sendMessage(String message) {
        try {
            output.writeObject("USER - " + message);
            output.flush();
            showMessage("\nUSER - " + message);
        } catch (IOException ioException) {
            chatBox.append("\nSomething is messed up!");
        }

    }

    //gets message from other sender
    private void showMessage(final String m) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatBox.append(m);
            }
        });
    }

    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userMessage.setEditable(tof);
            }
        });
    }
}
