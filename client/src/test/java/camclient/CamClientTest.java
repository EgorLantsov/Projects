package camclient;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import static org.junit.Assert.*;

public class CamClientTest {
    private CamClient camClient;
    private FrameCapture frameCapture;

    @Before
    public void runObject(){
        camClient = new CamClient();
        frameCapture = new FrameCapture();
    }

    @After
    public void nullObject(){
        camClient = null;
        frameCapture = null;
    }

    @Test
    public void testIpReader(){
        String ip = camClient.ipReader();
        Assert.assertNotNull(ip);
    }

    @Test
    public void testStream() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ImageIO.write(frameCapture.webcam.getImage(), "JPEG", byteOut);
        Assert.assertTrue(0 != byteOut.size());
    }


}