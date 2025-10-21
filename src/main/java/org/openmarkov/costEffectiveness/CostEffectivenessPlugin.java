package org.openmarkov.costEffectiveness;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openmarkov.core.exception.*;
import org.openmarkov.core.inference.MulticriteriaOptions;
import org.openmarkov.core.localize.StringDatabase;
import org.openmarkov.core.model.network.CEP;
import org.openmarkov.core.model.network.EvidenceCase;
import org.openmarkov.core.model.network.Finding;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.type.InfluenceDiagramType;
import org.openmarkov.core.model.network.type.MIDType;
import org.openmarkov.core.model.network.type.NetworkType;
import org.openmarkov.gui.dialog.common.OkCancelHorizontalDialog;
import org.openmarkov.gui.dialog.costeffectiveness.CEPDialog;
import org.openmarkov.gui.dialog.inference.common.InferenceOptionsDialog;
import org.openmarkov.gui.dialog.inference.common.ScopeSelectorPanel;
import org.openmarkov.gui.exception.NoNetOpenedException;
import org.openmarkov.gui.toolplugin.ToolPlugin;
import org.openmarkov.gui.window.MainPanel;
import org.openmarkov.inference.algorithm.decompositionIntoSymmetricDANs.ceanalysis.DANDecompositionIntoSymmetricDANsCEA;
import org.openmarkov.inference.algorithm.variableElimination.tasks.VECEAnalysis;

import javax.swing.*;

public final class CostEffectivenessPlugin implements ToolPlugin {
    
    @Override public @NotNull String menuOptionText() {
        return StringDatabase.getUniqueInstance().getString("Menus", "Tools.CostEffectiveness.Label");
    }
    
    @Override public @NotNull ToolPluginGroup pluginGroup() {
        return ToolPluginGroup.ANALYSIS;
    }
    
    @Override public int priorityInGroup() {
        return 0;
    }
    
    @Override public void showDialog(@Nullable JFrame parent) throws NonProjectablePotentialException,
            IncompatibleEvidenceException, NotEvaluableNetworkException.NotApplicableNetwork,
            NotEvaluableNetworkException.UnsatisfiedContraints, NoNetOpenedException, PotentialOperationException.DifferentSizesInPotentialsAndStates, NotSupportedOperationException, ConstraintViolatedException {
        if (MainPanel.getCurrentProbNet() == null) {
            throw new NoNetOpenedException();
        }
        ProbNet probNet = MainPanel.getCurrentProbNet();
        EvidenceCase preResolutionEvidence = MainPanel.getUniqueInstance()
                                                      .getMainPanelMenuAssistant()
                                                      .getCurrentNetworkPanel()
                                                      .getEditorPanel()
                                                      .getPreResolutionEvidence();
        InferenceOptionsDialog inferenceOptionsDialog =
                new InferenceOptionsDialog(probNet, parent, MulticriteriaOptions.Type.COST_EFFECTIVENESS);
        if (inferenceOptionsDialog.getSelectedButton() != OkCancelHorizontalDialog.OK_BUTTON) {
            return;
        }
        CostEffectivenessDialog costEffectivenessDialog = new CostEffectivenessDialog(parent, probNet, preResolutionEvidence);
        if ((costEffectivenessDialog.requestData() != OkCancelHorizontalDialog.OK_BUTTON)) {
            return;
        }
        ScopeSelectorPanel scopeSelectorPanel = costEffectivenessDialog.getScopeSelectorPanel();
        JDialog dialog = switch (scopeSelectorPanel.getScopeType()) {
            case DECISION -> {
                EvidenceCase newPreResolutionEvidence = new EvidenceCase(preResolutionEvidence);
                for (Finding finding : scopeSelectorPanel.getSelectedFindings()) {
                    newPreResolutionEvidence.addFinding(finding);
                }
                yield new CEDecisionResults(parent, probNet, newPreResolutionEvidence,
                                            scopeSelectorPanel.getDecisionSelected());
            }
            case GLOBAL -> {
                CEP cep = switch (probNet.getNetworkType()) {
                    case NetworkType networkType when networkType instanceof InfluenceDiagramType || networkType instanceof MIDType -> {
                        try {
                            VECEAnalysis veGlobalCEA = new VECEAnalysis(probNet);
                            veGlobalCEA.setPreResolutionEvidence(preResolutionEvidence);
                            yield (CEP) veGlobalCEA.getUtility().elementTable.get(0);
                        } catch (IncompatibleEvidenceException e) {
                            throw new UnreacheableException(e);
                        }
                    }
                    case null, default -> {
                        DANDecompositionIntoSymmetricDANsCEA decompositionAlgorithmArticleCEA = new DANDecompositionIntoSymmetricDANsCEA(
                                probNet, preResolutionEvidence);
                        yield (CEP) decompositionAlgorithmArticleCEA.getUtility().elementTable.get(0);
                    }
                };
                yield new CEPDialog(parent, cep, probNet);
            }
        };
        dialog.setVisible(true);
    }
}
