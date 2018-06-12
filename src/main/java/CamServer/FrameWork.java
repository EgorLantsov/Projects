package CamServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class FrameWork {
    volatile Image image = null;
    JFrame jFrame = null;
    //реализуем конструктор
    public FrameWork(){
//        this.image = image;
        this.jFrame = getFrame(); // создаем диалоговое окно
        jFrame.add(new MyComponent());
    }
    public void getImage(Image image){
//        jFrame.add(new MyComponent(image)); // выводим картинку в окне
        this.image = image;
        SwingUtilities.invokeLater(() -> jFrame.repaint());
    }
     JFrame getFrame(){
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width/2 - 300, dimension.height/2 - 240, 600, 480);
        jFrame.setTitle("Some app");
        return jFrame;
    }


    class MyComponent extends JComponent{
//        Image image = null;
//        public MyComponent(Image image){
//            this.image = image;
//        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.drawImage(image, 0, 0, null);
            }
    }
}
