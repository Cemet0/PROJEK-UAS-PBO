import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * ClinicaSync - Doctor Dashboard (Java Swing)
 * Konversi dari HTML ke Java Swing GUI
 * Lanjutan dari ClinicaSyncLogin.java
 */
public class ClinicaSyncDashboard extends JFrame {

    // =========================================================
    // Warna Tema ClinicaSync (sama dengan Login)
    // =========================================================
    static final Color C_PRIMARY           = new Color(0xa7, 0x36, 0x46);
    static final Color C_PRIMARY_CONT      = new Color(0xf6, 0x72, 0x80);
    static final Color C_ON_PRIMARY_CONT   = new Color(0x6c, 0x05, 0x1f);
    static final Color C_SECONDARY         = new Color(0x4a, 0x5c, 0x93);
    static final Color C_SECONDARY_CONT    = new Color(0xad, 0xbf, 0xfd);
    static final Color C_ON_SECONDARY_CONT = new Color(0x3a, 0x4c, 0x83);
    static final Color C_TERTIARY          = new Color(0x00, 0x6d, 0x3d);
    static final Color C_TERTIARY_CONT     = new Color(0x23, 0xb2, 0x6a);
    static final Color C_ON_TERTIARY_CONT  = new Color(0x00, 0x3d, 0x1f);
    static final Color C_BACKGROUND        = new Color(0xf6, 0xfa, 0xff);
    static final Color C_SURFACE           = new Color(0xf6, 0xfa, 0xff);
    static final Color C_SURFACE_CONT      = new Color(0xe6, 0xef, 0xf8);
    static final Color C_SURFACE_CONT_LOW  = new Color(0xec, 0xf5, 0xfe);
    static final Color C_SURFACE_HIGH      = new Color(0xe0, 0xe9, 0xf2);
    static final Color C_SURFACE_LOWEST    = Color.WHITE;
    static final Color C_ON_SURFACE        = new Color(0x14, 0x1d, 0x23);
    static final Color C_ON_SURFACE_VAR    = new Color(0x57, 0x41, 0x42);
    static final Color C_OUTLINE_VAR       = new Color(0xdd, 0xbf, 0xc0);
    static final Color C_OUTLINE           = new Color(0x8a, 0x71, 0x72);
    static final Color C_ERROR             = new Color(0xba, 0x1a, 0x1a);
    static final Color C_ERROR_CONT        = new Color(0xff, 0xda, 0xd6);

    // =========================================================
    // Font
    // =========================================================
    static Font F_HEADLINE_LG, F_HEADLINE_MD, F_BODY_MD, F_BODY_SM, F_LABEL_MD, F_LABEL_SM;

    // State AI Chat
    private JPanel chatWindow;
    private boolean chatOpen = false;
    private Timer dotTimer;
    private int dotCount = 0;

    public ClinicaSyncDashboard() {
        initFonts();
        initUI();
    }

    private void initFonts() {
        String p = isFontAvailable("Poppins")   ? "Poppins"   : "SansSerif";
        String o = isFontAvailable("Open Sans") ? "Open Sans" : "SansSerif";
        F_HEADLINE_LG = new Font(p, Font.BOLD,   26);
        F_HEADLINE_MD = new Font(p, Font.BOLD,   18);
        F_BODY_MD     = new Font(o, Font.PLAIN,  14);
        F_BODY_SM     = new Font(o, Font.PLAIN,  12);
        F_LABEL_MD    = new Font(p, Font.BOLD,   13);
        F_LABEL_SM    = new Font(p, Font.PLAIN,  11);
    }

    private boolean isFontAvailable(String name) {
        for (String f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
            if (f.equalsIgnoreCase(name)) return true;
        return false;
    }

    // =========================================================
    // Root UI
    // =========================================================
    private void initUI() {
        setTitle("ClinicaSync - Doctor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setPreferredSize(new Dimension(1300, 780));
        getContentPane().setBackground(C_BACKGROUND);

        // Layout: Sidebar kiri + Main kanan (menggunakan JSplitPane agar responsif)
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(C_BACKGROUND);
        root.add(buildSidebar(),  BorderLayout.WEST);
        root.add(buildMainArea(), BorderLayout.CENTER);

        // Layered pane untuk chat widget (floating button)
        JLayeredPane layered = new JLayeredPane();
        layered.setLayout(new OverlayLayout(layered));
        root.setAlignmentX(0f);
        root.setAlignmentY(0f);
        layered.add(root, JLayeredPane.DEFAULT_LAYER);

        JPanel chatOverlay = buildChatOverlay();
        chatOverlay.setOpaque(false);
        layered.add(chatOverlay, JLayeredPane.POPUP_LAYER);

        setContentPane(layered);
        pack();
        setLocationRelativeTo(null);
    }

    // =========================================================
    // SIDEBAR
    // =========================================================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(C_SURFACE_CONT_LOW);
        sidebar.setPreferredSize(new Dimension(220, Integer.MAX_VALUE));
        sidebar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 0, 1, C_OUTLINE_VAR),
            BorderFactory.createEmptyBorder(20, 12, 16, 12)
        ));

        // Logo area
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoRow.setOpaque(false);
        logoRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JLabel logoIcon = new JLabel("✚") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoIcon.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setPreferredSize(new Dimension(40, 40));
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel logoText = new JPanel();
        logoText.setOpaque(false);
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        JLabel clinicName = new JLabel("Main Clinic");
        clinicName.setFont(new Font(F_HEADLINE_MD.getFamily(), Font.BOLD, 15));
        clinicName.setForeground(C_ON_SURFACE);
        JLabel clinicRole = new JLabel("Administrator Access");
        clinicRole.setFont(F_LABEL_SM);
        clinicRole.setForeground(C_ON_SURFACE_VAR);
        logoText.add(clinicName);
        logoText.add(clinicRole);

        logoRow.add(logoIcon);
        logoRow.add(logoText);
        sidebar.add(logoRow);
        sidebar.add(Box.createVerticalStrut(20));

        // Nav items
        String[][] navItems = {
            {"📊", "Dashboard",   "true"},
            {"👤", "Patients",    "false"},
            {"📅", "Scheduling",  "false"},
            {"📋", "Queue",       "false"},
            {"💊", "Pharmacy",    "false"},
            {"💳", "Billing",     "false"},
            {"📈", "Analytics",   "false"},
        };
        for (String[] item : navItems) {
            sidebar.add(buildNavItem(item[0], item[1], "true".equals(item[2])));
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());

        // Bottom divider
        JSeparator sep = new JSeparator();
        sep.setForeground(C_OUTLINE_VAR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));

        // New Registration button
        JButton newRegBtn = createPillButton("+ New Registration", C_PRIMARY, Color.WHITE);
        newRegBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        newRegBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(newRegBtn);
        sidebar.add(Box.createVerticalStrut(6));

        // AI + Settings
        sidebar.add(buildNavItem("🤖", "AI Assistant", false));
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(buildNavItem("⚙", "Settings", false));

        return sidebar;
    }

    private JPanel buildNavItem(String icon, String label, boolean active) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6)) {
            @Override protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(C_PRIMARY_CONT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        item.setOpaque(!active);
        if (!active) item.setBackground(C_SURFACE_CONT_LOW);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL_MD);
        lbl.setForeground(active ? C_ON_PRIMARY_CONT : C_ON_SURFACE_VAR);

        item.add(ico);
        item.add(lbl);

        if (!active) {
            item.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { item.setBackground(C_SURFACE_HIGH); item.setOpaque(true); item.repaint(); }
                @Override public void mouseExited (MouseEvent e) { item.setBackground(C_SURFACE_CONT_LOW); item.repaint(); }
            });
        }
        return item;
    }

    // =========================================================
    // MAIN AREA (TopBar + Dashboard Content)
    // =========================================================
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(C_BACKGROUND);
        main.add(buildTopBar(),       BorderLayout.NORTH);
        main.add(buildDashboard(),    BorderLayout.CENTER);
        return main;
    }

    // ---- TOP BAR ----
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(C_SURFACE);
        bar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 58));
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, C_OUTLINE_VAR),
            BorderFactory.createEmptyBorder(0, 28, 0, 28)
        ));

        // Left: Title + Search
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        left.setOpaque(false);
        JLabel title = new JLabel("ClinicaSync");
        title.setFont(new Font(F_HEADLINE_MD.getFamily(), Font.BOLD, 20));
        title.setForeground(C_PRIMARY);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        searchBox.setBackground(C_SURFACE_CONT_LOW);
        searchBox.setBorder(new RoundedBorder(C_OUTLINE_VAR, 1, 20));
        searchBox.setPreferredSize(new Dimension(320, 34));
        JLabel searchIco = new JLabel("🔍");
        searchIco.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JTextField searchField = new JTextField(20);
        searchField.setFont(F_BODY_SM);
        searchField.setForeground(C_ON_SURFACE_VAR);
        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setBackground(new Color(0,0,0,0));
        // Placeholder trick
        searchField.setText("Search patients, records or appointments...");
        searchField.setForeground(C_ON_SURFACE_VAR);
        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (searchField.getText().startsWith("Search")) { searchField.setText(""); searchField.setForeground(C_ON_SURFACE); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) { searchField.setText("Search patients, records or appointments..."); searchField.setForeground(C_ON_SURFACE_VAR); }
            }
        });
        searchBox.add(searchIco);
        searchBox.add(searchField);
        left.add(title);
        left.add(searchBox);

        // Right: Notif + user
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setOpaque(false);

        JButton notifBtn = createIconButton("🔔");
        JButton swapBtn  = createIconButton("⇄");

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel("Dr. Julianne Moore");
        userName.setFont(F_LABEL_MD);
        userName.setForeground(C_ON_SURFACE);
        JLabel userRole = new JLabel("Cardiologist");
        userRole.setFont(F_LABEL_SM);
        userRole.setForeground(C_ON_SURFACE_VAR);
        userInfo.add(userName);
        userInfo.add(userRole);

        // Avatar circle
        JLabel avatar = new JLabel("JM") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_SECONDARY_CONT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font(F_LABEL_MD.getFamily(), Font.BOLD, 13));
        avatar.setForeground(C_ON_SECONDARY_CONT);
        avatar.setPreferredSize(new Dimension(38, 38));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(notifBtn);
        right.add(swapBtn);
        right.add(userInfo);
        right.add(avatar);

        bar.add(left,  BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ---- DASHBOARD CONTENT ----
    private JScrollPane buildDashboard() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(C_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        // 1. Welcome + Call Next
        content.add(buildWelcomeSection());
        content.add(Box.createVerticalStrut(20));

        // 2. Metric Cards
        content.add(buildMetricCards());
        content.add(Box.createVerticalStrut(20));

        // 3. Main Grid: Queue + Side Panel
        content.add(buildMainGrid());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        return scroll;
    }

    // ---- Welcome Section ----
    private JPanel buildWelcomeSection() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
        JLabel greeting = new JLabel("Good Morning, Dr. Moore 👋");
        greeting.setFont(F_HEADLINE_LG);
        greeting.setForeground(C_ON_SURFACE);
        JLabel subtext = new JLabel("You have 12 patients remaining in today's queue.");
        subtext.setFont(F_BODY_MD);
        subtext.setForeground(C_ON_SURFACE_VAR);
        textBlock.add(greeting);
        textBlock.add(Box.createVerticalStrut(4));
        textBlock.add(subtext);

        // Call Next Button
        JButton callNext = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? C_PRIMARY_CONT.darker() : C_PRIMARY_CONT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        callNext.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        callNext.setOpaque(false);
        callNext.setContentAreaFilled(false);
        callNext.setBorderPainted(false);
        callNext.setFocusPainted(false);
        callNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        callNext.setPreferredSize(new Dimension(220, 64));

        JLabel arrow = new JLabel("▶");
        arrow.setFont(new Font("SansSerif", Font.BOLD, 22));
        arrow.setForeground(C_ON_PRIMARY_CONT);

        JPanel btnText = new JPanel();
        btnText.setOpaque(false);
        btnText.setLayout(new BoxLayout(btnText, BoxLayout.Y_AXIS));
        JLabel nextLabel  = new JLabel("NEXT IN QUEUE");
        nextLabel.setFont(new Font(F_LABEL_SM.getFamily(), Font.PLAIN, 10));
        nextLabel.setForeground(new Color(C_ON_PRIMARY_CONT.getRed(), C_ON_PRIMARY_CONT.getGreen(), C_ON_PRIMARY_CONT.getBlue(), 180));
        JLabel callLabel  = new JLabel("Call Next Patient");
        callLabel.setFont(new Font(F_HEADLINE_MD.getFamily(), Font.BOLD, 16));
        callLabel.setForeground(C_ON_PRIMARY_CONT);
        btnText.add(nextLabel);
        btnText.add(callLabel);

        callNext.add(arrow);
        callNext.add(btnText);
        callNext.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "📢  Memanggil pasien berikutnya...\n\nSarah Connor — Nomor Antrian #01",
            "Call Next Patient", JOptionPane.INFORMATION_MESSAGE));

        row.add(textBlock, BorderLayout.WEST);
        row.add(callNext, BorderLayout.EAST);
        return row;
    }

    // ---- Metric Cards ----
    private JPanel buildMetricCards() {
        JPanel grid = new JPanel(new GridLayout(1, 3, 16, 0));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));

        grid.add(buildMetricCard("👥", "Total Patients Today",     "34", C_SECONDARY_CONT,    C_SECONDARY));
        grid.add(buildMetricCard("📋", "Pending Prescriptions",    "08", C_TERTIARY_CONT,     C_TERTIARY));
        grid.add(buildMetricCard("📅", "Upcoming Appointments",    "12", C_PRIMARY_CONT,      C_PRIMARY));
        return grid;
    }

    private JPanel buildMetricCard(String icon, String label, String value, Color bgColor, Color iconColor) {
        JPanel card = new BentoCard();
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 18));

        // Icon circle
        JLabel ico = new JLabel(icon) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 50);
                g2.setColor(c);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ico.setFont(new Font("SansSerif", Font.PLAIN, 24));
        ico.setPreferredSize(new Dimension(52, 52));
        ico.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL_SM);
        lbl.setForeground(C_ON_SURFACE_VAR);
        JLabel val = new JLabel(value);
        val.setFont(new Font(F_HEADLINE_LG.getFamily(), Font.BOLD, 28));
        val.setForeground(C_ON_SURFACE);
        txt.add(lbl);
        txt.add(val);

        card.add(ico);
        card.add(txt);
        return card;
    }

    // ---- Main Grid: Queue Table + Side Panel ----
    private JPanel buildMainGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 0);

        // Queue table (left, ~2/3)
        c.gridx = 0; c.weightx = 0.65;
        c.insets = new Insets(0, 0, 0, 16);
        grid.add(buildQueuePanel(), c);

        // Side panel (right, ~1/3)
        c.gridx = 1; c.weightx = 0.35;
        c.insets = new Insets(0, 0, 0, 0);
        grid.add(buildSidePanel(), c);

        return grid;
    }

    // ---- Queue Monitor ----
    private JPanel buildQueuePanel() {
        JPanel card = new BentoCard();
        card.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, C_OUTLINE_VAR),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        JLabel title = new JLabel("📋  Real-time Queue Monitor");
        title.setFont(new Font(F_HEADLINE_MD.getFamily(), Font.BOLD, 16));
        title.setForeground(C_ON_SURFACE);

        JPanel liveTag = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        liveTag.setOpaque(false);
        // Animated live dot
        JLabel liveDot = new JLabel("●");
        liveDot.setFont(new Font("SansSerif", Font.PLAIN, 11));
        liveDot.setForeground(C_TERTIARY);
        JLabel liveTxt = new JLabel("Live Syncing");
        liveTxt.setFont(F_LABEL_SM);
        liveTxt.setForeground(C_ON_SURFACE_VAR);
        liveTag.add(liveDot);
        liveTag.add(liveTxt);

        // Animate live dot (blink)
        Timer blink = new Timer(800, e -> {
            liveDot.setForeground(liveDot.getForeground().equals(C_TERTIARY) ? C_SURFACE_CONT : C_TERTIARY);
        });
        blink.start();

        header.add(title, BorderLayout.WEST);
        header.add(liveTag, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"Pos", "Patient Name", "Check-in", "Est. Wait", "Status"};
        Object[][] data = {
            {"#01", "Sarah Connor",  "08:45 AM", "0 mins",   "Ready"},
            {"#02", "James Smith",   "08:52 AM", "12 mins",  "Arrived"},
            {"#03", "Ellen Ripley",  "09:10 AM", "25 mins",  "Waiting"},
            {"#04", "Max Mad",       "09:15 AM", "40 mins",  "Waiting"},
        };
        JTable table = new JTable(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                comp.setBackground(isRowSelected(row) ? C_SURFACE_HIGH : C_SURFACE_LOWEST);
                comp.setForeground(C_ON_SURFACE);
                return comp;
            }
        };
        table.setFont(F_BODY_SM);
        table.setRowHeight(46);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(C_SURFACE_LOWEST);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(C_SURFACE_HIGH);
        table.getTableHeader().setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 11));
        table.getTableHeader().setBackground(C_SURFACE_CONT_LOW);
        table.getTableHeader().setForeground(C_ON_SURFACE_VAR);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        // Custom cell renderer untuk kolom Status (badge warna)
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel badge = new JLabel(val.toString());
                badge.setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 10));
                badge.setHorizontalAlignment(SwingConstants.CENTER);
                badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
                badge.setOpaque(true);
                String status = val.toString();
                switch (status) {
                    case "Ready":
                        badge.setBackground(new Color(C_TERTIARY_CONT.getRed(), C_TERTIARY_CONT.getGreen(), C_TERTIARY_CONT.getBlue(), 50));
                        badge.setForeground(C_ON_TERTIARY_CONT);
                        break;
                    case "Arrived":
                        badge.setBackground(new Color(C_SECONDARY_CONT.getRed(), C_SECONDARY_CONT.getGreen(), C_SECONDARY_CONT.getBlue(), 60));
                        badge.setForeground(C_ON_SECONDARY_CONT);
                        break;
                    default:
                        badge.setBackground(new Color(C_SURFACE_CONT.getRed(), C_SURFACE_CONT.getGreen(), C_SURFACE_CONT.getBlue(), 120));
                        badge.setForeground(C_ON_SURFACE_VAR);
                }
                JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
                wrap.setOpaque(false);
                wrap.add(badge);
                return wrap;
            }
        });

        // Padding renderer untuk semua kolom
        DefaultTableCellRenderer padRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                return c;
            }
        };
        for (int i = 0; i < 4; i++) table.getColumnModel().getColumn(i).setCellRenderer(padRenderer);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        tableScroll.getViewport().setBackground(C_SURFACE_LOWEST);
        card.add(tableScroll, BorderLayout.CENTER);

        // Footer button
        JButton viewFull = new JButton("View Full Queue");
        viewFull.setFont(F_LABEL_MD);
        viewFull.setForeground(C_PRIMARY);
        viewFull.setBackground(C_SURFACE_LOWEST);
        viewFull.setBorderPainted(false);
        viewFull.setFocusPainted(false);
        viewFull.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewFull.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        viewFull.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { viewFull.setBackground(new Color(C_PRIMARY_CONT.getRed(), C_PRIMARY_CONT.getGreen(), C_PRIMARY_CONT.getBlue(), 30)); }
            @Override public void mouseExited (MouseEvent e) { viewFull.setBackground(C_SURFACE_LOWEST); }
        });

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(C_SURFACE_LOWEST);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, C_OUTLINE_VAR));
        footer.add(viewFull, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    // ---- Side Panel (Consult Card + Lab Notifications) ----
    private JPanel buildSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        panel.add(buildCurrentConsultCard());
        panel.add(Box.createVerticalStrut(16));
        panel.add(buildLabNotifications());
        return panel;
    }

    private JPanel buildCurrentConsultCard() {
        JPanel card = new BentoCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Header row
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JLabel title = new JLabel("CURRENTLY IN CONSULT");
        title.setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 11));
        title.setForeground(C_ON_SURFACE);
        JLabel verified = new JLabel("✓");
        verified.setFont(new Font("SansSerif", Font.BOLD, 14));
        verified.setForeground(C_TERTIARY);
        hdr.add(title, BorderLayout.WEST);
        hdr.add(verified, BorderLayout.EAST);
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        card.add(hdr);
        card.add(Box.createVerticalStrut(12));

        // Patient info row
        JPanel patientRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        patientRow.setOpaque(false);
        patientRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));

        // Avatar placeholder
        JLabel avt = new JLabel("AP") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_PRIMARY_CONT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avt.setFont(new Font(F_LABEL_MD.getFamily(), Font.BOLD, 16));
        avt.setForeground(C_ON_PRIMARY_CONT);
        avt.setPreferredSize(new Dimension(60, 60));
        avt.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel patName = new JLabel("Amelia Pond");
        patName.setFont(new Font(F_HEADLINE_MD.getFamily(), Font.BOLD, 16));
        patName.setForeground(C_ON_SURFACE);
        JLabel patId = new JLabel("ID: #CX-88201");
        patId.setFont(F_BODY_SM);
        patId.setForeground(C_ON_SURFACE_VAR);
        info.add(patName);
        info.add(patId);

        patientRow.add(avt);
        patientRow.add(info);
        card.add(patientRow);
        card.add(Box.createVerticalStrut(12));

        // Details
        card.add(buildDetailRow("Condition:", "Stable - Post Op"));
        card.add(Box.createVerticalStrut(6));
        card.add(buildDetailRow("Duration:",  "12:45 min"));
        card.add(Box.createVerticalStrut(14));

        // Action buttons
        JPanel btns = new JPanel(new GridLayout(1, 2, 8, 0));
        btns.setOpaque(false);
        btns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btns.add(createOutlineButton("Lab Results"));
        btns.add(createOutlineButton("EHR File"));
        card.add(btns);
        return card;
    }

    private JPanel buildDetailRow(String key, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        JLabel k = new JLabel(key);
        k.setFont(F_BODY_SM);
        k.setForeground(C_ON_SURFACE_VAR);
        JLabel v = new JLabel(value);
        v.setFont(new Font(F_BODY_SM.getFamily(), Font.BOLD, 12));
        v.setForeground(key.contains("Duration") ? C_PRIMARY : C_ON_SURFACE);
        row.add(k, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    private JPanel buildLabNotifications() {
        JPanel card = new BentoCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("⚠  URGENT LAB REPORTS");
        title.setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 11));
        title.setForeground(C_ON_SURFACE);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        card.add(title);
        card.add(Box.createVerticalStrut(12));

        // Critical alert
        JPanel critAlert = buildAlert(true,
            "Critical: Thomas Shelby",
            "Abnormal Hemoglobin level. Immediate review required.");
        critAlert.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        card.add(critAlert);
        card.add(Box.createVerticalStrut(10));

        // Info alert
        JPanel infoAlert = buildAlert(false,
            "Update: Rose Tyler",
            "Imaging results available for review.");
        infoAlert.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        card.add(infoAlert);
        return card;
    }

    private JPanel buildAlert(boolean critical, String title, String body) {
        JPanel alert = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color bg = critical
                    ? new Color(C_ERROR_CONT.getRed(), C_ERROR_CONT.getGreen(), C_ERROR_CONT.getBlue(), 50)
                    : C_SURFACE_CONT;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                Color border = critical ? C_ERROR : C_SECONDARY;
                g2.setColor(border);
                g2.fillRect(0, 0, 4, getHeight());
                g2.dispose();
            }
        };
        alert.setOpaque(false);
        alert.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 10));

        JLabel ico = new JLabel(critical ? "⚠" : "ℹ");
        ico.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ico.setForeground(critical ? C_ERROR : C_SECONDARY);

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setFont(new Font(F_LABEL_MD.getFamily(), Font.BOLD, 12));
        t.setForeground(critical ? C_ERROR : C_ON_SURFACE);
        JLabel b = new JLabel("<html><span style='font-size:11px'>" + body + "</span></html>");
        b.setFont(F_LABEL_SM);
        b.setForeground(critical ? new Color(C_ERROR.getRed(), C_ERROR.getGreen(), C_ERROR.getBlue(), 180) : C_ON_SURFACE_VAR);
        txt.add(t);
        txt.add(b);

        alert.add(ico, BorderLayout.WEST);
        alert.add(txt, BorderLayout.CENTER);
        return alert;
    }

    // =========================================================
    // AI CHAT OVERLAY (Floating Widget)
    // =========================================================
    private JPanel buildChatOverlay() {
        JPanel overlay = new JPanel(null); // absolute positioning
        overlay.setOpaque(false);

        // Chat Window
        chatWindow = buildChatWindow();
        chatWindow.setBounds(0, 0, 300, 360); // will reposition on show
        chatWindow.setVisible(false);
        overlay.add(chatWindow);

        // Floating Button
        JButton fab = new JButton("🤖") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? C_SURFACE_HIGH : Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(C_OUTLINE_VAR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        fab.setFont(new Font("SansSerif", Font.PLAIN, 26));
        fab.setOpaque(false);
        fab.setContentAreaFilled(false);
        fab.setBorderPainted(false);
        fab.setFocusPainted(false);
        fab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fab.setPreferredSize(new Dimension(60, 60));

        fab.addActionListener(e -> toggleChat(overlay, fab, chatWindow));

        // Position FAB bottom-right (repositioned on resize)
        overlay.add(fab);
        overlay.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int w = overlay.getWidth();
                int h = overlay.getHeight();
                fab.setBounds(w - 80, h - 80, 60, 60);
                chatWindow.setBounds(w - 320, h - 440, 300, 360);
            }
        });

        return overlay;
    }

    private JPanel buildChatWindow() {
        JPanel win = new JPanel(new BorderLayout());
        win.setBackground(Color.WHITE);
        win.setBorder(new CompoundBorder(
            new LineBorder(C_OUTLINE_VAR, 1, true),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Header
        JPanel hdr = new JPanel(new BorderLayout(8, 0));
        hdr.setBackground(C_PRIMARY);
        hdr.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 10));

        JPanel hdrLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        hdrLeft.setOpaque(false);
        JLabel botIco = new JLabel("🤖");
        botIco.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JPanel botInfo = new JPanel();
        botInfo.setOpaque(false);
        botInfo.setLayout(new BoxLayout(botInfo, BoxLayout.Y_AXIS));
        JLabel botName = new JLabel("ClinicaSync AI");
        botName.setFont(new Font(F_LABEL_MD.getFamily(), Font.BOLD, 13));
        botName.setForeground(Color.WHITE);
        JLabel botSub = new JLabel("Powered by AI");
        botSub.setFont(new Font(F_LABEL_SM.getFamily(), Font.PLAIN, 10));
        botSub.setForeground(new Color(255, 255, 255, 180));
        botInfo.add(botName);
        botInfo.add(botSub);
        hdrLeft.add(botIco);
        hdrLeft.add(botInfo);

        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setOpaque(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> { chatWindow.setVisible(false); chatOpen = false; });

        hdr.add(hdrLeft, BorderLayout.WEST);
        hdr.add(closeBtn, BorderLayout.EAST);
        win.add(hdr, BorderLayout.NORTH);

        // Messages area
        JPanel messages = new JPanel();
        messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
        messages.setBackground(C_SURFACE_LOWEST);
        messages.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        addChatBubble(messages, "Hello Dr. Moore. I've analyzed Amelia Pond's recent vitals. Would you like a summary?", false);
        addChatBubble(messages, "Yes, please. Also check if there are any contraindications for her new meds.", true);
        addChatBubble(messages, "Checking... No known contraindications found with current profile.", false);

        JScrollPane msgScroll = new JScrollPane(messages);
        msgScroll.setBorder(BorderFactory.createEmptyBorder());
        msgScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        win.add(msgScroll, BorderLayout.CENTER);

        // Input row
        JPanel inputRow = new JPanel(new BorderLayout(6, 0));
        inputRow.setBackground(Color.WHITE);
        inputRow.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, C_OUTLINE_VAR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JTextField input = new JTextField();
        input.setFont(F_BODY_SM);
        input.setBackground(C_SURFACE_CONT_LOW);
        input.setBorder(new CompoundBorder(
            new RoundedBorder(C_OUTLINE_VAR, 1, 10),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        input.setPreferredSize(new Dimension(0, 32));

        JButton sendBtn = new JButton("▶") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        sendBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setOpaque(false);
        sendBtn.setContentAreaFilled(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setFocusPainted(false);
        sendBtn.setPreferredSize(new Dimension(34, 32));
        sendBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ActionListener sendAction = e -> {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                addChatBubble(messages, text, true);
                input.setText("");
                messages.revalidate();
                // Simulate AI reply
                Timer t = new Timer(900, ev -> {
                    addChatBubble(messages, "Memproses permintaan Anda... Harap tunggu.", false);
                    messages.revalidate();
                    messages.repaint();
                });
                t.setRepeats(false);
                t.start();
            }
        };
        sendBtn.addActionListener(sendAction);
        input.addActionListener(sendAction);

        inputRow.add(input, BorderLayout.CENTER);
        inputRow.add(sendBtn, BorderLayout.EAST);
        win.add(inputRow, BorderLayout.SOUTH);

        return win;
    }

    private void addChatBubble(JPanel container, String text, boolean isUser) {
        JPanel bubble = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = isUser
                    ? new Color(C_PRIMARY_CONT.getRed(), C_PRIMARY_CONT.getGreen(), C_PRIMARY_CONT.getBlue(), 40)
                    : C_SURFACE_CONT_LOW;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        bubble.setOpaque(false);
        JLabel msg = new JLabel("<html><p style='width:180px'>" + text + "</p></html>");
        msg.setFont(F_BODY_SM);
        msg.setForeground(C_ON_SURFACE);
        msg.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        bubble.add(msg);
        bubble.setMaximumSize(new Dimension(220, 200));

        JPanel row = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        row.add(bubble);

        container.add(row);
    }

    private void toggleChat(JPanel overlay, JButton fab, JPanel chatWin) {
        chatOpen = !chatOpen;
        chatWin.setVisible(chatOpen);
        fab.setText(chatOpen ? "✕" : "🤖");
        overlay.repaint();
    }

    // =========================================================
    // Helper Widgets
    // =========================================================
    private JButton createIconButton(String icon) {
        JButton btn = new JButton(icon) {
            @Override protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(C_SURFACE_CONT);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setForeground(C_PRIMARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createPillButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_LABEL_MD);
        btn.setForeground(fg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(F_LABEL_SM);
        btn.setForeground(C_ON_SURFACE);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new RoundedBorder(C_OUTLINE, 1, 8));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 34));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(C_SURFACE_CONT); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(Color.WHITE); }
        });
        return btn;
    }

    // =========================================================
    // Inner Classes
    // =========================================================

    /** Card bento dengan hover shadow */
    static class BentoCard extends JPanel {
        BentoCard() {
            setBackground(Color.WHITE);
            setBorder(new LineBorder(new Color(0xDE, 0xE2, 0xE6), 1, true));
            setOpaque(true);
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { setBorder(new ShadowBorder()); }
                @Override public void mouseExited (MouseEvent e) { setBorder(new LineBorder(new Color(0xDE, 0xE2, 0xE6), 1, true)); }
            });
        }
    }

    /** Border rounded kustom (sama dengan Login) */
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness, radius;
        RoundedBorder(Color color, int thickness, int radius) {
            this.color = color; this.thickness = thickness; this.radius = radius;
        }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2, w - thickness - 1, h - thickness - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2); }
    }

    /** Shadow border sederhana untuk hover effect */
    static class ShadowBorder extends AbstractBorder {
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 18));
            for (int i = 0; i < 4; i++) {
                g2.drawRoundRect(x + i, y + i, w - i * 2 - 1, h - i * 2 - 1, 12, 12);
            }
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(4, 4, 4, 4); }
    }

    // =========================================================
    // Main
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
            ClinicaSyncDashboard frame = new ClinicaSyncDashboard();
            frame.setVisible(true);
        });
    }
}