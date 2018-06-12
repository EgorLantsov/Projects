package CamServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ServerMenu extends JFrame{

public String command = null;

    public ServerMenu() {
//        JFrame jFrame = getMenuFrame();
//        ImageIcon img = new ImageIcon("src/main/resources/photo.png");
//        jFrame.setIconImage(img.getImage());
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        JButton button = new JButton("Start");
//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                cmdStart();
//            }
//        });
//        String [] elements = new String[] {"first", "second", "third"};
//        JComboBox combo = new JComboBox(elements);
//        panel.add(combo, BorderLayout.EAST);
//        panel.add(button, BorderLayout.WEST);
//        jFrame.getContentPane().add(panel);


//        this.jPanel = getjPanel();
//        this.jButton = getMenuButton();
//        this.jTextField = getjTextField(ip);

//        jFrame.pack();
    }

    public String cmdStart(){
        return this.command;
    }

    public void frameFactory(ArrayList<String> arrayList){
        if (true)
            return;

        JFrame jFrame = new JFrame("WebCam");
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
//        jFrame.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width/2 - 150, dimension.height/2 - 120, 300, 340);
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new FlowLayout());
        ImageIcon icon = new ImageIcon("src/main/resources/photo.png");
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(120,90)); // устанавливаем размеры кнопки
        // определяем действия кнопки:
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                command = "start";
            }
        });
        // добавляем массив куда буду приходить стринги айпишников из в виде списка MultiThreadServer
        String [] elements = arrayList.toArray(new String[arrayList.size()]); // переводим список в массив
        JComboBox<String> comboBox = new JComboBox<>(elements); // добавляем массив в комбокс для отображения в рамке
        JLabel label = new JLabel("WebCam application");
        panel.add(label);
        panel.add(button);
        panel.add(comboBox);
        jFrame.add(panel);
        jFrame.validate();
    }


    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<>();
        a.add("1.25.365.23");
        a.add("1.25.365.28");
        a.add("1.25.305.01");
        ServerMenu serverMenu = new ServerMenu();
        serverMenu.frameFactory(a);
    }
}
