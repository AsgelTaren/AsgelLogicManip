package asgel.signalmanip.objects;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import asgel.app.App;
import asgel.core.gfx.Point;
import asgel.core.model.GlobalRegistry;
import asgel.core.model.IParametersRequester;
import asgel.core.model.Model;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

/**
 * @author Florent Guille
 **/

public class ModelBox extends ModelOBJ {

	private Model model;

	private ArrayList<InputNode> inputs;
	private ArrayList<OutputNode> outputs;

	private File modelBoxFile, modelFile, workingDir;

	protected ModelBox(String name, String symbol, int x, int y, int width, int height, Model model,
			ArrayList<InputNode> inputs, ArrayList<OutputNode> outputs, File modelBoxFile, File modelFile,
			File workingDir) {
		super(name, symbol, x, y, width, height, inputs.size() + outputs.size());
		this.model = model;
		this.inputs = inputs;
		this.outputs = outputs;
		for (InputNode in : inputs) {
			pins[in.getID()] = new Pin(this, in.getRotation().getInverse(), in.getPins()[0].getSize(), in.toString(),
					true);
		}
		for (OutputNode out : outputs) {
			pins[out.getID()] = new Pin(this, out.getRotation().getInverse(), out.getPins()[0].getSize(),
					out.toString(), false);
		}
		this.modelBoxFile = modelBoxFile;
		this.modelFile = modelFile;
		this.workingDir = workingDir;
	}

	@Override
	public void update() {
		for (InputNode in : inputs) {
			in.getPins()[0].setData(pins[in.getID()].getData());
		}
		model.refresh(inputs);
		for (OutputNode out : outputs) {
			pins[out.getID()].setData(out.getPins()[0].getData());
		}

	}

	@Override
	public void toJsonInternal(JsonObject json) {
		json.addProperty("loc", workingDir.toPath().relativize(modelBoxFile.toPath()).toString());
	}

	public static ModelBox create(Point p, File modelBoxFile, File workingDir, GlobalRegistry regis) {
		if (modelBoxFile == null)
			return null;
		try {
			JsonObject data = JsonParser.parseReader(new FileReader(modelBoxFile)).getAsJsonObject();
			File modelFile = new File(modelBoxFile.getParent() + "/" + data.get("location").getAsString());
			JsonObject modelJson = JsonParser.parseReader(new FileReader(modelFile)).getAsJsonObject();
			Model model = new Model(modelJson, regis);
			ArrayList<InputNode> inputs = new ArrayList<InputNode>();
			ArrayList<OutputNode> outputs = new ArrayList<OutputNode>();

			for (ModelOBJ obj : model.getObjects()) {
				if (obj instanceof InputNode in) {
					inputs.add(in);
				}
				if (obj instanceof OutputNode out) {
					outputs.add(out);
				}
			}
			int width = data.get("width").getAsInt();
			int height = data.get("height").getAsInt();
			String name = data.get("name").getAsString();
			String symbol = data.get("symbol").getAsString();

			return new ModelBox(name, symbol, (int) p.x, (int) p.y, width, height, model, inputs, outputs, modelBoxFile,
					modelFile, workingDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ModelOBJ askFor(Point p, IParametersRequester req, GlobalRegistry regis) {
		FileLookerDialog looker = new FileLookerDialog(req.getJFrame(), req.getWorkingDir(), "Model Box Selection",
				f -> f.getName().endsWith(".asglogmodbox"));
		looker.setVisible(true);
		return looker.getResult() == null ? null : ModelBox.create(p, looker.getResult(), req.getWorkingDir(), regis);
	}

	public static ModelBox fromJson(JsonObject json, IParametersRequester req, GlobalRegistry regis) {
		return create(new Point(json.get("x").getAsInt(), json.get("y").getAsInt()),
				new File(req.getWorkingDir().getAbsolutePath() + "/" + json.get("loc").getAsString()),
				req.getWorkingDir(), regis);
	}

	@Override
	public JMenu getPopupMenu(App app) {
		JMenu res = new JMenu("Model Box");
		JMenuItem open = new JMenuItem("Open Model");
		open.addActionListener(e -> {
			try {
				Model m = new Model(modelFile, app.getGlobalRegistry());
				app.setModel(m, modelFile);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			;
		});
		res.add(open);
		return res;
	}

}