import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.net.*; 
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.EventListener;
import java.io.File;
import java.util.Scanner;
public class Converter implements ActionListener{
    private static JLabel title, url;
    private static JFrame frame;
    private static JTextField urlTextField;
    private static JTextArea contentsTextArea;
    private static JPanel panel;
    private static JScrollPane scroll;
    private static JButton loadButton, exitButton, browseButton, csvButton, xmlButton;
    
    public static void main(String[] args){
        frame=new JFrame();
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        JPanel panel=new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        
        title=new JLabel("URL File Extractor");
        title.setFont(new Font("ARIAL",Font.BOLD,16));
        title.setBounds(450, 10, 300, 20);
        panel.add(title);
        
        url =new JLabel("Type URL Link");
        url.setFont(new Font("ARIAL",Font.BOLD,16));
        url.setBounds(180, 50, 150, 20);
        panel.add(url);
        
        urlTextField = new JTextField();
        urlTextField.setBounds(300, 50, 300, 20);
        panel.add(urlTextField);
        
        browseButton = new JButton("Browse");
        browseButton.setBounds(650, 70, 100, 20);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File projectFolder = new File(".");
                JFileChooser fc=new JFileChooser();
                fc.setCurrentDirectory(projectFolder);
                int i=fc.showOpenDialog(null);
                
                if(i==JFileChooser.APPROVE_OPTION){    
                    projectFolder = new File(fc.getSelectedFile().getAbsolutePath());
                }
                else{
                    JOptionPane.showMessageDialog(null,"File Path not found","Error",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String wholeContent = "";
                try{
                    if(!projectFolder.exists()){
                        JOptionPane.showMessageDialog(null,"File Path is invalid","Error",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    
                    StringBuilder stringBuilder = new StringBuilder();
                    Scanner scanner = new Scanner(projectFolder);
                    
                    while (scanner.hasNextLine()) {
                        stringBuilder.append(scanner.nextLine());
                        stringBuilder.append(System.lineSeparator());
                    }
                    
                    scanner.close();
                    
                    wholeContent = stringBuilder.toString();
                    
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }   
                
                contentsTextArea.setText(wholeContent);
                contentsTextArea.setCaretPosition(0);
            }
        });
        panel.add(browseButton);
        
        loadButton = new JButton("Load");
        loadButton.setBounds(650, 50, 100, 20);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wholeContent = "";
                try{
                    String urlPath = urlTextField.getText();
                    if(urlPath.isEmpty()){
                    JOptionPane.showMessageDialog(null,"URL IS EMPTY","Error",JOptionPane.WARNING_MESSAGE);
                    return;
                    }
                    if (!isValidURL(urlPath)) {
                        JOptionPane.showMessageDialog(null,"URL IS INVALID","Error",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    URL url = new URL(urlPath);
                    URLConnection connection = url.openConnection();
                    
                    BufferedWriter output = new BufferedWriter(new FileWriter("data.txt"));
                    // Read the contents of the URL
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.write(line + "\n");
                        wholeContent += line + "\n";
                    }
                    reader.close();
                    output.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"URL IS INVALID","Error",JOptionPane.WARNING_MESSAGE);
                }   
                
                contentsTextArea.setText(wholeContent);
                contentsTextArea.setCaretPosition(0);
            }
        });
        panel.add(loadButton);
        
        contentsTextArea = new JTextArea();
        contentsTextArea.setBounds(200, 100, 600, 500);
        contentsTextArea.setEditable(false);
        //panel.add(contentsTextArea);
        
        scroll = new JScrollPane(contentsTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(200, 100, 600, 500);
        panel.add(scroll);
        
        csvButton = new JButton("Convert to CSV");
        csvButton.setBounds(620, 600, 150, 20);
        csvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    BufferedWriter output = new BufferedWriter(new FileWriter("data.csv"));
                    String contentsFromTextArea = contentsTextArea.getText(); 
                    if(contentsFromTextArea.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Text Area is Empty","Error",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String[] linesFromTextArea = contentsFromTextArea.split("\n");
                    
                    for (String line : linesFromTextArea) {
                        output.write(line + "\n");
                    }
                    JOptionPane.showMessageDialog(null, "Converted to CSV file successfully","Successful",JOptionPane.WARNING_MESSAGE);
                    output.close();
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(),"Error",JOptionPane.WARNING_MESSAGE);
                }  
                
            }
        });
        panel.add(csvButton);
        
        xmlButton = new JButton("Convert to XML");
        xmlButton.setBounds(620, 630, 150, 20);
        xmlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    BufferedWriter output = new BufferedWriter(new FileWriter("data.xml"));
                    String contentsFromTextArea = contentsTextArea.getText(); 
                    
                    if(contentsFromTextArea.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Text Area is Empty","Error",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String[] linesFromTextArea = contentsFromTextArea.split("\n");
                    
                    output.write("<Document>\n");
                    for (String line : linesFromTextArea) {
                        output.write("\t<Line>\n");
                        String[] wordsEveryLine = line.split("\\s+"); // Split by whitespace
                        for (String word : wordsEveryLine){
                            output.write("\t\t<Word>\n");
                            output.write("\t\t\t" + word + "\n");
                            output.write("\t\t</Word>\n");
                        }
                        output.write("\t</Line>\n");
                    }
                    output.write("</Document>");
                    
                    JOptionPane.showMessageDialog(null, "Converted to XML file successfully","Successful",JOptionPane.WARNING_MESSAGE);
                    output.close();
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(),"Error",JOptionPane.WARNING_MESSAGE);
                }  
                
            }
        });
        panel.add(xmlButton);
        
        exitButton = new JButton("Exit");
        exitButton.setBounds(450, 630, 100, 20);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(exitButton);
        
        frame.setVisible(true);
    }
    
    public static boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        
    }   
}
