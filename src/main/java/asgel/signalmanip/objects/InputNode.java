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
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class InputNode extends ModelOBJ {

	private int id;

	protected InputNode(int x, int y, int size, int id) {
		super("IN " + id, "IN " + id, x, y, 48, 48, 1);
		pins[0] = new Pin(this, Direction.EAST, size, "IN " + id, false);
		this.id = id;
	}

	@Override
	public void update() {

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

	public static InputNode askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Data Size", "ID");
		if (params == null)
			return null;
		return new InputNode((int) p.x, (int) p.y, params[0], params[1]);
	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
		json.addProperty("id", id);
	}

	public static InputNode fromJson(JsonObject json) {
		return new InputNode(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt(),
				json.get("id").getAsInt());
	}

}