package Sockets;

import AwesomeSockets.AwesomeClientSocket;
import AwesomeSockets.AwesomeServerSocket;
import Dots.Dot;
import Dots.Point;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by JiaHao on 25/2/15.
 */
public class DotsSocketHelper {

    // Serialization functions

    // Server calls these

    public static void sendBoardToClient(AwesomeServerSocket server, Dot[][] board) throws IOException {
        ObjectOutputStream serverObjectOutputStream = new ObjectOutputStream(server.getServerOutputStreamForClient(0));
        sendBoardToClient(serverObjectOutputStream, board);
    }

    private static void sendBoardToClient(ObjectOutputStream serverOutputStream, Dot[][] board) throws IOException {
        serverOutputStream.writeObject(board);
    }

    private static Point readMoveFromClient(ObjectInputStream ois) throws IOException, ClassNotFoundException {

        return (Point)ois.readObject();
    }

    public static Point readMoveFromClient(AwesomeServerSocket server) throws IOException, ClassNotFoundException {

        ObjectInputStream serverObjectInputStream = new ObjectInputStream(server.getServerInputStreamForClient(0));

        return readMoveFromClient(serverObjectInputStream);
    }

    // Client calls these

    public static Dot[][] readBoardFromServer(AwesomeClientSocket client) throws IOException, ClassNotFoundException {

        ObjectInputStream clientObjectInputStream = new ObjectInputStream(client.getClientInputStream());

        return readBoardFromServer(clientObjectInputStream);

    }

    private static Dot[][] readBoardFromServer(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (Dot[][])ois.readObject();
    }

    public static void sendMoveToServer(AwesomeClientSocket client, Point point) throws IOException {
        ObjectOutputStream clientObjectOutputStream = new ObjectOutputStream(client.getClientOutputStream());
        sendMoveToServer(clientObjectOutputStream, point);
    }

    private static void sendMoveToServer(ObjectOutputStream oos, Point point) throws IOException {
        oos.writeObject(point);
    }


    public static Point getPointFromScanner(Scanner scanner) {

        // change largest number here to match size of board - 1
        final String REG_EX = "[0-4][0-4][0-1]";

        String playerPointString = "";
        boolean correctInput = false;
        while (!correctInput) {

            System.out.println("Enter move x, y, state without spaces:");

            playerPointString = scanner.nextLine();

            if (!(correctInput = Pattern.matches(REG_EX, playerPointString))) {
                System.out.println("Invalid entry.");
            }
        }

        return parseStringToPoint(playerPointString);
    }


    private static Point parseStringToPoint(String inp) {

        final int NO_OF_PARAMETERS = 3;
        int[] pointParameters = new int[NO_OF_PARAMETERS];

        for (int i = 0; i < 3; i++) {
            char currentChar = inp.charAt(i);
            pointParameters[i] = Character.getNumericValue(currentChar);
        }

        return new Point(pointParameters[0], pointParameters[1], pointParameters[2]);

    }


}
