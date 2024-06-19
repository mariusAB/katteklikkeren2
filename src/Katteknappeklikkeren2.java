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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: fjern blit når man starter spillet
// TODO: fiks størrelsesforhold på bilder

public class Katteknappeklikkeren2 extends JFrame{
    private CardLayout scenes = new CardLayout();
    private int width = 1200;
    private int height = 800;
    private String currentScene = "Menu";
    private JPanel menu = new JPanel();
    private JPanel settings = new JPanel();
    private JPanel stats = new JPanel();
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
    private int i = 0;
    private Image img;
    private List<Thing> things = new ArrayList<>();

    public Katteknappeklikkeren2(int width, int heigth) {
        try {
            backgroundImage = ImageIO.read(new File("img/mainscreen.png"));
            setIconImage(ImageIO.read(new File("img/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        panel.add(game, "Game");
        panel.add(menu, "Menu");
        panel.add(settings, "Settings");
        panel.add(stats, "Stats");
        scenes.show(panel, "Menu");
    }

    

    public void start() {
        Logic l = new Logic(this);
        i = 0;
        changeBackground("img/room1.png");
        Timer timer = new Timer(1000 / 200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                things.clear();
                l.tick();
                i++;
                //((Timer) e.getSource()).stop(); // TODO: stop timer
            }
        });
        timer.start();
        Set<Integer> pressedKeys = new HashSet<>();

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
            }
        });
        this.requestFocusInWindow();
    }

    public void changeBackground(String path) {
        try {
            roomImg = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImage(Thing t) {
        try {
            img = ImageIO.read(new File(t.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        things.add(t);
        class Item extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image scaledImage = roomImg.getScaledInstance(game.getWidth(), game.getHeight(), Image.SCALE_SMOOTH);
                g.drawImage(scaledImage, 0, 0, this);
                for (int i = 0; i < things.size(); i++) {
                    g.drawImage(img, things.get(i).getX(), things.get(i).getY(), this);
                }
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(game.getWidth(), game.getHeight());
            }
        }
        Item item = new Item();
        item.setOpaque(false);
        game.removeAll();
        game.setLayout(new BorderLayout());
        game.add(item, BorderLayout.CENTER);
        game.revalidate();
        game.repaint();
    }

    public void resized() {
        int prevWidth = width;
        int prevHeight = height;
        width = this.getWidth();
        height = this.getHeight();

        if (currentScene.equals("Menu") || currentScene.equals("Stats") || currentScene.equals("Settings")) {
            for (Component c : menu.getComponents()) {
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
