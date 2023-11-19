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
			description = "Sends a notification once the vengeance needs to be recast."
	)

	default boolean shouldNotify()
	{
		return true;
	}

	@ConfigItem(
			keyName = "shouldFlash",
			name = "Flash the Reminder Box",
			description = "Makes the reminder box flash."
	)
	default boolean shouldFlash() { return false; }

	@ConfigItem(
			keyName = "vengeanceTimeoutSeconds",
			name = "Timeout Vengeance Box",
			description = "The duration of time before the vengeance box disappears."
	)
	@Units(Units.SECONDS)
	default int vengeanceTimeout()
	{
		return 180;
	}

	@ConfigItem(
			keyName = "onlyLunar",
			name = "Only on Lunar Spellbook",
			description = "Only display the reminder box when on the Lunar spellbook"
	)
	default boolean onlyLunar() { return true; }
}
