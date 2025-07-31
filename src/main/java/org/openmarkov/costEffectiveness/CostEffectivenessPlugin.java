package org.openmarkov.costEffectiveness;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openmarkov.core.localize.StringDatabase;
import org.openmarkov.gui.dialog.ExceptionDialog;
import org.openmarkov.gui.exception.NoNetOpenedException;
import org.openmarkov.gui.toolplugin.ToolPlugin;
import org.openmarkov.gui.window.MainPanel;

import javax.swing.*;

public final class CostEffectivenessPlugin implements ToolPlugin {
    
    @Override public @NotNull String menuOptionText() {
        return StringDatabase.getUniqueInstance().getString("Menus", "Tools.CostEffectiveness.Label");
    }
    
    @Override public void showDialog(@Nullable JFrame parent) {
        if (MainPanel.getUniqueInstance().getMainPanelListenerAssistant().getCurrentNetworkPanel() == null) {
            ExceptionDialog.show(new NoNetOpenedException());
            return;
        }
        var dialog = new CostEffectivenessFrame(parent).getCepDialog();
        if (dialog != null) {
            dialog.setVisible(true);
        }
    }
}
