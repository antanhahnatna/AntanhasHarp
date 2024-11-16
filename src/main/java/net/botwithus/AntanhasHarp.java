package net.botwithus;

import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.ChatMessageEvent;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;

import java.util.Random;

public class AntanhasHarp extends LoopingScript {

    private BotState botState = BotState.IDLE;
    //private Coordinate entrance = new Coordinate(2135, 3338, 1);
    private Random random = new Random();

    enum BotState {
        //define your own states here
        IDLE,
        SKILLING,
        //...
    }

    public AntanhasHarp(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new AntanhasHarpGraphicsContext(getConsole(), this);
    }

    @Override
    public boolean initialize() {
        super.initialize();

        //when we get the chat message about the harp being 50% out of tune, play the harp even if we already were doing it
        subscribe(ChatMessageEvent.class, chatMessageEvent -> {
            //more events available at https://botwithus.net/javadoc/net.botwithus.rs3/net/botwithus/rs3/events/impl/package-summary.html
            if(chatMessageEvent.getMessage().startsWith("<col=AE0000>Your harp is 50% out of tune. Your chance to get harmonic dust is halved.")) {
                SceneObject harp = SceneObjectQuery.newQuery().name("Harp").results().nearest();
                if(harp != null) {
                    harp.interact(harp.getOptions().get(0));
                }
            }
        });

        return true;
    }

    @Override
    public void onLoop() {
        //Loops every 100ms by default, to change:
        //this.loopDelay = 500;
        LocalPlayer player = Client.getLocalPlayer();
        if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN || botState == BotState.IDLE) {
            //wait some time so we dont immediately start on login.
            Execution.delay(random.nextLong(3000,7000));
            return;
        }
        switch (botState) {
            case IDLE -> {
                //do nothing
                println("We're idle!");
                Execution.delay(random.nextLong(1000,3000));
            }
            case SKILLING -> {
                //do some code that handles your skilling
                handleSkilling(player);
            }
        }
    }

    private void handleSkilling(LocalPlayer player) {
        //if idle, start playing the harp, waiting until the animation of the player sitting at the harp plays
        if(player.getAnimationId() == -1) {
            SceneObject harp = SceneObjectQuery.newQuery().name("Harp").results().nearest();
            if(harp != null) {
                harp.interact(harp.getOptions().get(0));
            }
            Execution.delayUntil(20000, () -> {
                return player.getAnimationId() == 25022;
            });
        }
        return;
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

}
