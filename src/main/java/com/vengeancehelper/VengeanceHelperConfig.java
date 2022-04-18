package com.vengeancehelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("vengeancehelper")
public interface VengeanceHelperConfig extends Config
{
	@ConfigItem(
			keyName = "shouldNotify",
			name = "Notify when vengeance expires",
			description = "Sends a notification once the vengeance needs to be casted"
	)

	default boolean shouldNotify()
	{
		return true;
	}

	@ConfigItem(
			keyName = "vengeanceTimeout",
			name = "Timeout Vengeance Box",
			description = "The duration of time before the vengeance box disappears."
	)
	@Units(Units.MINUTES)
	default int vengeanceTimeout()
	{
		return 3;
	}

	@ConfigItem(
			keyName = "shouldFlash",
			name = "Flash the Reminder Box",
			description = "Makes the reminder box flash."
	)
	default boolean shouldFlash() { return false; }
}