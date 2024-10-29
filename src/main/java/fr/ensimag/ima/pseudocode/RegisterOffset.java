package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Operand representing a register indirection with offset, e.g. 42(R3).
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class RegisterOffset extends DAddr {
    public int getOffset() {
        return offset;
    }
    public Register getRegister() {
        return register;
    }
    private final int offset;
    private final Register register;
    public RegisterOffset(int offset, Register register) {
        super();
        this.offset = offset;
        this.register = register;
    }

    public GPRegister loadIfNotAlreadyInRegister(DecacCompiler compiler) {
        GPRegister destinationRegister = compiler.getMemory().getAndAllocAvailableRegister(compiler);
        compiler.addInstruction(new LOAD(this, destinationRegister));
        return destinationRegister;
    }

    @Override
    public String toString() {
        return offset + "(" + register + ")";
    }

    @Override
    public void freeIfTopGPRegister(DecacCompiler compiler) {
        // Nothing
    }
    
}
