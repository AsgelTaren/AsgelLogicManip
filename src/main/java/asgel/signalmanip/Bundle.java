package asgel.signalmanip;

import java.awt.Color;
import java.io.File;

import asgel.app.Utils;
import asgel.core.model.GlobalRegistry;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelTab;
import asgel.signalmanip.objects.BiggerLever;
import asgel.signalmanip.objects.Buffer;
import asgel.signalmanip.objects.BufferedRegister;
import asgel.signalmanip.objects.Compressor;
import asgel.signalmanip.objects.Decompressor;
import asgel.signalmanip.objects.InputNode;
import asgel.signalmanip.objects.Lever;
import asgel.signalmanip.objects.LoadedRegister;
import asgel.signalmanip.objects.ModelBox;
import asgel.signalmanip.objects.OutputNode;
import asgel.signalmanip.objects.RAM;
import asgel.signalmanip.objects.ROM;
import asgel.signalmanip.objects.Register;
import asgel.signalmanip.objects.Splitter;

/**
 * @author Florent Guille
 **/

public class Bundle extends asgel.core.bundle.Bundle {

	public Bundle(File f, String id, GlobalRegistry global, ClassLoader loader, IParametersRequester requester) {
		super(f, id, global, loader, requester);
	}

	@Override
	public void onLoad() {
		// Regsiter tabs
		registry.registerTab(new ModelTab("signal_manip", "Signal Manip")
				.setIcon(Utils.loadIcon(resolveBundleResource("logo.png"), 16)).setColor(new Color(190, 3, 252)));
		registry.registerTab(new ModelTab("model_box", "Model Box").setColor(new Color(252, 235, 3))
				.setIcon(Utils.loadIcon(resolveBundleResource("box.png"), 16)));
		registry.registerTab(new ModelTab("memory", "Memory").setColor(new Color(252, 3, 61))
				.setIcon(Utils.loadIcon(resolveBundleResource("memory.png"), 16)));

		// Basic objects
		registry.registerObject("lever", "Lever", "signal_manip", p -> new Lever(p), json -> Lever.fromJson(json));
		registry.registerObject("splitter", "Splitter", "signal_manip",
				p -> Splitter.askFor((int) p.x, (int) p.y, requester), json -> Splitter.fromJson(json));
		registry.registerObject("decompressor", "Decompressor", "signal_manip", p -> Decompressor.askFor(p, requester),
				json -> Decompressor.fromJson(json));
		registry.registerObject("compressor", "Compressor", "signal_manip", p -> Compressor.askFor(p, requester),
				json -> Compressor.fromJson(json));
		registry.registerObject("buffer", "Buffer", "signal_manip", p -> Buffer.askFor(p, requester),
				json -> Buffer.fromJson(json));

		// Model Box
		registry.registerObject("inputnode", "Input Node", "model_box", p -> InputNode.askFor(p, requester),
				json -> InputNode.fromJson(json)).setBackground(resolveBundleImage("right_arrow.png"));
		registry.registerObject("outputnode", "Output Node", "model_box", p -> OutputNode.askFor(p, requester),
				json -> OutputNode.fromJson(json)).setBackground(resolveBundleImage("left_arrow.png"));
		registry.registerObject("modelbox", "Model Box", "model_box", p -> ModelBox.askFor(p, requester, global),
				json -> ModelBox.fromJson(json, requester, global));

		// Memory
		registry.registerObject("register", "Register", "memory", p -> Register.askFor(p, requester),
				json -> Register.fromJson(json));
		registry.registerObject("loaded_register", "Loaded Register", "memory",
				p -> LoadedRegister.askFor(p, requester), json -> LoadedRegister.fromJson(json));
		registry.registerObject("buffered_register", "Buffered Register", "memory",
				p -> BufferedRegister.askFor(p, requester), json -> BufferedRegister.fromJson(json));
		registry.registerObject("bigger_lever", "Bigger Lever", "signal_manip", p -> BiggerLever.askFor(p, requester),
				json -> BiggerLever.fromJson(json));
		registry.registerObject("ram", "RAM", "memory", p -> RAM.askFor(p, requester), json -> RAM.fromJson(json));
		registry.registerObject("rom", "ROM", "memory", p -> ROM.askFor(p, requester),
				json -> ROM.fromJson(json, requester));
	}

}
