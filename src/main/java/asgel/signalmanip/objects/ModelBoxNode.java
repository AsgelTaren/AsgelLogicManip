package asgel.signalmanip.objects;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

/**
 * @author Florent Guille
 **/

public class ModelBoxNode extends ModelOBJ {

	protected int id;

	protected ModelBoxNode(int x, int y, int size, int id, String name, Direction dir, boolean isInputNode) {
		super(name + " " + id, name + " " + id, x, y, 48, 48, 1);
		pins[0] = new Pin(this, dir, size, name + " " + id, !isInputNode);
		this.id = id;
	}

//	@Override
//	public void render(Renderer renderer, ModelOBJ highOBJ, Pin highPin, Pin anchor) {
//		renderer.push();
//		renderer.translate(x, y);
//
//		Point text = pins[0].getRealRotation().getInverse().asVec(60);
//		renderer.drawCenteredString(name, (width >> 1) + (int) text.x, (height >> 1) + (int) text.y, Color.BLACK);
//
//		renderer.applyRot(rot, new Point(width >> 1, height >> 1));
//		renderer.fillOval(0, 0, 48, pins[0].getSize() == 1 && pins[0].getData()[0] ? Color.CYAN : Color.gray);
//		if (highOBJ == this) {
//			renderer.drawOval(0, 0, 48, Color.GREEN);
//		}
//
//		for (Pin p : pins) {
//			p.render(renderer, highPin, anchor);
//
//			if (highOBJ == this || highPin == p) {
//				Point loc = p.getRotation().asVec(35).add(p.getPos());
//				renderer.drawCenteredString(p.toString(), (int) loc.x, (int) loc.y,
//						highPin == p ? Color.red : Color.BLACK);
//			}
//		}
//		renderer.pop();
//	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
		json.addProperty("id", id);
	}

	@SuppressWarnings("serial")
	@Override
	public JPanel[] getDetailsPanels() {
		JPanel res = new JPanel() {

			@Override
			public String toString() {
				return "Model Box";
			}
		};
		res.setBorder(BorderFactory.createTitledBorder("ID"));
		res.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.2;
		res.add(new JLabel("ID"), gbc);

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setAllowsInvalid(false);

		JFormattedTextField field = new JFormattedTextField(formatter);
		field.setText(id + "");
		field.addActionListener(e -> {
			this.id = Integer.parseInt(field.getText());
		});
		gbc.gridx = 1;
		gbc.weightx = 0.8;
		res.add(field, gbc);

		return new JPanel[] { res };
	}

	public int getID() {
		return id;
	}

}
