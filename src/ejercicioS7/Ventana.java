package ejercicioS7;

import javax.swing.*;

public class Ventana {
    // ===== ATRIBUTOS (componentes del .form) =====
    private JPanel Ventana;
    private JTextField textid;
    private JTextField textnombre;
    private JTextField textsuperpoder;
    private JComboBox combomision;
    private JComboBox combodificultad;
    private JTextField textpago;
    private JButton AGREGARButton;
    private JButton MODIFICARButton;
    private JButton BUSCARButton;
    private JButton LISTARButton;
    private JButton INFORMARButton;
    private JTextArea textArea1;

    // ===== ATRIBUTOS DE LÓGICA =====
    private GestorHeroe gestor;
    private boolean modoEdicion = false;   // a nivel de clase, no dentro del constructor

    // ===== CONSTRUCTOR: solo los listeners =====
    public Ventana() {
        gestor = new GestorHeroe();

        // ---------- AGREGAR ----------
        AGREGARButton.addActionListener(e -> {
            try {
                gestor.agregar(leerHeroeDeFormulario());
                JOptionPane.showMessageDialog(null, "¡Héroe registrado con éxito!");
                limpiarCampos();
                textArea1.setText(gestor.imprimirHeroes());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Por favor, ingrese valores numéricos válidos en ID, Dificultad y Pago.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // ---------- MODIFICAR (dos pasos) ----------
        MODIFICARButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(textid.getText().trim());

                if (!modoEdicion) {
                    // PASO 1: con solo el ID, cargar los datos
                    Heroe h = gestor.buscarPorId(id);
                    if (h == null) {
                        JOptionPane.showMessageDialog(null,
                                "No existe un héroe con el ID " + id,
                                "No encontrado", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textnombre.setText(h.getNombre());
                    textsuperpoder.setText(h.getSuperpoder());
                    combomision.setSelectedItem(h.getMision());
                    combodificultad.setSelectedItem(String.valueOf(h.getDificultad()));
                    textpago.setText(String.valueOf(h.getPagoMensual()));

                    textid.setEditable(false);
                    MODIFICARButton.setText("GUARDAR CAMBIOS");
                    AGREGARButton.setEnabled(false);
                    modoEdicion = true;

                } else {
                    // PASO 2: guardar los cambios
                    gestor.modificar(leerHeroeDeFormulario());
                    JOptionPane.showMessageDialog(null, "¡Héroe modificado con éxito!");
                    salirModoEdicion();
                    textArea1.setText(gestor.imprimirHeroes());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Verifique los campos numéricos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // ---------- BUSCAR ----------
        BUSCARButton.addActionListener(e -> {
            try {
                int idBuscado = Integer.parseInt(textid.getText().trim());
                Heroe encontrado = gestor.buscarPorId(idBuscado);

                if (encontrado != null) {
                    textnombre.setText(encontrado.getNombre());
                    textsuperpoder.setText(encontrado.getSuperpoder());
                    combomision.setSelectedItem(encontrado.getMision());
                    combodificultad.setSelectedItem(String.valueOf(encontrado.getDificultad()));
                    textpago.setText(String.valueOf(encontrado.getPagoMensual()));
                    textArea1.setText("--- HÉROE ENCONTRADO ---\n" + encontrado);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No se encontró ningún héroe con el ID " + idBuscado,
                            "No encontrado", JOptionPane.WARNING_MESSAGE);
                    textArea1.setText("Búsqueda sin resultados.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un ID válido para buscar.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ---------- LISTAR ----------
        LISTARButton.addActionListener(e -> {
            if (modoEdicion) salirModoEdicion();   // si estaba editando, cancela
            textArea1.setText(gestor.imprimirHeroes());
        });

        // ---------- INFORMAR ----------
        INFORMARButton.addActionListener(e -> textArea1.setText(gestor.generarInforme()));
    }

    // ===== MÉTODOS (fuera del constructor) =====

    private Heroe leerHeroeDeFormulario() {
        int id = Integer.parseInt(textid.getText().trim());
        String nombre = textnombre.getText().trim();
        String superpoder = textsuperpoder.getText().trim();
        String mision = combomision.getSelectedItem().toString();
        int dificultad = Integer.parseInt(combodificultad.getSelectedItem().toString());
        double pago = Double.parseDouble(textpago.getText().trim());
        return new Heroe(id, nombre, superpoder, mision, dificultad, pago);
    }

    private void salirModoEdicion() {
        modoEdicion = false;
        textid.setEditable(true);
        MODIFICARButton.setText("MODIFICAR");
        AGREGARButton.setEnabled(true);
        limpiarCampos();
    }

    private void limpiarCampos() {
        textid.setText("");
        textnombre.setText("");
        textsuperpoder.setText("");
        textpago.setText("");
        combomision.setSelectedIndex(0);
        combodificultad.setSelectedIndex(0);
        textid.requestFocus();
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Liga de la Justicia - Gestión de Misiones");
            frame.setContentPane(new Ventana().Ventana);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 550);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}