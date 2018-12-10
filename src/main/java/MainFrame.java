import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    public DatabaseHandler dbHandler;
    public Color buttonColor = new Color(204, 204, 204);

    public MainFrame(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        this.setTitle("Example Frame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(null);

        container.add(new ParcelsPanel(this));

        this.setSize(900, 600);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
