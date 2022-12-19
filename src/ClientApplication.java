import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientApplication {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 1024);

        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        Thread read = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (inputStream.available() > 0) {
                            int d = 0;
                            String msg = "";
                            while ((d = inputStream.read()) != 38) {
                                msg = msg + (char) d;
                            }
                            if (msg.equals("Exit")) System.exit(0);
                            System.out.println(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        read.start();

        Thread write = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                while (true) {
                    String msg = sc.nextLine();
                    try {
                        outputStream.write((msg + "&").getBytes());
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        write.start();
    }
}
