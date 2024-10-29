package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    public void codeGenBra(DecacCompiler compiler, boolean expectedValue, Label label) {
        BooleanGestionInterface leftOperand = (BooleanGestionInterface) this.getLeftOperand();
        BooleanGestionInterface rightOperand = (BooleanGestionInterface) this.getRightOperand();

        if (expectedValue) {
            Label labelFin = compiler.getLabels().getNewEndLabel();
            leftOperand.codeGenBra(compiler, false, labelFin);
            rightOperand.codeGenBra(compiler, true, label);
            compiler.addLabel(labelFin);
        } else {
            leftOperand.codeGenBra(compiler, false, label);
            rightOperand.codeGenBra(compiler, false, label);
        }
    }
}
