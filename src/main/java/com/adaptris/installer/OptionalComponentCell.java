package com.adaptris.installer;

import java.io.InputStream;
import java.util.Objects;

import org.gradle.internal.impldep.org.apache.commons.lang.BooleanUtils;

import com.adaptris.installer.models.OptionalComponent;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OptionalComponentCell {

  public static final String ARTIFACT_LOGO_BASE = "/img/optionals/";
  public static final String ARTIFACT_LOGO_DEFAULT = "default.png";

  private OptionalComponent optionalComponent;
  private StringProperty id;
  private StringProperty name;
  private StringProperty description;
  private StringProperty tags;
  private SimpleBooleanProperty licensed;
  private SimpleBooleanProperty selected;
  private ImageView icon;

  public OptionalComponentCell(OptionalComponent optionalComponent) {
    this.optionalComponent = optionalComponent;
    id = new SimpleStringProperty(optionalComponent, "id", optionalComponent.getId());
    name = new SimpleStringProperty(optionalComponent, "name", optionalComponent.getName());
    description = new SimpleStringProperty(optionalComponent, "description", optionalComponent.getDescription());
    tags = new SimpleStringProperty(optionalComponent, "tags", optionalComponent.getTags());
    licensed = new SimpleBooleanProperty(BooleanUtils.toBoolean(optionalComponent.getLicense()));
    selected = new SimpleBooleanProperty(false);
    icon = new ImageView(getImage(optionalComponent));
  }

  protected Image getImage(OptionalComponent optionalComponent) {
    InputStream iconStream = getClass().getResourceAsStream(ARTIFACT_LOGO_BASE + optionalComponent.getIcon());
    if (Objects.isNull(iconStream)) {
      iconStream = getClass().getResourceAsStream(ARTIFACT_LOGO_BASE + ARTIFACT_LOGO_DEFAULT);
    }
    return new Image(iconStream, 30, 30, true, true);
  }

  public String getId() {
    return idProperty().get();
  }

  public void setId(String id) {
    idProperty().set(id);
  }

  public StringProperty idProperty() {
    return id;
  }

  public String getName() {
    return nameProperty().get();
  }

  public void setName(String name) {
    nameProperty().set(name);
  }

  public StringProperty nameProperty() {
    return name;
  }

  public String getDescription() {
    return descriptionProperty().get();
  }

  public void setDescription(String description) {
    descriptionProperty().set(description);
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public String getTags() {
    return tagsProperty().get();
  }

  public void setTags(String tags) {
    tagsProperty().set(tags);
  }

  public StringProperty tagsProperty() {
    return tags;
  }

  public Boolean getLicensed() {
    return licensedProperty().get();
  }

  public void setLicensed(Boolean licensed) {
    licensedProperty().set(licensed);
  }

  public SimpleBooleanProperty licensedProperty() {
    return licensed;
  }

  public Boolean getSelected() {
    return selectedProperty().get();
  }

  public void setSelected(Boolean selected) {
    selectedProperty().set(selected);
  }

  public SimpleBooleanProperty selectedProperty() {
    return selected;
  }

  public ImageView getIcon() {
    return icon;
  }

  public OptionalComponent getOptionalComponent() {
    return optionalComponent;
  }

}
