/*
 * Copyright © 2024 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package io.cdap.wrangler.api.parser;

import com.google.gson.JsonObject;

/**
 * Represents a byte size value (e.g., 10KB, 2MB, etc.).
 * This class parses byte size strings into their byte equivalent.
 */

public class ByteSize implements Token {
  private final String raw;
  private final double value;
  private final String unit;

  public ByteSize(String raw) {
    this.raw = raw.trim().toUpperCase();
    int i = 0;
    while (i < this.raw.length() && (Character.isDigit(this.raw.charAt(i)) || this.raw.charAt(i) == '.')) {
      i++;
    }
    this.value = Double.parseDouble(this.raw.substring(0, i));
    this.unit = this.raw.substring(i);
  }

  public long getBytes() {
    switch (unit) {
      case "B":  return (long) value;
      case "KB": return (long) (value * 1024);
      case "MB": return (long) (value * 1024 * 1024);
      case "GB": return (long) (value * 1024 * 1024 * 1024);
      case "TB": return (long) (value * 1024L * 1024 * 1024 * 1024);
      case "PB": return (long) (value * 1024L * 1024 * 1024 * 1024 * 1024);
      default: throw new IllegalArgumentException("Unknown byte unit: " + unit);
    }
  }

  @Override
  public String value() {
    return raw;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonObject toJson() {
    JsonObject obj = new JsonObject();
    obj.addProperty("type", "BYTE_SIZE");
    obj.addProperty("value", value);
    obj.addProperty("unit", unit);
    obj.addProperty("bytes", getBytes());
    return obj;
  }
}
