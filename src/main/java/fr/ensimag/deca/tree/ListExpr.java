package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl44
 * @date 01/01/2024
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        boolean premiereExpression = true;
        for (AbstractExpr p : getList()) {
            if (premiereExpression){
                premiereExpression = false;
            }
            else{
                s.print(", ");
            }
            p.decompile(s);
        }
    }

}
