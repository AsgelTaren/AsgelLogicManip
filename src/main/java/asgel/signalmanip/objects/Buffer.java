package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

/**
 * @author Florent Guille
 **/

public class Buffer extends ModelOBJ {

	protected Buffer(int x, int y, int size) {
		super("Buffer", "Buff", x, y, 48, 48, 3);
		pins[0] = new Pin(this, Direction.WEST, size, "IN", true);
		pins[1] = new Pin(this, Direction.EAST, size, "OUT", false);
		pins[2] = new Pin(this, Direction.SOUTH, 1, "ENABLE", true);
	}

	@Override
	public void update() {
		for (int i = 0; i < pins[1].getSize(); i++) {
			pins[1].getData()[i] = pins[0].getData()[i] && pins[2].getData()[0];
		}
	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
	}

	public static Buffer askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new Buffer((int) p.x, (int) p.y, params[0]);
	}

	public static Buffer fromJson(JsonObject json) {
		return new Buffer(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt());
	}

}