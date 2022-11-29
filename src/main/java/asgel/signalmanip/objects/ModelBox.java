package asgel.signalmanip.objects;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import asgel.app.App;
import asgel.app.FileLookerDialog;
import asgel.app.Utils;
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

	private String modelURL;

	private File modelFile;

	protected ModelBox(String name, String symbol, int x, int y, int width, int height, Model model,
			ArrayList<InputNode> inputs, ArrayList<OutputNode> outputs, String modelURL, File modelFile) {
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
		this.modelURL = modelURL;
		this.modelFile = modelFile;
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
		json.addProperty("loc", modelURL);
	}

	public static ModelBox create(Point p, String modelBoxURL, IParametersRequester requester, GlobalRegistry regis,
			Model origin) {
		try {
			File modelBoxFile = Utils.resolvePath(origin.getFile(), requester.getWorkingDir(), modelBoxURL);
			JsonObject data = JsonParser.parseReader(new FileReader(modelBoxFile)).getAsJsonObject();
			File modelFile = Utils.resolvePath(modelBoxFile, requester.getWorkingDir(),
					data.get("location").getAsString());
			JsonObject modelJson = JsonParser.parseReader(new FileReader(modelFile)).getAsJsonObject();
			Model model = new Model(modelJson, regis, modelFile);
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

			return new ModelBox(name, symbol, (int) p.x, (int) p.y, width, height, model, inputs, outputs, modelBoxURL,
					modelFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ModelOBJ askFor(Point p, IParametersRequester req, GlobalRegistry regis) {
		if (req.getWorkingFile() == null) {
			JOptionPane.showMessageDialog(req.getJFrame(), "You need to be in a valid file to use this model object!");
			return null;
		}
		FileLookerDialog looker = new FileLookerDialog(req.getJFrame(), req.getWorkingDir(), "Model Box Selection",
				f -> f.getName().endsWith(".asglogmodbox"));
		looker.setVisible(true);
		if (looker.getResult() == null)
			return null;
		String modelBoxURL = Utils.askForRelativity(req.getWorkingFile().getParentFile().toPath(),
				looker.getResult().toPath(), req.getWorkingDir().toPath(), req.getJFrame());
		return looker.getResult() == null ? null
				: ModelBox.create(p, modelBoxURL, req, regis, req.getApp().getSelectedModelHolder().getModel());
	}

	public static ModelBox fromJson(JsonObject json, IParametersRequester req, GlobalRegistry regis, Model model) {
		return create(new Point(json.get("x").getAsInt(), json.get("y").getAsInt()), json.get("loc").getAsString(), req,
				regis, model);
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