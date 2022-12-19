import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {

    static String doStep(String msg, char step, char[] field, int stepsCount) {
        if (field[Integer.parseInt(msg) - 1] == '_') {
            field[Integer.parseInt(msg) - 1] = step;

            if ((field[0] == step && field[1] == step && field[2] == step) ||
                    (field[3] == step && field[4] == step && field[5] == step) ||
                    (field[6] == step && field[7] == step && field[8] == step) ||
                    (field[0] == step && field[4] == step && field[8] == step) ||
                    (field[2] == step && field[4] == step && field[6] == step) ||
                    (field[0] == step && field[3] == step && field[6] == step) ||
                    (field[1] == step && field[4] == step && field[7] == step) ||
                    (field[2] == step && field[5] == step && field[8] == step)) return "Win";
            else if (stepsCount == 9) return "Draw";

            return "Success";
        } else return "Cell not empty!";
    }

    static String fieldToString(char[] field) {
        String result = "";

        for (int i = 1; i <= 9; i++) {
            result += field[i - 1];
            if (i % 3 == 0 && i != 9) {
                result += "\n-----\n";
            } else if (i != 9) {
                result += "|";
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1024);
        Socket socket = null;

        char[] field = {'_', '_', '_', '_', '_', '_', '_', '_', '_'};

        char step = 'x';
        int stepsCount = 1;

        System.out.println("Waiting client 1...");
        socket = serverSocket.accept();
        Client client1 = new Client(socket);
        System.out.println("Client 1 joined");

        System.out.println("Waiting client 2...");
        socket = serverSocket.accept();
        Client client2 = new Client(socket);
        System.out.println("Client 2 joined");

        client1.write("\n\n" + fieldToString(field) + "\n" + "Your step now..");
        client2.write("\n\n" + fieldToString(field) + "\n" + client1.name + " step now..");

        String msg, temp = "";
        boolean isGameRunning = true;
        while (isGameRunning) {
            switch (step) {
                case 'x' -> {
                    msg = client1.read();

                    try {
                        temp = doStep(msg, step, field, stepsCount);
                    } catch (Exception e) {
                        client1.write("Enter correct cell...");
                        break;
                    }

                    switch (temp) {
                        case "Success" -> {
                            client1.write("\n\n" + fieldToString(field) + "\n" + client2.name + " step now..");
                            client2.write("\n\n" + fieldToString(field) + "\n" + "Your step now..");
                            step = 'o';
                            stepsCount++;
                        }
                        case "Win" -> {
                            client1.write("\n\n" + fieldToString(field) + "\n" + "You win!");
                            client2.write("\n\n" + fieldToString(field) + "\n" + client1.name + " win!");
                            isGameRunning = false;
                        }
                        case "Draw" -> {
                            client1.write("\n\n" + fieldToString(field) + "\n" + "Draw!");
                            client2.write("\n\n" + fieldToString(field) + "\n" + "Draw!");
                            isGameRunning = false;
                        }
                        default -> client1.write("Enter correct cell...");
                    }
                }
                case 'o' -> {
                    msg = client2.read();

                    try {
                        temp = doStep(msg, step, field, stepsCount);
                    } catch (Exception e) {
                        client2.write("Enter correct cell...");
                        break;
                    }

                    switch (temp) {
                        case "Success" -> {
                            client2.write("\n\n" + fieldToString(field) + "\n" + client1.name + " step now..");
                            client1.write("\n\n" + fieldToString(field) + "\n" + "Your step now..");
                            step = 'x';
                            stepsCount++;
                        }
                        case "Win" -> {
                            client2.write("\n\n" + fieldToString(field) + "\n" + "You win!");
                            client1.write("\n\n" + fieldToString(field) + "\n" + client2.name + " win!");
                            isGameRunning = false;
                        }
                        case "Draw" -> {
                            client1.write("\n\n" + fieldToString(field) + "\n" + "Draw!");
                            client2.write("\n\n" + fieldToString(field) + "\n" + "Draw!");
                            isGameRunning = false;
                        }
                        default -> client2.write("Enter correct cell...");
                    }
                }
            }
        }

        client1.write("Exit");
        client2.write("Exit");
    }
}