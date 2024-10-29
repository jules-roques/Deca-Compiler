package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclParam extends AbstractDeclParam {
    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier varName) {
        Validate.notNull(type);
        Validate.notNull(varName);
        this.type = type;
        this.varName = varName;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
    }

    @Override
    protected void verifyDeclParam(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Signature sign) throws ContextualError {
        Type t = type.verifyType(compiler, false);
        sign.add(t);

        ExpDefinition paramDef = localEnv.get(varName.getName());
        if (paramDef != null) {
            throw new ContextualError("nom de paramètre déjà déclaré (règle jsp)", getLocation());
        }
        paramDef = new ParamDefinition(t, getLocation());

        varName.setDefinition(paramDef);
        varName.setType(t);

        try {
            // ajout de la method dans l'EnvironmentExp   
            localEnv.declare(varName.getName(), paramDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable " + varName.getName() + " is already defined in the current scope", varName.getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
    }

}