package asgel.signalmanip.objects;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import asgel.core.gfx.Point;
import asgel.core.model.GlobalRegistry;

/**
 * @author Florent Guille
 **/

@SuppressWarnings("serial")
public class ModelBoxDialog extends JDialog {

	// Result file
	private File result;

	// Data
	private Point p;
	private File workingDir;
	private GlobalRegistry regis;

	public ModelBoxDialog(JFrame frame, Point p, File workingDir, GlobalRegistry regis) {
		super(frame, "Model Box Selection", true);
		this.p = p;
		this.workingDir = workingDir;
		this.regis = regis;

		// Creating the panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = gbc.gridheight = 1;

		// Creating the tree

		JTree tree = new JTree(createNode(workingDir));
		tree.setPreferredSize(new Dimension(400, 600));
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		gbc.gridwidth = 2;
		panel.add(new JScrollPane(tree), gbc);

		// Adding buttons

		JButton load = new JButton("Load");
		load.addActionListener(e -> {
			setVisible(false);
			dispose();
		});

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(load, gbc);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> {
			result = null;
			setVisible(false);
			dispose();
		});

		tree.getSelectionModel().addTreeSelectionListener(e -> {
			DefaultMutableTreeNode sel = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			result = null;
			if (sel.getUserObject() instanceof FileHolder holder) {
				if (holder.f.getName().endsWith(".asglogmodbox"))
					this.result = holder.f;
			}
			load.setEnabled(result != null);
			System.out.println("Load: " + result);
		});
		load.setEnabled(false);

		gbc.gridx = 1;
		panel.add(cancel, gbc);
		setContentPane(panel);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				result = null;
			}

		});
	}

	private DefaultMutableTreeNode createNode(File file) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new FileHolder(file));
		for (File child : file.listFiles(f -> f.getName().endsWith(".asglogmodbox"))) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileHolder(child));
			node.add(childNode);
		}

		for (File dir : file.listFiles(f -> f.isDirectory())) {
			node.add(createNode(dir));
		}
		return node;
	}

	public ModelBox getResult() {
		System.out.println("Result: " + result);
		if (result == null)
			return null;
		return ModelBox.create(p, result, workingDir, regis);
	}

	private class FileHolder {

		File f;

		public FileHolder(File f) {
			this.f = f;
		}

		@Override
		public String toString() {
			return f.getName();
		}

	}

}