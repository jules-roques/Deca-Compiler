package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl44
 * @date 01/01/2024
 */
public abstract class AbstractInitialization extends Tree {
    
    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;


    /**
     * Gen code for Initialisation or NoInitialisation
     * @param compiler
     */
    protected abstract void codeGenInitialization(DecacCompiler compiler, ExpDefinition varDefinition);
}
