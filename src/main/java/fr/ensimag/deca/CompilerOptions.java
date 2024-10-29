package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl44
 * @date 01/01/2024
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;


    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParse() {
        return parse;
    }

    public boolean getVerification() {
        return verification;
    }

    public boolean getNoCheck() {
        return noCheck;
    }

    public int getMaxRegister() {
        return maxRegister;
    }

    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getShow() {
        return show;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verification = false;
    private boolean noCheck = false;
    private int maxRegister = 15;  // By default
    private boolean registerSelection = false;
    private int debug = 0;
    private boolean parallel = false;
    private boolean show = false;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {
        
        // Parcours des différents arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {

                // Affichage de bannière
                case "-b":
                    if (printBanner || parse || verification || noCheck || registerSelection || debug > 0 || parallel || !sourceFiles.isEmpty()) {
                        throw new CLIException("L'option '-b' doit être utilisée seule");
                    }
                    printBanner = true;
                    break;
                
                // parse
                case "-p":
                    if (parse) {
                        throw new CLIException("L'option '-p' est renseignée plusieurs fois");
                    }
                    if (verification) {
                        throw new CLIException("Les options '-p' et '-v' sont incompatibles");
                    }
                    if (printBanner) {
                        throw new CLIException("L'option '-p' n'est pas compatible avec l'option '-b'");
                    }
                    parse = true;
                    break;

                // Verify
                case "-v":
                    if (verification) {
                        throw new CLIException("L'option '-v' est renseignée plusieurs fois");
                    }
                    if (parse) {
                        throw new CLIException("Les options '-p' et '-v' sont incompatibles");
                    }
                    if (printBanner) {
                        throw new CLIException("L'option '-v' n'est pas compatible avec l'option '-b'");
                    }
                    verification = true;
                    break;

                // noCheck
                case "-n":
                    if (noCheck) {
                        throw new CLIException("L'option '-n' est renseignée plusieurs fois");
                    }
                    if (printBanner) {
                        throw new CLIException("L'option '-n' n'est pas compatible avec l'option '-b'");
                    }
                    break;

                // Registers limitation
                case "-r":
                    if (registerSelection) {
                        throw new CLIException("L'option '-r' est renseignée plusieurs fois");
                    }
                    if (i + 1 < args.length) {
                        try {
                            maxRegister = Integer.parseInt(args[++i]) - 1;
                        } catch (NumberFormatException e) {
                            throw new CLIException("'-r' nécessite un argument entier");
                        }
                    } else {
                        throw new CLIException("'-r' nécessite un argument entier");
                    }
                    
                    if (maxRegister < 3 || maxRegister > 15) {
                            throw new CLIException("Le nombre de registres suivant '-r' doit être compris entre 4 et 16 inclus");
                        }
                    
                    registerSelection = true;
                    break;

                // Active les traces de debug (plusieurs fois pour plus de traces)
                case "-d":
                    if (printBanner) {
                        throw new CLIException("L'option '-d' n'est pas compatible avec l'option '-b'");
                    }
                    debug += 1;
                    break;

                // compilation des fichiers en parallèle
                case "-P":
                    if (parallel) {
                        throw new CLIException("L'option '-P' est renseignée plusieurs fois");
                    }
                    if (printBanner) {
                        throw new CLIException("L'option '-P' n'est pas compatible avec l'option '-b'");
                    }
                    parallel = true;
                    break;

                // Show decac compilation in stdout
                case "-s":
                    if (show) {
                        throw new CLIException("L'option '-s' est renseignée plusieurs fois");
                    }
                    if (printBanner) {
                        throw new CLIException("L'option '-s' n'est pas compatible avec l'option '-b'");
                    }
                    show = true;
                    break;
                    
                // Fichier source si l'argument ne commence pas par '-'
                default:
                    if (printBanner) {
                        throw new CLIException("L'option '-b' doit être utilisée seule");
                    }
                    if (arg.charAt(0) == '-') {
                        throw new CLIException("Option '" + arg + "' inconnue");
                    }
                    if (!arg.endsWith(".deca")) {
                        throw new CLIException("'" + arg + "' n'est pas un fichier deca");
                    }
                    sourceFiles.add(new File(arg));
                    break;
            }
        }
        setDebugLevel();
    }

    private void setDebugLevel() {
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        System.out.println("SYNOPSIS");
        System.out.println("      decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-s] <fichier deca>...] | [-b]");
        System.out.println();
        System.out.println("OPTIONS");
        System.out.println("   -b         (banner)           : affiche une bannière indiquant le nom de l'équipe");
        System.out.println("   -p         (parse)            : arrête decac après l'étape de construction de");
        System.out.println("                                   l'arbre, et affiche la décompilation de ce dernier");
        System.out.println("                                   (i.e. s'il n'y a qu'un fichier source à");
        System.out.println("                                   compiler, la sortie doit être un programme");
        System.out.println("                                   deca syntaxiquement correct)");
        System.out.println("   -v         (verification)     : arrête decac après l'étape de vérifications");
        System.out.println("                                   (ne produit aucune sortie en l'absence d'erreur)");
        System.out.println("   -n         (no check)         : supprime les tests à l'exécution spécifiés dans");
        System.out.println("                                   les points 11.1 et 11.3 de la sémantique de Deca.");
        System.out.println("   -r X       (registers)        : limite les registres banalisés disponibles à");
        System.out.println("                                   R0 ... R{X-1}, avec 4 <= X <= 16");
        System.out.println("   -d         (debug)            : active les traces de debug. Répéter");
        System.out.println("                                   l'option plusieurs fois pour avoir plus de");
        System.out.println("                                   traces.");
        System.out.println("   -P         (parallel)         : s'il y a plusieurs fichiers sources,");
        System.out.println("                                   lance la compilation des fichiers en");
        System.out.println("                                   parallèle (pour accélérer la compilation)");
        System.out.println("   -s         (show)             : affiche le fichier assembleur généré");
        System.out.println("                                   sur la sortie standard.");
    }
}
