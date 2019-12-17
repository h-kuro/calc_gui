package com.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MyFrame extends JFrame implements KeyListener, ActionListener {
    // グローバル変数とクラス
    private static final int clientWidth = 300;
    private static final int clientHeight = 450;

    // プライベートメソッド
    private static JPanel ui;
    private static HashMap<String, JLabel> labelList;
    private static HashMap<String, JButton> buttonList;
    private static HashMap<Integer, String> keyList = new LinkedHashMap<Integer, String>();

    static {
        keyList.put(127, "CE");            //DELキー
        keyList.put(27, "C");                //ESCキー
        keyList.put((int) '\b', "←");
        keyList.put((int) '/', "÷");

        keyList.put((int) '7', "7");
        keyList.put((int) '8', "8");
        keyList.put((int) '9', "9");
        keyList.put((int) '*', "×");

        keyList.put((int) '4', "4");
        keyList.put((int) '5', "5");
        keyList.put((int) '6', "6");
        keyList.put((int) '-', "-");

        keyList.put((int) '1', "1");
        keyList.put((int) '2', "2");
        keyList.put((int) '3', "3");
        keyList.put((int) '+', "+");

        keyList.put((int) ',', "+/-");
        keyList.put((int) '0', "0");
        keyList.put((int) '.', ".");
        keyList.put((int) '\n', "=");
    }

    // ウインドウ設定
    MyFrame(String title) {
        setTitle(title);
        getContentPane().setPreferredSize(new Dimension(clientWidth, clientHeight));
        pack();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buttonList = new HashMap<String, JButton>();
        labelList = new HashMap<String, JLabel>();
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        TextField(this);
    }

    // 入力場所
    private static void TextField(MyFrame Frame) {
        // 式入力部分
        labelList.put("formula", new JLabel());
        labelList.get("formula").setBounds(0, 0, clientWidth, (int) ((clientHeight / 3) * 0.3));
        labelList.get("formula").setOpaque(true);
        labelList.get("formula").setHorizontalAlignment(JLabel.RIGHT);
        labelList.get("formula").setBackground(Color.DARK_GRAY);
        labelList.get("formula").setForeground(Color.WHITE);
        labelList.get("formula").setFont(new Font("メイリオ", Font.BOLD, 10));
        // 答え出力部分
        labelList.put("answer", new JLabel());
        labelList.get("answer").setBounds(0, (int) (labelList.get("formula").getBounds().y + labelList.get("formula").getBounds().height), clientWidth, (int) ((clientHeight / 3) * 0.7)
        );
        labelList.get("answer").setOpaque(true);
        labelList.get("answer").setHorizontalAlignment(JLabel.RIGHT);
        labelList.get("answer").setBackground(Color.BLACK);
        labelList.get("answer").setForeground(Color.WHITE);
        labelList.get("answer").setText(Calculator.getAnswer());
        labelList.get("answer").setFont(new Font("メイリオ", Font.BOLD, 16));

        // 数字キー
        int i = 0;
        for (String key : keyList.values()) {
            buttonList.put(key, new JButton(key));
            buttonList.get(key).setBounds(
                    (int) (clientWidth / 4) * (i % 4),
                    (int) ((((clientHeight / 3) * 2) / 5)) * (i / 4) + (clientHeight / 3),
                    (int) (clientWidth / 4),
                    (int) (((clientHeight / 3) * 2) / 5)
            );
            if ('0' <= key.charAt(0) && key.charAt(0) <= '9' && key.length() == 1) {
                buttonList.get(key).setBackground(Color.BLACK);
            } else {
                buttonList.get(key).setBackground(Color.DARK_GRAY);
            }
            buttonList.get(key).setForeground(Color.WHITE);
            buttonList.get(key).setFocusPainted(false);
            buttonList.get(key).addActionListener(Frame);
            buttonList.get(key).setActionCommand(key);
            buttonList.get(key).setFont(new Font("メイリオ", Font.BOLD, 16));
            i++;
        }

        ui = new JPanel();
        ui.setLayout(null);

        for (JLabel l : labelList.values()) {
            ui.add(l);
        }

        for (JButton button : buttonList.values()) {
            ui.add(button);
        }

        Container contentPane = Frame.getContentPane();
        contentPane.add(ui, BorderLayout.CENTER);
    }

    // イベントメソッド
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (keyList.containsKey((int) e.getKeyChar())) {
            buttonList.get(keyList.get((int) e.getKeyChar())).doClick();
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        Calculator.getCmd(cmd);
        labelList.get("formula").setText(Calculator.getFormula());
        StringBuilder sb = new StringBuilder(Calculator.getAnswer());
        if (!Calculator.getAnswer().equals("0で除算することはできません。")) {
            int pos = sb.indexOf(".");
            if (pos == -1) {
                pos = sb.length();
            }
            pos -= 3; //初期位置調整
            for (int i = pos; i > 0; i -= 3) {
                sb.insert(i, ",");
            }
        }
        labelList.get("answer").setText(sb.toString());
        setFocusable(true);
        requestFocusInWindow();
    }
}
