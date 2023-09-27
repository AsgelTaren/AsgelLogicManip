package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class ReverseSelector extends ModelOBJ {

	protected ReverseSelector(int x, int y, int addr, int size) {
		super("Reverse Selector", "RevSel", x, y, 64, 64, (1 << addr) + 2);
		pins[0] = new Pin(this, Direction.NORTH, addr, "ADDR", true);
		pins[1] = new Pin(this, Direction.EAST, size, "OUT", false);
		for (int i = 0; i < (1 << addr); i++) {
			pins[i + 2] = new Pin(this, Direction.WEST, size, "IN" + i, true);
		}
	}

	@Override
	public void update() {
		int addr = 0;
		for (int i = 0; i < pins[0].getSize(); i++) {
			addr |= (pins[0].getData()[i] ? 1 : 0) << i;
		}
		pins[1].setData(pins[2 + addr].getData());
	}

	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[1].getSize());
		json.addProperty("addr", pins[0].getSize());
	}

	public static ReverseSelector askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Addresses", "Data Size");
		if (params == null)
			return null;
		return new ReverseSelector((int) p.x, (int) p.y, params[0], params[1]);
	}

	public static ReverseSelector fromJson(JsonObject json) {
		return new ReverseSelector(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("addr").getAsInt(),
				json.get("size").getAsInt());
	}

}