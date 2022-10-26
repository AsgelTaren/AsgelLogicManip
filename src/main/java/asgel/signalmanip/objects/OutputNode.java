package asgel.signalmanip.objects;

import asgel.core.gfx.Direction;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class OutputNode extends ModelOBJ {

	private int id;

	protected OutputNode(int x, int y, int size, int id) {
		super("OUT " + id, "OUT " + id, x, y, 48, 48, 1);
		pins[0] = new Pin(this, Direction.WEST, size, "OUT " + id, true);
		this.id = id;
	}

	@Override
	public void update() {

	}

}
