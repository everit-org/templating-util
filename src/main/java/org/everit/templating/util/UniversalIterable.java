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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterable that can iterate throug {@link Iterable} instances but also through primitive or
 * Object arrays.
 *
 * @param <T>
 *          The type of the iterable.
 */
public class UniversalIterable<T> implements Iterable<T> {

  /**
   * Iterator that goes through an array.
   *
   * @param <T>
   *          The type of the elements.
   */
  private abstract static class AbstractArrayIterator<T> extends AbstractIterator<T> {

    private int cursor = 0;

    private final int length;

    AbstractArrayIterator(final int length) {
      this.length = length;
    }

    public abstract T element(int index);

    @Override
    public boolean hasNext() {
      return cursor < length;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return element(cursor++);
    }

  }

  /**
   * Abstract Iterator implementation that does not support remove function.
   *
   * @param <T>
   *          The type of the elements.
   */
  private abstract static class AbstractIterator<T> implements Iterator<T> {
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Iterator that goes through a boolean array.
   */
  private static class BooleanArrayIterator extends AbstractArrayIterator<Boolean> {

    private final boolean[] array;

    BooleanArrayIterator(final boolean[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Boolean element(final int index) {
      return array[index];
    }

  }

  /**
   * Iterator that goes through a byte array.
   */
  private static class ByteArrayIterator extends AbstractArrayIterator<Byte> {

    private final byte[] array;

    ByteArrayIterator(final byte[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Byte element(final int index) {
      return array[index];
    }

  }

  /**
   * Iterator that goes through a character array.
   */
  private static class CharArrayIterator extends AbstractArrayIterator<Character> {

    private final char[] array;

    CharArrayIterator(final char[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Character element(final int index) {
      return array[index];
    }

  }

  /**
   * Types that a collection can have in a template.
   */
  private enum CollectionTypes {
    BOOLEAN_ARRAY(boolean[].class),

    BYTE_ARRAY(byte[].class),

    CHAR_ARRAY(char[].class),

    DOUBLE_ARRAY(double[].class),

    FLOAT_ARRAY(float[].class),

    INT_ARRAY(int[].class),

    INTEGER(Integer.class),

    ITERABLE(Iterable.class),

    LONG_ARRAY(long[].class),

    OBJECT_ARRAY(Object[].class),

    SHORT_ARRAY(short[].class);

    private final Class<?> collectionType;

    CollectionTypes(final Class<?> collectionType) {
      this.collectionType = collectionType;
    }

    public Class<?> getCollectionType() {
      return collectionType;
    }
  }

  /**
   * Iterator that goes through a double array.
   */
  private static class DoubleArrayIterator extends AbstractArrayIterator<Double> {

    private final double[] array;

    DoubleArrayIterator(final double[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Double element(final int index) {
      return array[index];
    }
  }

  /**
   * Iterator that goes through a float array.
   */
  private static class FloatArrayIterator extends AbstractArrayIterator<Float> {

    private final float[] array;

    FloatArrayIterator(final float[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Float element(final int index) {
      return array[index];
    }
  }

  /**
   * Iterator that goes threw an int array.
   */
  private static class IntArrayIterator extends AbstractArrayIterator<Integer> {

    private final int[] array;

    IntArrayIterator(final int[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Integer element(final int index) {
      return array[index];
    }
  }

  /**
   * Iterator that goes threw an int array.
   */
  private static class IntegerIterator extends AbstractIterator<Integer> {

    private int cursor = 0;

    private final int n;

    IntegerIterator(final Integer n) {
      this.n = n;
    }

    @Override
    public boolean hasNext() {
      return cursor < n;
    }

    @Override
    public Integer next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return cursor++;
    }

  }

  /**
   * Iterator that goes threw a long array.
   */
  private static class LongArrayIterator extends AbstractArrayIterator<Long> {

    private final long[] array;

    LongArrayIterator(final long[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Long element(final int index) {
      return array[index];
    }
  }

  /**
   * Iterator that goes threw an Object array.
   */
  private static class ObjectArrayIterator extends AbstractArrayIterator<Object> {

    private final Object[] array;

    ObjectArrayIterator(final Object[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Object element(final int index) {
      return array[index];
    }

  }

  /**
   * Iterator that goes threw a short array.
   */
  private static class ShortArrayIterator extends AbstractArrayIterator<Short> {

    private final short[] array;

    ShortArrayIterator(final short[] array) {
      super(array.length);
      this.array = array;
    }

    @Override
    public Short element(final int index) {
      return array[index];
    }
  }

  private final Object collection;

  private final CollectionTypes collectionType;

  /**
   * Constructor.
   *
   * @param collection
   *          The collection that this iterator will iterate through.
   */
  public UniversalIterable(final Object collection) {
    if (collection == null) {
      throw new NullPointerException("Collection must not be null");
    }

    CollectionTypes[] collectionTypeArray = CollectionTypes.values();

    CollectionTypes selectedCollectionType = null;
    Class<? extends Object> collectionClass = collection.getClass();
    for (int i = 0, n = collectionTypeArray.length; i < n && selectedCollectionType == null; i++) {
      if (collectionTypeArray[i].getCollectionType().isAssignableFrom(collectionClass)) {
        selectedCollectionType = collectionTypeArray[i];
      }
    }

    if (selectedCollectionType == null) {
      throw new IllegalArgumentException(
          "Unrecognized type of collection (Iterable, Array and number are accepted): "
              + collectionClass);
    }

    this.collectionType = selectedCollectionType;
    this.collection = collection;
  }

  @Override
  public Iterator<T> iterator() {
    Object result = null;

    switch (collectionType) {
      case BOOLEAN_ARRAY:
        result = new BooleanArrayIterator((boolean[]) collection);
        break;
      case BYTE_ARRAY:
        result = new ByteArrayIterator((byte[]) collection);
        break;
      case CHAR_ARRAY:
        result = new CharArrayIterator((char[]) collection);
        break;
      case DOUBLE_ARRAY:
        result = new DoubleArrayIterator((double[]) collection);
        break;
      case FLOAT_ARRAY:
        result = new FloatArrayIterator((float[]) collection);
        break;
      case INT_ARRAY:
        result = new IntArrayIterator((int[]) collection);
        break;
      case LONG_ARRAY:
        result = new LongArrayIterator((long[]) collection);
        break;
      case OBJECT_ARRAY:
        result = new ObjectArrayIterator((Object[]) collection);
        break;
      case SHORT_ARRAY:
        result = new ShortArrayIterator((short[]) collection);
        break;
      case ITERABLE:
        @SuppressWarnings("unchecked")
        Iterable<T> iterable = (Iterable<T>) collection;
        result = iterable.iterator();
        break;
      case INTEGER:
        result = new IntegerIterator((Integer) collection);
        break;
      default:
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    Iterator<T> typedResult = (Iterator<T>) result;
    return typedResult;
  }
}
