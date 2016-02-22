package org.openmarkov.costEffectiveness;

import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.UnexpectedInferenceException;
import org.openmarkov.core.gui.dialog.inference.common.InferenceOptionsDialog;
import org.openmarkov.core.gui.dialog.inference.common.ScopeSelectorPanel;
import org.openmarkov.core.gui.dialog.inference.common.ScopeType;
import org.openmarkov.core.gui.localize.StringDatabase;
import org.openmarkov.core.gui.plugin.ToolPlugin;
import org.openmarkov.core.gui.window.MainPanel;
import org.openmarkov.core.inference.MulticriteriaOptions;
import org.openmarkov.core.model.network.CEP;
import org.openmarkov.core.model.network.EvidenceCase;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.inference.tasks.VariableElimination.VECEAGlobal;

import javax.swing.*;

/**
 * @author jperez-martin
 */
@ToolPlugin( name="CostEffectivenessDeterministic", command="Tools.CostEffectivenessDeterministic")
public class CostEffectivenessFrame extends JFrame {

    /**
     * Constructor. initialises the instance.
     *
     * @param owner window that owns the dialog.
     */
    public CostEffectivenessFrame(JFrame owner) {
        super();
        ProbNet probNet = MainPanel.getUniqueInstance ().getMainPanelListenerAssistant ().getCurrentNetworkPanel ().getProbNet ();
        EvidenceCase preResolutionEvidence = MainPanel.getUniqueInstance().
                getMainPanelMenuAssistant().getCurrentNetworkPanel().getEditorPanel().getPreResolutionEvidence();

        InferenceOptionsDialog inferenceOptionsDialog = new InferenceOptionsDialog(
                probNet,
                owner,
                MulticriteriaOptions.Type.COST_EFFECTIVENESS);

        if(inferenceOptionsDialog.getSelectedButton() != InferenceOptionsDialog.OK_BUTTON){
            return;
        }

        CostEffectivenessDialog costEffectivenessDialog = new CostEffectivenessDialog(owner, probNet);

        if ((costEffectivenessDialog.requestData() == CostEffectivenessDialog.OK_BUTTON)) {
            ScopeSelectorPanel scopeSelectorPanel = costEffectivenessDialog.getScopeSelectorPanel();
            try {
                if(scopeSelectorPanel.getScopeType().equals(ScopeType.GLOBAL)) {

                    VECEAGlobal veGlobalCEA = new VECEAGlobal(probNet, preResolutionEvidence);
                    CEP cep = veGlobalCEA.getCEP();
                    CEPDialog cepDialog = new CEPDialog(owner,cep, probNet);
                    cepDialog.setVisible(true);
                } else {
                    CEDecisionResults ceDecisionResults = new CEDecisionResults(owner, probNet,
                            preResolutionEvidence,scopeSelectorPanel.getDecisionSelected());

                }
            } catch (NotEvaluableNetworkException e) {
                JOptionPane.showMessageDialog(
                        null,
                        StringDatabase.getUniqueInstance().getString("CostEffectivenessDeterministic.Error")
                                + e.getMessage());
                e.printStackTrace();
            } catch (IncompatibleEvidenceException | UnexpectedInferenceException e) {
                e.printStackTrace();
            }
        }
    }
}
