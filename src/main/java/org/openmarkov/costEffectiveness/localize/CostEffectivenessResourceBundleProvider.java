package org.openmarkov.costEffectiveness.localize;

import org.jetbrains.annotations.NotNull;
import org.openmarkov.gui.localize.spi.LocalizeResourcesProvider;



public class CostEffectivenessResourceBundleProvider implements LocalizeResourcesProvider {
	
	@Override
	public final @NotNull String getRootOfResources() {
		return "/costeffectiveness";
	}
	
	
}
