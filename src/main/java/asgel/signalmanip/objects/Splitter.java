package asgel.signalmanip.objects;

import asgel.core.gfx.Direction;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class Splitter extends ModelOBJ {

	protected Splitter(int x, int y, int outputs, int size) {
		super("Splitter", "Spl", x, y, 48, 16 * outputs + 16, outputs + 1);
		pins[0] = new Pin(this, Direction.WEST, size, "IN", true);
		for (int i = 0; i < outputs; i++) {
			pins[i + 1] = new Pin(this, Direction.EAST, size, "OUT " + i, false);
		}
	}

	@Override
	public void update() {
		for (int i = 1; i < pins.length; i++) {
			for (int j = 0; j < pins[0].getSize(); j++) {
				pins[i].getData()[j] = pins[0].getData()[j];
			}
		}
	}

	public static Splitter askFor(int x, int y, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Output Pins", "Data Size");
		if (params == null)
			return null;
		return new Splitter(x, y, params[0], params[1]);
	}

}