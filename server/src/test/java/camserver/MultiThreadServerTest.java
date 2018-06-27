package camserver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MultiThreadServerTest {
    private JComboBox<String>comboBox;
    private ServerFrame serverFrame;
    private String ip;

    @Before
    public void setObjects(){
        comboBox = new JComboBox<>();
        serverFrame = new ServerFrame();
        ip = "192.168.0.6.";
    }

    @Test
    public void TestComboBox(){
        comboBox.addItem(ip);
        Assert.assertNotNull(comboBox.getItemAt(0));
    }

}