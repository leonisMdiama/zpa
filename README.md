Continuous Integration | Status
--- | ---
**Travis** (Linux build) | [![Build Status](https://travis-ci.org/felipebz/sonar-plsql.svg?branch=master)](https://travis-ci.org/felipebz/sonar-plsql)
**AppVeyor** (Windows build and [deployment](https://ci.appveyor.com/project/felipebz/sonar-plsql/build/artifacts)) | [![Build status](https://ci.appveyor.com/api/projects/status/6tpemq3g7d0drub8/branch/master?svg=true)](https://ci.appveyor.com/project/felipebz/sonar-plsql/branch/master)

## SonarQube PL/SQL Community plugin
This plugin adds PL/SQL support to the [SonarQube](https://www.sonarqube.org). SonarQube is an open platform to manage code quality. This project supports SonarQube 4.5.5 and newer.

## Installation
- Download the latest [sonar-plsql-open-plugin-1.0-SNAPSHOT.jar from AppVeyor](https://ci.appveyor.com/project/felipebz/sonar-plsql/build/artifacts) packages into the SONARQUBE_HOME/extensions/plugins directory;
- Restart your SonarQube server;
- Navigate to the Update Center (<SonarQube URL>/updatecenter). The Update Center should list "PL/SQL (Community)" on the tab "Installed Plugins";
- Run an analysis with [SonarQube Scanner](http://docs.sonarqube.org/display/SONAR/Analyzing+with+SonarQube+Scanner).

## Contribute
You are welcome to contribute. PL/SQL and Oracle SQL are big languages and there are many pieces missing in the [grammar](https://github.com/felipebz/sonar-plsql/blob/master/plsql-frontend/src/main/java/org/sonar/plugins/plsqlopen/api/PlSqlGrammar.java).

## Alternatives:
If you're looking for PL/SQL support in SonarQube, there is also 
the [commercial plugin from SonarSource](http://www.sonarsource.com/products/plugins/languages/plsql/).
