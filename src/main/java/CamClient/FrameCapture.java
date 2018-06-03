package CamClient;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import java.awt.*;

public class FrameCapture {
    Webcam webcam;

    public FrameCapture(){
    webcam = Webcam.getDefault();
    webcam.setViewSize(new Dimension(640, 480)); // устанавливаем размер изображения
    webcam.setViewSize(WebcamResolution.VGA.getSize()); // устанавливаем разрешение
    webcam.open();
    }
}
