package camserver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import static javax.swing.SwingUtilities.invokeLater;

public class ServerFrame extends JFrame {
    private JComboBox<String> comboBox;

    public ServerFrame() throws HeadlessException {
        super("WebCam");
//        JFrame jFrame = new JFrame("WebCam");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setVisible(true);
//        jFrame.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds(dimension.width/2 - 150, dimension.height/2 - 120, 300, 340);
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new FlowLayout());
        // подгрузка картинки
        URL iconURL = getClass().getClassLoader().getResource("photo.png");
        ImageIcon icon = new ImageIcon(iconURL);
        // картинка на кнопку
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(120,90)); // устанавливаем размеры кнопки
        // определяем действия кнопки:
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo
                try {
                    MultiThreadServer.startTranslation(comboBox.getSelectedItem().toString());
                    // нажатие кнопки вызывает метод startTranslation при выбранном айпи в comboBox и переданном
                    // в качестве параметра в этот метод
                    removeIp(comboBox.getSelectedItem().toString()); // сразу как открыли трансляциюю с клиента,
                    // удаляем его сокет из comboBox
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        // добавляем массив куда буду приходить стринги айпишников из в виде списка MultiThreadServer
        comboBox = new JComboBox<>(); // добавляем массив в комбокс для отображения в рамке
        JLabel label = new JLabel("WebCam application");
        panel.add(label);
        panel.add(button);
        panel.add(comboBox);
        this.add(panel);
//        this.validate();
    }

    public void addIp(String ip) {
        invokeLater(() -> comboBox.addItem(ip));
        // добавляем элемент айпи из MultiThreadServer в конце очереди выполнения основного кода
    }

    public void removeIp(String ip) {
        invokeLater(() -> comboBox.removeItem(ip));
        // удаляем элемент айпи
    }
}
