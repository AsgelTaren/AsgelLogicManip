package asgel.signalmanip;

import java.awt.Color;

import asgel.core.bundle.BundleLoader;
import asgel.core.bundle.RessourceManager;
import asgel.core.bundle.Utils;
import asgel.core.model.BundleRegistry;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelTab;
import asgel.signalmanip.objects.InputNode;
import asgel.signalmanip.objects.Lever;
import asgel.signalmanip.objects.ModelBox;
import asgel.signalmanip.objects.OutputNode;
import asgel.signalmanip.objects.Splitter;

public class Bundle implements BundleLoader {

	@Override
	public void loadBundle(BundleRegistry registry, RessourceManager res, IParametersRequester requester) {
		// Regsiter tabs
		registry.registerTab(new ModelTab("signal_manip", "Signal Manip")
				.setIcon(Utils.loadIcon(res.resolveRessource("logo.png"), 16)).setColor(new Color(190, 3, 252)));
		registry.registerTab(new ModelTab("model_box", "Model Box").setColor(new Color(252, 235, 3))
				.setIcon(Utils.loadIcon(res.resolveRessource("box.png"), 16)));

		// Register objects
		registry.registerObject("lever", "Lever", "signal_manip", p -> new Lever(p), json -> Lever.fromJson(json));
		registry.registerObject("splitter", "Splitter", "signal_manip",
				p -> Splitter.askFor((int) p.x, (int) p.y, requester), json -> Splitter.fromJson(json));
		registry.registerObject("inputnode", "Input Node", "model_box", p -> InputNode.askFor(p, requester),
				json -> InputNode.fromJson(json));
		registry.registerObject("outputnode", "Output Node", "model_box", p -> OutputNode.askFor(p, requester),
				json -> OutputNode.fromJson(json));
		registry.registerObject("modelbox", "Model Box", "model_box",
				p -> ModelBox.askFor(p, requester, res.getGlobalRegistry()),
				json -> ModelBox.fromJson(json, res.getGlobalRegistry()));
	}

}
