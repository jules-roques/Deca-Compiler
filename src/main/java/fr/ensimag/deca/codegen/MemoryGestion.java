package fr.ensimag.deca.codegen;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;


/**
 * Utile à la gestion de la mémoire (registres, pile...)
 * 
 * @author Jules
 * @date 17/01/2024
 */
public class MemoryGestion {

    public MemoryGestion(int numberMaxRegister) {
        this.globalMemoryOffset = globalMemoryOffsetMinimum;
        this.numberMaxRegister = numberMaxRegister;
    }

    // Pile
    private int globalMemoryOffset;
    final private int globalMemoryOffsetMinimum = 1;
    private int pushedValues = 0;
    private int maxPushedValues = 0;

    public void updateMaxPushedValues() {
        if (maxPushedValues < pushedValues) {
            maxPushedValues = pushedValues;
        }
    }

    public int getMaxPushedValues() {
        return maxPushedValues;
    }

    public int getAndIncreaseGBOffset() {
        return globalMemoryOffset++;
    }

    public int getGBOffet() {
        return globalMemoryOffset;
    }

    public void offsetDecrease() {
        Validate.isTrue(globalMemoryOffset > globalMemoryOffsetMinimum, "Global base offset is already at minimum.");
        globalMemoryOffset--;
    }

    public boolean isAValuePushed() {
        return pushedValues > 0;
    }

    // Registres
    final private int numberMinRegister = 2;
    final private int numberMaxRegister;
    private int numberAvailableRegister = numberMinRegister;

    public int getAvailableGPRegisterNumber() {
        return numberAvailableRegister;
    }

    /**
     * Takes the register above the top of the used ones.
     * (Like a stack)
     */
    public GPRegister getAndAllocAvailableRegister(DecacCompiler compiler) {

        // Cas dans lequel on a asser de registres
        if (numberAvailableRegister <= numberMaxRegister) {
            return GPRegister.getR(numberAvailableRegister++);
        }

        // Pas asser de registres
        compiler.addInstruction(new PUSH(GPRegister.getR(numberMaxRegister)), "sauvegarde");
        pushedValues++;
        updateMaxPushedValues();
        
        return GPRegister.getR(numberMaxRegister);
    }

    /**
     * libère le registre du haut
     */
    public void freeTopGPRegister() {
        Validate.isTrue(numberAvailableRegister > numberMinRegister, "All registers are already free.");
        numberAvailableRegister--;
    }

    /**
     * libère plusieurs registres du haut
     * @param nbr
     */
    public void freeTopGPRegister(int nbr) {
        Validate.isTrue(nbr >= 0, "nbr must be positive or null");
        for (int i=0; i < nbr; i++) {
            freeTopGPRegister();
        }
    }

    /**
     * utilise POP
     * @param compiler
     */
    public DVal restoreIfNecessary(DecacCompiler compiler, DVal op) {
        // Si op n'est pas un GPRegister
        if (!(op instanceof GPRegister)) {
            return op;
        }

        // Si aucune valeur n'est dans la pile
        if (pushedValues == 0) {
            return op;
        }

        // Si op est un GPRegister et qu'une valeur est dans la pile
        // Alors il faut la restaurer
        compiler.addInstruction(new LOAD(op, GPRegister.R0));
        compiler.addInstruction(new POP(GPRegister.getR(numberMaxRegister)), "restauration");
        pushedValues--;
        return GPRegister.R0;
    }

    /**
     * libère tous les registres et la pile
     */
    public void freeAllRegisters() {
        numberAvailableRegister = numberMinRegister;
    }


}