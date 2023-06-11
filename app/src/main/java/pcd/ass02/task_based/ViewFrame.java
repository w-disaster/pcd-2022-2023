package pcd.ass02.task_based;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

import pcd.ass02.common.InputListener;

/**
 * 
 * GUI component of the view.
 * 
 * @author aricci
 *
 */
public class ViewFrame extends JFrame implements ActionListener {

	private JButton startButton;
	private JButton stopButton;
	private JButton chooseDir;
	
	private JTextField nMaxFilesToRank;
	private JTextField state;
	private JLabel selectedDir;
	private JTextField nBands;
	private JTextField maxLoc;
	
	private JTextArea sourceListArea;
	private JFileChooser startDirectoryChooser;
	private JLabel numSrcProcessed;
	private JPanel filesElabPanel;
	
	private File dir;
	private String selectedDirFullPath;
	
	private ArrayList<InputListener> listeners;

	public ViewFrame(String defStartDir, int defaultMaxFileToRank, int defaultNumBands, int defaultMaxLoc){
		super(".:: LoC Analyzer ::.");
		setSize(800,400);
		setLocation (100, 100);
		listeners = new ArrayList<InputListener>();
		
		startButton = new JButton("start");
		stopButton = new JButton("stop");
		chooseDir = new JButton("select dir");
		
		selectedDirFullPath = defStartDir;
		selectedDir = new JLabel(defStartDir);
		selectedDir.setSize(200,14);
		
		nMaxFilesToRank = new JTextField("" + defaultMaxFileToRank);
		nBands = new JTextField("" + defaultNumBands);
		maxLoc = new JTextField("" + defaultMaxLoc);
		
		numSrcProcessed = new JLabel("0");
		
		JPanel controlPanel1 = new JPanel();
		controlPanel1.add(chooseDir);
		controlPanel1.add(selectedDir);
		controlPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		controlPanel1.add(new JLabel("Num sources to view: "));
		controlPanel1.add(nMaxFilesToRank);
		controlPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		controlPanel1.add(new JLabel("Num Bands: "));
		controlPanel1.add(nBands);
		controlPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		controlPanel1.add(new JLabel("Max LoC: "));
		controlPanel1.add(maxLoc);
		
		JPanel controlPanel2 = new JPanel();
		controlPanel2.add(startButton);
		controlPanel2.add(stopButton);

		filesElabPanel = new JPanel();
		filesElabPanel.add(new JLabel("Num Sources Processed: "));
		filesElabPanel.add(numSrcProcessed);
		filesElabPanel.setEnabled(false);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(controlPanel1);
		controlPanel.add(controlPanel2);
		controlPanel.add(filesElabPanel);

				
		JPanel sourcesListPanel = new JPanel();
		sourceListArea = new JTextArea(15,40);
		sourcesListPanel.add(sourceListArea);
		sourceListArea.setEditable(false);
		JScrollPane scrollPane=new JScrollPane(sourcesListPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel infoPanel = new JPanel();
		state = new JTextField("ready.",40);
		state.setSize(700, 14);
		infoPanel.add(state);
		
		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.NORTH,controlPanel);
		cp.add(BorderLayout.CENTER,scrollPane);
		cp.add(BorderLayout.SOUTH, infoPanel);
		setContentPane(cp);		
		
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		chooseDir.addActionListener(this);
		
		this.startButton.setEnabled(true);
		this.stopButton.setEnabled(false);
		chooseDir.setEnabled(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	public void addListener(InputListener l){
		listeners.add(l);
	}
	
	public void actionPerformed(ActionEvent ev){
		Object src = ev.getSource();
		if (src == chooseDir) {
			startDirectoryChooser = new JFileChooser(new File("."));
			startDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int returnVal = startDirectoryChooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		        dir = startDirectoryChooser.getSelectedFile();
		    	selectedDirFullPath = dir.getAbsolutePath();
		        selectedDir.setText(dir.getName());
		     }
		} else if (src == startButton) {
			File dir = new File(selectedDirFullPath);
			int n = Integer.parseInt(nMaxFilesToRank.getText());			
			int nBands = Integer.parseInt(this.nBands.getText());			
			int maxLocInBand = Integer.parseInt(this.maxLoc.getText());			
			this.notifyStarted(dir, n, nBands, maxLocInBand);
			this.state.setText("Processing...");
			filesElabPanel.setEnabled(true);
			this.startButton.setEnabled(false);
			this.stopButton.setEnabled(true);
			chooseDir.setEnabled(false);
			
		} else if (src == stopButton) {
			this.notifyStopped();
			this.state.setText("Stopped.");

			this.startButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			chooseDir.setEnabled(true);
			filesElabPanel.setEnabled(false);
		}

	}

	private void notifyStarted(File dir, int nMaxFilesToRank, int nBands, int maxLoc){
		for (InputListener l: listeners){
			l.started(dir,  nMaxFilesToRank, nBands, maxLoc);
		}
	}
	
	private void notifyStopped(){
		for (InputListener l: listeners){
			l.stopped();
		}
	}
	
	public void update(long nSrcProcessed, ReportEntry[] entries) {
		SwingUtilities.invokeLater(() -> {
			numSrcProcessed.setText("" + nSrcProcessed);
			sourceListArea.setText("");
			for (int i = 0; i < entries.length; i++) {
				sourceListArea.append(entries[i].getSrcFileName() + " - " + entries[i].getNLoc() +  "\n");
			}
		});
	}
	
	public void done() {
		SwingUtilities.invokeLater(() -> {
			this.startButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			chooseDir.setEnabled(true);
			this.state.setText("Done.");
		});

	}

}
	
