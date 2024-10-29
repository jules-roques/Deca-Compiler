package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    public void codeGenOpCmp(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new SGT(register));
    }

    @Override
    public void codeGenBraInst(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BGT(label));
    }

    @Override
    public void codeGenBraOppositeInst(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BLE(label));
    }

}
