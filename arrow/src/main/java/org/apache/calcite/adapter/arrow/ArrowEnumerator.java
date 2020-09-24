/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.calcite.adapter.arrow;

import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.VectorSchemaRoot;

import org.apache.arrow.vector.types.pojo.Field;

import org.apache.calcite.linq4j.Enumerator;

import java.util.ArrayList;
import java.util.List;

public class ArrowEnumerator implements Enumerator<Object> {
  private final int[] fields;
  private final VectorSchemaRoot[] vectorSchemaRoots;

  public ArrowEnumerator(VectorSchemaRoot[] vectorSchemaRoots, int[] fields) {
    this.vectorSchemaRoots = vectorSchemaRoots;
    this.fields = fields;
  }

  public VectorSchemaRoot[] getVectorSchemaRoots() {
    final int[] projected = this.fields;
    int rootSize = vectorSchemaRoots.length;
    VectorSchemaRoot[] vectorSchemaRoots = new VectorSchemaRoot[rootSize];
    System.out.println(this.vectorSchemaRoots[0].getFieldVectors());
    for (int i = 0; i < rootSize; i++) {
      List<FieldVector> fieldVectors = new ArrayList<>();
      List<Field> fields = new ArrayList<>();
      for (int value : projected) {
        FieldVector fieldVector = this.vectorSchemaRoots[i].getFieldVectors().get(value);
        fieldVectors.add(fieldVector);
        fields.add(fieldVector.getField());
      }
      vectorSchemaRoots[i] = new VectorSchemaRoot(fields, fieldVectors, this.vectorSchemaRoots[i].getRowCount());
    }
    return vectorSchemaRoots;
  }

  public static int[] identityList(int n) {
    int[] integers = new int[n];
    for (int i = 0; i < n; i++) {
      integers[i] = i;
    }
    return integers;
  }

  public Object current() {
    return null;
  }

  public boolean moveNext() {
    return false;
  }

  public void reset() {
  }

  public void close() {
  }
}