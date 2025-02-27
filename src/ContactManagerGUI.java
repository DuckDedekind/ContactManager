import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class ContactManagerGUI {
    private JFrame frame;
    private JTextArea contactListArea;
    private JTextField nameField, numberField;

    public ContactManagerGUI() {
        frame = new JFrame("Gestor de Contactos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Nombre:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Número:"));
        numberField = new JTextField();
        panel.add(numberField);

        JButton addButton = new JButton("Añadir Contacto");
        JButton deleteButton = new JButton("Eliminar Contacto");
        JButton updateButton = new JButton("Actualizar Contacto");
        JButton displayButton = new JButton("Mostrar Contactos");

        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(updateButton);
        panel.add(displayButton);

        contactListArea = new JTextArea();
        contactListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contactListArea);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addContact());
        deleteButton.addActionListener(e -> deleteContact());
        updateButton.addActionListener(e -> updateContact());
        displayButton.addActionListener(e -> displayContacts());

        frame.setVisible(true);
    }

    private void addContact() {
        String name = nameField.getText().trim();
        String number = numberField.getText().trim();
        if (name.isEmpty() || number.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Ingrese un nombre y un número.");
            return;
        }
        try (FileWriter writer = new FileWriter("friendsContact.txt", true)) {
            writer.write(name + "!" + number + "\n");
            JOptionPane.showMessageDialog(frame, "Contacto añadido.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al guardar el contacto.");
        }
    }

    private void deleteContact() {
        String nameToDelete = nameField.getText().trim();
        if (nameToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Ingrese un nombre.");
            return;
        }
        File inputFile = new File("friendsContact.txt");
        File tempFile = new File("temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.split("!")[0].equalsIgnoreCase(nameToDelete)) {
                    found = true;
                    continue;
                }
                writer.write(line + "\n");
            }
            if (found) {
                tempFile.renameTo(inputFile);
                JOptionPane.showMessageDialog(frame, "Contacto eliminado.");
            } else {
                JOptionPane.showMessageDialog(frame, "Contacto no encontrado.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al eliminar contacto.");
        }
    }

    private void updateContact() {
        String nameToUpdate = nameField.getText().trim();
        String newNumber = numberField.getText().trim();
        if (nameToUpdate.isEmpty() || newNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Ingrese un nombre y un nuevo número.");
            return;
        }
        File inputFile = new File("friendsContact.txt");
        File tempFile = new File("temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("!");
                if (parts[0].equalsIgnoreCase(nameToUpdate)) {
                    line = nameToUpdate + "!" + newNumber;
                    found = true;
                }
                writer.write(line + "\n");
            }
            if (found) {
                tempFile.renameTo(inputFile);
                JOptionPane.showMessageDialog(frame, "Contacto actualizado.");
            } else {
                JOptionPane.showMessageDialog(frame, "Contacto no encontrado.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al actualizar contacto.");
        }
    }

    private void displayContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("friendsContact.txt"))) {
            contactListArea.setText("");
            String line;
            while ((line = reader.readLine()) != null) {
                contactListArea.append(line.replace("!", " - ") + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al leer los contactos.");
        }
    }

    public static void main(String[] args) {
        new ContactManagerGUI();
    }
}
