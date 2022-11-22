package asgel.signalmanip.objects;

import java.io.File;
import java.nio.file.Files;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class ROM extends ModelOBJ {

	private boolean[][] data;
	private File dataFile, workingDir;

	protected ROM(int x, int y, int size, int addresses, File dataFile, File workingDir) {
		super("ROM", "ROM", x, y, 64, 64, 3);
		this.dataFile = dataFile;
		this.workingDir = workingDir;
		pins[0] = new Pin(this, Direction.EAST, size, "DATA_OUT", false);
		pins[1] = new Pin(this, Direction.WEST, addresses, "ADDR", true);
		pins[2] = new Pin(this, Direction.SOUTH, 1, "RO", true);

		data = new boolean[1 << addresses][size];

		try {
			loadData(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
			data = new boolean[1 << addresses][size];
		}
	}

	private void loadData(File f) throws Exception {
		String[] data = Files.readString(f.toPath()).replace(" |_", "").split(";");
		for (String raw : data) {
			String[] split = raw.split("->");
			int addr = 0;
			for (int i = 0; i < split[0].length(); i++) {
				addr |= (split[0].charAt(i) == '1' ? 1 : 0) << i;
			}
			for (int i = 0; i < split[1].length(); i++) {
				this.data[addr][i] = split[1].charAt(i) == '1';
			}
		}
	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("data", workingDir.toPath().relativize(dataFile.toPath()).toString());
		json.addProperty("size", pins[0].getSize());
		json.addProperty("addresses", pins[1].getSize());
	}

	@Override
	public void update() {
		if (pins[2].getData()[0]) {
			int addr = 0;
			for (int i = 0; i < pins[1].getSize(); i++) {
				addr |= (pins[1].getData()[i] ? 1 : 0) << i;
			}
			pins[0].setData(data[addr]);
		} else {
			pins[0].clearData();
		}
	}

	public static ModelOBJ askFor(Point p, IParametersRequester req) {
		FileLookerDialog looker = new FileLookerDialog(req.getJFrame(), req.getWorkingDir(), "ROM Data Selection",
				f -> f.getName().endsWith(".asglogdata"));
		looker.setVisible(true);
		if (looker.getResult() == null)
			return null;
		int[] params = req.getParametersAsInt("Data Size", "Addresses");
		if (params == null)
			return null;
		return new ROM((int) p.x, (int) p.y, params[0], params[1], looker.getResult(), req.getWorkingDir());
	}

	public static ModelOBJ fromJson(JsonObject json, IParametersRequester req) {
		return new ROM(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt(),
				json.get("addresses").getAsInt(),
				new File(req.getWorkingDir().getAbsolutePath() + "/" + json.get("data").getAsString()),
				req.getWorkingDir());
	}

}