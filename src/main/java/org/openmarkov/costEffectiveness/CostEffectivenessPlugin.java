package org.openmarkov.costEffectiveness;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openmarkov.core.exception.OpenMarkovException;
import org.openmarkov.core.localize.LocalizedException;
import org.openmarkov.gui.localize.Nls;
import org.openmarkov.gui.toolplugin.ToolPlugin;
import org.openmarkov.gui.window.MainPanel;

import javax.swing.*;

public final class CostEffectivenessPlugin implements ToolPlugin {
    
    @Override public @NotNull String menuOptionText() {
        return Nls.Menus.Tools.CostEffectiveness.Label.stringify();
    }
    
    @Override public void showDialog(@Nullable JFrame parent) {
        if (MainPanel.getUniqueInstance().getMainPanelListenerAssistant().getCurrentNetworkPanel() == null) {
            new LocalizedException(new OpenMarkovException("Exception.NoNet"), parent).showException();
            return;
        }
        var dialog = new CostEffectivenessFrame(parent).getCepDialog();
        if (dialog != null) {
            dialog.setVisible(true);
        }
    }
}
