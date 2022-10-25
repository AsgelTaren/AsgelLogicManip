package asgel.signalmanip.objects;

import java.awt.Color;

import asgel.core.gfx.Point;
import asgel.core.gfx.Renderer;
import asgel.core.model.Clickable;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class Lever extends ModelOBJ implements Clickable {

	public Lever(int x, int y) {
		super("Lever", "L", x, y, 32, 32, 1);
		pins[0] = new Pin(this, 1, 0.5f, 1, "OUT", false);
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
		}
		renderer.pop();
	}

}