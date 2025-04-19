/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// ... import tetap sama
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class DataKasir extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNama, txtAgama, txtSearch, txtAlamat;
    private JPasswordField txtPassword;
    private JComboBox<String> comboJK;
    private JButton btnSimpan, btnUbah, btnHapus, btnKeluar;

    private Connection conn;

    public DataKasir() {
        setTitle("Data Kasir");
        setSize(850, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Font fontTitle = new Font("Segoe UI", Font.BOLD, 20);
        Font fontField = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblTitle = new JLabel("Manajemen Data Kasir", SwingConstants.CENTER);
        lblTitle.setFont(fontTitle);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblSearch = new JLabel("Cari Kasir:");
        lblSearch.setFont(fontField);
        txtSearch = new JTextField(20);
        txtSearch.setFont(fontField);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new LineBorder(new Color(200, 200, 200), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtNama = new JTextField(15);
        comboJK = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        txtAgama = new JTextField(15);
        txtAlamat = new JTextField(15);
        txtPassword = new JPasswordField(15);

        JLabel lblNama = new JLabel("Nama:");
        JLabel lblJK = new JLabel("Jenis Kelamin:");
        JLabel lblAgama = new JLabel("Agama:");
        JLabel lblAlamat = new JLabel("Alamat:");
        JLabel lblPassword = new JLabel("Password:");

        lblNama.setFont(fontField);
        lblJK.setFont(fontField);
        lblAgama.setFont(fontField);
        lblAlamat.setFont(fontField);
        lblPassword.setFont(fontField);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblNama, gbc);
        gbc.gridx = 1; formPanel.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblJK, gbc);
        gbc.gridx = 1; formPanel.add(comboJK, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblAgama, gbc);
        gbc.gridx = 1; formPanel.add(txtAgama, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblAlamat, gbc);
        gbc.gridx = 1; formPanel.add(txtAlamat, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lblPassword, gbc);
        gbc.gridx = 1; formPanel.add(txtPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnKeluar = new JButton("Keluar");

        JButton[] buttons = {btnSimpan, btnUbah, btnHapus, btnKeluar};
        for (JButton btn : buttons) {
            btn.setFocusPainted(false);
            btn.setFont(fontField);
            btn.setBackground(new Color(33, 150, 243));
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(100, 35));
            btn.setBorder(new LineBorder(new Color(0, 120, 215), 1, true));
        }
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnUbah);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnKeluar);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        model = new DefaultTableModel(new String[]{"ID", "Nama", "JK", "Agama", "Alamat"}, 0);
        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(fontField);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                "Data Kasir", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 14), new Color(60, 60, 60)
        ));
        scrollPane.setPreferredSize(new Dimension(750, 200));
        add(scrollPane, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnKeluar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtNama.setText(model.getValueAt(row, 1).toString());
                comboJK.setSelectedItem(model.getValueAt(row, 2).toString());
                txtAgama.setText(model.getValueAt(row, 3).toString());
                txtAlamat.setText(model.getValueAt(row, 4).toString());
                txtPassword.setText("");
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = txtSearch.getText().trim();
                loadData(query);
            }
        });

        koneksi();
        loadData("");
    }

    private void koneksi() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_koperasi", "root", "");
            System.out.println("Koneksi berhasil");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Koneksi Gagal: " + e.getMessage());
        }
    }

    private void loadData(String searchQuery) {
        model.setRowCount(0);
        try {
            String sql = "SELECT id, nama, jenis_kelamin, agama, alamat FROM kasir";
            if (!searchQuery.isEmpty()) {
                sql += " WHERE nama LIKE ?";
            }
            PreparedStatement pst = conn.prepareStatement(sql);
            if (!searchQuery.isEmpty()) {
                pst.setString(1, "%" + searchQuery + "%");
            }
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("id"));
                row.add(rs.getString("nama"));
                row.add(rs.getString("jenis_kelamin"));
                row.add(rs.getString("agama"));
                row.add(rs.getString("alamat"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void simpanData() {
        if (txtNama.getText().isEmpty() || txtAgama.getText().isEmpty() || txtAlamat.getText().isEmpty() || txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        try {
            String sql = "INSERT INTO kasir (nama, jenis_kelamin, agama, alamat, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNama.getText());
            pst.setString(2, comboJK.getSelectedItem().toString());
            pst.setString(3, txtAgama.getText());
            pst.setString(4, txtAlamat.getText());
            pst.setString(5, new String(txtPassword.getPassword()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            loadData("");
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + e.getMessage());
        }
    }

    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah!");
            return;
        }
        try {
            String id = model.getValueAt(row, 0).toString();
            String sql = "UPDATE kasir SET nama=?, jenis_kelamin=?, agama=?, alamat=?, password=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNama.getText());
            pst.setString(2, comboJK.getSelectedItem().toString());
            pst.setString(3, txtAgama.getText());
            pst.setString(4, txtAlamat.getText());
            pst.setString(5, new String(txtPassword.getPassword()));
            pst.setString(6, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
            loadData("");
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal ubah: " + e.getMessage());
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
            return;
        }
        try {
            String id = model.getValueAt(row, 0).toString();
            String sql = "DELETE FROM kasir WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            loadData("");
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal hapus: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtNama.setText("");
        comboJK.setSelectedIndex(0);
        txtAgama.setText("");
        txtAlamat.setText("");
        txtPassword.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataKasir().setVisible(true));
    }
}
