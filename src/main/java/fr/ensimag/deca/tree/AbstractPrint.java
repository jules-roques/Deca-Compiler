package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Print statement (print, println, ...).
 *
 * @author gl44
 * @date 01/01/2024
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public boolean getPrintHex() {
        return printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass, Type returnType)
        throws ContextualError {
    if (getArguments().getList() == null) {
        throw new ContextualError("identificateur non déclaré (règle 0.1)", getLocation());
    }
    for (AbstractExpr expr : getArguments().getList()) {
        Type exprType = expr.verifyExpr(compiler, localEnv, currentClass);

        // Vérification que le type est bien imprimable
        if (!(exprType.isFloat() || exprType.isInt() || exprType.isString())) {
            throw new ContextualError("le type doit être : “int, float, string” (règle 3.31)", getLocation());
        }
    }
}


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.setPrintHex(this.getPrintHex());
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print");
        s.print(getSuffix());
        if (getPrintHex()){
            s.print("x");
        }   
        s.print("(");
        arguments.decompile(s); 
        s.print(");");     
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
