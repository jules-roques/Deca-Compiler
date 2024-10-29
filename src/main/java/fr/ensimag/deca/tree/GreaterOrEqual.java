package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 * 
 * @author gl44
 * @date 01/01/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {
    
    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    public void codeGenOpCmp(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new SGE(register));
    }

    @Override
    public void codeGenBraInst(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BGE(label));
    }

    @Override
    public void codeGenBraOppositeInst(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BLT(label));
    }


}
