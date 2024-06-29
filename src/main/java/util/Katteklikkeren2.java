package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import things.Enemy;
import things.Main;
import things.Thing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Katteklikkeren2 extends JFrame {
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
    private Logic l;
    private boolean hasRun = false;
    private int roomWidth;
    private int roomHeight;
    private boolean displayMiniMap = false;
    private List<Integer> miniMap;
    private JLabel monKil;
    private SaveFileReader saveFileReader = new SaveFileReader();
    private ImageHandler imageHandler = new ImageHandler();
    private boolean miniMapRefreshRequired = true;
    private int fps = 120;
    private GameThread loop;

    public Katteklikkeren2(int width, int heigth) {
        try {
            backgroundImage = ImageIO.read(new File("src/resources/img/mainscreen.png"));
            setIconImage(ImageIO.read(new File("src/resources/img/magicStaffCatR.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            saveFileReader.readSaveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        monKilInt = saveFileReader.getMonKil();
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
        l = new Logic(this);
        if (loop != null) {
            loop.stopRunning();
        }
        loop = new GameThread(l, fps);
        l.toMid();
        loop.start();

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
                    if (e.getKeyCode() == KeyEvent.VK_E) {
                        l.interact();
                    }
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
                l.mouseClicked(translateMouseX(e.getX()), translateMouseY(e.getY()-30));
                l.mouseHeld(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                l.mouseHeld(false);
            }
            
            });
            hasRun = true;
        }
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                l.mouseClicked(translateMouseX(e.getX()), translateMouseY(e.getY()-30));
            }
        });
        this.requestFocusInWindow();
    }

    private int translateMouseX(int x) {
        return x * roomWidth / game.getWidth();
    }

    private int translateMouseY(int y) {
        return y * roomHeight / game.getHeight();
    }

    public void displayMiniMap(List<Integer> miniMap) {
        this.miniMap = miniMap;
        displayMiniMap = true;
    }

    public void hideMiniMap() {
        displayMiniMap = false;
    }

    public void pause() {
        scenes.show(this.getContentPane(), "Pause");
        currentScene = "Pause";
        loop.pause();
    }

    public void unPause() {
        scenes.show(this.getContentPane(), "Game");
        currentScene = "Game";
        this.requestFocusInWindow();
        loop.unPause();
    }

    public void win() {
        resetGame();
        scenes.show(this.getContentPane(), "Win");
        currentScene = "Win";
        loop.stopRunning();
    }

    public void changeBackground(Image img) {
        if (roomImg != null) {
            roomImg.flush();
        }
        roomImg = img.getScaledInstance(game.getWidth(), game.getHeight(), Image.SCALE_SMOOTH);
    }

    public void repaint() {
        item.repaint();
        
    }

    public void update(Graphics g) {
        paint(g);
        l.tick();
    }
    
    private int translateGameX(Thing t) {
        roomWidth = t.getRoomWidth();
        return t.getX() * game.getWidth() / roomWidth;
    }

    private int translateGameY(Thing t) {
        roomHeight = t.getRoomHeight();
        return t.getY() * game.getHeight() / roomHeight;
    }

    public void updateMiniMap() {
        miniMapRefreshRequired = true;
    }

    class Item extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(roomImg, 0, 0, null);
            for (Thing thing : l.getThings()) {
                Image img = thing.getImg(game.getWidth(), game.getHeight());
                if (img == null) {
                    break;
                }

                int imgWidth = img.getWidth(null);
                int imgHeight = img.getHeight(null);
                int centerX = translateGameX(thing) - imgWidth / 2;
                int centerY = translateGameY(thing) - imgHeight / 2;
                
                AffineTransform old = g2d.getTransform();
                g2d.rotate(Math.toRadians(thing.getRotation()), translateGameX(thing), translateGameY(thing));
                g2d.drawImage(img, centerX, centerY, this);
                g2d.setTransform(old);

                if (thing instanceof Main) {
                    int healthBarHeight = 5;
                    double healthPercentage = ((Main) thing).getHealth() / (double) ((Main) thing).getMaxHealth();
                    int healthBarWidth = (int) (imgWidth * healthPercentage);
                    int healthBarX = centerX;
                    int healthBarY = centerY + imgHeight + 5;
                    g2d.setColor(Color.RED);
                    g2d.fillRect(healthBarX, healthBarY, imgWidth, healthBarHeight);
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
                    g2d.setColor(Color.BLACK);
                }
                if (thing instanceof Enemy) {
                    int healthBarHeight = 5;
                    double healthPercentage = ((Enemy) thing).getHealth() / (double) ((Enemy) thing).getMaxHealth();
                    int healthBarWidth = (int) (imgWidth * healthPercentage);
                    int healthBarX = centerX;
                    int healthBarY = centerY + imgHeight + 5;
                    g2d.setColor(Color.YELLOW);
                    g2d.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
                    g2d.setColor(Color.BLACK);
                }
            }
            if (displayMiniMap) {
                g2d.drawImage(drawMiniMap(), 0, 0, null);
            }
        }

        private BufferedImage drawMiniMap() {
            BufferedImage bufferedImage = imageHandler.getMiniMap();
            if (imageHandler.getMiniMap() == null || miniMapRefreshRequired) {
                bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                int miniMapWidth = 30;
                int miniMapHeight = 30;
                int margin = 10;
                int tileWidth = l.getMapWidth();
                double transp = 0.8;
                int startX = getWidth() - (tileWidth * miniMapWidth) - margin;
                int startY = margin;
                g2d.setColor(new Color(255, 255, 255, (int) (255 * 0.9)));
                g2d.fillRect(startX - margin, startY, (tileWidth * miniMapWidth) + (margin), (miniMapHeight * (miniMap.size() / tileWidth)) + (margin));
            
                for (int i = 0; i < miniMap.size(); i++) {
                    int x = i % tileWidth;
                    int y = i / tileWidth;
                    if (miniMap.get(i) == 1) {
                        g2d.setColor(new Color(0, 0, 0, (int) (255 * transp)));
                    } else if (miniMap.get(i) == 2) {
                        g2d.setColor(new Color(128, 0, 0, (int) (255 * transp)));
                    } else if (miniMap.get(i) == 3) {
                        g2d.setColor(new Color(122, 121, 91, (int) (255 * transp)));
                    } else {
                        g2d.setColor(new Color(188, 181, 151, (int) (255 * transp)));
                    }
                    g2d.fillRect(startX + x * miniMapWidth, startY + margin + y * miniMapHeight, miniMapWidth - margin, miniMapHeight - margin);
                }
                g2d.dispose();
                imageHandler.setMiniMap(bufferedImage);
                miniMapRefreshRequired = false;
            }
            return bufferedImage;
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
            l.resized();
        }
    }

    public void gameOver() {
        resetGame();
        scenes.show(this.getContentPane(), "GameOver");
        currentScene = "GameOver";
        loop.stopRunning();
    }

    private void resetGame() {
        miniMapRefreshRequired = true;
        monKilInt += l.getKillCounter();
        try {
            saveFileReader.writeSaveFile(monKilInt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateMonKilLabel();
        l = new Logic(this);
    }

    private void menu(JPanel menu, JPanel panel) {
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
    
        JLabel title = new JLabel("Katteklikkeren 2");
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
                    gd.setFullScreenWindow(Katteklikkeren2.this);
                } else {
                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    gd.setFullScreenWindow(null);
                    Katteklikkeren2.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        monKil = new JLabel("Monsters killed: " + monKilInt);
        monKil.setFont(new Font("Rockwell", Font.PLAIN, 25));
        monKil.setAlignmentX(Component.CENTER_ALIGNMENT);
        monKil.setBorder(b);
        s.add(monKil);
        s.setOpaque(false);
    }

    private void updateMonKilLabel() {
        monKil.setText("Monsters killed: " + monKilInt);
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
                resetGame();
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
        ImageIcon buttonIcon = new ImageIcon("src/resources/img/back.png");
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
                Katteklikkeren2 frame = new Katteklikkeren2(width, height);
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
