/*
 * Sonar PL/SQL Plugin (Community)
 * Copyright (C) 2015-2018 Felipe Zorzo
 * mailto:felipebzorzo AT gmail DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plsqlopen.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.io.Files;
import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.Build;
import com.sonar.orchestrator.build.SonarScanner;
import com.sonar.orchestrator.locator.FileLocation;

public class PlSqlRulingTest {

    @ClassRule
    public static Orchestrator orchestrator = Orchestrator.builderEnv()
      .addPlugin(FileLocation.byWildcardMavenFilename(
              new File("../../sonar-plsql-open-plugin/target"),
              "sonar-plsql-open-plugin-*.jar"))
      .setOrchestratorProperty("litsVersion", "0.6")
      .addPlugin("lits")
      .restoreProfileAtStartup(FileLocation.of("src/test/resources/profile.xml"))
      .build();

    public SonarScanner initializeBuild(String projectId) throws Exception {
        return initializeBuild(projectId, null);
    }

    public SonarScanner initializeFormsBuild(String projectId, String sources) {
        SonarScanner build = initializeBuild(projectId, sources);
        build.setProperty("sonar.plsql.forms.metadata", "metadata.json");
        build.setProperty("sonar.plsql.file.suffixes", "pcd,fun,pks,pkb,tgg");
        return build;
    }

    public SonarScanner initializeBuild(String projectId, String sources) {
        assertTrue(
            "SonarQube 5.6 is the minimum version to generate the issues report",
            orchestrator.getConfiguration().getSonarVersion().isGreaterThanOrEquals("5.6"));
        orchestrator.getServer().provisionProject(projectId, projectId);
        orchestrator.getServer().associateProjectToQualityProfile(projectId, "plsqlopen", "rules");

        String originalSource = sources;
        if (originalSource == null) {
            originalSource = projectId;
        }

        return SonarScanner.create(FileLocation.of("../sources/" + originalSource).getFile())
            .setProjectKey(projectId)
            .setProjectVersion("1")
            .setLanguage("plsqlopen")
            .setSourceEncoding("UTF-8")
            .setSourceDirs(".")
            .setProperty("dump.old", FileLocation.of("src/test/resources/expected/" + projectId).getFile().getAbsolutePath())
            .setProperty("dump.new", FileLocation.of("target/actual/" + projectId).getFile().getAbsolutePath())
            .setProperty("sonar.plsql.file.suffixes", "sql,typ,pkg,pkb,pks,tab")
            .setProperty("sonar.plsql.errorRecoveryEnabled", "false")
            .setProperty("sonar.cpd.skip", "true")
            .setProperty("sonar.analysis.mode", "preview")
            .setProperty("sonar.issuesReport.html.enable", "true")
            .setEnvironmentVariable("SONAR_RUNNER_OPTS", "-Xmx1024m");
    }

    public void executeBuild(Build<?> build) throws Exception {
        String projectId = build.getProperty("sonar.projectKey");
        File litsDifferencesFile = FileLocation.of("target/differences_" + projectId).getFile();
        build.setProperty("lits.differences", litsDifferencesFile.getAbsolutePath());

        orchestrator.executeBuild(build);
        assertThat(Files.toString(litsDifferencesFile, StandardCharsets.UTF_8)).isEmpty();
    }

    @Test
    public void alexandria_plsql_utils() throws Exception {
        SonarScanner build = initializeBuild("alexandria-plsql-utils");
        executeBuild(build);
    }

    @Test
    public void pljson() throws Exception {
        SonarScanner build = initializeBuild("pljson");
        executeBuild(build);
    }

    @Test
    public void utPLSQL2() throws Exception {
        SonarScanner build = initializeBuild("utPLSQL2");
        executeBuild(build);
    }

    @Test
    public void utPLSQL3() throws Exception {
        SonarScanner build = initializeBuild("utPLSQL3");
        build.setSourceDirs("./source");
        build.setProperty("sonar.coverageReportPaths", null);
        build.setProperty("sonar.testExecutionReportPaths", null);
        executeBuild(build);
    }

    @Test
    public void demo0001() throws Exception {
        SonarScanner build = initializeFormsBuild("demo0001", "Doag-Forms-extracted/demo0001/WEBUTIL_DEMO");
        executeBuild(build);
    }
    
}
