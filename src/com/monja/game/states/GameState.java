package com.monja.game.states;

import com.monja.game.Game;
import com.monja.game.InputHandler;
import com.monja.game.PropertiesClient;
import com.monja.game.entities.Actor;
import com.monja.game.gfx.Colours;
import com.monja.game.gfx.Font;
import com.monja.game.gfx.Screen;
import com.monja.game.level.Level;
import com.monja.game.level.locations.DarkForestLocation;
import com.monja.game.level.locations.GladesLocation;
import com.monja.game.level.locations.LocationsEnumeration;
import com.monja.game.sound.SoundPlayer;
import com.monja.game.sound.SoundsEnumeration;

public class GameState extends RenderedState {

    public GameState(Game game) {
        super(game);
    }

    @Override
    void renderState() {
        Screen screen = game.getScreen();

        Actor actor = game.getActor();
        int xOffset = actor.x - (screen.width / 2);
        int yOffset = actor.y - (screen.height / 2);

        Level level = game.getLevel();
        level.renderTiles(screen, xOffset, yOffset);
        level.renderEntities(screen);

        StringBuilder levelInformation = new StringBuilder();
        levelInformation.append(PropertiesClient.getLocation(game.getSelectedLocation()).toString());
        levelInformation.append(" - ");
        levelInformation.append(PropertiesClient.getDifficulty(game.getSelectedDifficulty()).toString());

        Font.render(levelInformation.toString(), screen, screen.width + screen.xOffset - (levelInformation.length() * 8) - Font.getxBorderOffset(),
                screen.height + screen.yOffset - Font.getyBorderOffset(), Colours.get(-1, 000, -1, 555), 1);
    }

    @Override
    public void tick() {
        InputHandler input = game.getInput();
        game.tickCount++;
        game.getLevel().tick();

        if (input.escape.isPressed()) {
            game.getLocationStrategy().pauseBackground();
            SoundPlayer.getInstance().playSound(SoundsEnumeration.PAUSE_ACTIVATION);

            game.pauseStartTime = System.currentTimeMillis();
            game.changeState(StateClient.getState(StatesEnumeration.PAUSE));

            input.escape.toggle(false);
        }
    }
}
