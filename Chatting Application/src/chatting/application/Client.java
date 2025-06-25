package chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Client implements ActionListener {

    JTextField text;
    static JPanel messagePanel;
    static Box vertical = Box.createVerticalBox();
    static JFrame frame = new JFrame();
    static DataOutputStream dout;
    JScrollPane scrollPane;

    Client() {
        frame.setLayout(null);

        // Header panel
        JPanel header = new JPanel();
        header.setBackground(new Color(7, 94, 84));
        header.setBounds(0, 0, 450, 70);
        header.setLayout(null);
        frame.add(header);

        // Back icon
        ImageIcon backIcon = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        JLabel back = new JLabel(new ImageIcon(backIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
        back.setBounds(5, 20, 25, 25);
        header.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // Profile picture
        ImageIcon profileIcon = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        JLabel profile = new JLabel(new ImageIcon(profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        profile.setBounds(40, 10, 50, 50);
        header.add(profile);

        // Name
        JLabel name = new JLabel("ZenoGuy");
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        name.setForeground(Color.WHITE);
        name.setBounds(110, 15, 150, 18);
        header.add(name);

        // Status
        JLabel status = new JLabel("Active Now");
        status.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        status.setForeground(Color.WHITE);
        status.setBounds(110, 35, 100, 20);
        header.add(status);

        // Action icons
        header.add(iconLabel("icons/video.png", 300, 20, 30, 30));
        header.add(iconLabel("icons/phone.png", 355, 20, 35, 30));
        header.add(iconLabel("icons/3icon.png", 410, 20, 13, 25));

        // Chat area panel
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Color.WHITE);
        messagePanel.add(vertical);

        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBounds(5, 75, 440, 570);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(new Color(0, 63, 40));

        // Apply custom scrollbar UI
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        frame.add(scrollPane);


        // Text field
        text = new RoundedTextField(20);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        text.setBounds(5, 655, 310, 40);
        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionPerformed(new ActionEvent(text, ActionEvent.ACTION_PERFORMED, ""));
                }
            }
        });
        frame.add(text);

        // Send button
        JButton send = new JButton("Send");
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.setBounds(320, 655, 123, 40);
        send.setBorder(new EmptyBorder(0, 0, 0, 0));
        send.addActionListener(this);
        frame.add(send);

        // Frame setup
        frame.setSize(450, 700);
        frame.setLocation(200, 50);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        text.requestFocus();
    }

    private JLabel iconLabel(String path, int x, int y, int w, int h) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(path));
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT);
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBounds(x, y, w, h);
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String message = text.getText().trim();
        if (message.isEmpty()) return;

        JPanel messageBubble = formatLabel(message, true);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(messageBubble, BorderLayout.LINE_END);

        vertical.add(wrapper);
        vertical.add(Box.createVerticalStrut(15));
        text.setText("");

        try {
            if (dout != null) {
                dout.writeUTF(message);
            }
            // Save client message to DB
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "INSERT INTO messages (sender, content) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "Client");  // or the user's name/ID
                pstmt.setString(2, message);
                pstmt.executeUpdate();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        updateUI();
    }

    public static JPanel formatLabel(String message, boolean isSent) {
        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBackground(Color.WHITE);
        bubble.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));

        Color bgColor = isSent ? new Color(37, 211, 102) : new Color(230, 230, 230);
        float alignment = isSent ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT;

        JLabel msg = new JLabel("<html><p style='width: 150px'>" + message + "</p></html>");
        msg.setFont(new Font("Tahoma", Font.PLAIN, 16));
        msg.setOpaque(true);
        msg.setBackground(bgColor);
        msg.setBorder(new EmptyBorder(10, 15, 10, 15));
        msg.setAlignmentX(alignment);

        JLabel time = new JLabel(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 12));
        time.setForeground(Color.GRAY);
        time.setAlignmentX(alignment);

        bubble.add(msg);
        bubble.add(Box.createVerticalStrut(5));
        bubble.add(time);
        return bubble;
    }

// Existing method
private void updateUI() {
    SwingUtilities.invokeLater(() -> {
        messagePanel.revalidate();
        messagePanel.repaint();
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
    });
}

// 🔽 Paste this right below
private void loadChatHistory() {
    try {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT sender, message FROM messages ORDER BY id ASC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String sender = rs.getString("sender");
            String msg = rs.getString("message");
            boolean isSent = sender.equalsIgnoreCase("Client");

            JPanel panel = formatLabel(msg, isSent);
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.WHITE);

            if (isSent) {
                wrapper.add(panel, BorderLayout.LINE_END);
            } else {
                wrapper.add(panel, BorderLayout.LINE_START);
            }

            vertical.add(wrapper);
            vertical.add(Box.createVerticalStrut(15));
        }

        conn.close();
        updateUI();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.loadChatHistory(); 

            new Thread(() -> {
                try {
                    Socket socket = new Socket("127.0.0.1", 6001);
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    dout = new DataOutputStream(socket.getOutputStream());

                    while (true) {
                        String msg = din.readUTF();
                        // Save received server message to DB
                        try {
                            Connection conn = DBConnection.getConnection();
                            String sql = "INSERT INTO messages (sender, content) VALUES (?, ?)";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, "Server");
                            pstmt.setString(2, msg);
                            pstmt.executeUpdate();
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        JPanel panel = formatLabel(msg, false);

                        JPanel wrapper = new JPanel(new BorderLayout());
                        wrapper.setBackground(Color.WHITE);
                        wrapper.add(panel, BorderLayout.LINE_START);

                        vertical.add(wrapper);
                        vertical.add(Box.createVerticalStrut(15));

                        SwingUtilities.invokeLater(() -> {
                            messagePanel.revalidate();
                            messagePanel.repaint();
                            JScrollBar bar = client.scrollPane.getVerticalScrollBar();
                            bar.setValue(bar.getMaximum());
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
