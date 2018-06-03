package CamServer;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {

    static ExecutorService service = Executors.newFixedThreadPool(2); // пул потоков для подключения нескольких
    // клиентов



    public static void main(String[] args) {
        int port = 4444;
        String ipClient = null;
        Map<String, CamServer> camServerMap = new HashMap<>(); // в мапу будем добавлять сервера
        try (ServerSocket ss = new ServerSocket(port)){ // создали сокет сервера и указали порт
            while (!ss.isClosed()) { // сервер работает в цикле если он не закрыт (можно ли обойтись без цикла?)
                System.out.println("Server is waiting for client...");
                // тут код - запуск диалогового окна с полем ввода адреса клиента, и кнопки подключения к клиенту

                // нужна команда для того чтобы можно было вести трансляцию с конкретного компьютера
                // а не со всех сразу подключенных клиентов
                // нужен поток записи DataOutputStream (строки) для отправки команды по адресу компьютера клиента
                // запущенный ранее клиент получает компанду и начинает передавать картинку с камеры


                // создать мапу в него кладем наш кам сервер с ключем айпи клиента и значением сервером
                // и в самом кам сервере синхронизируемся и вейтим синхронизируемся(по мапе в которй он находится)
                // пока его не разбудят, командой по ключу(айпи)
//                    System.out.println(Inet4Address.getLocalHost().getHostAddress()); // получаем айпи компьютера
                Socket socketClient = ss.accept(); // ждем подключений клиентов
                System.out.println("Got a client");
                OutputStream out = socketClient.getOutputStream();
                DataOutputStream dout = new DataOutputStream(out);
                String command = null;
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                // получаем ip клиента
                InputStream inputIp = socketClient.getInputStream(); // чтение из клиента его айпи
                DataInputStream dataInIp = new DataInputStream(inputIp); // переводим чтение в дату
                ipClient = dataInIp.readUTF();

                service.execute(new CamServer(socketClient));
//                service.execute(camServerMap.put(ipClient, new CamServer(socketClient))); // передаем входящий поток
                // клиента на один из потоков сервера и помещаем в мапу созданный поток

                while (!"start".equals(command)){ // вводим команду для трансляции
                    System.out.println("waiting fo command...");
                    command = keyboard.readLine();
//                    camServerMap.get(ipClient).notify(); // будим объект - поток сервер
                    // в мапе берем объект по ключу - айпи, и будем его .notify()
                }
                dout.writeUTF(command);

            }
            service.shutdown(); // останавливаем работу
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
