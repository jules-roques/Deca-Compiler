package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

/**
 * @author gl44
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Vérification du type
        Type t = type.verifyType(compiler, false);
        
        initialization.verifyInitialization(compiler, t , localEnv, currentClass);

        ExpDefinition varDef = localEnv.get(varName.getName());
        if (varDef != null) {
            throw new ContextualError("nom d'attribut déjà déclaré (règle 2.4)", getLocation());
        }
        varDef = new VariableDefinition(t, getLocation());
        
        // définition de la variable
        varName.setDefinition(varDef);
        varName.setType(t);
        
        try {
            // ajout de la variable dans l'EnvironmentExp   
            localEnv.declare(varName.getName(), varDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable " + varName.getName() + " is already defined in the current scope", varName.getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
        
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    protected void codeGenDeclvar(DecacCompiler compiler) {
        ExpDefinition varDef = varName.getExpDefinition();
        int globalBaseOffset = compiler.getMemory().getAndIncreaseGBOffset();

        varDef.setOperand(new RegisterOffset(globalBaseOffset, Register.GB));

        initialization.codeGenInitialization(compiler, varDef);
    }
}
