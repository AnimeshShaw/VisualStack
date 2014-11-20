package com.rawcoders.visualstack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import com.rawcoders.JStack.JStack;

/**
 * @author psychocoder
 *
 */
public class VisualStack extends JApplet implements Runnable, ActionListener {

    private static final long serialVersionUID = 1L;
    private int WIDTH = 800;
    private int HEIGHT = 640;
    private JScrollPane sc2, sc3;
    private JButton run;
    private JTextPane commands;
    private JSplitPane sp;
    private JPanel panel, mainPanel, sidebar;
    private JTextArea log;
    private JLabel label;
    private MyScrollPane msp;

    private Thread thread;
    private int DELAY = 150;
    private final int cellHeight = 30; // For drawing the stack.
    private final int cellWidth = 300; // How wide to plot pink rectangles
    private final int cellGap = 6; //vertical space between successive cells
    private final int topMargin = 25; // Space above top of stack.
    private final int fontSize = 18; // Height of font for displaying stack
    // elements.
    private final int leftMargin = 40; // x value for left side of cells
    private final int leftOffset = 100; // space between left side of cell and
    // contents string.
    private JStack<String> stack;
    private int countIns = 0;
    private static String NEWLINE = "\n";
    private ArrayList<String> keywords;
    private Iterator<Object> itr;

    /*
     * Class that in which the stack is painted
     */
    class PaintPanel extends JPanel {

        private static final long serialVersionUID = -1218462339209060866L;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setFont(new Font("Helveltica", Font.BOLD, 22));
            g.setColor(Color.red);
            g.drawString("Top of Stack", 20, 20);
            int ystart = stack.size() * (cellHeight + cellGap) + topMargin;
            int ycentering = (cellHeight - fontSize) / 2;
            int ypos = ystart;
            java.util.List<Object> lst = Arrays.asList(stack.elements());
            Collections.reverse(lst);
            itr = lst.iterator();
            g.setFont(new Font("Helveltica", Font.BOLD, fontSize));
            while (itr.hasNext()) {
                String elt = (String) itr.next();
                g.setColor(new Color(140, 34, 34));
                g.fillRect(leftMargin, ypos, cellWidth, cellHeight);
                g.setColor(new Color(234, 40, 20));
                g.drawString(elt, leftMargin + leftOffset, ypos + cellHeight
                        - ycentering);
                ypos -= (cellHeight + cellGap);
            }
        }
    }

    class MyScrollPane extends JScrollPane {

        private static final long serialVersionUID = -4445998126917591970L;

        MyScrollPane(JPanel pane) {
            super(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        }
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#init()
     */
    @Override
    public void init() {
        super.setSize(new Dimension(WIDTH, HEIGHT));
        /* 
         * Execute a job on the event-dispatching thread; creating this applet's
         * GUI.
         */
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }

    /**
     * Creates the GUI of the Applet
     */
    public void createGUI() {
        panel = new PaintPanel();
        panel.setPreferredSize(new Dimension(450, 900));
        panel.setBackground(new Color(25, 25, 25));

        msp = new MyScrollPane(panel);

        commands = new JTextPane();
        commands.setPreferredSize(new Dimension(450, 250));
        commands.setBackground(new Color(20, 20, 20));
        commands.setForeground(new Color(140, 244, 23, 150));

        sc2 = new JScrollPane(commands,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        TextLineNumber line = new TextLineNumber(commands);

        sc2.setRowHeaderView(line);

        sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, msp, sc2);
        sp.setDividerLocation(450);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sp);

        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, getHeight()));

        label = new JLabel("Logs");

        sidebar.add(label, BorderLayout.NORTH);

        log = new JTextArea();
        log.setPreferredSize(new Dimension(220, getHeight() + 1000));
        log.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.BLACK));
        log.setEditable(false);

        sc3 = new JScrollPane(log,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        TextLineNumber line2 = new TextLineNumber(log);
        sc3.setRowHeaderView(line2);

        sidebar.add(sc3, BorderLayout.CENTER);

        run = new JButton("Compile Code");
        run.setText("Compile Code");
        run.setActionCommand("COMPILE");
        run.addActionListener(this);
        run.setMnemonic(KeyEvent.VK_F6);
        run.setPreferredSize(new Dimension(getWidth(), 40));

        sidebar.add(run, BorderLayout.SOUTH);

        mainPanel.add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        stack = new JStack<>();

        /*
         * Defining the keywords
         */
        keywords = new ArrayList<>();
        keywords.add("push");
        keywords.add("delay");
        keywords.add("size");
        keywords.add("pop");
        keywords.add("top");

    }

    @Override
    public void run() {
        String text = commands.getText();
        String line = "";
        StringTokenizer linesCodes;

        for (linesCodes = new StringTokenizer(text, "\n\r\f"); linesCodes.hasMoreTokens();) {
            line = linesCodes.nextToken();
            log("Instruction to be parsed :" + line);
            try {
                parser(line);
                count();
            } catch (IllegalCharacterException e) {
                log(e.getMessage().toString());
            }
        }
        log("Lines processed : " + countIns);
        commands.setText(""); // Erase all the input line of codes.
        thread = null;
    }

    /**
     * Counts number of lines processed
     */
    private synchronized void count() {
        countIns++;
    }

    /**
     * Updates the panel according to the size of the stack
     */
    private void updatePanel() {
        panel.repaint();
        if (DELAY > 0) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param commands Parses the commands for the instructions
     * @throws IllegalCharacterException
     */
    private void parser(String commands) throws IllegalCharacterException {
        String temp, ins;
        String[] temp2;
        if (commands == null) {
            return;
        }
        temp = commands.trim().toLowerCase();
        temp2 = temp.split(" ");
        ins = temp2[0].trim();

        if (keywords.contains(ins)) {
            log("Keyword found :" + ins);
            switch (ins) {
                case "push":
                    String val = temp2[1].trim();
                    stack.push(val);
                    log("Element pushed :" + val);
                    updatePanel();
                    break;
                case "pop":
                    if (stack.isEmpty()) {
                        log("Stack is empty. Unable to pop the top element");
                        break;
                    }
                    log("Element poped :" + stack.peek());
                    stack.pop();
                    updatePanel();
                    break;
                case "delay":
                    DELAY = Integer.parseInt(temp2[1].trim());
                    log("The animation delayed by " + DELAY);
                    break;
                case "size":
                    log(String.valueOf(stack.size()));
                    break;
                case "top":
                    if (stack.isEmpty()) {
                        log("Stack is empty. Unable to show the top element");
                        break;
                    }
                    log("Top Element : " + stack.peek());
            }
        } else {
            throw new IllegalCharacterException(
                    "Unknown instruction or character found");
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("COMPILE")) {
            thread = new Thread(this);
            thread.start();
            log("Compiling...");
            return;
        }
    }

    /**
     * @param logs Used to store the logs and steps of execution.
     */
    public void log(String logs) {
        log.append((logs + NEWLINE));
    }
}
