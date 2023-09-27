package asgel.signalmanip.objects;

import java.awt.Color;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.gfx.Renderer;
import asgel.core.model.Clickable;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

/**
 * @author Florent Guille
 **/

public class BiggerLever extends ModelOBJ implements Clickable {

	private boolean[][] data;
	private int seli = -1, selj = -1;

	protected BiggerLever(int x, int y, int rows, int columns) {
		super("Bigger Lever", "BL", x, y, 20 + 24 * columns, 20 + 24 * rows, 1);
		data = new boolean[rows][columns];
		pins[0] = new Pin(this, Direction.EAST, rows * columns, "OUT", false);
	}

	@Override
	public void update() {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				pins[0].getData()[i * data[0].length + j] = data[i][j];
			}
		}
	}

	@Override
	public void render(Renderer renderer, ModelOBJ highOBJ, Pin highPin, Pin anchor) {
		renderer.push();
		renderer.translate(x, y);
		renderer.applyRot(rot, new Point(width >> 1, height >> 1));
		renderer.fillRoundedRect(0, 0, width, height, 20, Color.GRAY);
		if (highOBJ == this) {
			renderer.drawRoundedRect(0, 0, width, height, 20, Color.GREEN);
		}

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				renderer.fillRect(12 + j * 24, 12 + i * 24, 20, 20, data[i][j] ? Color.RED : Color.LIGHT_GRAY);
				if (i == seli && j == selj) {
					renderer.drawRect(10 + j * 24, 10 + i * 24, 24, 24, Color.CYAN);
				}
			}
		}

		pins[0].render(renderer, highPin, anchor);
		if (highOBJ == this || highPin == pins[0]) {
			Point loc = pins[0].getRotation().asVec(35).add(pins[0].getPos());
			renderer.drawCenteredString(pins[0].toString(), (int) loc.x, (int) loc.y,
					highPin == pins[0] ? Color.red : Color.BLACK);
		}
		renderer.pop();
	}

	@Override
	public void updateMousePos(Point p) {
		int tempi = (int) Math.floor((p.y - 10) / 25f);
		int tempj = (int) Math.floor((p.x - 10) / 25f);
		if (tempi >= 0 && tempj >= 0 && tempi < data.length && tempj < data[0].length) {
			int tempx = (int) (p.x - (10 + 25 * tempj));
			int tempy = (int) (p.y - (10 + 25 * tempi));
			if (tempx <= 20 && tempy <= 20) {
				seli = tempi;
				selj = tempj;
				return;
			}
		}
		seli = selj = -1;
	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("rows", data.length);
		json.addProperty("columns", data[0].length);
		JsonArray arr = new JsonArray(data.length);
		for (int i = 0; i < data.length; i++) {
			arr.add(ModelOBJ.toJsonArray(data[i]));
		}
		json.add("data", arr);
	}

	@Override
	public void onClick() {
		if (seli >= 0 && selj >= 0 && seli < data.length && selj < data[0].length) {
			data[seli][selj] = !data[seli][selj];
		}

	}

	public static BiggerLever askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Rows", "Columns");
		if (params == null)
			return null;
		return new BiggerLever((int) p.x, (int) p.y, params[0], params[1]);
	}

	public static BiggerLever fromJson(JsonObject json) {
		BiggerLever result = new BiggerLever(json.get("x").getAsInt(), json.get("y").getAsInt(),
				json.get("rows").getAsInt(), json.get("columns").getAsInt());
		JsonArray arr = json.get("data").getAsJsonArray();
		for (int i = 0; i < arr.size(); i++) {
			result.data[i] = ModelOBJ.toBooleanArray(arr.get(i).getAsJsonArray());
		}
		return result;
	}

	@Override
	public void reset() {
		data = new boolean[data.length][data[0].length];

	}

}