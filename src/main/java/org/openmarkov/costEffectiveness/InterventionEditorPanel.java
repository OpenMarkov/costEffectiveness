package org.openmarkov.costEffectiveness;

import org.openmarkov.gui.dialog.treeadd.TreeADDCellRenderer;
import org.openmarkov.gui.dialog.treeadd.TreeADDEditorPanel;
import org.openmarkov.core.model.network.Node;

import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class InterventionEditorPanel extends TreeADDEditorPanel implements ActionListener {

    public InterventionEditorPanel(TreeADDCellRenderer cellRenderer, Node node, boolean readOnly) {
    	super(cellRenderer, node, readOnly);
    }

    public InterventionEditorPanel(TreeADDCellRenderer cellRenderer, Node node) {
        super(cellRenderer, node, true);
    }

}
