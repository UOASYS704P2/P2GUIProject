package localization_GUI.org.compsys704.p2.gui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
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
    public static final int WIDTH = 700;
    
    /**
     * frame height
     */
    public static final int HEIGHT = 800;
    
    private static SerialTool SERIAL_TOOL = SerialTool.getSerialTool();

    private List<String> commList = null;
    private SerialPort serialPort = null;
    
    private Choice commChoice = new Choice();
    private Choice bpsChoice = new Choice();
    
    private Button openSerialButton = new Button("Open");
    private JTable imuTable;
    private final JLabel lblNewLabel_1 = new JLabel("IMU:");
    private final JLabel lblNewLabel_2 = new JLabel("Output:");
    DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> list = new JList<>(listModel);
    private final JLabel label = new JLabel("Position history:");
    DefaultListModel<String> logListModel = new DefaultListModel<>();
    private final JList<String> logList = new JList<>(logListModel);
    
    Vector<Vector<String>> tableData = new Vector<>(3);
    Vector<String> tableTitles = new Vector<>(2);
    private final JScrollPane scrollPane_1 = new JScrollPane();
    
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
        
        commChoice.setBounds(106, 10, 99, 21);
        if (commList == null || commList.size()<1) {
            JOptionPane.showMessageDialog(null, "cannot find any serial port!", "Warn", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            for (String s : commList) {
                commChoice.add(s);
            }
        }
        getContentPane().add(commChoice);
        
        bpsChoice.setBounds(310, 10, 106, 21);
        bpsChoice.add("9600");
        bpsChoice.add("19200");
        bpsChoice.add("115200");
        getContentPane().add(bpsChoice);
        
        openSerialButton.setBounds(445, 10, 78, 21);
        openSerialButton.setBackground(SystemColor.activeCaption);
        openSerialButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        openSerialButton.setForeground(SystemColor.desktop);
        getContentPane().add(openSerialButton);
        
        JLabel lblNewLabel = new JLabel("Serial Ports:");
        lblNewLabel.setLabelFor(commChoice);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblNewLabel.setBounds(22, 10, 78, 21);
        getContentPane().add(lblNewLabel);
        
        JLabel lblBautRate = new JLabel("Baut Rate:");
        lblBautRate.setLabelFor(bpsChoice);
        lblBautRate.setHorizontalAlignment(SwingConstants.CENTER);
        lblBautRate.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblBautRate.setBounds(226, 10, 78, 21);
        getContentPane().add(lblBautRate);
        
//        imuTable = new JTable();
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblNewLabel_1.setLabelFor(imuTable);
        Vector<String> row0Data = new Vector<>();
        row0Data.addElement("x:");
        row0Data.addElement("");
        tableData.add(row0Data);
        Vector<String> row1Data = new Vector<>();
        row1Data.addElement("y:");
        row1Data.addElement("");
        tableData.add(row1Data);
        Vector<String> row2Data = new Vector<>();
        row2Data.addElement("z:");
        row2Data.addElement("");
        tableData.add(row2Data);
        tableTitles.addElement("IMU");
        tableTitles.addElement("IMU");
        
        DefaultTableModel tableModel = new DefaultTableModel(tableData, tableTitles);
        imuTable = new JTable(tableModel);
        
        imuTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        imuTable.setRowHeight(30);
        imuTable.setFont(new Font("Times New Roman", Font.BOLD, 16));
        imuTable.setBorder(new LineBorder(new Color(0, 0, 0)));
        imuTable.setBackground(new Color(255, 239, 213));
        imuTable.setBounds(22, 102, 202, 90);
        getContentPane().add(imuTable);
        lblNewLabel_1.setBackground(Color.WHITE);
        lblNewLabel_1.setBounds(22, 73, 202, 27);
        
        getContentPane().add(lblNewLabel_1);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(10, 595, 674, 166);
        scrollPane.getViewport().setOpaque(false);  
        getContentPane().add(scrollPane);
        logList.setBackground(new Color(253, 245, 230));
        logList.setFont(new Font("Times New Roman", Font.BOLD, 14));
        
        scrollPane.setViewportView(logList);
        lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblNewLabel_2.setBounds(10, 568, 86, 26);
        
        getContentPane().add(lblNewLabel_2);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        label.setBackground(Color.WHITE);
        label.setBounds(22, 229, 202, 27);
        
        getContentPane().add(label);
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.getVerticalScrollBar().setValue(529);
        scrollPane_1.setBounds(22, 250, 202, 289);
        
        getContentPane().add(scrollPane_1);
        list.setValueIsAdjusting(true);
        list.setVisibleRowCount(18);
        list.setLayoutOrientation(JList.VERTICAL);
        scrollPane_1.setViewportView(list);
        list.setFont(new Font("Times New Roman", Font.BOLD, 12));
        list.setBackground(new Color(255, 239, 213));
        list.setBorder(new LineBorder(new Color(0, 0, 0)));
        
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
        	
        	Random random = new Random();
        	int x,y,z;
            while(true) {
                repaint();
                x = random.nextInt(500);
                y = random.nextInt(500);
                z = random.nextInt(500);
                
                Iterator<Vector<String>> iterator = tableData.iterator();
                while (iterator.hasNext()) {
					Vector<String> rowData = (Vector<String>) iterator.next();
					rowData.set(1, "" + random.nextInt(256));
				}
                
                listModel.addElement("location: (x,y,z) : (" + x + "," + y + "," + z + ")");
                list.ensureIndexIsVisible(list.getModel().getSize() -1);
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                
                logListModel.addElement("{\"IMU\":\"" + x + "," + y + "," + z + "\", \"location\":" + x + "," + y + "," + z + "\"}\r\n");
                logList.ensureIndexIsVisible(list.getModel().getSize() -1);
                
//                commList = SERIAL_TOOL.findPort();
//                if (commList != null && commList.size()>0) {
//                    for (String s : commList) {
//                        boolean commExist = false;    
//                        for (int i=0; i<commChoice.getItemCount(); i++) {
//                            if (s.equals(commChoice.getItem(i))) {
//                                commExist = true;
//                                break;
//                            }                    
//                        }
//                        
//                        if (commExist) {
//                            continue;
//                        }
//                        else {
//                            commChoice.add(s);
//                        }
//                    }
//                    
//                    //remove unavailable serial ports
//                    for (int i=0; i<commChoice.getItemCount(); i++) {
//                        boolean commNotExist = true;    
//                        for (String s : commList) {
//                            if (s.equals(commChoice.getItem(i))) {
//                                commNotExist = false;
//                                break;
//                            }
//                        }
//                        if (commNotExist) {
//                            commChoice.remove(i);
//                        }
//                        else {
//                            continue;
//                        }
//                    }
//                    
//                }
//                else {
//                    commChoice.removeAll();
//                }
//
//                try {
//                    Thread.sleep(30);
//                } catch (InterruptedException e) {
//                    String err = ExceptionWriter.getErrorInfoFromException(e);
//                    JOptionPane.showMessageDialog(null, err, "错误", JOptionPane.INFORMATION_MESSAGE);
//                    System.exit(0);
//                }
            }
        }
    }
    
    /**
     * Serial ports listener
     * @author KGL
     *
     */
    private class SerialListener implements SerialPortEventListener {
        
        public void serialEvent(SerialPortEvent serialPortEvent) {
            
            switch (serialPortEvent.getEventType()) {
                case SerialPortEvent.BI: // 10 lose connection
                	JOptionPane.showMessageDialog(null, "lose connection!", "Erro", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case SerialPortEvent.OE: // 7 The OE port event signals an overrun error

                case SerialPortEvent.FE: // 9 The FE port event signals a framing error.

                case SerialPortEvent.PE: // 8 The PE port event signals a parity error

                case SerialPortEvent.CD: // 6 The CD port event is triggered when the Data Carrier Detect line on the port changes its logic level.

                case SerialPortEvent.CTS: // 3 clear buffered data

                case SerialPortEvent.DSR: // 4 ready to send data

                case SerialPortEvent.RI: // 5 The RI port event is triggered when the Ring Indicator line on the port changes its logic level.

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 output buffer has been cleared
                    break;
                case SerialPortEvent.DATA_AVAILABLE: // 1 data exists in serial port
                	
                    byte[] data = null;
                    try {
                        if (serialPort == null) {
                        	JOptionPane.showMessageDialog(null, "serialPort obj is null, listen failed!", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            data = SERIAL_TOOL.readFromPort(serialPort); //read data and store in a byte[]
                            //System.out.println(data);
                            
                            // parse customized receiving data
                            if (data == null || data.length < 1) {
                            	JOptionPane.showMessageDialog(null, "didn't receive valid data!", "Error", JOptionPane.INFORMATION_MESSAGE);
                                System.exit(0);
                            } else {
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
                                    } else {
                                        try {
                                            /*for (int i=0; i<elements.length; i++) {
                                                System.out.println(elements[i]);
                                            }*/
                                            //System.out.println("win_dir: " + elements[5]);
//                                        	tem.setText(elements[0] + " ℃");
//                                          hum.setText(elements[1] + " %");
//                                          pa.setText(elements[2] + " hPa");
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            JOptionPane.showMessageDialog(null, "数据解析过程出错，更新界面数据失败！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                                            System.exit(0);
                                        }
                                    }    
                                }
                            }
                        }                        
                        
                    } catch (ReadDataFromSerialPortFailure e) {
                        JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
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
