package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

import org.junit.Test;

import com.adaptris.TestUtils;
import com.adaptris.fxinstaller.models.InterlokProject;

public class BuildGradleFileGeneratorTest {

  @Test
  public void testGenerate() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve("gradle.properties")));

    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("build.gradle")));
    assertFalse(Files.readString(buildGradleDirPath.resolve("build.gradle")).isBlank());
    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("gradle.properties")));
    assertEquals(interlokProject.getDirectory().replaceAll("\\\\", "/") + "", gradleProperties.get("interlokDistDirectory"));
    assertEquals(interlokProject.getVersion(), gradleProperties.get("interlokVersion"));
    assertEquals(interlokProject.getAdditionalNexusBaseUrl(), gradleProperties.get("additionalNexusBaseUrl"));
    assertNull(gradleProperties.get("additionalNexusBaseUrl"));
  }

  @Test
  public void testGenerateWithAdditionalNexusBaseUrl() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);
    interlokProject.setAdditionalNexusBaseUrl("https://nexus.adaptris.net");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve("gradle.properties")));

    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("build.gradle")));
    assertFalse(Files.readString(buildGradleDirPath.resolve("build.gradle")).isBlank());
    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("gradle.properties")));
    assertEquals(interlokProject.getDirectory().replaceAll("\\\\", "/") + "", gradleProperties.get("interlokDistDirectory"));
    assertEquals(interlokProject.getVersion(), gradleProperties.get("interlokVersion"));
    assertEquals(interlokProject.getAdditionalNexusBaseUrl(), gradleProperties.get("additionalNexusBaseUrl"));
    assertNotNull(gradleProperties.get("additionalNexusBaseUrl"));
  }

  @Test
  public void testDownloadGradleFiles() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);

    BuildGradleFileGenerator buildGradleFileGenerator = new BuildGradleFileGenerator(resourcesPath);
    // We generate the tmp file first
    Path buildGradleDirPath = buildGradleFileGenerator.generate(interlokProject);
    try {
      assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("build.gradle")));

      buildGradleFileGenerator.downloadGradleFiles(interlokProject, resourcesPath.toFile());

      assertTrue(Files.isRegularFile(resourcesPath.resolve("interlok-gradle-files-3.9.2-RELEASE.zip")));
    } finally {
      Files.walk(buildGradleDirPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      Files.deleteIfExists(buildGradleDirPath);
      Files.deleteIfExists(resourcesPath.resolve("interlok-gradle-files-3.9.2-RELEASE.zip"));
    }
  }

  @Test
  public void testDownloadGradleFilesInstallerTmpDirDoesntExist() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);

    new BuildGradleFileGenerator(resourcesPath).downloadGradleFiles(interlokProject, resourcesPath.toFile());

    assertFalse(Files.isRegularFile(resourcesPath.resolve("interlok-gradle-files-3.9.2-RELEASE.zip")));

    // TODO At the moment we rely on testGenerate to be ran before.
    // We need to fix that and also test if the file don't exist
  }

  private InterlokProject newProject(Path interlokProjectPath) {
    InterlokProject interlokProject = new InterlokProject();
    interlokProject.setOptionalComponents(Collections.singletonList(TestUtils.buildOptionalComponent()));
    interlokProject.setDirectory(interlokProjectPath.toAbsolutePath().toString());
    interlokProject.setVersion("3.9.2-RELEASE");
    return interlokProject;
  }

}