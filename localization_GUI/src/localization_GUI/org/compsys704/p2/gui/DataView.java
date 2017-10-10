package localization_GUI.org.compsys704.p2.gui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import localization_GUI.org.compsys704.p2.exception.ExceptionWriter;
import localization_GUI.org.compsys704.p2.exception.NoSuchPort;
import localization_GUI.org.compsys704.p2.exception.NotASerialPort;
import localization_GUI.org.compsys704.p2.exception.PortInUse;
import localization_GUI.org.compsys704.p2.exception.ReadDataFromSerialPortFailure;
import localization_GUI.org.compsys704.p2.exception.SerialPortParameterFailure;
import localization_GUI.org.compsys704.p2.serial.SerialTool;

/**
 * @author KGL
 *
 */
public class DataView extends JFrame{

	private static final long serialVersionUID = 8725332179200430704L;
	
	/**
     * frame location x value
     */
    public static final int LOC_X = 200;
    
    /**
     * frame location y value
     */
    public static final int LOC_Y = 70;
    
    /**
     * frame width
     */
    public static final int WIDTH = 900;
    
    /**
     * frame height
     */
    public static final int HEIGHT = 700;
    
    private static SerialTool SERIAL_TOOL = SerialTool.getSerialTool();

    private List<String> commList = null;
    private SerialPort serialPort = null;
    
    private Font font = new Font("Times New Roman", Font.BOLD, 20);
    
    private Label tem = new Label("no data", Label.CENTER);    //x
    private Label hum = new Label("no data", Label.CENTER);    //y
    private Label pa = new Label("no data", Label.CENTER);    //z
    
    private Choice commChoice = new Choice();
    private Choice bpsChoice = new Choice();
    
    private Button openSerialButton = new Button("Open");
    
    public DataView() {
        commList = SERIAL_TOOL.findPort();
        setVisible(true);
        
        this.setBounds(LOC_X, LOC_Y, WIDTH, HEIGHT);
        this.setTitle("COMPSYS704 Project2");
        this.setBackground(Color.white);
        getContentPane().setLayout(null);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                if (serialPort != null) {
                    //close serial port when exit GUI program
                    SERIAL_TOOL.closePort(serialPort);
                }
                System.exit(0);
            }
            
        });
        
        tem.setBounds(140, 103, 225, 50);
        tem.setBackground(Color.black);
        tem.setFont(font);
        tem.setForeground(Color.white);
        getContentPane().add(tem);
        
        hum.setBounds(520, 103, 225, 50);
        hum.setBackground(Color.black);
        hum.setFont(font);
        hum.setForeground(Color.white);
        getContentPane().add(hum);
        
        pa.setBounds(140, 193, 225, 50);
        pa.setBackground(Color.black);
        pa.setFont(font);
        pa.setForeground(Color.white);
        getContentPane().add(pa);
        
        commChoice.setBounds(106, 10, 121, 21);
        if (commList == null || commList.size()<1) {
            JOptionPane.showMessageDialog(null, "cannot find any serial port!", "Warn", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            for (String s : commList) {
                commChoice.add(s);
            }
        }
        getContentPane().add(commChoice);
        
        bpsChoice.setBounds(393, 10, 106, 21);
        bpsChoice.add("9600");
        bpsChoice.add("19200");
        bpsChoice.add("115200");
        getContentPane().add(bpsChoice);
        
        openSerialButton.setBounds(538, 10, 106, 21);
        openSerialButton.setBackground(SystemColor.activeCaption);
        openSerialButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        openSerialButton.setForeground(SystemColor.desktop);
        getContentPane().add(openSerialButton);
        
        JLabel lblNewLabel = new JLabel("Serial Ports:");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblNewLabel.setBounds(22, 10, 78, 21);
        getContentPane().add(lblNewLabel);
        
        JLabel lblBautRate = new JLabel("Baut Rate:");
        lblBautRate.setHorizontalAlignment(SwingConstants.CENTER);
        lblBautRate.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblBautRate.setBounds(309, 10, 78, 21);
        getContentPane().add(lblBautRate);
        
        openSerialButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                String commName = commChoice.getSelectedItem();            
                String bpsStr = bpsChoice.getSelectedItem();
                
                //check available port name is not null
                if (commName == null || commName.equals("")) {
                    JOptionPane.showMessageDialog(null, "cannot find valid serial port!", "Error", JOptionPane.INFORMATION_MESSAGE);            
                }
                else {
                    //check baud rate is not null
                    if (bpsStr == null || bpsStr.equals("")) {
                        JOptionPane.showMessageDialog(null, "wrong baudrate!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        int bps = Integer.parseInt(bpsStr);
                        try {
                            
                            serialPort = SERIAL_TOOL.openPort(commName, bps);
                            SERIAL_TOOL.addListener(serialPort, new SerialListener());
                            JOptionPane.showMessageDialog(null, "listen to the port successful, data will show later.", "Message", JOptionPane.INFORMATION_MESSAGE);
                            
                        } catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListenersException e1) {
                            JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                
            }
        });
        
        
        this.setResizable(false);
        
        new Thread(new RepaintThread()).start();
    }
    
    /*
     * repaint every 30ms
     */
    private class RepaintThread implements Runnable {
        public void run() {
            while(true) {
                //调用重画方法
                repaint();
                
                //扫描可用串口
                commList = SERIAL_TOOL.findPort();
                if (commList != null && commList.size()>0) {
                    
                    //添加新扫描到的可用串口
                    for (String s : commList) {
                        
                        //该串口名是否已存在，初始默认为不存在（在commList里存在但在commChoice里不存在，则新添加）
                        boolean commExist = false;    
                        
                        for (int i=0; i<commChoice.getItemCount(); i++) {
                            if (s.equals(commChoice.getItem(i))) {
                                //当前扫描到的串口名已经在初始扫描时存在
                                commExist = true;
                                break;
                            }                    
                        }
                        
                        if (commExist) {
                            //当前扫描到的串口名已经在初始扫描时存在，直接进入下一次循环
                            continue;
                        }
                        else {
                            //若不存在则添加新串口名至可用串口下拉列表
                            commChoice.add(s);
                        }
                    }
                    
                    //移除已经不可用的串口
                    for (int i=0; i<commChoice.getItemCount(); i++) {
                        
                        //该串口是否已失效，初始默认为已经失效（在commChoice里存在但在commList里不存在，则已经失效）
                        boolean commNotExist = true;    
                        
                        for (String s : commList) {
                            if (s.equals(commChoice.getItem(i))) {
                                commNotExist = false;    
                                break;
                            }
                        }
                        
                        if (commNotExist) {
                            //System.out.println("remove" + commChoice.getItem(i));
                            commChoice.remove(i);
                        }
                        else {
                            continue;
                        }
                    }
                    
                }
                else {
                    //如果扫描到的commList为空，则移除所有已有串口
                    commChoice.removeAll();
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    String err = ExceptionWriter.getErrorInfoFromException(e);
                    JOptionPane.showMessageDialog(null, err, "错误", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        }
        
    }
    
    /**
     * 以内部类形式创建一个串口监听类
     * @author zhong
     *
     */
    private class SerialListener implements SerialPortEventListener {
        
        /**
         * 处理监控到的串口事件
         */
        public void serialEvent(SerialPortEvent serialPortEvent) {
            
            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI: // 10 通讯中断
                    JOptionPane.showMessageDialog(null, "与串口设备通讯中断", "错误", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case SerialPortEvent.OE: // 7 溢位（溢出）错误

                case SerialPortEvent.FE: // 9 帧错误

                case SerialPortEvent.PE: // 8 奇偶校验错误

                case SerialPortEvent.CD: // 6 载波检测

                case SerialPortEvent.CTS: // 3 清除待发送数据

                case SerialPortEvent.DSR: // 4 待发送数据准备好了

                case SerialPortEvent.RI: // 5 振铃指示

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
                    break;
                
                case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
                    
                    //System.out.println("found data");
                    byte[] data = null;
                    
                    try {
                        if (serialPort == null) {
                            JOptionPane.showMessageDialog(null, "串口对象为空！监听失败！", "错误", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            data = SERIAL_TOOL.readFromPort(serialPort);    //读取数据，存入字节数组
                            //System.out.println(new String(data));
                            
                       // 自定义解析过程，你在实际使用过程中可以按照自己的需求在接收到数据后对数据进行解析
                            if (data == null || data.length < 1) {    //检查数据是否读取正确    
                                JOptionPane.showMessageDialog(null, "读取数据过程中未获取到有效数据！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                System.exit(0);
                            }
                            else {
                                String dataOriginal = new String(data);    //将字节数组数据转换位为保存了原始数据的字符串
                                String dataValid = "";    //有效数据（用来保存原始数据字符串去除最开头*号以后的字符串）
                                String[] elements = null;    //用来保存按空格拆分原始字符串后得到的字符串数组    
                                //解析数据
                                if (dataOriginal.charAt(0) == '*') {    //当数据的第一个字符是*号时表示数据接收完成，开始解析                            
                                    dataValid = dataOriginal.substring(1);
                                    elements = dataValid.split(" ");
                                    if (elements == null || elements.length < 1) {    //检查数据是否解析正确
                                        JOptionPane.showMessageDialog(null, "数据解析过程出错，请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                        System.exit(0);
                                    }
                                    else {
                                        try {
                                            //更新界面Label值
                                            /*for (int i=0; i<elements.length; i++) {
                                                System.out.println(elements[i]);
                                            }*/
                                            //System.out.println("win_dir: " + elements[5]);
                                            tem.setText(elements[0] + " ℃");
                                            hum.setText(elements[1] + " %");
                                            pa.setText(elements[2] + " hPa");
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            JOptionPane.showMessageDialog(null, "数据解析过程出错，更新界面数据失败！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                            System.exit(0);
                                        }
                                    }    
                                }
                            }
                            
                        }                        
                        
                    } catch (ReadDataFromSerialPortFailure e) {
                        JOptionPane.showMessageDialog(null, e, "错误", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }    
                    
                    break;
    
            }

        }
    }
    
    public static void main(String[] args) {
        DataView view = new DataView();
    }
}
