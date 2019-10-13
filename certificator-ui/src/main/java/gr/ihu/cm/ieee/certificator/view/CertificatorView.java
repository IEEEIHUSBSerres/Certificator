package gr.ihu.cm.ieee.certificator.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import gr.ihu.cm.ieee.certificator.common.Constants;
import gr.ihu.cm.ieee.certificator.domain.Event;
import gr.ihu.cm.ieee.certificator.domain.Person;
import gr.ihu.cm.ieee.certificator.service.certificator.CertificatorServiceImpl;
import gr.ihu.cm.ieee.certificator.service.file.FileServiceImpl;
import gr.ihu.cm.ieee.certificator.service.pdf.PdfService;
import gr.ihu.cm.ieee.certificator.service.pdf.PdfServiceImpl;
import gr.ihu.cm.ieee.certificator.service.person.PersonService;
import gr.ihu.cm.ieee.certificator.service.person.PersonServiceImpl;
import io.vavr.control.Try;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;

public class CertificatorView extends JFrame {

    private JPanel jPanel;
    private JTextField templatePdfField;
    private JTextField participantsField;
    private JButton selectTemplatePDFButton;
    private JButton selectParticipantsButton;
    private JButton generateButton;
    private JTextField exportPathField;
    private JButton selectExportPathButton;
    private JTextField eventNameField;

    private final PersonService personService;
    private final PdfService pdfService;

    public CertificatorView() throws HeadlessException {
        setContentPane(jPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setTitle(
                String.format(
                        "%s %s | %s",
                        Constants.NAME_OF_PROJECT,
                        Constants.VERSION_OF_PROJECT,
                        Constants.DEVELOPER_TEAM_OF_PROJECT
                )
        );

        pack();
        setVisible(true);

        this.personService = new PersonServiceImpl(new FileServiceImpl());
        this.pdfService = new PdfServiceImpl();

        addClickHandlers();
    }

    private void addClickHandlers() {
        selectTemplatePDFButton.addActionListener(
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectTemplatePdf();
                    }
                }
        );

        selectParticipantsButton.addActionListener(
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectParticipants();
                    }
                }
        );

        selectExportPathButton.addActionListener(
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectExportPath();
                    }
                }
        );

        generateButton.addActionListener(
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        generateCertificates();
                    }
                }
        );
    }

    public void selectTemplatePdf() {
        final JFileChooser chooser = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "PDF Files",
                "pdf"
        );

        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            templatePdfField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void selectParticipants() {
        final JFileChooser chooser = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TXT Files",
                "txt"
        );

        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            participantsField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void selectExportPath() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            exportPathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void generateCertificates() {
        personService.fetchPersonsFromFilePath(participantsField.getText())
                .flatMap(people -> Try
                        .of(Event::builder)
                        .peek(eventBuilder -> eventBuilder
                                .name(eventNameField.getText()))
                        .peek(eventBuilder -> eventBuilder
                                .attendants(new HashSet<Person>(people)))
                        .map(Event.EventBuilder::build)
                        .flatMap(event -> Try
                                .of(() -> new CertificatorServiceImpl(
                                        templatePdfField.getText(),
                                        exportPathField.getText(),
                                        pdfService
                                )).peek(certificatorService -> certificatorService
                                        .createCertificates(event))));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jPanel = new JPanel();
        jPanel.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        jPanel.setMinimumSize(new Dimension(300, 300));
        final JLabel label1 = new JLabel();
        label1.setText("Template PDF");
        jPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Participants");
        jPanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        templatePdfField = new JTextField();
        templatePdfField.setEditable(false);
        jPanel.add(templatePdfField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        participantsField = new JTextField();
        participantsField.setEditable(false);
        jPanel.add(participantsField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectTemplatePDFButton = new JButton();
        selectTemplatePDFButton.setText("Select Template PDF");
        jPanel.add(selectTemplatePDFButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectParticipantsButton = new JButton();
        selectParticipantsButton.setText("Select Participants");
        jPanel.add(selectParticipantsButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generateButton = new JButton();
        generateButton.setText("Generate");
        jPanel.add(generateButton, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Export Path");
        jPanel.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportPathField = new JTextField();
        exportPathField.setEditable(false);
        jPanel.add(exportPathField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectExportPathButton = new JButton();
        selectExportPathButton.setText("Select Export Path");
        jPanel.add(selectExportPathButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Event Name");
        jPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eventNameField = new JTextField();
        jPanel.add(eventNameField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jPanel;
    }

}
