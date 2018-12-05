package IMClient;

import javax.swing.JFrame;


public class ClientTest {

    public static void main(String[] args) {
        Client user;
        user = new Client("SERVER IP ADDRESS HERE");
        user.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        user.startRunning();
    }
}
