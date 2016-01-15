package com.talanlabs.component.factory;

import com.talanlabs.component.IComponent;

public abstract class AbstractComponentFactoryListener implements IComponentFactoryListener {

	@Override
	public <G extends IComponent> void afterCreated(Class<G> interfaceClass, G instance) {
	}
}
