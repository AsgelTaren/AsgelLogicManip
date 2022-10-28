package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class Register extends ModelOBJ {

	protected Register(int x, int y, int size) {
		super("Register " + size + " bits", "R" + size + "b", x, y, 48, 64, 4);
		pins[0] = new Pin(this, Direction.WEST, size, "DATA_IN", true).setIsSensible(false);
		pins[1] = new Pin(this, Direction.SOUTH, 1, "CLK", true);
		pins[2] = new Pin(this, Direction.EAST, size, "DATA_OUT", false);
		pins[3] = new Pin(this, Direction.NORTH, 1, "CLEAR", true).setIsSensible(false);
	}

	@Override
	public void update() {
		if (pins[1].getData()[0]) {
			if (pins[3].getData()[0]) {
				pins[2].clearData();
			} else
				pins[2].setData(pins[0].getData());
		}
	}

	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
	}

	public static Register askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new Register((int) p.x, (int) p.y, params[0]);
	}

	public static Register fromJson(JsonObject json) {
		return new Register(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt());
	}

}
