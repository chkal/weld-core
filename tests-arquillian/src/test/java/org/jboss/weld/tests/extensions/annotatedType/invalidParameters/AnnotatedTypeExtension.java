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
package org.jboss.weld.tests.extensions.annotatedType.invalidParameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedCallable;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.weld.util.collections.Arrays2;

public class AnnotatedTypeExtension implements Extension
{
   
   public void addTumbleDryer(@Observes BeforeBeanDiscovery beforeBeanDiscovery)
   {
      
      final Set<AnnotatedConstructor<TumbleDryer>> constructors = new HashSet<AnnotatedConstructor<TumbleDryer>>();

      
      final AnnotatedType<TumbleDryer> tumbleDryer = new AnnotatedType<TumbleDryer>()
      {

         public Set<AnnotatedConstructor<TumbleDryer>> getConstructors()
         {
            return constructors;
         }

         public Set<AnnotatedField<? super TumbleDryer>> getFields()
         {
            return Collections.emptySet();
         }
         
         public Set<AnnotatedMethod<? super TumbleDryer>> getMethods()
         {
            return Collections.emptySet();
         }
         
         // Now the easy stuff

         public Class<TumbleDryer> getJavaClass()
         {
            return TumbleDryer.class;
         }

         public <T extends Annotation> T getAnnotation(Class<T> annotationType)
         {
            // Class has no annotations
            return null;
         }

         public Set<Annotation> getAnnotations()
         {
            return Collections.emptySet();
         }

         public Type getBaseType()
         {
            return TumbleDryer.class;
         }

         public Set<Type> getTypeClosure()
         {
            return Arrays2.<Type>asSet(TumbleDryer.class, Object.class);
         }

         public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
         {
            // Class has no annotations
            return false;
         }
         
      };
            
      final List<AnnotatedParameter<TumbleDryer>> clothesParameters = new ArrayList<AnnotatedParameter<TumbleDryer>>();
      final AnnotatedConstructor<TumbleDryer> clothesConstructor = new AnnotatedConstructor<TumbleDryer>()
      {

         public Constructor<TumbleDryer> getJavaMember()
         {
            try
            {
               return TumbleDryer.class.getDeclaredConstructor(Clothes.class);
            }
            catch (NoSuchMethodException e)
            {
               throw new RuntimeException(e);
            }
         }

         public List<AnnotatedParameter<TumbleDryer>> getParameters()
         {
            return clothesParameters;
         }

         public AnnotatedType<TumbleDryer> getDeclaringType()
         {
            return tumbleDryer;
         }

         public boolean isStatic()
         {
            return false;
         }

         public <T extends Annotation> T getAnnotation(Class<T> annotationType)
         {
            if (annotationType.equals(Inject.class))
            {
               return annotationType.cast(InjectLiteral.INSTANCE);
            }
            else
            {
               return null;
            }
         }

         public Set<Annotation> getAnnotations()
         {
            return Collections.<Annotation>singleton(InjectLiteral.INSTANCE);
         }

         public Type getBaseType()
         {
            return TumbleDryer.class;
         }

         public Set<Type> getTypeClosure()
         {
            return Arrays2.<Type>asSet(TumbleDryer.class, Object.class);
         }

         public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
         {
            if (annotationType.equals(Inject.class))
            {
               return true;
            }
            else
            {
               return false;
            }
         }
      };
      constructors.add(clothesConstructor);
      
      AnnotatedParameter<TumbleDryer> clothesParameter = new AnnotatedParameter<TumbleDryer>()
      {

         public AnnotatedCallable<TumbleDryer> getDeclaringCallable()
         {
            return clothesConstructor;
         }

         public int getPosition()
         {
            return 0;
         }


         public <T extends Annotation> T getAnnotation(Class<T> annotationType)
         {
            return null;
         }

         public Set<Annotation> getAnnotations()
         {
            return Collections.emptySet();
         }

         public Type getBaseType()
         {
            return Clothes.class;
         }

         public Set<Type> getTypeClosure()
         {
            return Arrays2.<Type>asSet(Clothes.class, Object.class);
         }

         public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
         {
            return false;
         }
      };
      clothesParameters.add(clothesParameter);
      clothesParameters.add(clothesParameter);
      
      beforeBeanDiscovery.addAnnotatedType(tumbleDryer);
   }
   
}
