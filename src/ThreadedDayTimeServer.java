import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ThreadedDayTimeServer {
    public final static int SERVICE_PORT = 13;

    public static void main(String[] args) {

        ServerSocket socServerSocket = null;
        Socket socC1ient = null;
        try {
            socServerSocket = new ServerSocket(SERVICE_PORT);
            System.out.println("Server started on port : "+SERVICE_PORT);
            for (;; ) {
                socC1ient = socServerSocket.accept();
                RequestProcessorThread thread = new RequestProcessorThread(socC1ient);
                System.out.println("Thread created and handed over the connection."
                        + "Thr " + thread.toString() + "] Soc [" + socC1ient.toString() + "]");
                Thread t = new Thread(thread);
                t.start();
            }
        }
        catch(IOException e){
                e.printStackTrace();

        }
    }
}

class RequestProcessorThread implements Runnable {
    private Socket socClient;

    public RequestProcessorThread(Socket soc) {
        socClient = soc;
    }

    @Override
    public void run() {
        try {
            Date today = new Date();
            //writing date to the client
            PrintWriter out;
            out = new PrintWriter(socClient.getOutputStream(), true);
            out.println(today);
            // Wait till client sends some data back to the server.
            InputStream is = socClient.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String receivedData = br.readLine();
            System.out.println("Received from client : " + receivedData);
            socClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.toString() + " : Thread exiting... E");

    }

}