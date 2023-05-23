import javax.xml.bind.ValidationException;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class Draw extends Canvas implements MouseWheelListener, KeyListener {



    private Vector<Vector<Long>> map = new Vector<>();
    private Vector<Vector<Boolean>> primes = new Vector<>();
    private int mouseX, mouseY;
    private int pX, pY;

    private boolean needsUpdate = true;

    private long lastUpdateKeys = 0;

    private boolean colorMap = false;

    private Camera camera = new Camera(0.0f, 0.0f, 1.0f);

    private int[] colors = {
            0x5f3a3a,
            0x34211a,
            0x483328,
            0x5c4333,
            0x656351,
            0x98774c,
            0xb38a52,
            0xc19f56,
            0xc3c3c3,
            0xffffff
};


    Draw(Vector<Vector<Long>> map) {
        mouseX = 0;
        mouseY = 0;
        setPreferredSize(new Dimension(800, 805));
        this.map = map;
        needsUpdate = true;
        addMouseWheelListener(this);
        addKeyListener(this);
        repaint();
    }

    public void setColorMap(boolean colorMap) {
        this.colorMap = colorMap;
    }

    public void updateMap(Vector<Vector<Long>> map) {
        this.map = map;
        needsUpdate = true;
        this.repaint();
    }


    @Override
    public void paint(Graphics g) {
        Image buffer = createImage(getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) buffer.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.translate(camera.getCameraX(), camera.getCameraY());
        g2d.scale(camera.getZoomLevel(), camera.getZoomLevel());

        g2d.setColor(Color.BLACK);

        if(needsUpdate) {
            primes = Ulam.checkForPrimary(map);
            needsUpdate = false;
        }

        if(!colorMap) {
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
        } else {
            for (int y = 0; y < map.size(); y++) {
                for (int x = 0; x < map.get(y).size(); x++) {
                    g2d.setColor(new Color(colors[Utils.getDigitLeft(map.get(y).get(x), 0)]));
                    g2d.drawLine(x, y, x, y);
                }
            }
        }

        g.drawImage(buffer, 0, 0, null);
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


        if(System.currentTimeMillis() - lastUpdateKeys < 225) {return;}

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

        repaint();

        lastUpdateKeys = System.currentTimeMillis();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
