package asgel.signalmanip.objects;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.gson.JsonObject;

import asgel.app.App;
import asgel.app.FileLookerDialog;
import asgel.app.Logger;
import asgel.app.Utils;
import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.Model;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class ROM extends ModelOBJ {

	private boolean[][] data;
	String dataURL;

	protected ROM(int x, int y, int size, int addresses, String dataURL, File workingDir, Model model) {
		super("ROM", "ROM", x, y, 64, 64, 3);
		this.dataURL = dataURL;
		pins[0] = new Pin(this, Direction.EAST, size, "DATA_OUT", false);
		pins[1] = new Pin(this, Direction.WEST, addresses, "ADDR", true);
		pins[2] = new Pin(this, Direction.SOUTH, 1, "RO", true);

		data = new boolean[1 << addresses][size];

		try {
			loadData(dataURL, model, workingDir);
		} catch (Exception e) {
			e.printStackTrace();
			data = new boolean[1 << addresses][size];
		}
	}

	private void loadData(String dataURL, Model model, File workingDir) throws Exception {
		Logger log = Logger.INSTANCE.derivateLogger("[ROM]");
		log.log("Started loading rom data from " + dataURL);
		File f = Utils.resolvePath(model.getFile(), workingDir, dataURL);
		String[] data = Files.readString(f.toPath()).replace(" ", "").split(";");
		this.data = new boolean[1 << pins[1].getSize()][pins[0].getSize()];
		for (String raw : data) {
			log.log("Adding instruction: " + raw);
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
		json.addProperty("data", dataURL);
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
		FileLookerDialog looker = new FileLookerDialog(req.getJFrame(), req.getWorkingDir(),
				req.getApp().getText("rom.selection"), f -> f.getName().endsWith(".asglogdata"));
		looker.setVisible(true);
		if (looker.getResult() == null)
			return null;
		String dataURL = Utils.askForRelativity(req.getWorkingFile().getParentFile().toPath(),
				looker.getResult().toPath(), req.getWorkingDir().toPath(), req.getJFrame());
		int[] params = req.getParametersAsInt("Data Size", "Addresses");
		if (params == null)
			return null;
		return new ROM((int) p.x, (int) p.y, params[0], params[1], dataURL, req.getWorkingDir(),
				req.getApp().getSelectedModelHolder().getModel());
	}

	public static ModelOBJ fromJson(JsonObject json, IParametersRequester req, Model model) {
		return new ROM(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt(),
				json.get("addresses").getAsInt(), json.get("data").getAsString(), req.getWorkingDir(), model);
	}

	@Override
	public JMenu getPopupMenu(App app) {
		JMenu res = new JMenu("ROM");

		JMenuItem setData = new JMenuItem(app.getText("rom.setdata"));
		setData.addActionListener(e -> {

			FileLookerDialog looker = new FileLookerDialog(app.getJFrame(), app.getWorkingDir(),
					app.getText("rom.selection"), f -> f.getName().endsWith(".asglogdata"));
			looker.setVisible(true);
			if (looker.getResult() == null)
				return;
			String dataURL = Utils.askForRelativity(app.getWorkingFile().getParentFile().toPath(),
					looker.getResult().toPath(), app.getWorkingDir().toPath(), app.getJFrame());
			this.dataURL = dataURL;
			try {
				loadData(dataURL, app.getSelectedModelHolder().getModel(), app.getWorkingDir());
				app.getSelectedModelHolder().getModel().refresh(Collections.singletonList(this));
			} catch (Exception err) {
				Logger.INSTANCE.log(err.getMessage());
			}

		});
		res.add(setData);

		return res;
	}

}