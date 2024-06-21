package util;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Katteknappeklikkeren2 extends JFrame{
    private CardLayout scenes = new CardLayout();
    private int width = 1200;
    private int height = 800;
    private String currentScene = "Menu";
    private JPanel menu = new JPanel();
    private JPanel settings = new JPanel();
    private JPanel stats = new JPanel();
    private JPanel gameOver = new JPanel();
    private JPanel win = new JPanel();
    private JPanel pause = new JPanel();
    private List<JPanel> panels = new ArrayList<>();
    private Item item = new Item();
    private JPanel game = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    };
    private Image backgroundImage;
    private int monKilInt = 0;
    private Image roomImg;
    private List<Thing> things = new ArrayList<>();
    private Logic l = new Logic(this);
    private String backPath;
    private boolean hasRun = false;
    private Timer timer;

    public Katteknappeklikkeren2(int width, int heigth) {
        try {
            backgroundImage = ImageIO.read(new File("img/mainscreen.png"));
            setIconImage(ImageIO.read(new File("img/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        panels.add(menu);
        panels.add(settings);
        panels.add(stats);
        panels.add(gameOver);
        panels.add(win);
        panels.add(pause);

        item.setOpaque(false);
        game.setLayout(new BorderLayout());
        game.add(item, BorderLayout.CENTER);
        this.width = width;
        this.height = heigth;
        setSize(width, height);
        setVisible(true);
        JPanel panel = new JPanel(scenes) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(panel);
        panel.setVisible(true);
        
        menu(menu, panel);
        settings(settings, panel);
        stats(stats, panel);
        gameOver(gameOver, panel);
        win(win, panel);
        pause(pause, panel);
        panel.add(game, "Game");
        panel.add(menu, "Menu");
        panel.add(settings, "Settings");
        panel.add(stats, "Stats");
        panel.add(gameOver, "GameOver");
        panel.add(win, "Win");
        panel.add(pause, "Pause");
        scenes.show(panel, "Menu");
    }

    

    public void start() {
        changeBackground("img/room1.png");
        l.toMid();


        timer = new Timer(1000 / 200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                things.clear();
                l.tick();
                l.giveSource(e);
            }
        });
        timer.start();
        Set<Integer> pressedKeys = new HashSet<>();
        if (!hasRun) {
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    pressedKeys.add(e.getKeyCode());
                    l.keys(pressedKeys);
                    }
    
                @Override
                public void keyReleased(KeyEvent e) {
                    pressedKeys.remove(e.getKeyCode());
                    l.keys(pressedKeys);
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE && currentScene.equals("Game")) {
                        pause();
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && currentScene.equals("Pause")) {
                        unPause();
                    }
                }
            });
    
            this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                l.mouseClicked(e.getX(), e.getY()-15*1713/getHeight());
            }
            });
            hasRun = true;
        }
        this.requestFocusInWindow();
    }

    public void pause() {
        scenes.show(this.getContentPane(), "Pause");
        currentScene = "Pause";
        timer.stop();
    }

    public void unPause() {
        scenes.show(this.getContentPane(), "Game");
        currentScene = "Game";
        timer.start();
        this.requestFocusInWindow();
    }

    public void win(ActionEvent e) {
        resetGame(e);
        scenes.show(this.getContentPane(), "Win");
        currentScene = "Win";
    }

    public void changeBackground(String path) {
        try {
            backPath = path;
            roomImg = ImageIO.read(new File(path));
            roomImg = roomImg.getScaledInstance(game.getWidth(), game.getHeight(), Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImage(Thing t) {
        things.add(t);
        item.repaint();
    }

    class Item extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(roomImg, 0, 0, this);
            for (int i = 0; i < things.size(); i++) {
                Image img = things.get(i).getImg(game.getWidth(), game.getHeight());
                int imgWidth = img.getWidth(null);
                int imgHeight = img.getHeight(null);
                int centerX = things.get(i).getX() - imgWidth / 2;
                int centerY = things.get(i).getY() - imgHeight / 2;
                
                AffineTransform old = g2d.getTransform();
                g2d.rotate(Math.toRadians(things.get(i).getRotation()), things.get(i).getX(), things.get(i).getY());
                g2d.drawImage(img, centerX, centerY, this);
                g2d.setTransform(old);
            }
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(game.getWidth(), game.getHeight());
        }
    }

    public void resized() {
        int prevWidth = width;
        int prevHeight = height;
        width = this.getWidth();
        height = this.getHeight();

        if (!currentScene.equals("Game")) {
            for (JPanel p : panels) {
                for (Component c : p.getComponents()) {
                    if (c instanceof JLabel) {
                        Font f = c.getFont();
                        int num = (int) prevWidth*prevHeight / f.getSize();
                        c.setFont(f.deriveFont((float) width*height/num));
                        ((JComponent) c).setBorder(new EmptyBorder(width*height / num, 0, width*height / num, 0));
                    }
                    else {
                        Font f = c.getFont();
                        int num = (int) prevWidth*prevHeight / f.getSize();
                        c.setFont(f.deriveFont((float) width*height/num));
                    }
                }
            }
        }
        else {
            l.resize(prevWidth, prevHeight, width, height);
            try {
                roomImg = ImageIO.read(new File(backPath));
                roomImg = roomImg.getScaledInstance(game.getWidth(), game.getHeight(), Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            game.revalidate();
            game.repaint();
        }
    }

    public void gameOver(ActionEvent e) {
        resetGame(e);
        scenes.show(this.getContentPane(), "GameOver");
        currentScene = "GameOver";
    }

    private void resetGame(ActionEvent e) {
        l = new Logic(this);
        ((Timer) e.getSource()).stop();
    }

    private void menu(JPanel menu, JPanel panel) {
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
    
        JLabel title = new JLabel("Katteknappeklikkeren 2");
        title.setFont(new Font("Rockwell", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        EmptyBorder b = new EmptyBorder(15, 0, 30, 0);
        title.setBorder(b);
        menu.add(title);
    
        JButton startButton = new JButton("Start Game");
        formatButton(startButton);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(panel, "Game");
                currentScene = "Game";
                start();
            }
        });
        menu.add(startButton);
    
        JButton settingsButton = new JButton("Settings");
        formatButton(settingsButton);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(panel, "Settings");
                currentScene = "Settings";
            }
        });
        menu.add(settingsButton);
    
        JButton statsButton = new JButton("Stats");
        formatButton(statsButton);
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(panel, "Stats");
                currentScene = "Stats";
            }
        });
        menu.add(statsButton);

        JButton exitButton = new JButton("Exit game");
        formatButton(exitButton);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(ABORT);
            }
        });
        menu.add(exitButton);
        menu.setOpaque(false);
    }

    private void settings(JPanel s, JPanel p) {
        s.setLayout(new BoxLayout(s, BoxLayout.Y_AXIS));
    
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Rockwell", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        EmptyBorder b = new EmptyBorder(15, 0, 30, 0);
        title.setBorder(b);
        s.add(title);

        JButton sButton = new JButton("");
        formatButton(sButton, 1);
        sButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(p, "Menu");
                currentScene = "Menu";
            }
        });
        s.add(sButton);

        JToggleButton fullScreen = new JToggleButton("Toggle fullscreen");
        formatButton(fullScreen);
        fullScreen.setAlignmentX(Component.CENTER_ALIGNMENT);
        fullScreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fullScreen.isSelected()) {
                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    gd.setFullScreenWindow(Katteknappeklikkeren2.this);
                } else {
                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    gd.setFullScreenWindow(null);
                    Katteknappeklikkeren2.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }
        });
        s.add(fullScreen);
        s.setOpaque(false);
    }

    private void stats(JPanel s, JPanel p) {
        s.setLayout(new BoxLayout(s, BoxLayout.Y_AXIS));
    
        JLabel title = new JLabel("Stats");
        title.setFont(new Font("Rockwell", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        EmptyBorder b = new EmptyBorder(15, 0, 30, 0);
        title.setBorder(b);
        s.add(title);

        JButton statsButton = new JButton("");
        formatButton(statsButton, 1);
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(p, "Menu");
                currentScene = "Menu";
            }
        });
        s.add(statsButton);

        JLabel monKil = new JLabel("Monsters killed: " + monKilInt);
        monKil.setFont(new Font("Rockwell", Font.PLAIN, 25));
        monKil.setAlignmentX(Component.CENTER_ALIGNMENT);
        monKil.setBorder(b);
        s.add(monKil);
        s.setOpaque(false);
    }

    private void gameOver(JPanel go, JPanel p) {
        go.setLayout(new BoxLayout(go, BoxLayout.Y_AXIS));
    
        JLabel title = new JLabel("Game Over");
        title.setFont(new Font("Rockwell", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        EmptyBorder b = new EmptyBorder(15, 0, 30, 0);
        title.setBorder(b);
        go.add(title);

        JButton goButton = new JButton("");
        formatButton(goButton, 1);
        goButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(p, "Menu");
                currentScene = "Menu";
            }
        });
        go.add(goButton);
        go.setOpaque(false);
    }

    private void win(JPanel w, JPanel p) {
        w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS));
    
        JLabel title = new JLabel("You win!");
        title.setFont(new Font("Rockwell", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        EmptyBorder b = new EmptyBorder(15, 0, 30, 0);
        title.setBorder(b);
        w.add(title);

        JButton wButton = new JButton("");
        formatButton(wButton, 1);
        wButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        wButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(p, "Menu");
                currentScene = "Menu";
            }
        });
        w.add(wButton);
        w.setOpaque(false);
    }

    private void pause(JPanel p, JPanel panel) {
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    
        JLabel title = new JLabel("Paused");
        title.setFont(new Font("Rockwell", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        EmptyBorder b = new EmptyBorder(15, 0, 30, 0);
        title.setBorder(b);
        p.add(title);

        JButton pButton = new JButton("");
        formatButton(pButton, 1);
        pButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unPause();
            }
        });
        p.add(pButton);

        JLabel space = new JLabel("");
        space.setFont(new Font("Rockwell", Font.BOLD, 10));
        space.setAlignmentX(Component.CENTER_ALIGNMENT);
        space.setBorder(b);
        p.add(space);
        
        JButton menuButton = new JButton("Main menu (resets run)");
        formatButton(menuButton);
        menuButton.setFont(new Font("Rockwell", Font.PLAIN, 20));
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenes.show(panel, "Menu");
                currentScene = "Menu";
                resetGame(e);
            }
        });
        p.add(menuButton);

        p.setOpaque(false);
    }

    private void formatButton(JButton b) {
        JLabel empty = new JLabel("");
        empty.setBorder(new EmptyBorder(15,0,15,0));
        menu.add(empty);
        b.setFont(new Font("Rockwell", Font.PLAIN, 35));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void formatButton(JButton b, int i) {
        JLabel empty = new JLabel("");
        empty.setBorder(new EmptyBorder(15,0,15,0));
        menu.add(empty);
        b.setFont(new Font("Rockwell", Font.BOLD, 25));
        ImageIcon buttonIcon = new ImageIcon("img/back.png");
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setIcon(buttonIcon);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void formatButton(JToggleButton b) {
        JLabel empty = new JLabel("");
        empty.setBorder(new EmptyBorder(15,0,15,0));
        menu.add(empty);
        b.setFont(new Font("Rockwell", Font.PLAIN, 25));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int width = 1200;
                int height = 800;
                Katteknappeklikkeren2 frame = new Katteknappeklikkeren2(width, height);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent componentEvent) {
                        frame.resized();
                    }
                });
            }
        });
    }
}
