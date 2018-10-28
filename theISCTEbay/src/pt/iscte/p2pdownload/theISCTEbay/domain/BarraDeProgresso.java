package pt.iscte.p2pdownload.theISCTEbay.domain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;

public class InterfaceGrafica extends JPanel implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private JProgressBar barraDeProgresso;
    private JButton botaoDescarregar;
    private JTextArea outputTarefa;
    private Task tarefa;
 
    class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
        	//--------------------------------------
            Random random = new Random();  //--> ver!
            //--------------------------------------
            
            int progresso = 0;
            //Inicializa a propriedade progress.
            setProgress(0);
            while (progresso < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //-->
                //Make random progress.
                progresso += random.nextInt(10);
                setProgress(Math.min(progresso, 100));
            }
            return null;
        }
        
 
        /*
         * Executado no evento de despachar a thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            botaoDescarregar.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            outputTarefa.append("Done!\n");
        }
    }
 
    public InterfaceGrafica() {
        super(new BorderLayout());
 
        //Create the demo's UI.
        botaoDescarregar = new JButton("Start");
        botaoDescarregar.setActionCommand("start");
        botaoDescarregar.addActionListener(this);
 
        barraDeProgresso = new JProgressBar(0, 100);
        barraDeProgresso.setValue(0);
        barraDeProgresso.setStringPainted(true);
 
        outputTarefa = new JTextArea(5, 20);
        outputTarefa.setMargin(new Insets(5,5,5,5));
        outputTarefa.setEditable(false);
 
        JPanel panel = new JPanel();
        panel.add(botaoDescarregar);
        panel.add(barraDeProgresso);
 
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(outputTarefa), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
    }
 
    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
        botaoDescarregar.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        tarefa = new Task();
        tarefa.addPropertyChangeListener(this);
        tarefa.execute();
    }
 
    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            barraDeProgresso.setValue(progress);
            outputTarefa.append(String.format(
                    "Completed %d%% of task.\n", tarefa.getProgress()));
        } 
    }
 
 
    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ProgressBarDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new InterfaceGrafica();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
