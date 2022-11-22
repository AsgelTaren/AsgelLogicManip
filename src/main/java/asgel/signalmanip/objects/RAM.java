package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class RAM extends ModelOBJ {

	private boolean[][] data;

	protected RAM(int x, int y, int size, int addresses) {
		super("RAM", "RAM", x, y, 64, 64, 9);
		pins[0] = new Pin(this, Direction.WEST, size, "DATA_IN", true).setIsSensible(false);
		pins[1] = new Pin(this, Direction.WEST, addresses, "ADDR", true);
		pins[2] = new Pin(this, Direction.SOUTH, 1, "CLK", true);
		pins[3] = new Pin(this, Direction.SOUTH, 1, "CLEAR", true);
		pins[4] = new Pin(this, Direction.SOUTH, 1, "PROD", true);
		pins[5] = new Pin(this, Direction.NORTH, 1, "TOGGLE", true);
		pins[6] = new Pin(this, Direction.NORTH, 1, "RI", true).setIsSensible(false);
		pins[7] = new Pin(this, Direction.NORTH, 1, "RO", true);
		pins[8] = new Pin(this, Direction.EAST, size, "DATA_OUT", false);
		data = new boolean[1 << addresses][size];
	}

	@Override
	public void update() {
		int addr = 0;
		for (int i = 0; i < pins[1].getSize(); i++) {
			addr |= (pins[1].getData()[i] ? 1 : 0) << i;
		}
		if ((pins[4].getData()[0] && pins[5].getData()[0])
				|| (!pins[4].getData()[0] && pins[2].getData()[0] && pins[6].getData()[0])) {
			for (int i = 0; i < pins[0].getSize(); i++) {
				data[addr][i] = pins[0].getData()[i];
			}
		}
		if (pins[2].getData()[0] && pins[3].getData()[0]) {
			for (int i = 0; i < pins[0].getSize(); i++) {
				data[addr][i] = false;
			}
		}
		if (pins[7].getData()[0]) {
			pins[8].setData(data[addr]);
		} else {
			pins[8].clearData();
		}

	}

	public static RAM askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Data Size", "Addresses");
		if (params == null)
			return null;
		return new RAM((int) p.x, (int) p.y, params[0], params[1]);
	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
		json.addProperty("addresses", pins[1].getSize());
	}

	public static RAM fromJson(JsonObject json) {
		return new RAM(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt(),
				json.get("addresses").getAsInt());
	}

}