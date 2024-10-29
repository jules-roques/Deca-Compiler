package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodBody extends AbstractMethodBody {
    private ListDeclVar declVars;
    private ListInst insts;

    public MethodBody(ListDeclVar declVars, ListInst insts) {
        Validate.notNull(declVars);
        Validate.notNull(insts);
        this.declVars = declVars;
        this.insts = insts;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnedType)
            throws ContextualError {
        this.declVars.verifyListDeclVariable(compiler, localEnv, currentClass);
        this.insts.verifyListInst(compiler, localEnv, currentClass, returnedType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("{");
        declVars.decompile(s);
        insts.decompile(s);
        s.print("}");
    }

    

    @Override
    protected void iterChildren(TreeFunction f) {
        declVars.iter(f);
        insts.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVars.prettyPrint(s, prefix, true);
        insts.prettyPrint(s, prefix, true);
    }
}