package asgel.signalmanip.objects;

import javax.swing.JPanel;

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
		// res.setBorder(BorderFactory.createTitledBorder("ID and position"));

		return new JPanel[] { res };
	}

	public static InputNode askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Data Size", "ID");
		if (params == null)
			return null;
		return new InputNode((int) p.x, (int) p.y, params[0], params[1]);
	}

}