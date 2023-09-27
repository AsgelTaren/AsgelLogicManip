package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class Selector extends ModelOBJ {

	protected Selector(int x, int y, int addr, int size) {
		super("Selector", "Sel", x, y, 48, 32 + 16 * (1 << addr), 2 + (1 << addr));
		pins[0] = new Pin(this, Direction.WEST, size, "IN", true);
		pins[1] = new Pin(this, Direction.NORTH, addr, "ADDR", true);
		for (int i = 0; i < 1 << addr; i++) {
			pins[2 + i] = new Pin(this, Direction.EAST, size, "OUT " + i, false);
		}
	}

	@Override
	public void update() {
		for (int i = 2; i < getPins().length; i++) {
			pins[i].clearData();
		}
		int addr = 0;
		for (int i = 0; i < pins[1].getSize(); i++) {
			addr |= (pins[1].getData()[i] ? 1 : 0) << i;
		}
		for (int i = 0; i < pins[0].getSize(); i++) {
			pins[2 + addr].getData()[i] = pins[0].getData()[i];
		}
	}

	public static Selector askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Addresses", "Size");
		if (params == null)
			return null;
		return new Selector((int) p.x, (int) p.y, params[0], params[1]);
	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
		json.addProperty("addr", pins[1].getSize());
	}

	public static Selector fromJson(JsonObject json) {
		return new Selector(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("addr").getAsInt(),
				json.get("size").getAsInt());
	}

}