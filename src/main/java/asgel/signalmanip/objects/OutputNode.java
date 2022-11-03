package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;

/**
 * @author Florent Guille
 **/

public class OutputNode extends ModelBoxNode {

	protected OutputNode(int x, int y, int size, int id) {
		super(x, y, size, id, "OUT", Direction.EAST, false);
	}

	public static OutputNode askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Data Size", "ID");
		if (params == null)
			return null;
		return new OutputNode((int) p.x, (int) p.y, params[0], params[1]);
	}

	public static OutputNode fromJson(JsonObject json) {
		return new OutputNode(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt(),
				json.get("id").getAsInt());
	}

}
