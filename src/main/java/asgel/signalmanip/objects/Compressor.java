package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class Compressor extends ModelOBJ {

	protected Compressor(int x, int y, int size) {
		super("Compressor", "C", x, y, 20, 32 + 16 * size, size + 1);
		pins[0] = new Pin(this, Direction.NORTH, size, "OUT", false);
		for (int i = 0; i < size; i++) {
			pins[i + 1] = new Pin(this, Direction.WEST, 1, "IN " + i, true);
		}
	}

	@Override
	public void update() {
		for (int i = 0; i < pins.length - 1; i++) {
			pins[0].getData()[i] = pins[i + 1].getData()[0];
		}
	}

	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
	}

	public static Compressor askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new Compressor((int) p.x, (int) p.y, params[0]);
	}

	public static Compressor fromJson(JsonObject json) {
		return new Compressor(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt());
	}
}
