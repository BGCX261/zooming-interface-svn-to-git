import testers.AbstractTester;

class MainForm {
    public static void main(String[] args) {
        new AbstractTester("default.xml"){}.setVisible(true);
    }
}