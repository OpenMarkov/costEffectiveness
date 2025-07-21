/*
 * Copyright (c) CISIAD, UNED, Spain,  2019. Licensed under the GPLv3 licence
 * Unless required by applicable law or agreed to in writing,
 * this code is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OF ANY KIND.
 */

package org.openmarkov.costEffectiveness;

import org.jetbrains.annotations.Nullable;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.UnexpectedInferenceException;
import org.openmarkov.core.inference.MulticriteriaOptions;
import org.openmarkov.core.inference.tasks.CEAnalysis;
import org.openmarkov.core.localize.StringDatabase;
import org.openmarkov.core.model.network.CEP;
import org.openmarkov.core.model.network.EvidenceCase;
import org.openmarkov.core.model.network.Finding;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.type.DecisionAnalysisNetworkType;
import org.openmarkov.core.model.network.type.InfluenceDiagramType;
import org.openmarkov.core.model.network.type.MIDType;
import org.openmarkov.gui.dialog.costeffectiveness.CEPDialog;
import org.openmarkov.gui.dialog.inference.common.InferenceOptionsDialog;
import org.openmarkov.gui.dialog.inference.common.ScopeSelectorPanel;
import org.openmarkov.gui.dialog.inference.common.ScopeType;
import org.openmarkov.gui.window.MainPanel;
import org.openmarkov.inference.algorithm.decompositionIntoSymmetricDANs.ceanalysis.DANDecompositionIntoSymmetricDANsCEA;
import org.openmarkov.inference.algorithm.variableElimination.tasks.VECEAnalysis;

import javax.swing.*;

/**
 * @author jperez-martin
 */

public class CostEffectivenessFrame {
    
    @Nullable private CEPDialog cepDialog;
    
    /**
     * Constructor. initialises the instance.
     *
     * @param owner window that owns the dialog.
     */
    public CostEffectivenessFrame(JFrame owner) {
        super();
        ProbNet probNet = MainPanel.getUniqueInstance().getMainPanelListenerAssistant().getCurrentNetworkPanel()
                                   .getProbNet();
        EvidenceCase preResolutionEvidence = MainPanel.getUniqueInstance().
                                                      getMainPanelMenuAssistant()
                                                      .getCurrentNetworkPanel()
                                                      .getEditorPanel()
                                                      .getPreResolutionEvidence();
        InferenceOptionsDialog inferenceOptionsDialog = new InferenceOptionsDialog(probNet, owner,
                                                                                   MulticriteriaOptions.Type.COST_EFFECTIVENESS);
        if (inferenceOptionsDialog.getSelectedButton() != InferenceOptionsDialog.OK_BUTTON) {
            return;
        }
        CostEffectivenessDialog costEffectivenessDialog = new CostEffectivenessDialog(owner, probNet,
                                                                                      preResolutionEvidence);
        if ((costEffectivenessDialog.requestData() == CostEffectivenessDialog.OK_BUTTON)) {
            ScopeSelectorPanel scopeSelectorPanel = costEffectivenessDialog.getScopeSelectorPanel();
            try {
                if (scopeSelectorPanel.getScopeType().equals(ScopeType.GLOBAL)) {
                    if (probNet.getNetworkType() instanceof InfluenceDiagramType || probNet
                            .getNetworkType() instanceof MIDType) {
                        CEAnalysis veGlobalCEA = new VECEAnalysis(probNet);
                        veGlobalCEA.setPreResolutionEvidence(preResolutionEvidence);
                        CEP cep = (CEP) veGlobalCEA.getUtility().elementTable.get(0);
                        cepDialog = new CEPDialog(owner, cep, probNet);
                    } else if (probNet.getNetworkType() instanceof DecisionAnalysisNetworkType) {
                        CEAnalysis decompositionAlgorithmArticleCEA = new DANDecompositionIntoSymmetricDANsCEA(
                                probNet, preResolutionEvidence);
                        CEP cep = (CEP) decompositionAlgorithmArticleCEA.getUtility().elementTable.get(0);
                        cepDialog = new CEPDialog(owner, cep, probNet);
                    }
                } else {
                    EvidenceCase newPreResolutionEvidence = new EvidenceCase(preResolutionEvidence);
                    for (Finding finding : scopeSelectorPanel.getSelectedFindings()) {
                        try {
                            newPreResolutionEvidence.addFinding(finding);
                        } catch (IncompatibleEvidenceException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), StringDatabase.getUniqueInstance()
                                                                                              .getString("LoadEvidence.Error.IncompatibleEvidence"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    new CEDecisionResults(owner, probNet, newPreResolutionEvidence,
                                          scopeSelectorPanel.getDecisionSelected());
                }
            } catch (NotEvaluableNetworkException e) {
                JOptionPane.showMessageDialog(null,
                                              StringDatabase.getUniqueInstance()
                                                            .getString("CostEffectivenessDeterministic.Error") + ". " + e
                                                      .getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IncompatibleEvidenceException | UnexpectedInferenceException e) {
                e.printStackTrace();
            }
        }
    }
    
    public @Nullable CEPDialog getCepDialog() {
        return this.cepDialog;
    }
}
