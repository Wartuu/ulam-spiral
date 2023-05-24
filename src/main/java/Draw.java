import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

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

        addMouseWheelListener(this);
        addKeyListener(this);
        repaint();
    }

    public void setColorMap(boolean colorMap) {
        this.colorMap = colorMap;
        repaint();
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




        for (int y = 0; y < primes.size(); y++) {
            for (int x = 0; x < primes.get(y).size(); x++) {
                if(primes.get(y).get(x)) {
                    g2d.setColor(Color.LIGHT_GRAY);
                } else {
                    g2d.setColor(Color.DARK_GRAY);
                }

                g2d.drawLine(x, y, x, y);
            }
        }

        g2d = (Graphics2D) bufferColors.getGraphics();

        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).size(); x++) {
                g2d.setColor(colors[Utils.getDigitLeft(map.get(y).get(x), 0) - 1]);

                g2d.drawLine(x, y, x, y);
            }
        }

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(bg);
        g.fillRect(0, 0, getWidth(), getHeight());
        ((Graphics2D) g).translate(camera.getCameraX(), camera.getCameraY());
        ((Graphics2D) g).scale(camera.getZoomLevel(), camera.getZoomLevel());

        if(colorMap) {
            g.drawImage(bufferColors, 0, 0, null);
        } else {
            g.drawImage(bufferPrimes, 0, 0, null);
        }

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        if(e.getWheelRotation() < 0) {
            camera.setZoomLevel(camera.getZoomLevel() * 1.1f);
        } else {
            camera.setZoomLevel(camera.getZoomLevel() / 1.1f);
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


        if(System.currentTimeMillis() - lastUpdateKeys < 1) {return;}

        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            camera.setCameraX(camera.getCameraX() - 7f);
        } else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            camera.setCameraX(camera.getCameraX() + 7f);
        } else if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            camera.setCameraY(camera.getCameraY() - 7f);
        } else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            camera.setCameraY(camera.getCameraY() + 7f);
        }

        lastUpdateKeys = System.currentTimeMillis();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
