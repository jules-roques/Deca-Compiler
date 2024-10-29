package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl44
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        // Passe 1
        this.classes.verifyListClass(compiler);
        // Passe 2
        this.classes.verifyListClassMembers(compiler);
        // Passe 3
        this.classes.verifyListClassBody(compiler);
        this.main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // compiler.addComment("Declaration de classes");
        // classes.codeGenMain(compiler);
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());

        // Debut du programme principal (avec add first)
        Label pilePleine = new Label("pile_pleine");
        compiler.addInstructionAtFirst(new ADDSP(compiler.getMemory().getGBOffet()));
        compiler.addInstructionAtFirst(new BOV(pilePleine));
        compiler.addInstructionAtFirst(new TSTO(compiler.getMemory().getMaxPushedValues() + compiler.getMemory().getGBOffet()));
        compiler.addCommentAtFirst("Debut du programme principal");

        // Erreurs
        compiler.addComment("Messages d'erreurs");
        compiler.addLabel(pilePleine);
        compiler.addInstruction(new WSTR("Erreur: pile pleine"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
