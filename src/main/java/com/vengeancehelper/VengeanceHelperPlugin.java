package com.vengeancehelper;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
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

	private Instant overlayLastDisplayed;
	private static final int SPELLBOOK_VARBIT = 4070;
	private static final int LUNAR_SPELLBOOK = 2;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
		clearOverlay();
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (config.onlyLunar() && client.getVarbitValue(SPELLBOOK_VARBIT) != LUNAR_SPELLBOOK)
		{
			clearOverlay();
		}
		if (overlayLastDisplayed != null)
		{
			Duration timeoutOverlay = Duration.ofSeconds(config.vengeanceTimeout());
			Duration sinceLastOverlayDisplay = Duration.between(overlayLastDisplayed, Instant.now());
			if (sinceLastOverlayDisplay.compareTo(timeoutOverlay) >= 0)
			{
				clearOverlay();
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
				if (config.onlyLunar() && client.getVarbitValue(SPELLBOOK_VARBIT) != LUNAR_SPELLBOOK)
				{
					return;
				}
				overlayManager.add(overlay);
				if(config.shouldNotify())
				{
					notifier.notify("You need to cast vengeance!");
				}
				overlayLastDisplayed = Instant.now();
			}
			else
			{
				clearOverlay();
			}
		}
	}

	public void clearOverlay()
	{
		overlayManager.remove(overlay);
		overlayLastDisplayed = null;
	}

	@Provides
	VengeanceHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VengeanceHelperConfig.class);
	}
}
