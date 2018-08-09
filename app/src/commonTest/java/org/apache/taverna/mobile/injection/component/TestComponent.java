package org.apache.taverna.mobile.injection.component;

import org.apache.taverna.mobile.injection.module.ApplicationTestModule;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
