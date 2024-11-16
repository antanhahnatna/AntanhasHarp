package net.botwithus;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class AntanhasHarpGraphicsContext extends ScriptGraphicsContext {

    private AntanhasHarp script;

    public AntanhasHarpGraphicsContext(ScriptConsole scriptConsole, AntanhasHarp script) {
        super(scriptConsole);
        this.script = script;
    }

    @Override
    public void drawSettings() {
        if (ImGui.Begin("Antanha's harp", ImGuiWindowFlag.None.getValue())) {
            ImGui.Text("Script state: " + script.getBotState());
            ImGui.BeginDisabled(script.getBotState() != AntanhasHarp.BotState.IDLE);
            if (ImGui.Button("Start")) {
                //button has been clicked
                script.setBotState(AntanhasHarp.BotState.SKILLING);
            }
            ImGui.EndDisabled();
            ImGui.SameLine();
            ImGui.BeginDisabled(script.getBotState() == AntanhasHarp.BotState.IDLE);
            if (ImGui.Button("Stop")) {
                //button has been clicked
                script.setBotState(AntanhasHarp.BotState.IDLE);
            }
            ImGui.EndDisabled();
            ImGui.Text("Instructions:");
            ImGui.Text("Move to the harp building and start the script. It will re-tune when it reaches 50 percent.");
            ImGui.End();
        }

    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
