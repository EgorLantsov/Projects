package camserver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class CamServer implements Runnable{ // сервер будет как поток

    private Socket socketClient;


    public CamServer (Socket socketClient){
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try {
//            synchronized (CamServer.this){ // синхронизируемся на объекте сервера
//                try {
//                    CamServer.this.wait(); // и ждем пока его не разбудят из вне, выберут его из мапы по его айпи
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                FrameWork frameWork = new FrameWork();
            InputStream in = socketClient.getInputStream();
            while (!socketClient.isClosed()) { // в цикле получаем изображения для смены картинки

                DataInputStream din = new DataInputStream(in);

                int len = din.readInt(); // получаем размер картинки (например 14 байт)

                byte[] data = new byte[len]; // создаем на основе известной величины массив байт (длина 14 байт)

                len = in.read(data); // считываем массив из потока в массив байтов (буфер) и возвращаем количество считанных байт
                // он считывает побайтно поэтому вернет 1 (len == 1)
                if (len == -1)
                    return;

                while (len < data.length) { // в цикле пока не считаем все картинку равную длине массива
                    int read = in.read(data, len, data.length - len); // возвращает количесттво считанных байт
                    // здесь мы в массив байт data с ранее записанным 1 байтом (по индексу 0), дописываем с позиции len
                    // т.е. с индекса 1, и вычитаем из общей длин в 14 байт ранее прочитанный len равный 1 байту
                    if (read == -1)
                        break;
                    // прибавляем к len == 1 полученное число записанных байт read == 13
                    len += read; // теперь len не меньши длины массива, выходим из цикла
                }

                BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
                frameWork.getImage(image);

                // событие при закрытии окна трансляции с клиента
                if (!frameWork.jFrame.isActive()){ // если окно закрыто то закрываем сокет, у клиента вызывается исключение
                   socketClient.close();
                    break; // выходим из цикла
                }

//                BufferedImage image = ImageIO.read(in); // читаем полученное изображение
//                frameWork.getImage(image); // передаем изображение в диалоговое окно
            }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

// java -jar webcam.jar
// java -cp webcam.jar camclient.CamClient
