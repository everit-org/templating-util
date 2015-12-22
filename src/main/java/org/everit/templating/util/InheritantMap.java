/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.templating.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.everit.templating.TemplateConstants;

/**
 * A Map implementation that allows to return values from a parent map in the way that modifications
 * are only applied to the current instance.
 *
 * @param <K>
 *          Type of key of the map.
 * @param <V>
 *          Type of value of the map.
 */
public class InheritantMap<K, V> implements Map<K, V> {

  /**
   * The map that holds the newly inserted keys and values.
   */
  private final Map<K, V> internalMap;

  /**
   * Parent map.
   */
  private final Map<K, V> parentMap;

  /**
   * In case of true, those keys will be updated on the parent that were already available there,
   * otherwise every operation will be processed on the child map.
   */
  private final boolean updateOnParent;

  /**
   * Constructor that uses a {@link HashMap} as a child Map.
   *
   * @param parentMap
   *          The map that holds key-value pairs that are accessible, but not modifiable via this
   *          map instance.
   * @param updateOnParent
   *          In case of true, those keys will be updated on the parent that were already available
   *          there, otherwise every operation will be processed on the child map.
   */
  public InheritantMap(final Map<K, V> parentMap, final boolean updateOnParent) {
    this(parentMap, new HashMap<K, V>(), updateOnParent);
  }

  /**
   * Constructor.
   *
   * @param parentMap
   *          The map that holds key-value pairs that are accessible, but not modifiable via this
   *          map instance.
   * @param childMap
   *          The child map that is read first or updated directly.
   * @param updateOnParent
   *          In case of true, those keys will be updated on the parent that were already available
   *          there, otherwise every operation will be processed on the child map.
   */
  public InheritantMap(final Map<K, V> parentMap, final Map<K, V> childMap,
      final boolean updateOnParent) {
    this.parentMap = parentMap;
    this.internalMap = childMap;
    this.updateOnParent = updateOnParent;
  }

  @Override
  public void clear() {
    internalMap.clear();
  }

  @Override
  public boolean containsKey(final Object key) {
    boolean result = internalMap.containsKey(key);
    if (!result && parentMap != null) {
      return parentMap.containsKey(key);
    }
    return result;
  }

  @Override
  public boolean containsValue(final Object value) {
    boolean result = internalMap.containsValue(value);
    if (!result && parentMap != null) {
      return parentMap.containsValue(value);
    }
    return result;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    @SuppressWarnings("unchecked")
    InheritantMap<K, V> other = (InheritantMap<K, V>) obj;
    if (internalMap == null) {
      if (other.internalMap != null) {
        return false;
      }
    } else if (!internalMap.equals(other.internalMap)) {
      return false;
    }
    if (parentMap == null) {
      if (other.parentMap != null) {
        return false;
      }
    } else if (!parentMap.equals(other.parentMap)) {
      return false;
    }
    return true;
  }

  @Override
  public V get(final Object key) {

    V result = internalMap.get(key);

    if (result != null) {
      return result;
    }

    if (!internalMap.containsKey(key)) {
      return parentMap.get(key);
    }
    return null;

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((internalMap == null) ? 0 : internalMap.hashCode());
    result = prime * result + ((parentMap == null) ? 0 : parentMap.hashCode());
    return result;
  }

  @Override
  public boolean isEmpty() {
    boolean result = internalMap.isEmpty();
    if (result && parentMap != null) {
      return parentMap.isEmpty();
    }
    return result;
  }

  @Override
  public Set<K> keySet() {
    throw new UnsupportedOperationException();
  }

  @Override
  public V put(final K key, final V value) {
    if (TemplateConstants.VAR_TEMPLATE_CONTEXT.equals(key)) {
      throw new ReservedWordException(
          "'" + TemplateConstants.VAR_TEMPLATE_CONTEXT + "' is a reserved word");
    }

    if (updateOnParent && parentMap != null && parentMap.containsKey(key)) {
      return parentMap.put(key, value);
    } else {
      return internalMap.put(key, value);
    }
  }

  @Override
  public void putAll(final Map<? extends K, ? extends V> m) {
    for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Same as {@link #put(Object, Object)} but without checking reserved words.
   *
   * @param key
   *          The key.
   * @param value
   *          The value.
   * @return The previous value if existed.
   */
  public V putWithoutChecks(final K key, final V value) {
    return internalMap.put(key, value);
  }

  @Override
  public V remove(final Object key) {
    return internalMap.remove(key);
  }

  @Override
  public int size() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<V> values() {
    throw new UnsupportedOperationException();
  }
}
