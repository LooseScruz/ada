import java.util.concurrent.ConcurrentLinkedQueue;
import java.lang.Thread;

/**
 * main sending functionality.
 */
class NetworkSender {

    /**
     * An unbounded thread-safe queue based on linked nodes.
     * This queue orders elements FIFO (first-in-first-out).
     */
    private ConcurrentLinkedQueue<String> outgoingMessages;
    private volatile boolean shouldProcessSendQueue;
    private NetworkSocket clientSocket;
    private Thread sendingThread;

    NetworkSender(NetworkSocket socket) {
        outgoingMessages = new ConcurrentLinkedQueue<>();
        shouldProcessSendQueue = true;
        clientSocket = socket;
        sendingThread = new Thread(this::SendAvailable);
        sendingThread.start();
    }

    void SendMessage(String msg) {
        if (shouldProcessSendQueue) {
            outgoingMessages.add(msg);
        }
    }

    private void SendAvailable() {
        while (shouldProcessSendQueue || !outgoingMessages.isEmpty()) {
            if (!outgoingMessages.isEmpty()) {
                clientSocket.WriteToSocket(outgoingMessages.poll());
            }
        }
    }

    /**
     * Closest thing to a destructor in Java.
     */
    void Close() {
        shouldProcessSendQueue = false;
        try {
            sendingThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

}