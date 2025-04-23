package org.openmarkov.costEffectiveness.localize;

import org.jetbrains.annotations.NotNull;
import org.openmarkov.annotation_processing.localization_bindings.BindLocalizations;
import org.openmarkov.gui.localize.spi.LocalizeResourcesProvider;


@BindLocalizations(filePath = "costeffectiveness/localize")
public class CostEffectivenessResourceBundleProvider implements LocalizeResourcesProvider {
	
	@Override
	public final @NotNull String getRootOfResources() {
		return "/costeffectiveness";
	}
	
}
