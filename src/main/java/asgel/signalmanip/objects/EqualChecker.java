package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class EqualChecker extends ModelOBJ {

	protected EqualChecker( int x, int y, int size) {
		super("Equal Checker", "=", x, y, 48, 48, 3);
		pins[0] = new Pin(this, Direction.WEST, size, "IN 1", true);
		pins[1] = new Pin(this, Direction.WEST, size, "IN 2", true);
		pins[2] = new Pin(this, Direction.EAST, 1, "OUT", false);
	}

	@Override
	public void update() {
		pins[2].getData()[0] = true;
		for (int i = 0; i < pins[0].getSize(); i++) {
			pins[2].getData()[0] &= (pins[0].getData()[i] == pins[1].getData()[i]);
		}
	}
	
	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
	}

	public static EqualChecker askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new EqualChecker((int) p.x, (int) p.y, params[0]);
	}

	public static EqualChecker fromJson(JsonObject json) {
		return new EqualChecker(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt());
	}

}
