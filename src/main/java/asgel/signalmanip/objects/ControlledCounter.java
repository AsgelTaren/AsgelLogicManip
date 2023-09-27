package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class ControlledCounter extends ModelOBJ {

	protected ControlledCounter(int x, int y, int size) {
		super("ContCounter", "Counter", x, y, 48, 48, 6);
		pins[0] = new Pin(this, Direction.WEST, size, "INPUT", true);
		pins[1] = new Pin(this, Direction.EAST, size, "OUT", false);
		pins[2] = new Pin(this, Direction.NORTH, 1, "JUMP", true).setIsSensible(false);
		pins[3] = new Pin(this, Direction.NORTH, 1, "INC", true).setIsSensible(false);
		pins[4] = new Pin(this, Direction.NORTH, 1, "CLEAR", true).setIsSensible(false);
		pins[5] = new Pin(this, Direction.SOUTH, 1, "CLK", true);
	}

	@Override
	public void update() {
		if (pins[5].getData()[0]) {
			if (pins[4].getData()[0]) {
				pins[1].clearData();
			} else if (pins[3].getData()[0]) {
				int data = 0;
				for (int i = 0; i < pins[1].getSize(); i++) {
					data |= (pins[1].getData()[i] ? 1 : 0) << i;
				}
				data++;
				for (int i = 0; i < pins[1].getSize(); i++) {
					pins[1].getData()[i] = ((data >> i) & 1) == 1;
				}
			} else if (pins[2].getData()[0]) {
				pins[1].setData(pins[0].getData());
			}
		}
	}

	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
	}

	public static ControlledCounter askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new ControlledCounter((int) p.x, (int) p.y, params[0]);
	}

	public static ControlledCounter fromJson(JsonObject json) {
		return new ControlledCounter(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt());
	}

}