/*
 * Copyright 2004-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.faces.webflow;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.el.SpringBeanELResolver;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.binding.expression.el.MapAdaptableELResolver;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.expression.el.FlowResourceELResolver;
import org.springframework.webflow.expression.el.ImplicitFlowVariableELResolver;
import org.springframework.webflow.expression.el.RequestContextELResolver;
import org.springframework.webflow.expression.el.ScopeSearchingELResolver;

/**
 * Custom {@link ELResolver} for resolving web flow specific expressions.
 * 
 * @author Jeremy Grelle
 * @author Phillip Webb
 * 
 * @since 2.4
 */
public class FlowELResolver extends CompositeELResolver {

	public FlowELResolver() {
		add(new RequestContextELResolver());
		add(new ImplicitFlowVariableELResolver());
		add(new FlowResourceELResolver());
		add(new ScopeSearchingELResolver());
		add(new MapAdaptableELResolver());
		add(new BeanELResolver());
	}

	private static class BeanELResolver extends SpringBeanELResolver {

		private static final BeanFactory EMPTY_BEAN_FACTORY = new StaticListableBeanFactory();

		protected BeanFactory getBeanFactory(ELContext elContext) {
			RequestContext requestContext = RequestContextHolder.getRequestContext();
			if (requestContext == null) {
				return EMPTY_BEAN_FACTORY;
			}
			BeanFactory beanFactory = requestContext.getActiveFlow().getApplicationContext();
			return beanFactory != null ? beanFactory : EMPTY_BEAN_FACTORY;
		}
	}

}
