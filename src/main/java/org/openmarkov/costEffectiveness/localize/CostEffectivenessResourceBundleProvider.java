package org.openmarkov.costEffectiveness.localize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openmarkov.gui.loader.element.ImageLoader;
import org.openmarkov.gui.localize.StringDatabase;
import org.openmarkov.gui.localize.spi.LocalizeResourcesProvider;



public class CostEffectivenessResourceBundleProvider implements LocalizeResourcesProvider {

	
	@Override
	public String getInfixForPathGetBundles() {
		return getInfixForPathGetBundles("org.openmarkov.costEffectiveness");
	}

	@Override
	public ResourceBundle getBundle(String baseName, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public InputStream auxGetResourceAsStream(String name) throws IOException {
		Module m = this.getClass().getModule();
		return m.getResourceAsStream(name);
	}

	@Override
	public URL auxGetResource(String infix) {
		return this.getClass().getResource(infix);
	}
	
	@Override public Class<? extends LocalizeResourcesProvider> auxClass() {
		return CostEffectivenessResourceBundleProvider.class;
	}
	
	
}
