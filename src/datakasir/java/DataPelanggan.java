/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FormPelanggan;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DataPelanggan extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNama, txtEmail, txtTelepon, txtSearch;
    private JTextArea txtAlamat;
    private JRadioButton rbLaki, rbPerempuan;
    private JButton btnSimpan, btnUbah, btnHapus, btnKeluar;
    private Connection conn;

    public DataPelanggan() {
        setTitle("Data Pelanggan");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Font fontTitle = new Font("Segoe UI", Font.BOLD, 20);
        Font fontField = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblTitle = new JLabel("Manajemen Data Pelanggan", SwingConstants.CENTER);
        lblTitle.setFont(fontTitle);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(new Color(245, 245, 245));
        JLabel lblSearch = new JLabel("🔍 Cari Pelanggan:");
        lblSearch.setFont(fontField);
        txtSearch = new JTextField(20);
        txtSearch.setFont(fontField);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadData(txtSearch.getText().trim());
            }
        });

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            new LineBorder(new Color(200, 200, 200), 1, true)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNama = new JLabel("Nama:"); lblNama.setFont(fontField);
        formPanel.add(lblNama, gbc);
        gbc.gridx = 1;
        txtNama = new JTextField(20); txtNama.setFont(fontField);
        formPanel.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblJK = new JLabel("Jenis Kelamin:"); lblJK.setFont(fontField);
        formPanel.add(lblJK, gbc);
        gbc.gridx = 1;
        rbLaki = new JRadioButton("Laki-Laki"); rbLaki.setFont(fontField);
        rbPerempuan = new JRadioButton("Perempuan"); rbPerempuan.setFont(fontField);
        ButtonGroup bgJK = new ButtonGroup();
        bgJK.add(rbLaki); bgJK.add(rbPerempuan);
        JPanel pnlJK = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlJK.setBackground(Color.WHITE);
        pnlJK.add(rbLaki); pnlJK.add(rbPerempuan);
        formPanel.add(pnlJK, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblEmail = new JLabel("Email:"); lblEmail.setFont(fontField);
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20); txtEmail.setFont(fontField);
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTel = new JLabel("Telepon:"); lblTel.setFont(fontField);
        formPanel.add(lblTel, gbc);
        gbc.gridx = 1;
        txtTelepon = new JTextField(15); txtTelepon.setFont(fontField);
        formPanel.add(txtTelepon, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblAlamat = new JLabel("Alamat:"); lblAlamat.setFont(fontField);
        formPanel.add(lblAlamat, gbc);
        gbc.gridx = 1;
        txtAlamat = new JTextArea(3, 20); txtAlamat.setFont(fontField);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        formPanel.add(scrollAlamat, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnKeluar = new JButton("Keluar");
        for (JButton b : new JButton[]{btnSimpan, btnUbah, btnHapus, btnKeluar}) {
            b.setFont(fontField);
            b.setBackground(new Color(33, 150, 243));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(100, 35));
            b.setBorder(new LineBorder(new Color(0, 120, 215), 1, true));
            buttonPanel.add(b);
        }
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Jenis Kelamin", "Email", "Telepon", "Alamat"}, 0);
        table = new JTable(model);
        table.setFont(fontField);
        table.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(180, 180, 180), 1, true),
            "Data Pelanggan", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 14), new Color(60, 60, 60)
        ));
        scroll.setPreferredSize(new Dimension(850, 200));
        add(scroll, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnKeluar.addActionListener(e -> dispose());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                txtNama.setText(model.getValueAt(row, 1).toString());
                String jk = model.getValueAt(row, 2).toString();
                if (jk.equals("Laki-Laki")) rbLaki.setSelected(true);
                else rbPerempuan.setSelected(true);
                txtEmail.setText(model.getValueAt(row, 3).toString());
                txtTelepon.setText(model.getValueAt(row, 4).toString());
                txtAlamat.setText(model.getValueAt(row, 5).toString());
            }
        });

        koneksi();
        loadData("");
    }

    private void koneksi() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_koperasi", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi: " + ex.getMessage());
        }
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        try {
            String sql = "SELECT id, nama, jenis_kelamin, email, telepon, alamat FROM pelanggan WHERE nama LIKE ? OR email LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("email"),
                    rs.getString("telepon"),
                    rs.getString("alamat")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + ex.getMessage());
        }
    }

    private void simpanData() {
        if (txtNama.getText().isEmpty() || (!rbLaki.isSelected() && !rbPerempuan.isSelected())
                || txtEmail.getText().isEmpty() || txtTelepon.getText().isEmpty() || txtAlamat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }
        try {
            String sql = "INSERT INTO pelanggan (nama, jenis_kelamin, email, telepon, alamat) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNama.getText());
            pst.setString(2, rbLaki.isSelected() ? "Laki-Laki" : "Perempuan");
            pst.setString(3, txtEmail.getText());
            pst.setString(4, txtTelepon.getText());
            pst.setString(5, txtAlamat.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            loadData("");
            resetForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + ex.getMessage());
        }
    }

    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }
        try {
            String id = model.getValueAt(row, 0).toString();
            String sql = "UPDATE pelanggan SET nama=?, jenis_kelamin=?, email=?, telepon=?, alamat=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNama.getText());
            pst.setString(2, rbLaki.isSelected() ? "Laki-Laki" : "Perempuan");
            pst.setString(3, txtEmail.getText());
            pst.setString(4, txtTelepon.getText());
            pst.setString(5, txtAlamat.getText());
            pst.setString(6, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
            loadData("");
            resetForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal ubah: " + ex.getMessage());
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String id = model.getValueAt(row, 0).toString();
                String sql = "DELETE FROM pelanggan WHERE id=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                loadData("");
                resetForm();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal hapus: " + ex.getMessage());
            }
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtEmail.setText("");
        txtTelepon.setText("");
        txtAlamat.setText("");
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataPelanggan().setVisible(true));
    }
}
