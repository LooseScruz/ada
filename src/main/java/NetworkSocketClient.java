import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class NetworkSocketClient implements NetworkSocket {

    private Socket clientSocket;
    private DataOutputStream clientOutputStream;
    private BufferedReader clientInputStream;

    NetworkSocketClient(Socket socket) {
        try {
            clientSocket = socket;
            clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            clientInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    NetworkSocketClient(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            clientInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void WriteToSocket(String msg) {
        try {
            clientOutputStream.writeBytes(msg + "\n");
            clientOutputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String ReadFromSocket() {
        try {
            if (clientInputStream.ready()) {
                return clientInputStream.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /* Closest thing to a destructor in Java */
    public void Close() {
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}