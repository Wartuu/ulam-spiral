import javax.swing.*;

public class Layout {
    private JButton generate;
    private JLabel size;
    private JPanel grid;
    private JPanel root;
    private JTextField inputNumber;
    private JRadioButton colorMapEnable;
    private JRadioButton primeEnable;
    private JRadioButton squareMode;
    private JRadioButton spiralMode;

    public JButton getGenerate() {
        return generate;
    }

    public JLabel getSize() {
        return size;
    }

    public JPanel getGrid() {
        return grid;
    }

    public JPanel getRoot() {
        return root;
    }

    public JTextField getInputNumber() {
        return inputNumber;
    }

    private void createUIComponents() {
        return;
    }

    public JRadioButton getColorMapEnable() {
        return colorMapEnable;
    }

    public JRadioButton getPrimeEnable() {
        return primeEnable;
    }

    public JRadioButton getSquareMode() {
        return squareMode;
    }

    public JRadioButton getSpiralMode() {
        return spiralMode;
    }
}
