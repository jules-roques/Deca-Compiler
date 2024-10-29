package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Equals extends AbstractOpExactCmp {
    
    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }    
    
    @Override
    public void codeGenOpCmp(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new SEQ(register));
    }

    @Override
    public void codeGenBraInst(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BEQ(label));
    }

    @Override
    public void codeGenBraOppositeInst(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BNE(label));
    }

}
