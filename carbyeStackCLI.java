import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

// class definition of the CarbyeStack Command Line Interface
@Command(name = "CarbyeStackCLI", version = "Carbye Stack CLI 1.0", mixinStandardHelpOptions = true)
public class carbyeStackCLI implements Callable<Integer> {
    int nbChars = 0;
    int nbSpaces = 0;
    int nbWords = 0;
    int nbLines = 0;
    String textInput = "";
    boolean fileInputOption;
    boolean exportResultOption;

    // defintion of all possible options of the CLI
    @Option(names = { "-f", "--fileInput" }, paramLabel = "<Input-file>", description = "Input via a TEXT file.") 
    File inputFile;

    @Option(names = "--nbLines", description = "Output number of lines instead of number of human-readable characters.")
    boolean nbLinesOption;

    @Option(names = "--nbWords", description = "Output number of words instead of number of human-readable charcaters.")
    boolean nbWordsOption;

    @Option(names = "--exportResult", paramLabel = "<Output-path>", description = "Write result into a given file.")
    String exportPath;

    @Option(names = {"-t", "--typeInput"}, paramLabel = "<text>", description = "Type input directly into the console.")
    boolean directInputOption;

    // call method modification 
    @Override
    public Integer call() throws Exception {

        if(inputFile != null) fileInputOption = true;
        else fileInputOption = false;

        if(exportPath != null) exportResultOption = true;
        else exportResultOption = false;

        // read from words, stdin or from file
        if (directInputOption) {
            System.out.println("In the following, please insert your text input whose length ought to be calculated: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
            textInput = reader.readLine();  
            nbChars = textInput.length();
            for (char c : textInput.toCharArray()) {
                if (c == ' ' | c == '\t') {
                     nbSpaces++;
                }
            }
            nbChars -= nbSpaces;
            String[] Words = textInput.split(" ");
            nbWords = Words.length;
            String[] Lines = textInput.split("\n");
            nbLines = Lines.length;
            return printResult(fileInputOption, directInputOption, exportResultOption, nbWords, nbSpaces, nbLines, nbChars, textInput, inputFile);
        } else if (fileInputOption) {
            try { 
                // for doc, docx and other office open xml files
                if (FilenameUtils.getExtension(inputFile.getName()).equals("docx") 
                        | FilenameUtils.getExtension(inputFile.getName()).equals("doc")) {
                    String data = getPlainTextFromFile(inputFile);
                    String[] lines = data.split("\n");
                    for (String line : lines){
                        textInput += line;
                        //nbChars += line.length();
                        String[] Words = line.split(" ");
                        nbWords += Words.length;
                        nbLines++;
                        for (String word : Words){
                            nbChars += word.length();
                        }
                    } 
                    for (char c : textInput.toCharArray()) {
                        if (c == ' ' | c == '\n' | c == '\t') {
                             nbSpaces++;
                        }
                    }
                } else if (FilenameUtils.getExtension(inputFile.getName()).equals("pdf")) { // for pdf files
                    System.out.println("pdf detected.");
                    PDDocument document = PDDocument.load(inputFile);
                    String data;
                    if (!document.isEncrypted()) {
                        PDFTextStripper stripper = new PDFTextStripper();
                        data = stripper.getText(document);
                    } else {
                        System.out.println("Error! Cannot read encrypted document.");
                        return -1;
                    }
                    document.close();
                    String[] lines = data.split("\n");
                    for (String line : lines){
                        textInput += line;
                        String[] Words = line.split(" ");
                        int errCount = 0;
                        for (String word : Words){
                            if (!word.isEmpty() | !word.isBlank() | !word.equals(" ")) {
                                nbChars += word.length();
                            } 
                            if (word.isEmpty() | word.isBlank() | word.equals(" ")) {
                                errCount++;
                            }
                        }
                        nbWords += Words.length - errCount;
                        nbLines++;
                    } 
                    for (char c : textInput.toCharArray()) {
                        if (c == ' ') nbSpaces++;
                    }
                    // pdf specific correction
                    nbChars -= nbLines;
                } else { //for text files
                    BufferedReader reader = null;
                    FileInputStream fileStream = new FileInputStream(inputFile);
                    InputStreamReader input = new InputStreamReader(fileStream);
                    reader = new BufferedReader(input);
                    String data;
                    while ((data = reader.readLine()) != null) {
                        textInput += data;
                        nbChars += data.length();
                        String[] Words = data.split(" ");
                        int errCount = 0;
                        for (String word : Words){ 
                            if (word.isEmpty() | word.isBlank() | word.equals(" ")) {
                                errCount++;
                            }
                        }
                        nbWords += Words.length - errCount;
                        nbLines++;
                    }
                    for (char c : textInput.toCharArray()) {
                        if (c == ' ') {
                             nbSpaces++;
                        }
                    }
                    nbChars -= nbSpaces;
                    textInput += "\n \t(Content of file '" + inputFile.getName() + "')"; 
                    reader.close();
                }
                return printResult(fileInputOption, directInputOption, exportResultOption, nbWords, nbSpaces, nbLines, nbChars, textInput, inputFile);
                
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }
    
    /**
     * Method for extracting text from a word, excel or other office open xml files.
     * Input is our given inputFile.
     * Output is a String containing the corresponding text content.
     */
    public String getPlainTextFromFile(File file){
        InputStream inputstream;
        try {
            inputstream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } 
        // read the file 
        XWPFDocument adoc;
        try {
            adoc = new XWPFDocument(inputstream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        // and place it in a xwpf format
        XWPFWordExtractor extractor = new XWPFWordExtractor(adoc);
        String aString = extractor.getText(); 
        try {
            extractor.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }          
        return aString;
    }

    /*
     method for printing the result into the console or a given file.
     it yields a valid result if the return value is 0, 
     otherwise it returns -1 which will then cause an error message
    */
    public int printResult(boolean fileInputOption, boolean directInputOption, boolean exportResultOption, int nbWords, int nbSpaces, int nbLines, int nbChars, String textInput, File inputFile){
        String outputType;
        if(directInputOption) outputType = "Your input";
        else if(fileInputOption & !directInputOption) outputType = "The file '" + inputFile.getName() + "'";
        else return -1;
        
        if(!exportResultOption){
            if(nbWordsOption) System.out.println(outputType + " has " + nbWords + " word(s). ");
            if(nbLinesOption) System.out.println(outputType + " has " + nbLines + " line(s). ");
            if(!nbLinesOption & !nbWordsOption) System.out.println(outputType + " has " + nbChars + " human-readable character(s) as well as " + nbSpaces + " space(s).");
            return 0;    
        } else {
            File outputFile = new File(exportPath);
            try {
                FileWriter myWriter = new FileWriter(outputFile);
                myWriter.write("INPUT: \n" + "\t" + textInput + "\n");
                myWriter.write("RESULT: \n");
                if(nbWordsOption) myWriter.write("\t" + outputType + " has " + nbWords + " word(s). \n");
                if(nbLinesOption) myWriter.write("\t" + outputType + " has " + nbLines + " line(s). \n");
                if(!nbLinesOption & !nbWordsOption) myWriter.write("\t" + outputType + " has " + nbChars + " human-readable character(s) as well as " + nbSpaces + " space(s). \n");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
                return 0;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return -1;
            }
        }
    }

    // getter methods
    public int getNbChars(){
        return nbChars;
    }

    public int getNbSpaces(){
        return nbSpaces;
    }

    public int getNbWords(){
        return nbWords;
    }

    public int getNbLines(){
        return nbLines;
    }

    // main method
    public static void main(String[] args) { 
        int exitCode = new CommandLine(new carbyeStackCLI()).execute(args);
        System.exit(exitCode);
    }
}