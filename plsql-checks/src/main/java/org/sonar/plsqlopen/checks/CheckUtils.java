/*
 * Sonar PL/SQL Plugin (Community)
 * Copyright (C) 2015 Felipe Zorzo
 * felipe.b.zorzo@gmail.com
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plsqlopen.checks;

import java.util.List;

import javax.annotation.Nullable;

import org.sonar.plugins.plsqlopen.api.PlSqlGrammar;
import org.sonar.plugins.plsqlopen.api.PlSqlKeyword;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;

public class CheckUtils {

    private static final AstNodeType[] TERMINATION_STATEMENTS = {
            PlSqlGrammar.RETURN_STATEMENT,
            PlSqlGrammar.EXIT_STATEMENT,
            PlSqlGrammar.CONTINUE_STATEMENT,
            PlSqlGrammar.RAISE_STATEMENT};
    
    private static final AstNodeType[] PROGRAM_UNITS = { 
            PlSqlGrammar.ANONYMOUS_BLOCK,
            PlSqlGrammar.CREATE_PROCEDURE,
            PlSqlGrammar.PROCEDURE_DECLARATION,
            PlSqlGrammar.CREATE_FUNCTION,
            PlSqlGrammar.FUNCTION_DECLARATION,
            PlSqlGrammar.CREATE_PACKAGE_BODY};
    
    private CheckUtils() {
    }
    
    public static AstNodeType[] getTerminationStatements() {
        return TERMINATION_STATEMENTS.clone();
    }
    
    public static boolean isNullLiteralOrEmptyString(AstNode node) {
        AstNode literal = node.getFirstChild(PlSqlGrammar.LITERAL);

        if (literal != null) {
            if (literal.hasDirectChildren(PlSqlGrammar.NULL_LITERAL)) {
                return true;
            }

            if (isEmptyString(literal)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmptyString(AstNode node) {
        AstNode characterLiteral = node.getFirstChild(PlSqlGrammar.CHARACTER_LITERAL);
        if (characterLiteral != null && "''".equals(characterLiteral.getTokenValue())) {
            return true;
        }

        return false;
    }

    public static boolean equalNodes(AstNode node1, AstNode node2) {
        if (!node1.getType().equals(node2.getType()) || node1.getNumberOfChildren() != node2.getNumberOfChildren()) {
            return false;
        }

        if (node1.getNumberOfChildren() == 0) {
            return node1.getToken().getValue().equals(node2.getToken().getValue());
        }

        List<AstNode> children1 = node1.getChildren();
        List<AstNode> children2 = node2.getChildren();
        for (int i = 0; i < children1.size(); i++) {
            if (!equalNodes(children1.get(i), children2.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isTerminationStatement(AstNode node) {
        return node.is(TERMINATION_STATEMENTS) && !node.hasDirectChildren(PlSqlKeyword.WHEN);
    }
    
    public static boolean isProgramUnit(@Nullable AstNode node) {
        return node != null && node.is(PROGRAM_UNITS);
    }
}
