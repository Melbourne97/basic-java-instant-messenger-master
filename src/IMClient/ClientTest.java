package IMClient;

import javax.swing.JFrame;

public class ClientTest {

    public static void main(String[] args) {
        Client user;
        user = new Client("157.62.180.88");
        user.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        user.startRunning();
    }
}
