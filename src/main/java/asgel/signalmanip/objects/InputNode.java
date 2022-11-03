package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.Clickable;
import asgel.core.model.IParametersRequester;

/**
 * @author Florent Guille
 **/

public class InputNode extends ModelBoxNode implements Clickable {

	protected InputNode(int x, int y, int size, int id) {
		super(x, y, size, id, "IN", Direction.EAST, true);
	}

	public static InputNode askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Data Size", "ID");
		if (params == null)
			return null;
		return new InputNode((int) p.x, (int) p.y, params[0], params[1]);
	}

	public static InputNode fromJson(JsonObject json) {
		return new InputNode(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt(),
				json.get("id").getAsInt());
	}

	@Override
	public void onClick() {
		pins[0].getData()[0] = !pins[0].getData()[0];

	}

}