import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * ClinicaSync - Login Page (Java Swing with Fluid Glassmorphism Effect)
 */
public class ClinicaSyncLogin extends JFrame {

    // === Warna Tema ClinicaSync ===
    private static final Color COLOR_PRIMARY          = new Color(0xa7, 0x36, 0x46);
    private static final Color COLOR_PRIMARY_CONTAINER= new Color(0xf6, 0x72, 0x80);
    private static final Color COLOR_ON_PRIMARY       = Color.WHITE;
    private static final Color COLOR_BACKGROUND       = new Color(0xf0, 0xf4, 0xf8);
    private static final Color COLOR_SURFACE          = new Color(255, 255, 255, 160); // Semi-transparan untuk field
    private static final Color COLOR_SURFACE_CONTAINER= new Color(230, 239, 248, 100); // Kaca kiri lebih tipis
    private static final Color COLOR_SURFACE_HIGH     = new Color(0xe0, 0xe9, 0xf2, 180);
    private static final Color COLOR_ON_SURFACE       = new Color(0x14, 0x1d, 0x23);
    private static final Color COLOR_ON_SURFACE_VAR   = new Color(0x57, 0x41, 0x42);
    private static final Color COLOR_OUTLINE_VAR      = new Color(255, 255, 255, 180); // Border putih transparan khas glass
    private static final Color COLOR_OUTLINE          = new Color(0x8a, 0x71, 0x72);
    private static final Color COLOR_ERROR            = new Color(0xba, 0x1a, 0x1a);

    // Font
    private static Font fontHeadlineLg;
    private static Font fontHeadlineMd;
    private static Font fontBodyMd;
    private static Font fontBodySm;
    private static Font fontLabelMd;
    private static Font fontLabelSm;

    // Komponen Form
    private ButtonGroup roleGroup;
    private JToggleButton[] roleButtons;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheckbox;
    private JButton loginButton;
    private JButton togglePasswordBtn;
    private boolean passwordVisible = false;

    public ClinicaSyncLogin() {
        initFonts();
        initUI();
    }

    private void initFonts() {
        fontHeadlineLg = new Font("Poppins", Font.BOLD, 28);
        fontHeadlineMd = new Font("Poppins", Font.BOLD, 20);
        fontBodyMd     = new Font("Open Sans", Font.PLAIN, 14);
        fontBodySm     = new Font("Open Sans", Font.PLAIN, 12);
        fontLabelMd    = new Font("Poppins", Font.BOLD, 13);
        fontLabelSm    = new Font("Poppins", Font.PLAIN, 11);

        if (!isFontAvailable("Poppins")) {
            fontHeadlineLg = new Font("SansSerif", Font.BOLD, 28);
            fontHeadlineMd = new Font("SansSerif", Font.BOLD, 20);
            fontLabelMd    = new Font("SansSerif", Font.BOLD, 13);
            fontLabelSm    = new Font("SansSerif", Font.PLAIN, 11);
        }
        if (!isFontAvailable("Open Sans")) {
            fontBodyMd = new Font("SansSerif", Font.PLAIN, 14);
            fontBodySm = new Font("SansSerif", Font.PLAIN, 12);
        }
    }

    private boolean isFontAvailable(String name) {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String f : fonts) if (f.equalsIgnoreCase(name)) return true;
        return false;
    }

    private void initUI() {
        setTitle("Masuk | ClinicaSync");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 620));
        setPreferredSize(new Dimension(1100, 680));

        // Root panel menggunakan Fluid Background (Efek cairan bergerak/gradasi cerah)
        FluidBackgroundPanel root = new FluidBackgroundPanel();
        root.setLayout(new GridBagLayout());
        setContentPane(root);

        // Card utama dimodifikasi menjadi GlassCardPanel
        GlassCardPanel card = new GlassCardPanel();
        card.setLayout(new GridLayout(1, 2));
        card.setPreferredSize(new Dimension(1000, 580));

        card.add(buildBrandingPanel());
        card.add(buildFormPanel());

        root.add(card);
        pack();
        setLocationRelativeTo(null);
    }

    // =========================================================
    // Panel Kiri: Branding (Semi-transparan Glass)
    // =========================================================
    private JPanel buildBrandingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Di-override kosong agar background utama transparan mengikuti GlassCardPanel
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(48, 40, 48, 40));

        // Icon Wrap
        JPanel iconWrap = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xf6, 0x72, 0x80, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconWrap.setOpaque(false);
        iconWrap.setPreferredSize(new Dimension(72, 72));
        iconWrap.setMaximumSize(new Dimension(72, 72));
        iconWrap.setLayout(new GridBagLayout());
        JLabel iconLabel = new JLabel("⟳");
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        iconLabel.setForeground(new Color(0x6c, 0x05, 0x1f));
        iconWrap.add(iconLabel);
        iconWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconWrap);
        panel.add(Box.createVerticalStrut(20));

        // Title
        JLabel title = new JLabel("ClinicaSync");
        title.setFont(new Font(fontHeadlineLg.getFamily(), Font.BOLD, 32));
        title.setForeground(COLOR_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(12));

        // Subtitle
        JTextArea subtitle = new JTextArea(
            "Mengelola layanan kesehatan dengan presisi.\nPlatform sinkronisasi medis terintegrasi."
        );
        subtitle.setFont(fontBodyMd);
        subtitle.setForeground(COLOR_ON_SURFACE_VAR);
        subtitle.setOpaque(false);
        subtitle.setEditable(false);
        subtitle.setFocusable(false);
        subtitle.setWrapStyleWord(true);
        subtitle.setLineWrap(true);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setMaximumSize(new Dimension(280, 80));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(32));

        // Info cards
        String[][] features = {
            {"🔒", "Kepatuhan HIPAA", "Data terenkripsi secara end-to-end"},
            {"📋", "Sinkronisasi Real-time",  "Pembaruan rekam medis pasien secara langsung"},
            {"👥", "Multi-Peran",      "Admin, Dokter, Perawat, Apoteker"}
        };
        for (String[] f : features) {
            panel.add(buildFeatureRow(f[0], f[1], f[2]));
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    private JPanel buildFeatureRow(String icon, String title, String desc) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(320, 56));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("SansSerif", Font.PLAIN, 20));
        ico.setPreferredSize(new Dimension(36, 36));
        ico.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setFont(new Font(fontLabelMd.getFamily(), Font.BOLD, 12));
        t.setForeground(COLOR_ON_SURFACE);
        JLabel d = new JLabel(desc);
        d.setFont(new Font(fontBodySm.getFamily(), Font.PLAIN, 11));
        d.setForeground(COLOR_ON_SURFACE_VAR);
        text.add(t);
        text.add(d);

        row.add(ico, BorderLayout.WEST);
        row.add(text, BorderLayout.CENTER);
        return row;
    }

    // =========================================================
    // Panel Kanan: Form Login (Lebih Solid tapi tetap Glassy)
    // =========================================================
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Di-override kosong agar menyatu dengan GlassCardPanel dasar
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 44, 32, 44));

        // Header
        JLabel welcome = new JLabel("Selamat Datang Kembali");
        welcome.setFont(new Font(fontHeadlineLg.getFamily(), Font.BOLD, 26));
        welcome.setForeground(COLOR_ON_SURFACE);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(welcome);
        panel.add(Box.createVerticalStrut(4));

        JLabel sub = new JLabel("Silakan masukkan kredensial Anda untuk mengakses dasbor.");
        sub.setFont(fontBodySm);
        sub.setForeground(COLOR_ON_SURFACE_VAR);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sub);
        panel.add(Box.createVerticalStrut(22));

        // Role Selection
        panel.add(buildSectionLabel("Pilih Peran Anda"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(buildRoleSelector());
        panel.add(Box.createVerticalStrut(16));

        // Email
        panel.add(buildSectionLabel("Alamat Email"));
        panel.add(Box.createVerticalStrut(6));
        emailField = buildStyledTextField("dr.smith@clinicasync.com", "✉");
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(14));

        // Password
        JPanel passHeader = new JPanel(new BorderLayout());
        passHeader.setOpaque(false);
        passHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        passHeader.add(buildSectionLabel("Kata Sandi"), BorderLayout.WEST);
        JLabel forgot = new JLabel("<html><a href='#' style='color:#a73646;'>Lupa Kata Sandi?</a></html>");
        forgot.setFont(fontLabelSm);
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passHeader.add(forgot, BorderLayout.EAST);
        panel.add(passHeader);
        panel.add(Box.createVerticalStrut(6));
        panel.add(buildPasswordRow());
        panel.add(Box.createVerticalStrut(14));

        // Remember me
        rememberCheckbox = new JCheckBox("Pertahankan sesi masuk selama 30 hari");
        rememberCheckbox.setFont(fontBodySm);
        rememberCheckbox.setForeground(COLOR_ON_SURFACE_VAR);
        rememberCheckbox.setOpaque(false);
        rememberCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(rememberCheckbox);
        panel.add(Box.createVerticalStrut(20));

        // Login Button
        loginButton = buildLoginButton();
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(20));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255,255,255,100));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(14));

        // Footer
        JLabel footer = new JLabel(
            "<html><center><span style='color:#574142;'>Belum memiliki akun? </span>" +
            "<a href='#' style='color:#a73646;font-weight:bold;'>Hubungi Administrator Sistem</a></center></html>"
        );
        footer.setFont(fontBodySm);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(footer);

        panel.add(Box.createVerticalStrut(10));
        JLabel status = new JLabel("🔒  Sesuai Standar HIPAA  •  Enkripsi TLS 256-bit  •  © 2026 ClinicaSync");
        status.setFont(new Font(fontBodySm.getFamily(), Font.PLAIN, 10));
        status.setForeground(new Color(0x8a, 0x71, 0x72));
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(status);

        return panel;
    }

    private JLabel buildSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(fontLabelMd);
        label.setForeground(COLOR_ON_SURFACE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // =========================================================
    // Role Selector (Glass Styled Tabs)
    // =========================================================
    private JPanel buildRoleSelector() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        roleGroup = new ButtonGroup();
        String[][] roles = {
            {"⚙", "Admin"},
            {"🩺", "Dokter"},
            {"💊", "Perawat"},
            {"💉", "Apoteker"}
        };
        roleButtons = new JToggleButton[roles.length];
        for (int i = 0; i < roles.length; i++) {
            JToggleButton btn = createRoleButton(roles[i][0], roles[i][1]);
            roleButtons[i] = btn;
            roleGroup.add(btn);
            grid.add(btn);
        }
        roleButtons[0].setSelected(true);
        updateRoleStyles();
        return grid;
    }

    private JToggleButton createRoleButton(String icon, String label) {
        JToggleButton btn = new JToggleButton(icon + "  " + label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(new Color(0xf6, 0x72, 0x80, 180));
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(fontLabelSm);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new RoundedBorder(COLOR_OUTLINE_VAR, 1, 10));

        btn.addItemListener(e -> updateRoleStyles());
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!btn.isSelected()) btn.setBackground(new Color(255, 255, 255, 200));
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!btn.isSelected()) btn.setBackground(new Color(255, 255, 255, 100));
            }
        });
        btn.setBackground(new Color(255, 255, 255, 100));
        return btn;
    }

    private void updateRoleStyles() {
        for (JToggleButton btn : roleButtons) {
            if (btn.isSelected()) {
                btn.setForeground(Color.WHITE);
                btn.setBorder(new RoundedBorder(new Color(0xa7, 0x36, 0x46), 1, 10));
            } else {
                btn.setForeground(COLOR_ON_SURFACE_VAR);
                btn.setBorder(new RoundedBorder(new Color(255,255,255,120), 1, 10));
            }
            btn.repaint();
        }
    }

    // =========================================================
    // Styled Text Field (Glassy & Input Focus Glow)
    // =========================================================
    private JTextField buildStyledTextField(String placeholder, String prefixIcon) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(COLOR_OUTLINE);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 36, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        styleField(field, prefixIcon);
        return field;
    }

    private void styleField(JTextField field, String prefix) {
        field.setFont(fontBodyMd);
        field.setForeground(COLOR_ON_SURFACE);
        field.setBackground(COLOR_SURFACE);
        field.setOpaque(false);
        field.setBorder(new CompoundBorder(
            new RoundedBorder(COLOR_OUTLINE_VAR, 1, 8),
            BorderFactory.createEmptyBorder(10, 38, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setBackground(new Color(255,255,255,230)); // Lebih solid saat fokus
                field.setBorder(new CompoundBorder(
                    new RoundedBorder(COLOR_PRIMARY_CONTAINER, 1, 8),
                    BorderFactory.createEmptyBorder(10, 38, 10, 12)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                field.setBackground(COLOR_SURFACE);
                field.setBorder(new CompoundBorder(
                    new RoundedBorder(COLOR_OUTLINE_VAR, 1, 8),
                    BorderFactory.createEmptyBorder(10, 38, 10, 12)
                ));
            }
        });
    }

    private JPanel buildPasswordRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(fontBodyMd);
        passwordField.setForeground(COLOR_ON_SURFACE);
        passwordField.setBackground(COLOR_SURFACE);
        passwordField.setOpaque(false);
        passwordField.setBorder(new CompoundBorder(
            new RoundedBorder(COLOR_OUTLINE_VAR, 1, 8),
            BorderFactory.createEmptyBorder(10, 38, 10, 44)
        ));
        passwordField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                passwordField.setBackground(new Color(255,255,255,230));
                passwordField.setBorder(new CompoundBorder(
                    new RoundedBorder(COLOR_PRIMARY_CONTAINER, 1, 8),
                    BorderFactory.createEmptyBorder(10, 38, 10, 44)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                passwordField.setBackground(COLOR_SURFACE);
                passwordField.setBorder(new CompoundBorder(
                    new RoundedBorder(COLOR_OUTLINE_VAR, 1, 8),
                    BorderFactory.createEmptyBorder(10, 38, 10, 44)
                ));
            }
        });

        togglePasswordBtn = new JButton("👁");
        togglePasswordBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        togglePasswordBtn.setForeground(COLOR_ON_SURFACE_VAR);
        togglePasswordBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        togglePasswordBtn.setFocusPainted(false);
        togglePasswordBtn.setContentAreaFilled(false);
        togglePasswordBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        togglePasswordBtn.addActionListener(e -> {
            passwordVisible = !passwordVisible;
            passwordField.setEchoChar(passwordVisible ? (char) 0 : '•');
            togglePasswordBtn.setText(passwordVisible ? "🙈" : "👁");
        });

        row.add(passwordField, BorderLayout.CENTER);
        row.add(togglePasswordBtn, BorderLayout.EAST);
        return row;
    }

    // =========================================================
    // Login Button
    // =========================================================
    private JButton buildLoginButton() {
        JButton btn = new JButton("Masuk ke ClinicaSync  →") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(COLOR_PRIMARY.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(COLOR_PRIMARY);
                } else {
                    g2.setColor(COLOR_PRIMARY_CONTAINER);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font(fontLabelMd.getFamily(), Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 48));

        btn.addActionListener(e -> handleLogin());
        return btn;
    }

    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role     = getSelectedRole();

        if (email.isEmpty() || !email.contains("@") || password.isEmpty() || password.length() < 6) {
            showError("Kredensial yang Anda masukkan tidak valid. Silakan periksa kembali.");
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("⏳  Mengautentikasi...");

        Timer timer = new Timer(1500, evt -> {
            loginButton.setEnabled(true);
            loginButton.setText("Masuk ke ClinicaSync  →");
            JOptionPane.showMessageDialog(this, "✅  Autentikasi berhasil!\nPeran: " + role, "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private String getSelectedRole() {
        String[] roleNames = {"Admin", "Dokter", "Perawat", "Apoteker"};
        for (int i = 0; i < roleButtons.length; i++) {
            if (roleButtons[i].isSelected()) return roleNames[i];
        }
        return "Unknown";
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validasi", JOptionPane.ERROR_MESSAGE);
    }

    // =========================================================
    // Kustom Komponen Efek Fluid Glassmorphism
    // =========================================================

    /**
     * 1. Background Panel dengan Efek Cairan/Fluid Gradasi Terang (Mesh Gradient Style)
     * Memberikan visual warna melingkar di belakang kaca agar pembiasan terlihat nyata.
     */
    static class FluidBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Base Background
            g2.setColor(COLOR_BACKGROUND);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Fluid Blob 1 (Top Left - Pink/Red Container)
            Color c1 = new Color(0xf6, 0x72, 0x80, 90);
            RadialGradientPaint rgb1 = new RadialGradientPaint(
                new Point(getWidth() / 4, getHeight() / 4), getWidth() / 2f,
                new float[]{0f, 1f}, new Color[]{c1, new Color(255,255,255,0)}
            );
            g2.setPaint(rgb1);
            g2.fillOval(-100, -100, getWidth() / 2 + 200, getHeight() / 2 + 200);

            // Fluid Blob 2 (Bottom Right - Blue Cyan Soft)
            Color c2 = new Color(0xad, 0xbf, 0xfd, 110);
            RadialGradientPaint rgb2 = new RadialGradientPaint(
                new Point(getWidth() * 3 / 4, getHeight() * 3 / 4), getWidth() / 2f,
                new float[]{0f, 1f}, new Color[]{c2, new Color(255,255,255,0)}
            );
            g2.setPaint(rgb2);
            g2.fillOval(getWidth() / 2 - 100, getHeight() / 2 - 100, getWidth() / 2 + 200, getHeight() / 2 + 200);

            // Dot Pattern Overlay semitransparan
            g2.setColor(new Color(0xDE, 0xE2, 0xE6, 80));
            int step = 24;
            for (int x = 0; x < getWidth(); x += step) {
                for (int y = 0; y < getHeight(); y += step) {
                    g2.fillOval(x - 1, y - 1, 2, 2);
                }
            }
            g2.dispose();
        }
    }

    /**
     * 2. Card Panel Utama dengan Efek Kaca (Glassmorphism Frame)
     * Menggunakan warna putih dengan opacity rendah, ditumpuk dengan border putih murni transparan.
     */
    static class GlassCardPanel extends JPanel {
        public GlassCardPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Lapisan Kaca Utama (Frosty Glass White)
            g2.setColor(new Color(255, 255, 255, 110)); 
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

            // Sub-Glass Effect khusus sisi kiri (Branding panel overlay) untuk estetika dual-panel
            g2.setColor(new Color(230, 239, 248, 70));
            g2.fillRoundRect(0, 0, getWidth() / 2, getHeight(), 24, 24);
            g2.fillRect(getWidth() / 2 - 20, 0, 20, getHeight()); // Meratakan sisi tengah card

            // Highlight Kilauan Sudut (Simulasi React-glass specular reflection)
            GradientPaint glassGlow = new GradientPaint(
                0, 0, new Color(255, 255, 255, 180),
                getWidth(), getHeight(), new Color(255, 255, 255, 10)
            );
            g2.setPaint(glassGlow);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);

            g2.dispose();
        }
    }

    /** Border rounded kustom */
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                             w - thickness - 1, h - thickness - 1,
                             radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
        }
    }

    // =========================================================
    // Main Entry Point
    // =========================================================
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            ClinicaSyncLogin frame = new ClinicaSyncLogin();
            frame.setVisible(true);
        });
    }
}