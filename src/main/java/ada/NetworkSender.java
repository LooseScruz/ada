package ada;

import java.io.Closeable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * main sending functionality.
 */
class NetworkSender implements Closeable {

    /**
     * An unbounded thread-safe queue based on linked nodes. This queue
     * orders elements FIFO
     * (first-in-first-out).
     */
    private final ConcurrentLinkedQueue<String> outgoingMessages;

    private volatile boolean shouldProcessSendQueue;
    private final NetworkSocket clientSocket;
    private final Thread sendingThread;

    NetworkSender(NetworkSocket socket) {
        outgoingMessages = new ConcurrentLinkedQueue<>();
        shouldProcessSendQueue = true;
        clientSocket = socket;
        sendingThread = new Thread(this::SendAvailable);
        sendingThread.start();
    }

    /**
     * Sends a message.
     */
    public void SendMessage(String msg) {
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
    @Override
    public void close() {
        shouldProcessSendQueue = false;
        try {
            sendingThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
