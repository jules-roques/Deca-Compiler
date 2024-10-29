package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Immediate operand representing an integer.
 * 
 * @author Ensimag
 * @date 01/01/2024
 */
public class ImmediateInteger extends DVal {
    private int value;

    public ImmediateInteger(int value) {
        super();
        this.value = value;
    }

    public GPRegister loadIfNotAlreadyInRegister(DecacCompiler compiler) {
        GPRegister destinationRegister = compiler.getMemory().getAndAllocAvailableRegister(compiler);
        compiler.addInstruction(new LOAD(this, destinationRegister));
        return destinationRegister;
    }

    @Override
    public void freeIfTopGPRegister(DecacCompiler compiler) {
        // Nothing
    }

    @Override
    public String toString() {
        return "#" + value;
    }
}
