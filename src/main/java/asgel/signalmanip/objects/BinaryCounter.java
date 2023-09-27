package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.Clickable;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class BinaryCounter extends ModelOBJ implements Clickable {

	private int data;
	private int mask;

	protected BinaryCounter(int x, int y, int size, int data) {
		super("Counter", "" + data, x, y, 48, 48, 1);
		pins[0] = new Pin(this, Direction.EAST, size, "OUT", false);
		mask = (1 << size) - 1;
		this.data = data;
	}

	@Override
	public void update() {

	}

	@Override
	public void onClick() {
		data = (data + 1) & mask;
		for (int i = 0; i < pins[0].getSize(); i++) {
			pins[0].getData()[i] = ((data >> i) & 1) == 1;
		}
		this.symbol = Integer.toHexString(data);
	}

	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("data", data);
		json.addProperty("size", pins[0].getSize());
	}

	public static BinaryCounter askFor(Point p, IParametersRequester requester) {
		int[] params = requester.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new BinaryCounter((int) p.x, (int) p.y, params[0], 0);
	}

	public static BinaryCounter fromJson(JsonObject json) {
		return new BinaryCounter(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt(),
				json.get("data").getAsInt());
	}

	@Override
	public void reset() {
		this.data = 0;
		pins[0].clearData();
		this.symbol = "0";

	}
}