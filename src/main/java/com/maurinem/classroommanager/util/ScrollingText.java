package com.maurinem.classroommanager.util;

import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class ScrollingText extends JLabel {

    /** */
    private static final long serialVersionUID = 1L;

    private int speed, period, offset, x;

    public ScrollingText(String text) {
        this(text, 1);
    }

    public ScrollingText(String text, int speed) {
        this(text, speed, 100);
    }

    public ScrollingText(String text, int speed, int period) {
        this(text, speed, period, 0);
    }

    public ScrollingText(String text, int speed, int period, int offset) {
        super(text);
        this.speed = speed;
        this.period = period;
        this.offset = offset;
    }

    public void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setColor(getForeground());

        FontMetrics fm = g.getFontMetrics();
        Insets insets = getInsets();

        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);

        int textWidth = fm.stringWidth(getText());
        if (width < textWidth) {
            width = textWidth + offset;
        }
        x %= width;

        int textX = insets.left + x;
        int textY = insets.top + (height - fm.getHeight()) / 2 + fm.getAscent();

        g.drawString(getText(), textX, textY);
        g.drawString(getText(), textX + (speed > 0 ? -width : width), textY);
    }

    public void start() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                x += speed;
                repaint();
            }
        };
        timer.scheduleAtFixedRate(task, 0, period);
    }

    // public static void main(String[] args) {
    // JFrame frame = new JFrame();
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setSize(300, 200);
    // JButton quit = new JButton("Quitter");
    // quit.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // System.exit(0);
    // }
    // });
    // frame.getContentPane().add(quit);
    // ScrollingText scrollingText1 = new ScrollingText("Barre des tâches... Pressez
    // le bouton Quitter", -3);
    // scrollingText1.setBorder(BorderFactory.createEtchedBorder());
    // scrollingText1.start();
    // frame.getContentPane().add(scrollingText1, BorderLayout.NORTH);
    // ScrollingText scrollingText2 = new ScrollingText("Barre des tâches... Pressez
    // le bouton Quitter");
    // scrollingText2.setBorder(BorderFactory.createEtchedBorder());
    // scrollingText2.start();
    // scrollingText2.setBackground(Color.YELLOW);
    // scrollingText2.setOpaque(true);
    // frame.getContentPane().add(scrollingText2, BorderLayout.SOUTH);
    // frame.setVisible(true);
    // }
}