package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    public void codeGenBra(DecacCompiler compiler, boolean expectedValue, Label label) {
        AbstractExpr leftOperand = this.getLeftOperand();
        AbstractExpr rightOperand = this.getRightOperand();

        BooleanGestionInterface equivalentOperand = new Not(new And(new Not(leftOperand), new Not(rightOperand)));
        equivalentOperand.codeGenBra(compiler, expectedValue, label);
    }
}
