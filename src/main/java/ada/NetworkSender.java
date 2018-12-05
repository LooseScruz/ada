package ada;

import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * main sending functionality.
 */
class NetworkSender {

    /**
     * An unbounded thread-safe queue based on linked nodes. This queue orders elements FIFO
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

    void SendMessage(String msg) {
        if (shouldProcessSendQueue) {

            /* DB: check for history command */
            if (msg.equals(":history:")) {
                System.out.println("coming soon!");
                //                new HistoryUtil();
                //                HistoryUtil.main(msg);
            } else {
                /* we don't want to send
                 * our own request for history
                 */
                outgoingMessages.add(msg);
            }
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
