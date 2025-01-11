package gm.fit_zone.gui;

import gm.fit_zone.Service.ClientService;
import gm.fit_zone.Service.IClientService;
import gm.fit_zone.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// @Component
public class FitZoneForm extends JFrame{
    private JPanel principalPanel;
    private JTable clientsTable;
    private JTextField nameText;
    private JTextField lastnameText;
    private JTextField membershipText;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton clearButton;
    IClientService clientService;
    private DefaultTableModel tableModelClient;
    private Integer idClient;

    @Autowired
    public FitZoneForm(ClientService clientService) {
        this.clientService = clientService;
        initForm();
        saveButton.addActionListener(e -> saveClient());
        clientsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadSelectedClient();
            }
        });
        deleteButton.addActionListener(e -> deleteClient());
        clearButton.addActionListener(e -> clearForm());
    }

    private void initForm() {
        setContentPane(principalPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createUIComponents() {
        // this.tableModelClient = new DefaultTableModel(0, 4);
        this.tableModelClient = new DefaultTableModel(0, 4) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // cell edition disabled
            }
        };
        String[] headers = {"ID", "Name", "Lastname", "Membership"};
        this.tableModelClient.setColumnIdentifiers(headers);
        this.clientsTable = new JTable(tableModelClient);
        // Only one selectable record
        this.clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // LOAD CLIENT LIST
        clientList();
    }

    private void clientList() {
        this.tableModelClient.setRowCount(0);
        var clients = this.clientService.listClients();
        clients.forEach(client -> {
            Object[] rowClient = {
                    client.getId(),
                    client.getName(),
                    client.getLastname(),
                    client.getMembership()
            };
            this.tableModelClient.addRow(rowClient);
        });
    }

    private void saveClient() {
        if (nameText.getText().equals("")) {
            showMessage("You must provide a name");
            nameText.requestFocusInWindow();
            return;
        }

        if (membershipText.getText().equals("")) {
            showMessage("You must provide a membership number");
            membershipText.requestFocusInWindow();
            return;
        }

        // Recover form values
        var name  = nameText.getText();
        var lastname = lastnameText.getText();
        var membership = Integer.parseInt(membershipText.getText());
        var client = new Client();
        client.setId(this.idClient);
        client.setName(name);
        client.setLastname(lastname);
        client.setMembership(membership);
        this.clientService.saveClient(client); // update/create
        if (this.idClient == null)
            showMessage("New Client Saved");
        else
            showMessage("Client Updated");
        clearForm();
        clientList();
    }

    private void loadSelectedClient() {
        var row = clientsTable.getSelectedRow();
        if (row != -1) { // -1 = none register selected
            // get/set id
            var id = clientsTable.getModel().getValueAt(row, 0).toString();
            this.idClient = Integer.parseInt(id);
            // get/set name
            var name = clientsTable.getModel().getValueAt(row, 1).toString();
            this.nameText.setText(name);
            // get/set lastname
            var lastname = clientsTable.getModel().getValueAt(row, 2).toString();
            this.lastnameText.setText(lastname);
            // get/set membership
            var membership = clientsTable.getModel().getValueAt(row, 3).toString();
            this.membershipText.setText(membership);
        }
    }

    private void deleteClient() {
        var row = clientsTable.getSelectedRow();
        if (row != -1) {
            var idClientStr = clientsTable.getModel().getValueAt(row, 0).toString();
            this.idClient = Integer.parseInt(idClientStr);
            var client = new Client();
            client.setId(this.idClient);
            clientService.deleteClientById(client);
            showMessage("Client with ID: " + this.idClient +  " deleted");
            clearForm();
            clientList();
        }
        else
            showMessage("Must select a client to delete");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void clearForm() {
        nameText.setText("");
        lastnameText.setText("");
        membershipText.setText("");
        // Clear ID client
        this.idClient = null;
        // Deselect record in table
        this.clientsTable.getSelectionModel().clearSelection();
    }
}
















