/**
 * Z PL/SQL Analyzer
 * Copyright (C) 2015-2021 Felipe Zorzo
 * mailto:felipe AT felipezorzo DOT com DOT br
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
package org.sonar.plsqlopen.checks

import com.felipebz.flr.api.AstNode
import org.sonar.plsqlopen.sslr.IfStatement
import org.sonar.plsqlopen.tryGetAsTree
import org.sonar.plugins.plsqlopen.api.PlSqlGrammar
import org.sonar.plugins.plsqlopen.api.annotations.*

@Rule(priority = Priority.MINOR)
@ConstantRemediation("5min")
@RuleInfo(scope = RuleInfo.Scope.ALL)
@ActivatedByDefault
class IfWithExitCheck : AbstractBaseCheck() {

    override fun init() {
        subscribeTo(PlSqlGrammar.EXIT_STATEMENT)
    }

    override fun visitNode(node: AstNode) {
        val statement = node.parent
        val ifStatement = statement.parent.parent.tryGetAsTree<IfStatement>()
        if (ifStatement != null &&
                ifStatement.elsifClauses.isEmpty() && ifStatement.elseClause == null &&
                ifStatement.statements.size == 1) {
            addIssue(ifStatement, getLocalizedMessage())
        }
    }

}
