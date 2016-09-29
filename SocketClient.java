
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    private String serverAddress = "127.0.0.1";
    private int serverPort = 5566;
    private String roomName = "";
    private boolean enterRoom = false;
    private String userName = "user";

    public SocketClient() {

		System.out.println("Welcome to Bulletin Board Service");
		System.out.println("Use default server address? (Y/n)");


		Scanner scn = new Scanner(System.in);
		if (scn.nextLine().equalsIgnoreCase("n"))
		{
			System.out.print("Please input the server address : ");
			this.serverAddress = scn.nextLine();
		}

		System.out.print("Input username :");
		userName = scn.nextLine();

        Socket clientSocket = new Socket();

        InetSocketAddress isa = new InetSocketAddress(this.serverAddress, this.serverPort);

        Scanner inScanner = new Scanner(System.in);

        DataOutputStream out;
        DataInputStream in;

        try {
            System.out.println("Client try to connect to server ("+this.serverAddress+")");
            clientSocket.connect(isa, 10000);

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

            System.out.println("Connect successfully");
			System.out.println("====================");
			System.out.println("Emergency Chat Room");
            System.out.println("1. Enter Channel");
       		System.out.println("2. View Message");
       		System.out.println("3. Exit");

			boolean exit = true;

			while (exit)
			{
			System.out.print("Command:");

			int caseAnswer = scn.nextInt();

			switch(caseAnswer) {
				case 1:
					System.out.print("Write channel name:");
					scn.nextLine();
					roomName = scn.nextLine();
					System.out.print("Write message:");
					String msg = scn.nextLine();
					out.writeUTF("chat$"+roomName+"$"+userName+"$"+msg);
					break;

				case 2:
					System.out.print("Write channel name:");
					scn.nextLine();
					roomName = scn.nextLine();
					out.writeUTF("view$"+roomName);
					String allContent = in.readUTF();
					String[] content = allContent.split("\\#");


					System.out.print("Message in channel "+roomName);

					int i = 1;
					for (String temp : content)
					{
						String[] token = temp.split("\\$");
						if (token[0].equals("chat"))
						{
							if (token[1].equals(roomName))
								{
									System.out.println("#"+i+" "+token[2]+":"+token[3]);
								}
						}
						i++;
					}
					break;

				case 3:
					exit = false;
					break;

				default:
					break;
			}


			}


            out.flush();
            out.close();
            out = null;

            in.close();
            in = null;

            clientSocket.close();
            clientSocket = null;

        } catch (IOException e) {
            System.out.println("Server close connection!");
        }
    }
}