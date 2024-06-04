package UI.Componentes.Botones;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(String label, int cornerRadius) {
        super(label);
        this.cornerRadius = cornerRadius;
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // Si quieres mantener el borde y asegurarte de que respete el color correcto
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        g2.dispose();

        // Si no deseas un borde visible, puedes comentar o eliminar el contenido de este método
        // o simplemente hacer que no haga nada, así:
        // 
        // @Override
        // protected void paintBorder(Graphics g) {
        //     // No dibujar el borde.
        // }
    }
}
