package com.vengeancehelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class VengeancePlugin
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(VengeanceHelperPlugin.class);
        RuneLite.main(args);
    }
}