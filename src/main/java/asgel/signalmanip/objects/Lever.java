package asgel.signalmanip.objects;

import java.awt.Color;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.gfx.Renderer;
import asgel.core.model.Clickable;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class Lever extends ModelOBJ implements Clickable {

	public Lever(Point p) {
		super("Lever", "L", (int) p.x, (int) p.y, 32, 32, 1);
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
		renderer.fillRect(0, 0, width, height, pins[0].getData()[0] ? Color.CYAN : Color.GRAY);
		renderer.drawRect(0, 0, width, height, highOBJ == this ? Color.GREEN : Color.BLACK);

		renderer.drawCenteredString(symbol, width >> 1, height >> 1, Color.BLACK);

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

}
