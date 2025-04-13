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
 * Represents a time duration value (e.g., 10ms, 2s, etc.).
 * This class parses time duration strings into their nanosecond equivalent.
 */

public class TimeDuration implements Token {
  private final String raw;
  private final double value;
  private final String unit;

  public TimeDuration(String raw) {
    this.raw = raw.trim().toLowerCase();
    int i = 0;
    while (i < raw.length() && (Character.isDigit(raw.charAt(i)) || raw.charAt(i) == '.')) {
      i++;
    }
    this.value = Double.parseDouble(raw.substring(0, i));
    this.unit = raw.substring(i);
  }

  public long getMilliseconds() {
    switch (unit) {
      case "ns": return (long) (value / 1_000_000);
      case "us": return (long) (value / 1_000);
      case "ms": return (long) value;
      case "s":  return (long) (value * 1000);
      case "m":  return (long) (value * 60 * 1000);
      case "h":  return (long) (value * 60 * 60 * 1000);
      default: throw new IllegalArgumentException("Unknown time unit: " + unit);
    }
  }

  public long getNanoseconds() {
    switch (unit) {
      case "ns": return (long) value;
      case "us": return (long) (value * 1_000);
      case "ms": return (long) (value * 1_000_000);
      case "s":  return (long) (value * 1_000_000_000);
      case "m":  return (long) (value * 60 * 1_000_000_000L);
      case "h":  return (long) (value * 60 * 60 * 1_000_000_000L);
      default: throw new IllegalArgumentException("Unknown time unit: " + unit);
    }
  }

  @Override
  public String value() {
    return raw;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonObject toJson() {
    JsonObject obj = new JsonObject();
    obj.addProperty("type", "TIME_DURATION");
    obj.addProperty("value", value);
    obj.addProperty("unit", unit);
    obj.addProperty("milliseconds", getMilliseconds());
    return obj;
  }
}
