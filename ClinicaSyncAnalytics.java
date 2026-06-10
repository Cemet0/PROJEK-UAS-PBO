import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * ClinicaSync - Analytics & Room Management (Java Swing)
 * Konversi dari HTML ke Java Swing GUI
 * Lanjutan dari ClinicaSyncLogin.java & ClinicaSyncDashboard.java
 */
public class ClinicaSyncAnalytics extends JFrame {

    // =========================================================
    // Warna Tema ClinicaSync (konsisten dengan file sebelumnya)
    // =========================================================
    static final Color C_PRIMARY           = new Color(0xa7, 0x36, 0x46);
    static final Color C_PRIMARY_CONT      = new Color(0xf6, 0x72, 0x80);
    static final Color C_PRIMARY_FIXED     = new Color(0xff, 0xda, 0xdb);
    static final Color C_ON_PRIMARY_CONT   = new Color(0x6c, 0x05, 0x1f);
    static final Color C_ON_PRIMARY_FIXED_VAR = new Color(0x87, 0x1d, 0x30);
    static final Color C_SECONDARY         = new Color(0x4a, 0x5c, 0x93);
    static final Color C_SECONDARY_CONT    = new Color(0xad, 0xbf, 0xfd);
    static final Color C_ON_SECONDARY_CONT = new Color(0x3a, 0x4c, 0x83);
    static final Color C_TERTIARY          = new Color(0x00, 0x6d, 0x3d);
    static final Color C_TERTIARY_CONT     = new Color(0x23, 0xb2, 0x6a);
    static final Color C_TERTIARY_FIXED    = new Color(0x7a, 0xfb, 0xab);
    static final Color C_ON_TERTIARY_CONT  = new Color(0x00, 0x3d, 0x1f);
    static final Color C_ON_TERTIARY_FIXED = new Color(0x00, 0x52, 0x2c);
    static final Color C_BACKGROUND        = new Color(0xf6, 0xfa, 0xff);
    static final Color C_SURFACE           = new Color(0xf6, 0xfa, 0xff);
    static final Color C_SURFACE_CONT      = new Color(0xe6, 0xef, 0xf8);
    static final Color C_SURFACE_CONT_LOW  = new Color(0xec, 0xf5, 0xfe);
    static final Color C_SURFACE_CONT_HIGH = new Color(0xe0, 0xe9, 0xf2);
    static final Color C_SURFACE_HIGHEST   = new Color(0xdb, 0xe4, 0xed);
    static final Color C_SURFACE_LOWEST    = Color.WHITE;
    static final Color C_ON_SURFACE        = new Color(0x14, 0x1d, 0x23);
    static final Color C_ON_SURFACE_VAR    = new Color(0x57, 0x41, 0x42);
    static final Color C_OUTLINE_VAR       = new Color(0xdd, 0xbf, 0xc0);
    static final Color C_OUTLINE           = new Color(0x8a, 0x71, 0x72);
    static final Color C_ERROR             = new Color(0xba, 0x1a, 0x1a);
    static final Color C_ERROR_CONT        = new Color(0xff, 0xda, 0xd6);
    static final Color C_ON_ERROR_CONT     = new Color(0x93, 0x00, 0x0a);

    // =========================================================
    // Font
    // =========================================================
    static Font F_HEADLINE_LG, F_HEADLINE_MD, F_BODY_MD, F_BODY_SM, F_LABEL_MD, F_LABEL_SM;

    public ClinicaSyncAnalytics() {
        initFonts();
        initUI();
    }

    private void initFonts() {
        String p = isFontAvailable("Poppins")   ? "Poppins"   : "SansSerif";
        String o = isFontAvailable("Open Sans") ? "Open Sans" : "SansSerif";
        F_HEADLINE_LG = new Font(p, Font.BOLD,   24);
        F_HEADLINE_MD = new Font(p, Font.BOLD,   17);
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
    // ROOT UI
    // =========================================================
    private void initUI() {
        setTitle("ClinicaSync - Analytics & Room Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1150, 720));
        setPreferredSize(new Dimension(1380, 820));
        getContentPane().setBackground(C_BACKGROUND);

        // Layered pane: main layout + FAB overlay
        JLayeredPane layered = new JLayeredPane();
        layered.setLayout(new OverlayLayout(layered));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(C_BACKGROUND);
        root.add(buildSidebar(),   BorderLayout.WEST);
        root.add(buildMainArea(),  BorderLayout.CENTER);

        JPanel fabOverlay = buildFabOverlay();
        fabOverlay.setOpaque(false);

        layered.add(root,       JLayeredPane.DEFAULT_LAYER);
        layered.add(fabOverlay, JLayeredPane.POPUP_LAYER);

        setContentPane(layered);
        pack();
        setLocationRelativeTo(null);
    }

    // =========================================================
    // SIDEBAR (Analytics aktif)
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

        // Logo
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

        // Nav items — Analytics aktif
        Object[][] navItems = {
            {"📊", "Dashboard",   false},
            {"👤", "Patients",    false},
            {"📅", "Scheduling",  false},
            {"📋", "Queue",       false},
            {"💊", "Pharmacy",    false},
            {"💳", "Billing",     false},
            {"📈", "Analytics",   true },  // ACTIVE
        };
        for (Object[] item : navItems) {
            sidebar.add(buildNavItem((String) item[0], (String) item[1], (Boolean) item[2]));
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalStrut(12));

        // New Registration button
        JButton newRegBtn = buildPillButton("+ New Registration", C_PRIMARY, Color.WHITE);
        newRegBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        newRegBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(newRegBtn);

        sidebar.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setForeground(C_OUTLINE_VAR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(buildNavItem("🤖", "AI Assistant", false));
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(buildNavItem("⚙",  "Settings",     false));

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
        lbl.setFont(active ? new Font(F_LABEL_MD.getFamily(), Font.BOLD, 13) : F_LABEL_MD);
        lbl.setForeground(active ? C_ON_PRIMARY_CONT : C_ON_SURFACE_VAR);

        item.add(ico); item.add(lbl);

        if (!active) {
            item.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { item.setBackground(C_SURFACE_CONT_HIGH); item.setOpaque(true); item.repaint(); }
                @Override public void mouseExited (MouseEvent e) { item.setBackground(C_SURFACE_CONT_LOW); item.repaint(); }
            });
        }
        return item;
    }

    // =========================================================
    // MAIN AREA
    // =========================================================
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(C_BACKGROUND);
        main.add(buildTopBar(),  BorderLayout.NORTH);
        main.add(buildContent(), BorderLayout.CENTER);
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

        // Left
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
        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setText("Global clinic search...");
        searchField.setForeground(C_ON_SURFACE_VAR);
        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { if (searchField.getText().startsWith("Global")) { searchField.setText(""); searchField.setForeground(C_ON_SURFACE); } }
            @Override public void focusLost (FocusEvent e) { if (searchField.getText().isEmpty()) { searchField.setText("Global clinic search..."); searchField.setForeground(C_ON_SURFACE_VAR); } }
        });
        searchBox.add(searchIco); searchBox.add(searchField);
        left.add(title); left.add(searchBox);

        // Right
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setOpaque(false);

        // Notification btn
        JButton notifBtn = createIconBtn("🔔");
        JButton swapBtn  = createIconBtn("⇄");

        JPanel divider = new JPanel();
        divider.setPreferredSize(new Dimension(1, 28));
        divider.setBackground(C_OUTLINE_VAR);

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel("Dr. Moore");
        userName.setFont(F_LABEL_MD);
        userName.setForeground(C_ON_SURFACE);
        JLabel userRole = new JLabel("Lead Physician");
        userRole.setFont(F_LABEL_SM);
        userRole.setForeground(C_ON_SURFACE_VAR);
        userInfo.add(userName); userInfo.add(userRole);

        JLabel avatar = new JLabel("DM") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_PRIMARY_FIXED);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font(F_LABEL_MD.getFamily(), Font.BOLD, 12));
        avatar.setForeground(C_ON_PRIMARY_FIXED_VAR);
        avatar.setPreferredSize(new Dimension(38, 38));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(notifBtn); right.add(swapBtn); right.add(divider); right.add(userInfo); right.add(avatar);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ---- SCROLLABLE CONTENT ----
    private JScrollPane buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(C_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        // Page header + filter bar
        content.add(buildPageHeader());
        content.add(Box.createVerticalStrut(22));

        // Bento: summary cards (left) + room panel (right)
        content.add(buildBentoGrid());
        content.add(Box.createVerticalStrut(20));

        // Drug usage table
        content.add(buildDrugTable());
        content.add(Box.createVerticalStrut(32));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ---- PAGE HEADER + FILTER ----
    private JPanel buildPageHeader() {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        // Title block
        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
        JLabel pageTitle = new JLabel("Analytics & Logistics");
        pageTitle.setFont(F_HEADLINE_LG);
        pageTitle.setForeground(C_ON_SURFACE);
        JLabel pageSub   = new JLabel("System performance and room allocation real-time overview.");
        pageSub.setFont(F_BODY_MD);
        pageSub.setForeground(C_ON_SURFACE_VAR);
        textBlock.add(pageTitle);
        textBlock.add(Box.createVerticalStrut(2));
        textBlock.add(pageSub);

        // Filter bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        filterBar.setOpaque(false);

        JPanel filterWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        filterWrap.setBackground(C_SURFACE_CONT_LOW);
        filterWrap.setBorder(new CompoundBorder(
            new RoundedBorder(C_OUTLINE_VAR, 1, 12),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));

        filterWrap.add(buildFilterChip("📅", "Last 30 Days", true));
        filterWrap.add(buildFilterChip("🏢", "All Departments", false));

        JButton applyBtn = buildPillButton("⚙  Apply Filters", C_PRIMARY, Color.WHITE);
        applyBtn.setPreferredSize(new Dimension(140, 34));
        filterWrap.add(applyBtn);

        filterBar.add(filterWrap);

        row.add(textBlock, BorderLayout.WEST);
        row.add(filterBar,  BorderLayout.EAST);
        return row;
    }

    private JPanel buildFilterChip(String icon, String label, boolean hasDivider) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        chip.setOpaque(false);
        if (hasDivider) chip.setBorder(new MatteBorder(0, 0, 0, 1, C_OUTLINE_VAR));
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ico.setForeground(C_PRIMARY);
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL_MD);
        lbl.setForeground(C_ON_SURFACE);
        JLabel arr = new JLabel("▾");
        arr.setFont(new Font("SansSerif", Font.PLAIN, 11));
        arr.setForeground(C_ON_SURFACE_VAR);
        chip.add(ico); chip.add(lbl); chip.add(arr);
        chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return chip;
    }

    // =========================================================
    // BENTO GRID: Summary Cards + Chart (left) | Room Panel (right)
    // =========================================================
    private JPanel buildBentoGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.BOTH;
        c.weighty = 1.0;

        // LEFT COLUMN: summary cards + chart
        c.gridx = 0; c.weightx = 0.63;
        c.insets = new Insets(0, 0, 0, 16);
        grid.add(buildLeftColumn(), c);

        // RIGHT COLUMN: room availability
        c.gridx = 1; c.weightx = 0.37;
        c.insets = new Insets(0, 0, 0, 0);
        grid.add(buildRoomPanel(), c);

        return grid;
    }

    // ---- LEFT COLUMN ----
    private JPanel buildLeftColumn() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        // 3 summary cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
        cards.add(buildSummaryCard("💳", "Monthly Revenue",  "$142,580",  "+12.4%", true,  C_PRIMARY_FIXED,  C_ON_PRIMARY_FIXED_VAR));
        cards.add(buildSummaryCard("👥", "New Patients",     "1,248",     "+5.2%",  true,  C_SECONDARY_CONT, C_ON_SECONDARY_CONT));
        cards.add(buildSummaryCard("💊", "Drug Dispensed",   "4,892 units","-2.1%", false, C_TERTIARY_FIXED, C_ON_TERTIARY_FIXED));
        col.add(cards);
        col.add(Box.createVerticalStrut(16));

        // Growth chart
        col.add(buildGrowthChart());
        return col;
    }

    private JPanel buildSummaryCard(String icon, String label, String value,
                                     String trend, boolean up, Color iconBg, Color iconFg) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_SURFACE_LOWEST);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
            new RoundedBorder(C_OUTLINE_VAR, 1, 14),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        // Top row: icon + trend
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel iconLbl = new JLabel(icon) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
        iconLbl.setForeground(iconFg);
        iconLbl.setPreferredSize(new Dimension(38, 38));
        iconLbl.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel trendLbl = new JLabel((up ? "↑ " : "↓ ") + trend);
        trendLbl.setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 11));
        trendLbl.setForeground(up ? C_TERTIARY : C_ERROR);

        topRow.add(iconLbl, BorderLayout.WEST);
        topRow.add(trendLbl, BorderLayout.EAST);

        // Bottom: label + value
        card.add(topRow);
        card.add(Box.createVerticalStrut(10));

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(F_BODY_SM);
        labelLbl.setForeground(C_ON_SURFACE_VAR);
        labelLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font(F_HEADLINE_MD.getFamily(), Font.BOLD, 18));
        valueLbl.setForeground(C_ON_SURFACE);
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(labelLbl);
        card.add(Box.createVerticalStrut(2));
        card.add(valueLbl);
        return card;
    }

    // ---- GROWTH CHART ----
    private JPanel buildGrowthChart() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_SURFACE_LOWEST);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new CompoundBorder(
            new RoundedBorder(C_OUTLINE_VAR, 1, 14),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        // Chart header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel chartTitle = new JLabel("Patient Growth vs Revenue");
        chartTitle.setFont(F_HEADLINE_MD);
        chartTitle.setForeground(C_ON_SURFACE);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        legend.setOpaque(false);
        legend.add(buildLegendDot(C_PRIMARY,   "Revenue"));
        legend.add(buildLegendDot(C_SECONDARY, "Patients"));

        hdr.add(chartTitle, BorderLayout.WEST);
        hdr.add(legend,     BorderLayout.EAST);
        card.add(hdr, BorderLayout.NORTH);

        // Chart canvas
        BarChartPanel chartPanel = new BarChartPanel();
        card.add(chartPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildLegendDot(Color color, String label) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dot.setForeground(color);
        JLabel lbl = new JLabel(label);
        lbl.setFont(F_LABEL_SM);
        lbl.setForeground(C_ON_SURFACE_VAR);
        p.add(dot); p.add(lbl);
        return p;
    }

    // =========================================================
    // ROOM AVAILABILITY PANEL
    // =========================================================
    private JPanel buildRoomPanel() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_SURFACE_LOWEST);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new CompoundBorder(
            new RoundedBorder(C_OUTLINE_VAR, 1, 14),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel hdrTitle = new JLabel("Room Availability");
        hdrTitle.setFont(F_HEADLINE_MD);
        hdrTitle.setForeground(C_ON_SURFACE);

        JPanel legendRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        legendRow.setOpaque(false);
        legendRow.add(buildLegendDot(C_TERTIARY_CONT, "Free"));
        legendRow.add(buildLegendDot(C_PRIMARY_CONT,  "Busy"));

        hdr.add(hdrTitle,   BorderLayout.WEST);
        hdr.add(legendRow,  BorderLayout.EAST);
        card.add(hdr, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(12));

        // Scrollable room grid
        JPanel roomGrid = new JPanel();
        roomGrid.setLayout(new BoxLayout(roomGrid, BoxLayout.Y_AXIS));
        roomGrid.setOpaque(false);

        // Cardiology Wing
        roomGrid.add(buildWingLabel("Cardiology Wing"));
        roomGrid.add(Box.createVerticalStrut(6));
        roomGrid.add(buildRoomRow(new Object[][]{
            {"101","Available",true}, {"102","Occupied",false},
            {"103","Occupied",false}, {"104","Available",true}
        }));
        roomGrid.add(Box.createVerticalStrut(12));

        // Pediatrics
        roomGrid.add(buildWingLabel("Pediatrics"));
        roomGrid.add(Box.createVerticalStrut(6));
        roomGrid.add(buildRoomRow(new Object[][]{
            {"201","Available",true}, {"202","Available",true},
            {"203","Occupied",false}, {"204","Available",true}
        }));
        roomGrid.add(Box.createVerticalStrut(6));
        roomGrid.add(buildRoomRow(new Object[][]{
            {"205","Occupied",false}, {"206","Available",true},
            {"207","Available",true}, {"208","Available",true}
        }));
        roomGrid.add(Box.createVerticalStrut(12));

        // Urgent Care
        roomGrid.add(buildWingLabel("Urgent Care"));
        roomGrid.add(Box.createVerticalStrut(6));
        roomGrid.add(buildRoomRow(new Object[][]{
            {"U1","Critical",false}, {"U2","Cleaning",true}, {"U3","Occupied",false}
        }));

        JScrollPane roomScroll = new JScrollPane(roomGrid);
        roomScroll.setBorder(BorderFactory.createEmptyBorder());
        roomScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        roomScroll.getViewport().setBackground(C_SURFACE_LOWEST);
        roomScroll.setOpaque(false);
        roomScroll.getViewport().setOpaque(false);

        card.add(roomScroll, BorderLayout.CENTER);
        return card;
    }

    private JLabel buildWingLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 10));
        lbl.setForeground(new Color(C_ON_SURFACE_VAR.getRed(), C_ON_SURFACE_VAR.getGreen(), C_ON_SURFACE_VAR.getBlue(), 160));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel buildRoomRow(Object[][] rooms) {
        JPanel row = new JPanel(new GridLayout(1, 4, 8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        for (Object[] room : rooms) {
            row.add(buildRoomCell((String) room[0], (String) room[1], (Boolean) room[2]));
        }
        // Pad empty cells if fewer than 4
        for (int i = rooms.length; i < 4; i++) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            row.add(empty);
        }
        return row;
    }

    private JPanel buildRoomCell(String roomNum, String status, boolean available) {
        Color bg    = available ? C_TERTIARY_CONT : C_PRIMARY_CONT;
        Color fg    = available ? Color.WHITE : C_ON_PRIMARY_CONT;
        Color fgSub = available
            ? new Color(255, 255, 255, 200)
            : new Color(C_ON_PRIMARY_CONT.getRed(), C_ON_PRIMARY_CONT.getGreen(), C_ON_PRIMARY_CONT.getBlue(), 180);

        JPanel cell = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cell.setOpaque(false);
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setBorder(new CompoundBorder(
            new RoundedBorder(C_OUTLINE_VAR, 1, 12),
            BorderFactory.createEmptyBorder(8, 4, 8, 4)
        ));
        cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cell.setPreferredSize(new Dimension(60, 60));

        JLabel numLbl = new JLabel(roomNum);
        numLbl.setFont(new Font(F_HEADLINE_MD.getFamily(), Font.BOLD, 16));
        numLbl.setForeground(fg);
        numLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLbl = new JLabel(status.toUpperCase());
        statusLbl.setFont(new Font(F_LABEL_SM.getFamily(), Font.PLAIN, 9));
        statusLbl.setForeground(fgSub);
        statusLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        cell.add(numLbl);
        cell.add(statusLbl);

        // Hover scale effect (simulated via border change)
        cell.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                cell.setBorder(new CompoundBorder(
                    new RoundedBorder(C_ON_SURFACE, 2, 12),
                    BorderFactory.createEmptyBorder(8, 4, 8, 4)
                ));
                cell.repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                cell.setBorder(new CompoundBorder(
                    new RoundedBorder(C_OUTLINE_VAR, 1, 12),
                    BorderFactory.createEmptyBorder(8, 4, 8, 4)
                ));
                cell.repaint();
            }
            @Override public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(cell.getTopLevelAncestor(),
                    "Ruangan: " + roomNum + "\nStatus: " + status,
                    "Detail Ruangan", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return cell;
    }

    // =========================================================
    // DRUG USAGE TABLE
    // =========================================================
    private JPanel buildDrugTable() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_SURFACE_LOWEST);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new RoundedBorder(C_OUTLINE_VAR, 1, 14));

        // Table header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, C_OUTLINE_VAR),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        JLabel tableTitle = new JLabel("Recent Drug Usage & Inventory Alerts");
        tableTitle.setFont(F_HEADLINE_MD);
        tableTitle.setForeground(C_ON_SURFACE);
        JButton viewAll = new JButton("View Full Inventory");
        viewAll.setFont(F_LABEL_MD);
        viewAll.setForeground(C_PRIMARY);
        viewAll.setBorderPainted(false);
        viewAll.setContentAreaFilled(false);
        viewAll.setFocusPainted(false);
        viewAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hdr.add(tableTitle, BorderLayout.WEST);
        hdr.add(viewAll,    BorderLayout.EAST);
        card.add(hdr, BorderLayout.NORTH);

        // Drug data: {medication, department, stock, usagePct, status, isStable}
        Object[][] data = {
            {"Amoxicillin 500mg",  "General Practice", "1,240 units", 60,  "Stable",    true},
            {"Insulin Glargine",   "Endocrinology",    "85 units",    90,  "Low Stock",  false},
            {"Lisinopril 10mg",    "Cardiology",       "450 units",   40,  "Stable",    true},
        };

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setOpaque(false);
        tableWrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Build custom panel table (for progress bar column)
        JPanel tableBody = new JPanel();
        tableBody.setLayout(new BoxLayout(tableBody, BoxLayout.Y_AXIS));
        tableBody.setBackground(C_SURFACE_LOWEST);

        // Header row
        JPanel colHdr = buildTableHeaderRow();
        tableBody.add(colHdr);

        JSeparator sepHdr = new JSeparator();
        sepHdr.setForeground(C_OUTLINE_VAR);
        sepHdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        tableBody.add(sepHdr);

        for (Object[] row : data) {
            tableBody.add(buildDrugRow(row));
            JSeparator rowSep = new JSeparator();
            rowSep.setForeground(C_OUTLINE_VAR);
            rowSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            tableBody.add(rowSep);
        }

        tableWrap.add(tableBody, BorderLayout.CENTER);
        card.add(tableWrap, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTableHeaderRow() {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(C_SURFACE_CONT_LOW);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        String[] cols = {"Medication", "Department", "Stock Level", "Weekly Usage", "Status"};
        for (String col : cols) {
            JLabel lbl = new JLabel(col.toUpperCase());
            lbl.setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 10));
            lbl.setForeground(C_ON_SURFACE_VAR);
            lbl.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
            row.add(lbl);
        }
        return row;
    }

    private JPanel buildDrugRow(Object[] data) {
        String med    = (String) data[0];
        String dept   = (String) data[1];
        String stock  = (String) data[2];
        int pct       = (Integer) data[3];
        String status = (String) data[4];
        boolean stable = (Boolean) data[5];

        JPanel row = new JPanel(new GridLayout(1, 5)) {
            @Override protected void paintComponent(Graphics g) {
                if (getModel_() != null) {
                    g.setColor(C_SURFACE_CONT);
                } else {
                    g.setColor(C_SURFACE_LOWEST);
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
            private Object getModel_() { return null; } // trick for hover
        };
        row.setBackground(C_SURFACE_LOWEST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        row.setOpaque(true);

        // Hover effect
        row.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { row.setBackground(C_SURFACE_CONT); row.repaint(); }
            @Override public void mouseExited (MouseEvent e) { row.setBackground(C_SURFACE_LOWEST); row.repaint(); }
        });

        // Col 1: medication name
        JLabel medLbl = new JLabel(med);
        medLbl.setFont(new Font(F_BODY_SM.getFamily(), Font.BOLD, 12));
        medLbl.setForeground(C_ON_SURFACE);
        medLbl.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));

        // Col 2: department
        JLabel deptLbl = new JLabel(dept);
        deptLbl.setFont(F_BODY_SM);
        deptLbl.setForeground(C_ON_SURFACE_VAR);
        deptLbl.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));

        // Col 3: stock
        JLabel stockLbl = new JLabel(stock);
        stockLbl.setFont(F_BODY_SM);
        stockLbl.setForeground(C_ON_SURFACE);
        stockLbl.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));

        // Col 4: usage progress bar
        JPanel progressWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        progressWrap.setOpaque(false);
        ProgressBar pb = new ProgressBar(pct, stable ? C_TERTIARY : C_ERROR);
        pb.setPreferredSize(new Dimension(100, 8));
        progressWrap.add(pb);

        // Col 5: status badge
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        badgeWrap.setOpaque(false);
        JLabel badge = new JLabel(status) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = stable ? C_TERTIARY_FIXED : C_ERROR_CONT;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font(F_LABEL_SM.getFamily(), Font.BOLD, 11));
        badge.setForeground(stable ? C_ON_TERTIARY_FIXED : C_ON_ERROR_CONT);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        badge.setOpaque(false);
        badgeWrap.add(badge);

        row.add(medLbl);
        row.add(deptLbl);
        row.add(stockLbl);
        row.add(progressWrap);
        row.add(badgeWrap);
        return row;
    }

    // =========================================================
    // FLOATING ACTION BUTTON OVERLAY
    // =========================================================
    private JPanel buildFabOverlay() {
        JPanel overlay = new JPanel(null);
        overlay.setOpaque(false);

        JButton fab = new JButton("+") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? C_PRIMARY.brighter() : C_PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                // Shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        fab.setFont(new Font("SansSerif", Font.BOLD, 26));
        fab.setForeground(Color.WHITE);
        fab.setOpaque(false);
        fab.setContentAreaFilled(false);
        fab.setBorderPainted(false);
        fab.setFocusPainted(false);
        fab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fab.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Buka form pendaftaran pasien baru.", "New Registration", JOptionPane.INFORMATION_MESSAGE));

        overlay.add(fab);
        overlay.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int w = overlay.getWidth(), h = overlay.getHeight();
                fab.setBounds(w - 80, h - 80, 56, 56);
            }
        });
        return overlay;
    }

    // =========================================================
    // Helper Widgets
    // =========================================================
    private JButton buildPillButton(String text, Color bg, Color fg) {
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

    private JButton createIconBtn(String icon) {
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
        btn.setForeground(C_ON_SURFACE_VAR);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =========================================================
    // Inner Classes
    // =========================================================

    /** Bar chart custom canvas */
    static class BarChartPanel extends JPanel {
        private static final String[] MONTHS   = {"Jan","Feb","Mar","Apr","May","Jun"};
        private static final int[]    REVENUE  = {60, 65, 80, 75, 90, 85}; // % tinggi
        private static final int[]    PATIENTS = {40, 45, 50, 55, 65, 70};

        BarChartPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 260));
            setToolTipText("Hover bar untuk info");
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int padL = 36, padR = 16, padT = 16, padB = 28;
            int chartW = w - padL - padR;
            int chartH = h - padT - padB;

            // Grid lines
            g2.setColor(new Color(0x57, 0x41, 0x42, 30));
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10, new float[]{4, 4}, 0));
            for (int i = 0; i <= 4; i++) {
                int y = padT + (chartH * i / 4);
                g2.drawLine(padL, y, padL + chartW, y);
            }
            g2.setStroke(new BasicStroke(1));

            // Axes
            g2.setColor(C_OUTLINE_VAR);
            g2.drawLine(padL, padT, padL, padT + chartH);
            g2.drawLine(padL, padT + chartH, padL + chartW, padT + chartH);

            // Bars
            int n = MONTHS.length;
            int groupW = chartW / n;
            int barW   = groupW / 3;
            int barGap = 2;

            for (int i = 0; i < n; i++) {
                int groupX = padL + i * groupW + groupW / 8;

                // Revenue bar (primary)
                int revH = (int)(chartH * REVENUE[i] / 100.0);
                int revY = padT + chartH - revH;
                g2.setColor(C_PRIMARY);
                g2.fillRoundRect(groupX, revY, barW, revH, 4, 4);

                // Patients bar (secondary)
                int patH = (int)(chartH * PATIENTS[i] / 100.0);
                int patY = padT + chartH - patH;
                g2.setColor(C_SECONDARY);
                g2.fillRoundRect(groupX + barW + barGap, patY, barW, patH, 4, 4);

                // Month label
                g2.setColor(C_ON_SURFACE_VAR);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                FontMetrics fm = g2.getFontMetrics();
                int labelX = groupX + (barW * 2 + barGap) / 2 - fm.stringWidth(MONTHS[i]) / 2;
                g2.drawString(MONTHS[i], labelX, padT + chartH + 16);
            }

            // Y-axis labels
            g2.setColor(C_ON_SURFACE_VAR);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            for (int i = 0; i <= 4; i++) {
                int val = 100 - i * 25;
                int y   = padT + (chartH * i / 4);
                g2.drawString(val + "%", 2, y + 4);
            }

            g2.dispose();
        }
    }

    /** Progress bar custom */
    static class ProgressBar extends JPanel {
        private final int pct;
        private final Color barColor;
        ProgressBar(int pct, Color barColor) {
            this.pct = pct; this.barColor = barColor;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Track
            g2.setColor(C_SURFACE_HIGHEST);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            // Fill
            int fillW = (int)(getWidth() * pct / 100.0);
            g2.setColor(barColor);
            g2.fillRoundRect(0, 0, fillW, getHeight(), getHeight(), getHeight());
            g2.dispose();
        }
    }

    /** Border rounded kustom */
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness, radius;
        RoundedBorder(Color c, int t, int r) { color = c; thickness = t; radius = r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness/2, y + thickness/2, w - thickness - 1, h - thickness - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
        }
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
            ClinicaSyncAnalytics frame = new ClinicaSyncAnalytics();
            frame.setVisible(true);
        });
    }
}