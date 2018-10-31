import org.junit.Assert;
import org.junit.Test;
import java.util.Optional;

public class Messages_setup_Test {

    private static final int port = 6259;

    @Test
    public void manyNetworkClients() {
        TCPHost host = new TCPHost(port);

        Thread hostThread =
                new Thread(
                        () -> {
                            while (host.Tick()) ;
                            host.Close();
                        });
        hostThread.start();

        NetworkSocketClient client1 = new NetworkSocketClient("localhost", port);
        Assert.assertNotNull(client1);
        client1.Close();
        NetworkSocketClient client2 = new NetworkSocketClient("localhost", port);
        Assert.assertNotNull(client2);
        client2.Close();


        hostThread.interrupt();

    }

    @Test(expected = IllegalArgumentException.class)
    public void failingPorts() {

        TCPHost host = new TCPHost(port);

        NetworkSocketClient client = new NetworkSocketClient("localhost", 0);


    }

}