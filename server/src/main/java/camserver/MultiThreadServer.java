package camserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {

    static ExecutorService service = Executors.newFixedThreadPool(2); // пул потоков для подключения нескольких

    private static Map<String, Connection> connections = new HashMap<>(); // для хранения подключенных клиентов
    // и поток-серверов под этих клиентов

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


        int port = 4444;
        String ipClient = null;

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
                OutputStream out = socketClient.getOutputStream(); // исходящий поток для отправки комманды
                DataOutputStream dout = new DataOutputStream(out); // добавляем действий, оборачивая в Дату

                // получаем ip клиента
                InputStream inputIp = socketClient.getInputStream(); // чтение из клиента его айпи
                DataInputStream dataInIp = new DataInputStream(inputIp); // переводим чтение в дату

                ipClient = socketClient.getInetAddress().getHostAddress() + ":" + socketClient.getPort();

                // добавили в мапу сокет аксепт в мапу, с ключем по айпи
                // далее через графическое окно вызываем из мапы нужный сокет
                connections.put(ipClient, new Connection(socketClient, new CamServer(socketClient), dout, dataInIp));

                frame.addIp(ipClient); // добавляем сокет в меню отложенным методом, после формирования окна меню
            }
            service.shutdown(); // останавливаем работу
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void startTranslation(String ipClient) throws IOException {
        Connection con = connections.get(ipClient); // берем соединение по айпи из мапы connections

        assert con != null; // проверка истинности выражения

        service.execute(con.srv); //  в пуле запускаем конкретный сервер из класса соединений
        con.out.writeUTF("start"); // отправляем команду для начала трансляции

    }
}
