/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component;

import static org.testng.AssertJUnit.assertEquals;

import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.core.io.Resource;
import org.springframework.security.util.InMemoryResource;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.opengamma.util.test.TestGroup;

/**
 * Test.
 */
@Test(groups = TestGroup.UNIT)
public class ComponentConfigIniLoaderTest {

  private static final ComponentLogger LOGGER = ComponentLogger.Sink.INSTANCE;
  private static final String NEWLINE = "\n";

  public void test_loadValid() {
    ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();
    ComponentConfigIniLoader loader = new ComponentConfigIniLoader(LOGGER, properties );
    String text =
        "# comment" + NEWLINE +
        "[global]" + NEWLINE +
        "a = c" + NEWLINE +
        "b = d" + NEWLINE +
        "" + NEWLINE +
        "[block]" + NEWLINE +
        "m = p" + NEWLINE +
        "n = ${a}" + NEWLINE +  // property from [global]
        "o = ${input}" + NEWLINE;  // property from injected properties
    Resource resource = new InMemoryResource(text.getBytes(Charsets.UTF_8), "Test"
    );
    properties.put("input", "text");
    
    ComponentConfig test = new ComponentConfig();
    loader.load(resource, 0, test);
    assertEquals(2, test.getGroups().size());
    
    LinkedHashMap<String, String> testGlobal = test.getGroup("global");
    assertEquals(2, testGlobal.size());
    assertEquals("c", testGlobal.get("a"));
    assertEquals("d", testGlobal.get("b"));
    
    LinkedHashMap<String, String> testBlock = test.getGroup("block");
    assertEquals(3, testBlock.size());
    assertEquals("p", testBlock.get("m"));
    assertEquals("c", testBlock.get("n"));
    assertEquals("text", testBlock.get("o"));
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void test_loadInvalid_doubleKey() {
    ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();
    ComponentConfigIniLoader loader = new ComponentConfigIniLoader(LOGGER, properties );
    Resource resource = new InMemoryResource(
        "[block]" + NEWLINE +
        "m = p" + NEWLINE +
        "m = s" + NEWLINE
    );
    
    loader.load(resource, 0, new ComponentConfig());
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void test_loadInvalid_replacementNotFound() {
    ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();
    ComponentConfigIniLoader loader = new ComponentConfigIniLoader(LOGGER, properties );
    Resource resource = new InMemoryResource(
        "[block]" + NEWLINE +
        "m = ${notFound}" + NEWLINE
    );
    
    loader.load(resource, 0, new ComponentConfig());
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void test_loadInvalid_propertyNotInGroup() {
    ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();
    ComponentConfigIniLoader loader = new ComponentConfigIniLoader(LOGGER, properties );
    Resource resource = new InMemoryResource(
        "m = foo" + NEWLINE
    );
    
    loader.load(resource, 0, new ComponentConfig());
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void test_loadInvalid_propertyNoEquals() {
    ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();
    ComponentConfigIniLoader loader = new ComponentConfigIniLoader(LOGGER, properties );
    Resource resource = new InMemoryResource(
        "[block]" + NEWLINE +
        "m" + NEWLINE
    );
    
    loader.load(resource, 0, new ComponentConfig());
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void test_loadInvalid_propertyEmptyKey() {
    ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();
    ComponentConfigIniLoader loader = new ComponentConfigIniLoader(LOGGER, properties );
    Resource resource = new InMemoryResource(
        "[block]" + NEWLINE +
        "= foo" + NEWLINE
    );
    
    loader.load(resource, 0, new ComponentConfig());
  }

}
