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
                //�����ػ�����
                repaint();
                
                //ɨ����ô���
                commList = SERIAL_TOOL.findPort();
                if (commList != null && commList.size()>0) {
                    
                    //�����ɨ�赽�Ŀ��ô���
                    for (String s : commList) {
                        
                        //�ô������Ƿ��Ѵ��ڣ���ʼĬ��Ϊ�����ڣ���commList����ڵ���commChoice�ﲻ���ڣ�������ӣ�
                        boolean commExist = false;    
                        
                        for (int i=0; i<commChoice.getItemCount(); i++) {
                            if (s.equals(commChoice.getItem(i))) {
                                //��ǰɨ�赽�Ĵ������Ѿ��ڳ�ʼɨ��ʱ����
                                commExist = true;
                                break;
                            }                    
                        }
                        
                        if (commExist) {
                            //��ǰɨ�赽�Ĵ������Ѿ��ڳ�ʼɨ��ʱ���ڣ�ֱ�ӽ�����һ��ѭ��
                            continue;
                        }
                        else {
                            //��������������´����������ô��������б�
                            commChoice.add(s);
                        }
                    }
                    
                    //�Ƴ��Ѿ������õĴ���
                    for (int i=0; i<commChoice.getItemCount(); i++) {
                        
                        //�ô����Ƿ���ʧЧ����ʼĬ��Ϊ�Ѿ�ʧЧ����commChoice����ڵ���commList�ﲻ���ڣ����Ѿ�ʧЧ��
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
                    //���ɨ�赽��commListΪ�գ����Ƴ��������д���
                    commChoice.removeAll();
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    String err = ExceptionWriter.getErrorInfoFromException(e);
                    JOptionPane.showMessageDialog(null, err, "����", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        }
        
    }
    
    /**
     * ���ڲ�����ʽ����һ�����ڼ�����
     * @author zhong
     *
     */
    private class SerialListener implements SerialPortEventListener {
        
        /**
         * �����ص��Ĵ����¼�
         */
        public void serialEvent(SerialPortEvent serialPortEvent) {
            
            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI: // 10 ͨѶ�ж�
                    JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case SerialPortEvent.OE: // 7 ��λ�����������

                case SerialPortEvent.FE: // 9 ֡����

                case SerialPortEvent.PE: // 8 ��żУ�����

                case SerialPortEvent.CD: // 6 �ز����

                case SerialPortEvent.CTS: // 3 �������������

                case SerialPortEvent.DSR: // 4 ����������׼������

                case SerialPortEvent.RI: // 5 ����ָʾ

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
                    break;
                
                case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������
                    
                    //System.out.println("found data");
                    byte[] data = null;
                    
                    try {
                        if (serialPort == null) {
                            JOptionPane.showMessageDialog(null, "���ڶ���Ϊ�գ�����ʧ�ܣ�", "����", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            data = SERIAL_TOOL.readFromPort(serialPort);    //��ȡ���ݣ������ֽ�����
                            //System.out.println(new String(data));
                            
                       // �Զ���������̣�����ʵ��ʹ�ù����п��԰����Լ��������ڽ��յ����ݺ�����ݽ��н���
                            if (data == null || data.length < 1) {    //��������Ƿ��ȡ��ȷ    
                                JOptionPane.showMessageDialog(null, "��ȡ���ݹ�����δ��ȡ����Ч���ݣ������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
                                System.exit(0);
                            }
                            else {
                                String dataOriginal = new String(data);    //���ֽ���������ת��λΪ������ԭʼ���ݵ��ַ���
                                String dataValid = "";    //��Ч���ݣ���������ԭʼ�����ַ���ȥ���ͷ*���Ժ���ַ�����
                                String[] elements = null;    //�������水�ո���ԭʼ�ַ�����õ����ַ�������    
                                //��������
                                if (dataOriginal.charAt(0) == '*') {    //�����ݵĵ�һ���ַ���*��ʱ��ʾ���ݽ�����ɣ���ʼ����                            
                                    dataValid = dataOriginal.substring(1);
                                    elements = dataValid.split(" ");
                                    if (elements == null || elements.length < 1) {    //��������Ƿ������ȷ
                                        JOptionPane.showMessageDialog(null, "���ݽ������̳��������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
                                        System.exit(0);
                                    }
                                    else {
                                        try {
                                            //���½���Labelֵ
                                            /*for (int i=0; i<elements.length; i++) {
                                                System.out.println(elements[i]);
                                            }*/
                                            //System.out.println("win_dir: " + elements[5]);
                                            tem.setText(elements[0] + " ��");
                                            hum.setText(elements[1] + " %");
                                            pa.setText(elements[2] + " hPa");
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            JOptionPane.showMessageDialog(null, "���ݽ������̳������½�������ʧ�ܣ������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
                                            System.exit(0);
                                        }
                                    }    
                                }
                            }
                            
                        }                        
                        
                    } catch (ReadDataFromSerialPortFailure e) {
                        JOptionPane.showMessageDialog(null, e, "����", JOptionPane.INFORMATION_MESSAGE);
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
