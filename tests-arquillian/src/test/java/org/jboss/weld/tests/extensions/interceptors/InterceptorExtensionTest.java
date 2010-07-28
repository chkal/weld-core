/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.tests.extensions.interceptors;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.BeanArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.weld.test.Utils;
import org.jboss.weld.tests.category.Integration;
import org.jboss.weld.tests.util.annotated.TestAnnotatedTypeBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * Tests that interceptors registered via the SPI work correctly
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
@Category(Integration.class)
@RunWith(Arquillian.class)
public class InterceptorExtensionTest
{
   @Deployment
   public static Archive<?> deploy() 
   {
      return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
         .addModule(
               ShrinkWrap.create(BeanArchive.class)
                  .intercept(IncrementingInterceptor.class, LifecycleInterceptor.class)
                  .addPackage(InterceptorExtensionTest.class.getPackage())
                  .addPackage(TestAnnotatedTypeBuilder.class.getPackage())
                  .addServiceProvider(Extension.class, InterceptorExtension.class)
         );
   }
   
   @Inject
   private BeanManager beanManager;
   
   @Test
   public void testInterceptorCalled(NumberSource ng)
   {
      Assert.assertEquals(1, ng.value());
      Assert.assertTrue(IncrementingInterceptor.isDoAroundCalled());
   }

   @Test
   @SuppressWarnings("unchecked")
   public void testLifecycleInterceptor()
   {
      Bean bean = beanManager.getBeans(Marathon.class).iterator().next();
      CreationalContext creationalContext = beanManager.createCreationalContext(bean);
      Marathon m = (Marathon)bean.create(creationalContext);
      
      Assert.assertTrue(LifecycleInterceptor.isPostConstructCalled());
      Assert.assertEquals(24, m.getLength());
      bean.destroy(m, creationalContext);
      Assert.assertTrue(LifecycleInterceptor.isPreDestroyCalled());
   }

}
