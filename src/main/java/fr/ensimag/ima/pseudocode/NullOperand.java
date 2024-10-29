package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;

/**
 * The #null operand.
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class NullOperand extends DVal {

    public GPRegister loadIfNotAlreadyInRegister(DecacCompiler compiler) {
        throw new DecacInternalError("On ne peut pas stocker 'null' dans un registre");
    }

    @Override
    public String toString() {
        return "#null";
    }

    @Override
    public void freeIfTopGPRegister(DecacCompiler compiler) {
        // Nothing
    }

}
