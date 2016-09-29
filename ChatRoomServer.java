
import java.io.IOException;
import java.util.Scanner;


public class ChatRoomServer {

    public static void main(String[] args) throws IOException {
       		System.out.println("Starting Service...");
			new SocketServer().start();
    }
}