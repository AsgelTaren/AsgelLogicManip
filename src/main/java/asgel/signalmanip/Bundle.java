package asgel.signalmanip;

import java.util.function.Function;

import com.google.gson.JsonObject;

import asgel.core.bundle.BundleLoader;
import asgel.core.bundle.RessourceManager;
import asgel.core.bundle.Utils;
import asgel.core.model.BundleRegistry;
import asgel.core.model.ModelOBJ;
import asgel.core.model.ModelTab;
import asgel.signalmanip.objects.*;

public class Bundle implements BundleLoader {

	@Override
	public void loadBundle(BundleRegistry registry, RessourceManager res) {
		// Regsiter tabs
		registry.registerTab(new ModelTab("signal_manip", "Signal Manip")
				.setIcon(Utils.loadIcon(res.resolveRessource("logo.png"), 16)));

		// Register objects
		registry.registerObject("lever", "Lever", "signal_manip", p -> new Lever((int) p.x, (int) p.y),
				(Function<JsonObject, ModelOBJ>) null);
	}

}
