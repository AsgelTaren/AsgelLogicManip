package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class Visualizer extends ModelOBJ {

	protected Visualizer(int x, int y, int chars) {
		super("Visualizer", "", x, y, 16 * (chars + 1), 48, 1);
		pins[0] = new Pin(this, Direction.WEST, chars << 2, "IN", true);
	}

	@Override
	public void update() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < pins[0].getSize(); i += 4) {
			int target = 0;
			for (int j = 0; j < 4; j++) {
				target += (pins[0].getData()[j + i] ? 1 : 0) << j;
			}
			builder.append(Integer.toHexString(target));
		}
		this.symbol = builder.reverse().toString();
	}

	public static Visualizer askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Number of characters");
		if (params == null)
			return null;
		return new Visualizer((int) p.x, (int) p.y, params[0]);
	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("chars", pins[0].getSize() >> 2);
	}

	public static Visualizer fromJson(JsonObject json) {
		return new Visualizer(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("chars").getAsInt());
	}

}
