package com.vengeancehelper;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
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
	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	private VengeanceHelperOverlay overlay;

	@Inject
	private VengeanceHelperConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayLastDisplayed = null;
		overlayManager.remove(overlay);
	}

	private Instant overlayLastDisplayed;

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (overlayLastDisplayed != null)
		{
			Duration timeoutOverlay = Duration.ofMinutes(config.vengeanceTimeout());
			Duration sinceLastOverlayDisplay = Duration.between(overlayLastDisplayed, Instant.now());
			if (sinceLastOverlayDisplay.compareTo(timeoutOverlay) >= 0)
			{
				overlayManager.remove(overlay);
				overlayLastDisplayed = null;
			}
		}
	}
	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarbitId() == Varbits.VENGEANCE_ACTIVE || event.getVarbitId() == Varbits.VENGEANCE_COOLDOWN)
		{
			boolean isVengeanceActive = client.getVarbitValue(Varbits.VENGEANCE_ACTIVE) == 1;
			boolean isVengeanceCastable = client.getVarbitValue(Varbits.VENGEANCE_COOLDOWN) == 0;
			if (!isVengeanceActive && isVengeanceCastable)
			{
				overlayManager.add(overlay);
				if(config.shouldNotify())
				{
					notifier.notify("You need to cast a vengeance!");
				}
				overlayLastDisplayed = Instant.now();
			}
		}
	}

	@Provides
	VengeanceHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VengeanceHelperConfig.class);
	}
}
