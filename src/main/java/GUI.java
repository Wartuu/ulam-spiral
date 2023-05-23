import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class GUI {
    private final JFrame jFrame = new JFrame("ulam-spiral");
    private final Layout layout = new Layout();
    private final ButtonGroup buttonGroup = new ButtonGroup();

    public GUI() {
        jFrame.setContentPane(layout.getRoot());
        jFrame.setSize(800, 900);
        layout.getInputNumber().setHorizontalAlignment(SwingConstants.CENTER);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layout.getGrid().add(new Draw(Ulam.generate2DSquare(1)));

        if(layout.getGrid() == null) {
            System.out.println("wtf");
        }

        buttonGroup.add(layout.getColorMapEnable());
        buttonGroup.add(layout.getPrimeEnable());


        SwingUtilities.invokeLater(()->{

            layout.getGenerate().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int size = Integer.parseInt(layout.getInputNumber().getText());
                    for (Component c : layout.getGrid().getComponents()) {
                        if(c instanceof Draw) {
                            boolean colorMap = layout.getColorMapEnable().isSelected();


                            ((Draw) c).setColorMap(colorMap);
                            ((Draw) c).updateMap(Ulam.generate2DSquare(size));
                            c.repaint();


                            System.out.println("Found canvas!");
                        }
                    }


                    Vector<Vector<Boolean>> primes = Ulam.checkForPrimary(Ulam.generate2DSquare(size));
                    long counter = 0;
                    long sizePrimes = (long) primes.size() *primes.size();

                    for (Vector<Boolean> y : primes) {
                        for (boolean x : y) {
                            if(x) {
                                counter++;
                            }
                        }
                    }


                    layout.getGenerate().setText("generate | " + counter + "/" + sizePrimes + " (last generated)");
                }
            });
        });



    }
}
