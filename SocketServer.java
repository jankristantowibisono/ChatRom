
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketServer extends Thread {

    private ServerSocket serverSocket;
    private boolean runnningServer = true;
    private final int serverPort = 5566;
    private ArrayList<String> contentArray = new ArrayList<String>();

    public SocketServer() throws IOException {
        serverSocket = new ServerSocket(serverPort);
    }

    public void run() {
        Socket socket;

        System.out.println("Server started in port: " + serverPort);

        while (runnningServer) {
            try {
                synchronized (serverSocket) {
                    socket = serverSocket.accept();
                }

                System.out.println("Connected, client address: " + socket.getInetAddress());
                new Thread(new HandleClient(socket)).start();


            } catch(IOException e) {
                e.printStackTrace();

                System.out.println("Socket connected fail");
                System.out.println("IOException :" + e.toString());
            }
        }
    }


    private class HandleClient implements Runnable {

        private Socket socket;

        private DataInputStream in;
        private DataOutputStream out;

        HandleClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
               in = new DataInputStream(socket.getInputStream());
               out = new DataOutputStream(socket.getOutputStream());

               while (true) {
				   try{
						String content = in.readUTF();

						System.out.println("Send From Client: " + content);
						String[] token = content.split("\\$");

						if (token[0].equals("chat"))
						{
							contentArray.add(content);
						}
						else if (token[0].equals("view"))
						{
							String result = "";

							for (String temp : contentArray) {
							String[] tempArr = temp.split("\\$");
								if (tempArr[1].equals(token[1]))
									{
										result = result + temp + "#";
									}
							}

							out.writeUTF(result);

						}
					}
					catch(Exception e)
					{
						 e.printStackTrace();
					}
                }

                /*System.out.println("Server Shutdown");

                out.flush();
                out.close();

                socket.close();*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}