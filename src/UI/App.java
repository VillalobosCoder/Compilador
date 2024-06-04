package UI;

import ArbolSintactico.Declarax;
import ArbolSintactico.Programax;
import Compilador.Parser;
import Compilador.Scanner;
import UI.Componentes.Botones.RoundedButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App extends JFrame {

    private JTextArea codeArea;
    private JTextArea resultArea;
    private JTextArea treeArea;
    private JTable symbolsTable;
    private JTextArea bytecodeArea;
    private DefaultTableModel symbolsTableModel;

    public App() {
        initUI();
    }

    private void initUI() {
        setTitle("Compilador [Alejandro Villalobos]");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLookAndFeel();
        setApplicationIcon();
        Font font = new Font("Open Sans", Font.PLAIN, 14);
        JPanel codePanel = createCodePanel(font);
        JScrollPane resultScrollPane = createResultArea(font);
        JScrollPane treeScrollPane = createTreeArea(font);
        JScrollPane symbolsScrollPane = createSymbolsTable(font);
        JScrollPane bytecodeScrollPane = createBytecodeArea(font);

        JTabbedPane tabbedPane = createTabbedPane(
                resultScrollPane,
                symbolsScrollPane,
                treeScrollPane,
                bytecodeScrollPane);
        JPanel sidePanel = createSidePanel(font);
        JSplitPane splitPane = createSplitPane(codePanel, tabbedPane);

        // Panel principal
        JPanel mainPanel = createMainPanel(sidePanel, splitPane);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setApplicationIcon() {
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("Componentes/Iconos/compilador.png"));
        Image img = imgIcon.getImage();
        setIconImage(img);
    }

    private JPanel createCodePanel(Font font) {
        JPanel codePanel = new JPanel(new BorderLayout());
        JLabel codeLabel = new JLabel("Código a analizar:");
        codeLabel.setFont(new Font("Open Sans", Font.BOLD, 16));
        codeArea = new JTextArea();
        codeArea.setFont(font);
        codePanel.add(codeLabel, BorderLayout.NORTH);
        codePanel.setBackground(Color.decode("#1f2126"));
        codeLabel.setForeground(Color.white);
        codePanel.add(new JScrollPane(codeArea), BorderLayout.CENTER);
        return codePanel;
    }

    private JScrollPane createResultArea(Font font) {
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(font);
        return new JScrollPane(resultArea);
    }

    private JScrollPane createTreeArea(Font font) {
        treeArea = new JTextArea();
        treeArea.setEditable(false);
        treeArea.setFont(font);
        return new JScrollPane(treeArea);
    }

    private JScrollPane createSymbolsTable(Font font) {
        symbolsTableModel = new DefaultTableModel();
        symbolsTableModel.addColumn("Variable");
        symbolsTableModel.addColumn("Tipo");
        symbolsTable = new JTable(symbolsTableModel);
        return new JScrollPane(symbolsTable);
    }

    private JTabbedPane createTabbedPane(
            JScrollPane resultScrollPane,
            JScrollPane symbolsScrollPane,
            JScrollPane treeScrollPane,
            JScrollPane bytecodeScrollPane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Resultados", resultScrollPane);
        tabbedPane.addTab("Bytecode", bytecodeScrollPane);
        tabbedPane.addTab("Tabla de Símbolos", symbolsScrollPane);
        tabbedPane.addTab("Árbol Sintáctico", treeScrollPane);
        return tabbedPane;
    }

    private JPanel createSidePanel(Font font) {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.decode("#1f2126"));

        JButton lexicoButton = new RoundedButton("Lex", 10);
        JButton sintacticoButton = new RoundedButton("Sin", 10);
        JButton infoButton = new RoundedButton("Gram",50);
        infoButton.setFont(font);

        infoButton.setBorderPainted(false);
        infoButton.setForeground(Color.white);
        infoButton.addActionListener(e -> showInfoDialog());
        infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        lexicoButton.setFont(font);
        sintacticoButton.setFont(font);

        lexicoButton.setFocusPainted(false);
        sintacticoButton.setFocusPainted(false);
        infoButton.setFocusPainted(false);

        Color softColor = new Color(116, 116, 199);
        Color hoverColor = new Color(138, 138, 237);
        lexicoButton.setBackground(softColor);
        sintacticoButton.setBackground(softColor);
        infoButton.setBackground(softColor);
        lexicoButton.setBorderPainted(false);
        sintacticoButton.setBorderPainted(false);
        lexicoButton.setForeground(Color.white);
        sintacticoButton.setForeground(Color.white);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setBackground(softColor);
            }
        };
        lexicoButton.addMouseListener(mouseAdapter);
        sintacticoButton.addMouseListener(mouseAdapter);
        infoButton.addMouseListener(mouseAdapter);

        lexicoButton.addActionListener(this::actionLexico);
        sintacticoButton.addActionListener(this::actionSintactico);

        lexicoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sintacticoButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension buttonSize = new Dimension(Integer.MAX_VALUE, lexicoButton.getPreferredSize().height);
        lexicoButton.setMaximumSize(buttonSize);
        sintacticoButton.setMaximumSize(buttonSize);

        int verticalSpacing = 20;
        Component verticalStrut = Box.createVerticalStrut(verticalSpacing);

        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(lexicoButton);
        sidePanel.add(verticalStrut);
        sidePanel.add(sintacticoButton);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        sidePanel.add(infoButton);


        return sidePanel;
    }

    private void showInfoDialog() {
        JOptionPane.showMessageDialog(this, "P -> D S <eof>\r\n" + //
                        "D -> id (int | string) ℇ ; D\r\n" + //
                        "D -> ℇ (cadena nula)\r\n" + //
                        "S -> if E then S else S\r\n" + //
                        "S -> id = E\r\n" + //
                        "S -> print E\r\n" + //
                        "E -> id + id\r\n" + //
                        "E -> id\r\n" + //
                        "id -> letra (letra | digito) *", "Gramatica del lenguaje", JOptionPane.INFORMATION_MESSAGE);
    }

    private JSplitPane createSplitPane(JPanel codePanel, JTabbedPane tabbedPane) {
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                codePanel,
                tabbedPane);
        splitPane.setResizeWeight(0.5);
        return splitPane;
    }

    private JPanel createMainPanel(JPanel sidePanel, JSplitPane splitPane) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sidePanel, BorderLayout.WEST);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JScrollPane createBytecodeArea(Font font) {
        bytecodeArea = new JTextArea();
        bytecodeArea.setEditable(false);
        bytecodeArea.setFont(font);
        return new JScrollPane(bytecodeArea);
    }

    private void actionLexico(ActionEvent e) {
        try {
            Scanner scanner = new Scanner(codeArea.getText());
            StringBuilder results = new StringBuilder();
            String token;
            while (!(token = scanner.getToken(true)).equals("EOF")) {
                results
                        .append(token)
                        .append(" - ")
                        .append(scanner.getTipoToken())
                        .append("\n");
            }
            resultArea.setText(results.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error durante el análisis léxico: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionSintactico(ActionEvent e) {
        try {
            Parser parser = new Parser(codeArea.getText());
            Programax programa = parser.P();
            actualizarTablaSimbolos(parser.getTablaSimbolos());
            resultArea.setText(parser.getLog());
            String representacionArbol = programa.toTreeString(0);
            treeArea.setText(representacionArbol);

            String bytecode = parser.getBytecode();
            actualizarBytecodeArea(bytecode);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error durante el análisis sintáctico: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarBytecodeArea(String bytecode) {
        bytecodeArea.setText(bytecode);
    }

    private void actualizarTablaSimbolos(Vector tablaSimbolos) {
        symbolsTableModel.setRowCount(0);
        for (Object item : tablaSimbolos) {
            if (item instanceof Declarax) {
                Declarax declaracion = (Declarax) item;
                if (declaracion.isVariable()) {
                    symbolsTableModel.addRow(
                            new Object[] {
                                    declaracion.getVar(),
                                    declaracion.getType().getTypex(),
                            });
                } else if (declaracion.isToken()) {
                    symbolsTableModel.addRow(new Object[] { declaracion.getToken(), "" });
                }
            } else {
                // Manejo de casos no esperados
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}