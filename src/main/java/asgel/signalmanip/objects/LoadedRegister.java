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

public class LoadedRegister extends ModelOBJ {

	protected LoadedRegister(int x, int y, int size) {
		super("Loaded Register " + size + " bits", "LR" + size + "b", x, y, 48, 64, 5);
		pins[0] = new Pin(this, Direction.WEST, size, "DATA_IN", true).setIsSensible(false);
		pins[1] = new Pin(this, Direction.NORTH, 1, "LOAD", true).setIsSensible(false);
		pins[2] = new Pin(this, Direction.SOUTH, 1, "CLK", true);
		pins[3] = new Pin(this, Direction.EAST, size, "DATA_OUT", false);
		pins[4] = new Pin(this, Direction.NORTH, 1, "CLEAR", true).setIsSensible(false);
	}

	@Override
	public void update() {
		if (pins[2].getData()[0]) {
			if (pins[4].getData()[0]) {
				pins[3].clearData();
			} else if (pins[1].getData()[0]) {
				pins[3].setData(pins[0].getData());
			}
		}
	}

	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
	}

	public static LoadedRegister askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new LoadedRegister((int) p.x, (int) p.y, params[0]);
	}

	public static LoadedRegister fromJson(JsonObject json) {
		return new LoadedRegister(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt());
	}

}
