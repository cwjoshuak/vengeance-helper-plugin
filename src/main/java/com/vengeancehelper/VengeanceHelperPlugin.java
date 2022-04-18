package com.vengeancehelper;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@PluginDescriptor(
		name = "Vengeance Helper"
)
public class VengeanceHelperPlugin extends Plugin {

	boolean casted = false;
	private Instant last_vengeance_casted;

	@Inject
	private ClientThread clientThread;

	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	private SpriteManager spriteManager;

	@Inject
	private VengeanceHelperOverlay overlay;

	@Inject
	private VengeanceHelperConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception {
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event) {
		if (last_vengeance_casted != null) {
			final Duration veng_timeout = Duration.ofMinutes(config.vengeanceTimeout() + 1);
			final Duration since_veng = Duration.between(last_vengeance_casted, Instant.now());

			if (since_veng.compareTo(veng_timeout) >= 0) {
				overlayManager.remove(overlay);
				last_vengeance_casted = null;
			}
		}

		int veng = client.getVarbitValue(Varbits.VENGEANCE_ACTIVE);
		int vengCooldownVarb = client.getVarbitValue(Varbits.VENGEANCE_COOLDOWN);

		if (veng == 0 && !casted && vengCooldownVarb == 0) {
			overlayManager.add(overlay);
			if (config.shouldFlash())
				notifier.notify("You need to cast a vengeance!");
			casted = true;
		} else {
			overlayManager.remove(overlay);
			last_vengeance_casted = Instant.now();
			casted = false;
		}
	}

	@Provides
	VengeanceHelperConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(VengeanceHelperConfig.class);
	}
}