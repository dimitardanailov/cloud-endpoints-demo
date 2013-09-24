/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-09-16 16:01:30 UTC)
 * on 2013-09-24 at 17:41:38 UTC 
 * Modify at your own risk.
 */

package com.appspot.demos_biira.birra.model;

/**
 * Model definition for Beer.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the birra. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Beer extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String beerName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String country;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String description;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Text image;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String kindOfBeer;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double latitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double longitude;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long numberOfDrinks;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long score;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getBeerName() {
    return beerName;
  }

  /**
   * @param beerName beerName or {@code null} for none
   */
  public Beer setBeerName(java.lang.String beerName) {
    this.beerName = beerName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCountry() {
    return country;
  }

  /**
   * @param country country or {@code null} for none
   */
  public Beer setCountry(java.lang.String country) {
    this.country = country;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * @param description description or {@code null} for none
   */
  public Beer setDescription(java.lang.String description) {
    this.description = description;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Beer setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Text getImage() {
    return image;
  }

  /**
   * @param image image or {@code null} for none
   */
  public Beer setImage(Text image) {
    this.image = image;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getKindOfBeer() {
    return kindOfBeer;
  }

  /**
   * @param kindOfBeer kindOfBeer or {@code null} for none
   */
  public Beer setKindOfBeer(java.lang.String kindOfBeer) {
    this.kindOfBeer = kindOfBeer;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getLatitude() {
    return latitude;
  }

  /**
   * @param latitude latitude or {@code null} for none
   */
  public Beer setLatitude(java.lang.Double latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getLongitude() {
    return longitude;
  }

  /**
   * @param longitude longitude or {@code null} for none
   */
  public Beer setLongitude(java.lang.Double longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getNumberOfDrinks() {
    return numberOfDrinks;
  }

  /**
   * @param numberOfDrinks numberOfDrinks or {@code null} for none
   */
  public Beer setNumberOfDrinks(java.lang.Long numberOfDrinks) {
    this.numberOfDrinks = numberOfDrinks;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getScore() {
    return score;
  }

  /**
   * @param score score or {@code null} for none
   */
  public Beer setScore(java.lang.Long score) {
    this.score = score;
    return this;
  }

  @Override
  public Beer set(String fieldName, Object value) {
    return (Beer) super.set(fieldName, value);
  }

  @Override
  public Beer clone() {
    return (Beer) super.clone();
  }

}
