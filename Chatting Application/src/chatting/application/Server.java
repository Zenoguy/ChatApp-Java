package chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.sql.*;
public class Server implements ActionListener {

    JTextField text;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    JScrollPane scrollPane;

    Server() {
        f.setLayout(null);

        // Header panel setup
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 40, 44));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        // Back button
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        // Profile
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/1.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        // Buttons
        JLabel video = new JLabel(new ImageIcon(
                new ImageIcon(ClassLoader.getSystemResource("icons/video.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        JLabel phone = new JLabel(new ImageIcon(
                new ImageIcon(ClassLoader.getSystemResource("icons/phone.png")).getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT)));
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        JLabel morevert = new JLabel(new ImageIcon(
                new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png")).getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT)));
        morevert.setBounds(420, 20, 10, 25);
        p1.add(morevert);

        // Labels
        JLabel name = new JLabel("ZenoBhai");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.ITALIC, 12));
        p1.add(status);

        // Chat area panel
        a1 = new JPanel();
        a1.setLayout(new BoxLayout(a1, BoxLayout.Y_AXIS));
        a1.setBackground(new Color(0, 63, 40));
        a1.add(vertical);

        scrollPane = new JScrollPane(a1);
        scrollPane.setBounds(5, 75, 440, 570);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(new Color(0, 63, 40));

        // Apply custom scrollbar UI
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        f.add(scrollPane);


        // Input
        text = new RoundedTextField(20);
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);
        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionPerformed(new ActionEvent(text, ActionEvent.ACTION_PERFORMED, ""));
                }
            }
        });

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 110, 35);
        send.setBackground(new Color(0, 127, 16));
        send.setForeground(Color.WHITE);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        send.addActionListener(this);
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(new Color(0, 92, 29));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        text.requestFocus();
        loadChatHistory();

    }

    public void actionPerformed(ActionEvent ae) {
    try {
        String out = text.getText().trim();
        if (out.isEmpty()) return;

        JPanel p2 = formatLabel(out);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(0, 63, 40));
        right.add(p2, BorderLayout.LINE_END);

        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15));

        text.setText("");

        if (dout != null) {
            dout.writeUTF(out);

            // ✅ Save server message to DB
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "INSERT INTO messages (sender, content) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "Server");  // you can customize this later
                pstmt.setString(2, out);
                pstmt.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            a1.revalidate();
            a1.repaint();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0, 63, 40));
        panel.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel output = new JLabel("<html><div style='width: 150px; word-wrap: break-word;'>" + out + "</div></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(0, 96, 50));
        output.setForeground(Color.WHITE);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 12));
        time.setForeground(Color.LIGHT_GRAY);
        panel.add(time);

        return panel;
    }

    public static JPanel formatReceivedLabel(String msg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0, 63, 40));

        JLabel output = new JLabel("<html><div style='width: 150px; word-wrap: break-word;'>" + msg + "</div></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(51, 55, 63));
        output.setForeground(Color.WHITE);
        output.setOpaque(true);
        output.setBorder(new CompoundBorder(
                new LineBorder(new Color(51, 55, 63), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        output.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 12));
        time.setForeground(Color.LIGHT_GRAY);
        time.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(time);

        return panel;
    }
private void loadChatHistory() {
    try {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT sender, message FROM messages ORDER BY id ASC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String sender = rs.getString("sender");
            String message = rs.getString("message");

            JPanel panel = sender.equals("Server") ? formatLabel(message) : formatReceivedLabel(message);
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(new Color(0, 63, 40));
            wrapper.add(panel, sender.equals("Server") ? BorderLayout.LINE_END : BorderLayout.LINE_START);

            vertical.add(wrapper);
            vertical.add(Box.createVerticalStrut(15));
        }

        conn.close();
        a1.revalidate();
        a1.repaint();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Server server = new Server();
            new Thread(() -> {
                try (ServerSocket skt = new ServerSocket(6001)) {
                    System.out.println("Server started on port 6001");
                    while (true) {
                        Socket s = skt.accept();
                        System.out.println("Client connected: " + s.getInetAddress());
                        new Thread(() -> handleClient(s, server)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private static void handleClient(Socket clientSocket, Server server) {
        try (DataInputStream din = new DataInputStream(clientSocket.getInputStream())) {
            dout = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                String msg = din.readUTF();

                // Save to database
// Save to database
                try {
                    Connection conn = DBConnection.getConnection();
                    String sql = "INSERT INTO messages (sender, content) VALUES (?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, "Client");
                    pstmt.setString(2, msg);  // Use the received message
                    pstmt.executeUpdate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }



                SwingUtilities.invokeLater(() -> {
                    JPanel panel = formatReceivedLabel(msg);
                    JPanel left = new JPanel(new BorderLayout());
                    left.setBackground(new Color(0, 63, 40));
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));
                    server.a1.revalidate();
                    server.a1.repaint();
                    server.scrollPane.getVerticalScrollBar().setValue(server.scrollPane.getVerticalScrollBar().getMaximum());
                });
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Closing socket failed: " + e.getMessage());
            }
        }
    }
}
