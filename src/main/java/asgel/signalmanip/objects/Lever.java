package asgel.signalmanip.objects;

import java.awt.Color;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.gfx.Renderer;
import asgel.core.model.Clickable;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

/**
 * @author Florent Guille
 **/

public class Lever extends ModelOBJ implements Clickable {

	public Lever(Point p) {
		super("Lever", "L", (int) p.x, (int) p.y, 32, 32, 1);
		pins[0] = new Pin(this, Direction.EAST, 1, "OUT", false);
	}

	public Lever(int x, int y) {
		super("Lever", "L", x, y, 32, 32, 1);
		pins[0] = new Pin(this, Direction.EAST, 1, "OUT", false);
	}

	@Override
	public void update() {

	}

	@Override
	public void onClick() {
		pins[0].getData()[0] = !pins[0].getData()[0];
	}

	@Override
	public void render(Renderer renderer, ModelOBJ highOBJ, Pin highPin, Pin anchor) {
		renderer.push();
		renderer.translate(x, y);
		renderer.applyRot(rot, new Point(width >> 1, height >> 1));
		renderer.fillRoundedRect(0, 0, width, height, 20, pins[0].getData()[0] ? Color.RED : Color.GRAY);
		if (highOBJ == this) {
			renderer.drawRoundedRect(0, 0, width, height, 20, Color.GREEN);
		}

		renderer.drawCenteredString(pins[0].getData()[0] ? "1" : "0", width >> 1, height >> 1, Color.BLACK);

		for (Pin p : pins) {
			p.render(renderer, highPin, anchor);

			if (highOBJ == this || highPin == p) {
				Point loc = p.getRotation().asVec(25).add(p.getPos());
				renderer.drawCenteredString(p.toString(), (int) loc.x, (int) loc.y,
						highPin == p ? Color.red : Color.BLACK);
			}
		}
		renderer.pop();
	}

	public static Lever fromJson(JsonObject json) {
		return new Lever(json.get("x").getAsInt(), json.get("y").getAsInt());
	}

	@Override
	public void reset() {
		pins[0].getData()[0] = false;

	}

}
