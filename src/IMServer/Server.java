package IMServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame {

    private JTextField userMessage;
    private JTextArea chatBox;
    private JComboBox fontComboBox;
    private JComboBox sizeComboBox;
    private JComboBox weightComboBox;
    private String currentFontName = "DIALOG";
    private int currentFontSize = 12;
    private JMenuBar fontBar;
    private JMenu optionMenu;
    private JMenu BGOptionMenu;
    private JMenuItem clearItem;
    private JMenuItem BGBlue;
    private JMenuItem BGLight_Gray;
    private JMenuItem BGRed;
    private JMenuItem BGPink;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    String timestamp = new SimpleDateFormat("HH:mm").format(new Date());

    
    String [] fonts = {"Dialog", "Monospaced", "Sans Serif", "Serif"};
    String [] sizes = {"10 pt.", "12 pt.", "14 pt."};
    String [] weights = {"Plain", "Bold", "Italic"};
    
    Font defaultFont = new Font(Font.DIALOG, Font.PLAIN, 12);

    
    //JFrame created and edit
    public Server() {
        super("Instant Messenger");
        userMessage = new JTextField();
        userMessage.setEditable(false);
        userMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendMessage(event.getActionCommand());
                chatBox.setFont(defaultFont);
                userMessage.setFont(defaultFont);
                userMessage.setText("");
            }
        });
        
        //Add font style choice menu.
        fontBar = new JMenuBar();
        add(fontBar, BorderLayout.NORTH);
        fontComboBox = new JComboBox(fonts);
        fontComboBox.setSelectedIndex(0);
        fontComboBox.addActionListener(fontComboBox);
        
        fontComboBox.addActionListener (new ActionListener () 
        {
            public void actionPerformed(ActionEvent e) 
            {
        		JComboBox source = (JComboBox) e.getSource();
                String style = (String) source.getSelectedItem();
                switch (style) 
                {
                    case "Dialog":
                    	Font font = new Font(Font.DIALOG, weightComboBox.getSelectedIndex(), currentFontSize);
                    	userMessage.setFont(font);
                    	chatBox.setFont(font);
                    	currentFontName = "DIALOG";
                        break;
                    case "Monospaced":
                    	Font font2 = new Font(Font.MONOSPACED, weightComboBox.getSelectedIndex(), currentFontSize);
                    	userMessage.setFont(font2);
                    	chatBox.setFont(font2);
                    	currentFontName = "MONOSPACED";
                        break;
                    case "Sans Serif":
                    	Font font3 = new Font(Font.SANS_SERIF, weightComboBox.getSelectedIndex(), currentFontSize);
                    	userMessage.setFont(font3);
                    	chatBox.setFont(font3);
                    	currentFontName = "SANS_SERIF";
                        break;
                    case "Serif":
                    	Font font4 = new Font(Font.SERIF, weightComboBox.getSelectedIndex(), currentFontSize);
                    	userMessage.setFont(font4);
                    	chatBox.setFont(font4);
                    	currentFontName = "SERIF";
                        break;
                    default:
                    	userMessage.setFont(defaultFont);
                    	chatBox.setFont(defaultFont);
                    	currentFontName = "DIALOG";
                    	currentFontSize = 12;
                        break;
                }
            }
        });
        
        fontBar.add(fontComboBox);
        
        //Size combo box.
        sizeComboBox = new JComboBox(sizes);
        sizeComboBox.setSelectedIndex(1);
        sizeComboBox.addActionListener(sizeComboBox);
        
        sizeComboBox.addActionListener (new ActionListener () 
        {
            public void actionPerformed(ActionEvent e) 
            {
        		JComboBox source = (JComboBox) e.getSource();
                String size = (String) source.getSelectedItem();
                switch (size) 
                {
                    case "10 pt.":
                    	Font font = new Font(currentFontName, weightComboBox.getSelectedIndex(), 10);
                    	currentFontSize = 10;
                    	userMessage.setFont(font);
                    	chatBox.setFont(font);
                        break;
                    case "12 pt.":
                    	Font font2 = new Font(currentFontName, weightComboBox.getSelectedIndex(), 12);
                    	currentFontSize = 12;
                    	userMessage.setFont(font2);
                    	chatBox.setFont(font2);
                        break;
                    case "14 pt.":
                    	Font font3 = new Font(currentFontName, weightComboBox.getSelectedIndex(), 14);
                    	userMessage.setFont(font3);
                    	chatBox.setFont(font3);
                    	currentFontSize = 14;
                        break;
                    default:
                    	userMessage.setFont(defaultFont);
                    	chatBox.setFont(defaultFont);
                    	currentFontName = "DIALOG";
                    	currentFontSize = 12;
                        break;
                }
            }
        });
        fontBar.add(sizeComboBox);
        
        //Weight combo box.
        weightComboBox = new JComboBox(weights);
        weightComboBox.setSelectedIndex(0);
        weightComboBox.addActionListener(weightComboBox);
        
        weightComboBox.addActionListener (new ActionListener () 
        {
            public void actionPerformed(ActionEvent e) 
            {
        		JComboBox source = (JComboBox) e.getSource();
                String weight = (String) source.getSelectedItem();
                switch (weight) 
                {
                    case "Plain":
                    	Font font = new Font(currentFontName, Font.PLAIN, currentFontSize);
                    	userMessage.setFont(font);
                    	chatBox.setFont(font);
                        break;
                    case "Bold":
                    	Font font2 = new Font(currentFontName, Font.BOLD, currentFontSize);
                    	userMessage.setFont(font2);
                    	chatBox.setFont(font2);
                        break;
                    case "Italic":
                    	Font font3 = new Font(currentFontName, Font.ITALIC, currentFontSize);
                    	userMessage.setFont(font3);
                    	chatBox.setFont(font3);
                        break;
                    default:
                    	userMessage.setFont(defaultFont);
                    	chatBox.setFont(defaultFont);
                    	currentFontName = "DIALOG";
                    	currentFontSize = 12;
                        break;
                }
            }
        });
        
        fontBar.add(weightComboBox);
        
        
        add(userMessage, BorderLayout.SOUTH);
        chatBox = new JTextArea();
        add(new JScrollPane(chatBox));
        chatBox.setBackground(Color.lightGray);
        
        
        
        setSize(500, 500);
        setVisible(true);


        
        
        //add menu to change background color
          
          optionMenu = new JMenu("Options");
          BGOptionMenu = new JMenu("Background Color");
          clearItem = new JMenuItem("Clear Message");
          fontBar.add(optionMenu);
          optionMenu.add(BGOptionMenu);
          optionMenu.add(clearItem);
          BGBlue = new JMenuItem("Blue");
          BGLight_Gray = new JMenuItem("Grey");
          BGRed = new JMenuItem("Red");
          BGPink = new JMenuItem("Pink");
          BGOptionMenu.add(BGBlue);
          BGOptionMenu.add(BGLight_Gray);
          BGOptionMenu.add(BGRed);
          BGOptionMenu.add(BGPink);
          
          clearItem.addActionListener(new ActionListener (){
              public void actionPerformed(ActionEvent e){
              	userMessage.setText("");
              }
          });
          
          BGBlue.addActionListener (new ActionListener (){
              public void actionPerformed(ActionEvent e){
              	chatBox.setBackground(Color.CYAN);
              }
          });
          
          BGLight_Gray.addActionListener (new ActionListener (){
              public void actionPerformed(ActionEvent e){
              	chatBox.setBackground(Color.LIGHT_GRAY);
              }
          });
          
          BGRed.addActionListener (new ActionListener (){
              public void actionPerformed(ActionEvent e){
              	chatBox.setBackground(Color.RED);}
          });
          
          BGPink.addActionListener (new ActionListener (){
              public void actionPerformed(ActionEvent e){
              	chatBox.setBackground(Color.PINK);
              }
          });
          
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
            showMessage("\n[ " + timestamp + " ]" + "USER - " + message);

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
    
    //displays server IP address in the console
    public void MyIpAddress() {
   	 try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            System.out.println(ipAddr.getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
   }
}