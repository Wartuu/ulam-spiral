import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class Draw extends Canvas implements MouseWheelListener, KeyListener {



    private Vector<Vector<Long>> map = new Vector<>();
    private Vector<Vector<Boolean>> primes = new Vector<>();
    private int mouseX, mouseY;
    private int pX, pY;

    private boolean needsUpdate = true;

    private long lastUpdateKeys = 0;

    private boolean colorMap;

    private Camera camera = new Camera(0.0f, 0.0f, 1.0f);
    private BufferedImage bufferPrimes;
    private BufferedImage bufferColors;


    private boolean mr = false, ml = false, md = false, mu = false;

    private long sizeOfPrimes = 0;

    private double dt;
    private long lastTime = System.nanoTime();
    private double targetFPS = 30.0;
    private double targetTime = 1000000000 / targetFPS;
    private double deltaTime = 0.0f;
    private boolean drawing = false;

    private Color[] colors = {
            new Color(0x5f3a3a),
            new Color(0x34211a),
            new Color(0x483328),
            new Color(0x5c4333),
            new Color(0x656351),
            new Color(0x98774c),
            new Color(0xb38a52),
            new Color(0xc19f56),
            new Color(0xc3c3c3),
            new Color(0xffffff)
};

    private Color bg = new Color(0x303030);




    Draw(Vector<Vector<Long>> map) {
        mouseX = 0;
        mouseY = 0;
        setPreferredSize(new Dimension(800, 805));
        this.map = map;
        colorMap = false;
        needsUpdate = true;
        bufferPrimes = (BufferedImage) createImage(getWidth(), getHeight());

        new Thread(this::cameraThread).start();
        sizeOfPrimes = 0;

        addMouseWheelListener(this);
        addKeyListener(this);
        repaint();
    }

    public void setColorMap(boolean colorMap) {
        this.colorMap = colorMap;
        repaint();
    }

    private void cameraThread() {
        while (true) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            lastTime = now;

            dt += elapsedTime / targetTime;

            while (dt >= 1) {
                deltaTime = 1.0f / targetFPS;
                dt--;
            }



            boolean updated = false;

            if(ml) {camera.setCameraX((float) (camera.getCameraX() + (0.3f * deltaTime) / camera.getZoomLevel())); updated = true;}
            if(mr) {camera.setCameraX((float) (camera.getCameraX() - (0.3f * deltaTime) / camera.getZoomLevel())); updated = true;}
            if(md) {camera.setCameraY((float) (camera.getCameraY() - (0.3f * deltaTime) / camera.getZoomLevel())); updated = true;}
            if(mu) {camera.setCameraY((float) (camera.getCameraY() + (0.3f * deltaTime) / camera.getZoomLevel())); updated = true;}

            System.out.println(camera.getCameraX() + " | " + camera.getCameraY());


            if(updated) {
                repaint();
            }
        }
    }

    public void updateMap(Vector<Vector<Long>> map) {
        this.map = map;
        needsUpdate = true;

        updateBuffer();
        repaint();

    }


    public void updateBuffer() {
        if(needsUpdate) {
            primes = Ulam.checkForPrimary(map);
            needsUpdate = false;
        }

        if(bufferPrimes == null || bufferPrimes.getWidth(null) != getWidth() || bufferPrimes.getHeight(null) != getHeight()) {
            bufferPrimes = (BufferedImage) createImage(map.size(), map.size());
        }

        if(bufferColors == null || bufferColors.getWidth(null) != getWidth() || bufferColors.getHeight(null) != getHeight()) {
            bufferColors = (BufferedImage) createImage(map.size(), map.size());
        }



        Graphics2D g2d = (Graphics2D) bufferPrimes.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRect(0, 0, primes.size(), primes.size());


        AtomicLong sPrimes = new AtomicLong();


        Thread beginPrimes = new Thread(()-> {
            Graphics2D g2dpb = bufferPrimes.createGraphics();

            for (int y = 0; y < primes.size()/2; y++) {
                for (int x = 0; x < primes.get(y).size(); x++) {
                    if(primes.get(y).get(x)) {
                        sPrimes.getAndIncrement();
                        g2dpb.setColor(Color.LIGHT_GRAY);
                    } else {
                        g2dpb.setColor(Color.DARK_GRAY);
                    }
                    g2dpb.drawLine(x, y, x, y);
                }
            }
        });

        Thread endPrimes = new Thread(()-> {
            Graphics2D g2dpe = bufferPrimes.createGraphics();
            for (int y = primes.size()/2; y < primes.size(); y++) {
                for (int x = 0; x < primes.get(y).size(); x++) {
                    if(primes.get(y).get(x)) {
                        sPrimes.getAndIncrement();
                        g2dpe.setColor(Color.LIGHT_GRAY);
                    } else {
                        g2dpe.setColor(Color.DARK_GRAY);
                    }

                    g2dpe.drawLine(x, y, x, y);
                }
            }
        });


        Thread beginColorMap = new Thread(()-> {
            Graphics2D g2dcb = bufferColors.createGraphics();
            for (int y = 0; y < map.size()/2; y++) {
                for (int x = 0; x < map.get(y).size(); x++) {
                    g2dcb.setColor(colors[Utils.getDigitLeft(map.get(y).get(x), 0)]);
                    g2dcb.drawLine(x, y, x, y);
                }
            }
        });

        Thread endColorMap = new Thread(()-> {
            Graphics2D g2dce = bufferColors.createGraphics();

            for (int y = map.size()/2; y < map.size(); y++) {
                for (int x = 0; x < map.get(y).size(); x++) {
                    g2dce.setColor(colors[Utils.getDigitLeft(map.get(y).get(x), 0)]);
                    g2dce.drawLine(x, y, x, y);
                }
            }
        });

        beginPrimes.start();
        endPrimes.start();
        beginColorMap.start();
        endColorMap.start();

        try {
            beginPrimes.join();
            endPrimes.join();
            beginColorMap.join();
            endColorMap.join();
        } catch (Exception e) {
            e.printStackTrace();
        }


        sizeOfPrimes = sPrimes.get();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        drawing = true;
        g.setColor(bg);
        g.fillRect(0, 0, getWidth(), getHeight());

        ((Graphics2D) g).scale(camera.getZoomLevel(), camera.getZoomLevel());
        ((Graphics2D) g).translate(camera.getCameraX(), camera.getCameraY());

        if(colorMap) {
            g.drawImage(bufferColors, 0, 0, null);
        } else {
            g.drawImage(bufferPrimes, 0, 0, null);
        }



        Toolkit.getDefaultToolkit().sync();
        drawing = false;

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0) {
            float value = camera.getZoomLevel() * 1.1f;
            camera.setZoomLevel(value);
        } else {
            float value = camera.getZoomLevel() / 1.1f;
            camera.setZoomLevel(value);
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            ml = true;
        }
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            mr = true;
        }

        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            mu = true;
        }
        if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            md = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            ml = false;
        } else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            mr = false;
        }
        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            mu = false;
        } else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            md = false;
        }
    }

    public long getSizeOfPrimes() {
        return sizeOfPrimes;
    }
}
