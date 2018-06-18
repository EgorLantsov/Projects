package camclient;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.*;

public class CamClient {

    private static String ipReader(){
        try {
            FileInputStream fstream = new FileInputStream("C:/Users/Manager/Desktop/IP.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String ip = br.readLine();
            return ip;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        while (true) { // в цикле клиент пытается присоединиться к серверу каждые 5 секунд
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int serverPort = 4444; // порт сервера
//            String address = "93.100.73.109";  // IP компьютера на котором установлен сервер (для клиента на другом компе)
            String address = ipReader(); // читаем айпи из текстового файла
            try {
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект с указанным IP сервера
                Socket socket = new Socket(address, serverPort);
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
                //
//                InputStream in = socket.getInputStream(); // поток для чтения команды о закрытие окна трансляции
//                DataInputStream inData = new DataInputStream(in);
//                int i = 0;
                //
//            command = "start";
                if ("start".equals(command)) {
                    FrameCapture frameCapture = new FrameCapture(); // объект для настройки изображения и открытия камеры

                    while (frameCapture.webcam.isOpen()) {
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        ImageIO.write(frameCapture.webcam.getImage(), "JPEG", byteOut);
                        try { // отлавливаем исключение
                            // исключение образуется если сервер закрывает сокет (из-за закрытия окна транслияцци)
                        DataOutputStream dout = new DataOutputStream(bout);
                        dout.writeInt(byteOut.size());
                        dout.flush();
                            bout.write(byteOut.toByteArray());
                        } catch (SocketException e){ // если отловили то выключаем камеру
                            frameCapture.webcam.close();
                            break; // выходим из цикла
                        }


//                        System.out.println("write image");
                    }

                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
