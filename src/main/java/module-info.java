module org.openmarkov.costeffectiveness {
	requires org.openmarkov.core;
	requires org.openmarkov.gui;
	requires org.openmarkov.inference.decompositionintosymmetricdans;
	requires org.apache.commons.io;
	requires swing.layout;
	requires org.jfree.jfreechart;
	requires org.openmarkov.inference.variableelimination;
	requires org.jetbrains.annotations;
    requires java.desktop;
    
    exports org.openmarkov.costEffectiveness;
    exports org.openmarkov.costEffectiveness.localize;
	
    provides org.openmarkov.gui.localize.spi.LocalizeResourcesProvider with org.openmarkov.costEffectiveness.localize.CostEffectivenessResourceBundleProvider;
	/*
	 * requires org.openmarkov.gui; requires org.jfree.jfreechart; requires
	 * org.openmarkov.inference.variableelimination; requires java.desktop; requires
	 * org.apache.logging.log4j;
	 */

}
