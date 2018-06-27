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
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds(dimension.width/2 - 100, dimension.height/2 - 120, 200, 280);
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);

        // подгрузка картинки
        URL iconURL = getClass().getClassLoader().getResource("ph.png");
        ImageIcon icon = new ImageIcon(iconURL);
        // картинка на кнопку
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(120,120)); // устанавливаем размеры кнопки
        button.setBorderPainted(true);
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
        JLabel label = new JLabel("Select client:");
        //
        GridBagLayout gbl = new GridBagLayout();
        panel.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill   = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth  = GridBagConstraints.REMAINDER;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        gbl.setConstraints(button, c);
        panel.add(button);
        c.insets = new Insets(30, 0, 0, 0);
        gbl.setConstraints(label, c);
        panel.add(label);
        c.insets = new Insets(10, 0,0,0);
        c.weightx = 0.0;
        c.weighty = 1.0;
        c.gridheight = GridBagConstraints.REMAINDER;
        gbl.setConstraints(comboBox,c);
        panel.add(comboBox);
        //
        this.add(panel);
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
