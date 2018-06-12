package CamServer;

import javax.swing.*;
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.swing.SwingUtilities.invokeLater;

public class MultiThreadServer {

    static ExecutorService service = Executors.newFixedThreadPool(2); // пул потоков для подключения нескольких
//    private static Map<String, Socket> socketMap;
//    private static Map<String, CamServer> camServerMap;
    private static Map<String, Connection> connections = new HashMap<>();
    // клиентов

    private static class Connection {
        Socket sock;
        CamServer srv;
        DataOutputStream out;
        DataInputStream in;

        public Connection(Socket sock, CamServer srv, DataOutputStream out, DataInputStream in) {
            this.sock = sock;
            this.srv = srv;
            this.out = out;
            this.in = in;
        }
    }


    public static void main(String[] args) {
        ServerFrame frame = new ServerFrame();
        frame.validate();
        frame.setVisible(true);

//        socketMap = new HashMap<>();
//        ArrayList<String> ipList = new ArrayList<>();
        int port = 4444;
        String ipClient = null;
//        ServerMenu serverMenu = new ServerMenu();
        // в мапу будем добавлять сервера
//        camServerMap = new HashMap<>();
        try (ServerSocket ss = new ServerSocket(port)){ // создали сокет сервера и указали порт
            while (!ss.isClosed()) { // сервер работает в цикле если он не закрыт (можно ли обойтись без цикла?)
                System.out.println("Server is waiting for client...");
                // тут код - запуск диалогового окна с полем ввода адреса клиента, и кнопки подключения к клиенту

                // нужна команда для того чтобы можно было вести трансляцию с конкретного компьютера
                // а не со всех сразу подключенных клиентов
                // нужен поток записи DataOutputStream (строки) для отправки команды по адресу компьютера клиента
                // запущенный ранее клиент получает компанду и начинает передавать картинку с камеры


                Socket socketClient = ss.accept(); // ждем подключений клиентов
                System.out.println("Got a client");
                OutputStream out = socketClient.getOutputStream();
                DataOutputStream dout = new DataOutputStream(out);
                String command = null;
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                // получаем ip клиента
                InputStream inputIp = socketClient.getInputStream(); // чтение из клиента его айпи
                DataInputStream dataInIp = new DataInputStream(inputIp); // переводим чтение в дату
//                ipClient = dataInIp.readUTF();

                ipClient = socketClient.getInetAddress().getHostAddress() + ":" + socketClient.getPort();

                // добавили в мапу сокет аксепт в мапу, с ключем по айпи
                // далее через графическое окно вызываем из мапы нужный сокет
//                socketMap.put(ipClient, socketClient);
                connections.put(ipClient, new Connection(socketClient, new CamServer(socketClient), dout, dataInIp));

                frame.addIp(ipClient);
//                for (Map.Entry<String, Socket> entry : socketMap.entrySet()) {
//                    ipList.add(entry.getKey()); // перекидываем апишники из мапы в список для передачи во ServerMenu
//                }
                //
//                serverMenu.frameFactory(ipList); // вызываем создание меню
//                command = serverMenu.cmdStart(); // команда в которй при нажатии кнопки присваивается "start"
                //
//                command = "start";

//                if (command.equals("start")) { // если нажали на кнопку то запускаем код ниже
//                    CamServer srv = new CamServer(socketMap.get(ipClient)); // создаем объект сервера под входящий клиент
//
//                    camServerMap.put(ipClient, srv); // храним его в мапе потоков, по айпи
//
//                    service.execute(camServerMap.get(ipClient)); // и в пуле запускаем вытаскивая из мапы по айпи
////                    while (!"start".equals(command)) { // вводим команду для трансляции
////                        System.out.println("waiting fo command...");
////                        command = keyboard.readLine();
////                        synchronized (camServerMap.get(ipClient)) { // синхронизируемся на нужном нам объекте-сервере по айпи
//////                        camServerMap.get(ipClient).notify();// будим объект - поток сервер
////                        }
////                    }
//                    dout.writeUTF(command); // отправляем команду для начала трансляции
//
//                    command = null;
//                }
            }
            service.shutdown(); // останавливаем работу
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void startTranslation(String ipClient) throws IOException {
        Connection con = connections.get(ipClient);

        assert con != null;

        service.execute(con.srv); // и в пуле запускаем вытаскивая из мапы по айпи
        con.out.writeUTF("start"); // отправляем команду для начала трансляции

    }
}
