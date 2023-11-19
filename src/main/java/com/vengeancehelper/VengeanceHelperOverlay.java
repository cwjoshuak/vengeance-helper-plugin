package com.vengeancehelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class VengeanceHelperOverlay extends OverlayPanel {

    private static final Color FOCUS = new Color(255, 0, 0, 150);

    private final VengeanceHelperConfig config;
    private final Client client;

    @Inject
    private VengeanceHelperOverlay(VengeanceHelperConfig config, Client client)
    {
        this.config = config;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();

        panelComponent.getChildren().add((LineComponent.builder())
                .left("You need to cast vengeance!")
                .build());

        if (config.shouldFlash()) {
            if (client.getGameCycle() % 40 >= 20)
            {
                panelComponent.setBackgroundColor(getPreferredColor());
            } else
            {
                panelComponent.setBackgroundColor(FOCUS);
            }
        } else {
            panelComponent.setBackgroundColor(FOCUS);
        }

        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        return panelComponent.render(graphics);
    }
}
