package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 * Operand that contains a value.
 * 
 * @author Ensimag
 * @date 01/01/2024
 */
public abstract class DVal extends Operand {
    public abstract GPRegister loadIfNotAlreadyInRegister(DecacCompiler compiler);

    public abstract void freeIfTopGPRegister(DecacCompiler compiler);
}
