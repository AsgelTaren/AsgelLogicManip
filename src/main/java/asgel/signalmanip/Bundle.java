package asgel.signalmanip;

import java.awt.Color;
import java.io.File;

import asgel.app.Utils;
import asgel.core.model.GlobalRegistry;
import asgel.core.model.IParametersRequester;
import asgel.signalmanip.objects.BiggerLever;
import asgel.signalmanip.objects.BinaryCounter;
import asgel.signalmanip.objects.Buffer;
import asgel.signalmanip.objects.BufferedRegister;
import asgel.signalmanip.objects.Compressor;
import asgel.signalmanip.objects.ControlledCounter;
import asgel.signalmanip.objects.Decompressor;
import asgel.signalmanip.objects.EqualChecker;
import asgel.signalmanip.objects.InputNode;
import asgel.signalmanip.objects.Lever;
import asgel.signalmanip.objects.LoadedRegister;
import asgel.signalmanip.objects.ModelBox;
import asgel.signalmanip.objects.OutputNode;
import asgel.signalmanip.objects.RAM;
import asgel.signalmanip.objects.ROM;
import asgel.signalmanip.objects.Register;
import asgel.signalmanip.objects.ReverseSelector;
import asgel.signalmanip.objects.Selector;
import asgel.signalmanip.objects.Splitter;
import asgel.signalmanip.objects.Visualizer;

/**
 * @author Florent Guille
 **/

public class Bundle extends asgel.core.bundle.Bundle {

	public Bundle(File f, String id, GlobalRegistry global, ClassLoader loader, IParametersRequester requester) {
		super(f, id, global, loader, requester);
	}

	@Override
	public void onLoad() {
		// Register tabs
		registry.registerTab("signal_manip").setIcon(Utils.loadIcon(resolveBundleResource("logo.png"), 16))
				.setColor(new Color(190, 3, 252));
		registry.registerTab("model_box").setColor(new Color(252, 235, 3))
				.setIcon(Utils.loadIcon(resolveBundleResource("box.png"), 16));
		registry.registerTab("memory").setColor(new Color(252, 3, 61))
				.setIcon(Utils.loadIcon(resolveBundleResource("memory.png"), 16));

		// Basic objects
		registry.registerObject("lever", "signal_manip", p -> new Lever(p), json -> Lever.fromJson(json.json));
		registry.registerObject("splitter", "signal_manip", p -> Splitter.askFor((int) p.x, (int) p.y, requester),
				json -> Splitter.fromJson(json.json));
		registry.registerObject("decompressor", "signal_manip", p -> Decompressor.askFor(p, requester),
				json -> Decompressor.fromJson(json.json));
		registry.registerObject("compressor", "signal_manip", p -> Compressor.askFor(p, requester),
				json -> Compressor.fromJson(json.json));
		registry.registerObject("buffer", "signal_manip", p -> Buffer.askFor(p, requester),
				json -> Buffer.fromJson(json.json));
		registry.registerObject("counter", "signal_manip", p -> BinaryCounter.askFor(p, requester),
				json -> BinaryCounter.fromJson(json.json));
		registry.registerObject("selector", "signal_manip", p -> Selector.askFor(p, requester),
				json -> Selector.fromJson(json.json));
		registry.registerObject("equal_checker", "signal_manip", p -> EqualChecker.askFor(p, requester),
				json -> EqualChecker.fromJson(json.json));
		registry.registerObject("visualizer", "signal_manip", p -> Visualizer.askFor(p, requester),
				json -> Visualizer.fromJson(json.json));
		registry.registerObject("controlled_counter", "signal_manip", p -> ControlledCounter.askFor(p, requester),
				json -> ControlledCounter.fromJson(json.json));
		registry.registerObject("reverse_selector", "signal_manip", p -> ReverseSelector.askFor(p, requester),
				json -> ReverseSelector.fromJson(json.json));

		// Model Box
		registry.registerObject("inputnode", "model_box", p -> InputNode.askFor(p, requester),
				json -> InputNode.fromJson(json.json)).setBackground(resolveBundleImage("right_arrow.png"));
		registry.registerObject("outputnode", "model_box", p -> OutputNode.askFor(p, requester),
				json -> OutputNode.fromJson(json.json)).setBackground(resolveBundleImage("left_arrow.png"));
		registry.registerObject("modelbox", "model_box", p -> ModelBox.askFor(p, requester, global),
				json -> ModelBox.fromJson(json.json, requester, global, json.model));

		// Memory
		registry.registerObject("register", "memory", p -> Register.askFor(p, requester),
				json -> Register.fromJson(json.json));
		registry.registerObject("loaded_register", "memory", p -> LoadedRegister.askFor(p, requester),
				json -> LoadedRegister.fromJson(json.json));
		registry.registerObject("buffered_register", "memory", p -> BufferedRegister.askFor(p, requester),
				json -> BufferedRegister.fromJson(json.json));
		registry.registerObject("bigger_lever", "signal_manip", p -> BiggerLever.askFor(p, requester),
				json -> BiggerLever.fromJson(json.json));
		registry.registerObject("ram", "memory", p -> RAM.askFor(p, requester), json -> RAM.fromJson(json.json));
		registry.registerObject("rom", "memory", p -> ROM.askFor(p, requester),
				json -> ROM.fromJson(json.json, requester, json.model));
	}

}
