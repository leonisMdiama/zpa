# Z PL/SQL Analyzer - ZPA

[![Latest release](https://img.shields.io/github/release/felipebz/zpa.svg) ](https://github.com/felipebz/zpa/releases/latest)
[![Build Status](https://dev.azure.com/felipebz/z-plsql-analyzer/_apis/build/status/Build?branchName=main)](https://dev.azure.com/felipebz/z-plsql-analyzer/_build/latest?definitionId=3&branchName=main)
[![Azure DevOps coverage](https://img.shields.io/azure-devops/coverage/felipebz/z-plsql-analyzer/3/main.svg)](https://dev.azure.com/felipebz/z-plsql-analyzer/_build/latest?definitionId=3&branchName=main)
[![Azure DevOps tests](https://img.shields.io/azure-devops/tests/felipebz/z-plsql-analyzer/3/main.svg)](https://dev.azure.com/felipebz/z-plsql-analyzer/_build/latest?definitionId=3&branchName=main)
[![Quality Gate Status](https://sonarqube.felipezorzo.com.br/api/project_badges/measure?project=org.sonar.plsqlopen%3Aplsql&metric=alert_status)](https://sonarqube.felipezorzo.com.br/dashboard?id=org.sonar.plsqlopen%3Aplsql)

The Z PL/SQL Analyzer (or simply ZPA) is a code analyzer for PL/SQL and Oracle SQL code.

Currently you can use it in a [SonarQube](https://www.sonarqube.org) on-premise instance. SonarQube is an open platform to manage code quality. This project supports SonarQube 6.7.x and newer.

See some examples in our [SonarQube instance](https://sonarqube.felipezorzo.com.br/projects?languages=plsqlopen)!

Do you want to use this analyzer in a project hosted on [SonarCloud](https://sonarcloud.io)? Try the [zpa-cli](https://github.com/felipebz/zpa-cli)!

## Installation

- Download the [latest sonar-plsql-open-plugin release](https://github.com/felipebz/zpa/releases/latest) and copy to the SONARQUBE_HOME/extensions/plugins directory;
- Restart your SonarQube server;
- Navigate to the Marketplace (SONARQUBE_URL/marketplace?filter=installed). It should list "Z PL/SQL Analyzer" on the tab "Installed Plugins";
- Run an analysis with [SonarQube Scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner).

If you like to live on the bleeding edge, you can use the [latest development version](https://felipezorzo.com.br/api/download/org.sonar.plsqlopen:sonar-plsql-open-plugin:latest).

## Compatibility matrix

| ZPA version     | SonarQube version |
|-----------------|-------------------|
| 2.4.0 (current) | 6.7 LTS - 8.9 LTS |
| 3.0.0 (next)    | 7.6 - 8.9 LTS     |

## Contribute

Everyone is welcome to contribute. Note that no matter how you contribute, your participation is governed by our [code of conduct](CODE_OF_CONDUCT.md).

There are a few things you need to know about the code. It is divided in these modules:

- `its` - Integration tests with SonarQube (more below).
- `plsql-checks-testkit` - Test helper for coding rules, it can be used to test custom rules.
- `plsql-checks` - The built-in coding rules provided by ZPA.
- `plsql-custom-rules` - Demo project showing how to extend ZPA with custom coding rules.
- `sonar-plsql-open-plugin` - The SonarQube plugin itself, this module contains all the code necessary to integrate with the SonarQube platform.
- `zpa-core` - The heart of this project. It contains the lexer, the parser and the code required to understand and process PL/SQL code.
- `zpa-toolkit` - A visual tool to review the AST (abstract syntax tree) generated by the parser.

The API exposed to custom plugins must be located in the package `org.sonar.plugins.plsqlopen.api` (it's a requirement from the SonarQube server). The classes located outside this package are not prepared for external consumption, so if you use them, your code can break without any further notice.

If you're interested in a stable API to integrate ZPA with another software, please [open an issue](https://github.com/felipebz/zpa/issues/new) or [contact us directly](https://felipezorzo.com.br/contact) explaining your needs.

### Running the integration tests

There are two sets of integration tests:

- [plugin](https://github.com/felipebz/zpa/tree/main/its/plugin): checks if the metrics are imported correctly in SonarQube
- [ruling](https://github.com/felipebz/zpa/tree/main/its/ruling): checks the quality of parser and rules against real-world code

To run the integrations tests, update the submodules:

    git submodule update --init --recursive
    
Build the main plugin and the custom rules example:

    ./mvnw clean install
    ./mvnw -f plsql-custom-rules/pom.xml package
    
Download the [Oracle HTML documentation](https://docs.oracle.com/en/database/oracle/oracle-database/19/zip/oracle-database_19.zip) to any folder.
    
Then run the tests:

    ./mvnw test -Pit -DoracleDocs="path/to/oracle-database_19.zip"

By default the tests will be executed using SonarQube 6.7.x LTS. You can change the SonarQube version using the property `sonar.runtimeVersion`, passing the specific version or one of `LATEST_RELEASE[6.7]` (for SonarQube 6.7.x LTS), `LATEST_RELEASE` (latest official release) or `DEV` (unstable version, in development): 

    ./mvnw test -Pit -DoracleDocs="path/to/oracle-database_19.zip" -Dsonar.runtimeVersion=7.7
