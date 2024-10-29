package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;



/**
 * @author gl44
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {

    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    public BinaryInstructionDValToReg getInstruction(DVal op1, GPRegister op2) {
        return new ADD(op1, op2);
    }
}
