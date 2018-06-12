package CamClient;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class CamClient {

    public static void main(String[] args) {

        int serverPort = 4444; // порт сервера
        String address = "111.1.1.1";  // IP компьютера на котором установлен сервер (для клиента на другом компе)
        try {
//            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект с указанным IP сервера
            Socket socket = new Socket("localhost", serverPort);
            String command = null; // команда для начала трансляции с камеры (получаем с сервера)

            // отправляем айпи адресс клиента на сервер:
//            URL whatismyip = new URL("http://myip.ru/");
//            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
//            String ip = in.readLine();
//            OutputStream outIp = socket.getOutputStream();
//            DataOutputStream doutIp = new DataOutputStream(outIp);
//            doutIp.writeUTF(ip); // пишем в поток айпи клиента

            // отправляем на сервер изображения и принимаем команду записи:
            OutputStream out = socket.getOutputStream(); // для отправки изображений на сервер
            InputStream input = socket.getInputStream(); // для чтения команды трансляции
            BufferedOutputStream bout = new BufferedOutputStream(out);
            DataInputStream din = new DataInputStream(input); // оборачиваем в Дата поток для чтения команды
            command = din.readUTF(); // читаем команду с сервера
//            command = "start";
            if ("start".equals(command)) {
                FrameCapture frameCapture = new FrameCapture(); // объект для настройки изображения и открытия камеры

                while (frameCapture.webcam.isOpen()) {
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    ImageIO.write(frameCapture.webcam.getImage(), "JPEG", byteOut);

                    DataOutputStream dout = new DataOutputStream(bout);
                    dout.writeInt(byteOut.size());
                    dout.flush();

                    bout.write(byteOut.toByteArray());

                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
