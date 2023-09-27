package asgel.signalmanip.objects;

import asgel.core.gfx.Direction;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class ALU extends ModelOBJ {

	protected ALU(int x, int y, int size) {
		super("ALU", "ALU", x, y, 64, 64, 4);
		pins[0] = new Pin(this, Direction.WEST, size, "A", true);
		pins[1] = new Pin(this, Direction.WEST, size, "B", true);
		pins[2] = new Pin(this, Direction.NORTH, 2, "OP", true);
		pins[3] = new Pin(this, Direction.EAST, size, "OUT", false);
	}

	@Override
	public void update() {

	}

}