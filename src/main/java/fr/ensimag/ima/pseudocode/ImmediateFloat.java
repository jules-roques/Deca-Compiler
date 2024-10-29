package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Immediate operand containing a float value.
 * 
 * @author Ensimag
 * @date 01/01/2024
 */
public class ImmediateFloat extends DVal {
    private float value;

    public ImmediateFloat(float value) {
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
        return "#" + Float.toHexString(value);
    }
}
