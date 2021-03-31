package TextAnalysis;

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
import java.io.Reader;
import java.util.concurrent.Callable;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDefaultParagraphStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.tika.Tika;

// class definition of the Text Analysis Command Line Interface
@Command(name = "TextAnalysis", version = "1.0", mixinStandardHelpOptions = true)
public class TextAnalysis implements Callable<Integer> {
    private int nbChars = 0;
    private int nbSpaces = 0;
    private int nbWords = 0;
    private int nbLines = 0;
    private String textInput = "";
    private boolean fileInputOption;
    private boolean exportResultOption;

    // defintion of all possible options of the CLI
    @Option(names = { "-f", "--fileInput" }, paramLabel = "<Input-file>", description = "Input via a TEXT or PDF file.") 
    private File inputFile;

    @Option(names = "--nbLines", description = "Output number of lines instead of number of human-readable characters.")
    private boolean nbLinesOption;

    @Option(names = "--nbWords", description = "Output number of words instead of number of human-readable charcaters.")
    private boolean nbWordsOption;

    @Option(names = "--exportResult", paramLabel = "<Output-path>", description = "Write result into a given text file.")
    private String exportPath;

    @Option(names = {"-t", "--typeInput"}, paramLabel = "<text>", description = "Type input directly into the console.")
    private boolean directInputOption;

    // call method modification 
    @Override
    public Integer call() throws Exception {

        if(inputFile != null) fileInputOption = true;
        else fileInputOption = false;

        if(this.exportPath != null) exportResultOption = true;
        else exportResultOption = false;

        // read from words, stdin or from file
        if (directInputOption) {
            System.out.println("In the following, please insert your text input whose length ought to be calculated: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
            textInput = reader.readLine(); 
            doCalculationDirectInput();
            reader.close();
            return printResult(fileInputOption, directInputOption, exportResultOption, nbWords, nbSpaces, nbLines, nbChars, textInput, inputFile);
        } else if (fileInputOption) {
            try { 
                // for doc, docx and other office open xml files
                if (FilenameUtils.getExtension(inputFile.getName()).equals("docx") 
                        | FilenameUtils.getExtension(inputFile.getName()).equals("doc")) {
                    System.out.println("DOCX file detected.");
                    InputStream fis = new FileInputStream(this.inputFile);
                    POITextExtractor extractor;
                    // if docx
                    if (this.inputFile.getName().toLowerCase().endsWith(".docx")) {
                        XWPFDocument doc = new XWPFDocument(fis);
                        extractor = new XWPFWordExtractor(doc);
                    } else {
                        // if doc
                        POIFSFileSystem fileSystem = new POIFSFileSystem(fis);
                        extractor = ExtractorFactory.createExtractor(fileSystem);
                    }
                    this.textInput = extractor.getText();
                    // System.out.println(this.textInput);
                    // calculation based on textInput
                    doCalculationWordFile();
                } else if (FilenameUtils.getExtension(this.inputFile.getName()).equals("pdf")) { // for pdf files
                    System.out.println("PDF file detected.");
                    PDDocument document = PDDocument.load(this.inputFile);
                    if (!document.isEncrypted()) {
                        PDFTextStripper stripper = new PDFTextStripper();
                        this.textInput = stripper.getText(document);
                    } else {
                        System.out.println("Error! Cannot read encrypted document.");
                        return -1;
                    }
                    document.close();
                    // calculation based on data
                    doCalculationPDFFile();
                } else { //for text files
                    // initiate stream and reader
                    System.out.println("TXT file detected.");
                    BufferedReader reader = null;
                    FileInputStream fileStream = new FileInputStream(this.inputFile);
                    InputStreamReader input = new InputStreamReader(fileStream);
                    reader = new BufferedReader(input);
                    // calculation based on data
                    String data;
                    while ((data = reader.readLine()) != null) {
                        this.textInput += data;
                        this.nbChars += data.length();
                        String[] Words = data.split(" ");
                        int errCount = 0;
                        for (String word : Words){ 
                            if (word.isEmpty() | word.isBlank() | word.equals(" ")) {
                                errCount++;
                            }
                        }
                        this.nbWords += Words.length - errCount;
                        this.nbLines++;
                    }
                    countSpaces();
                    this.nbChars -= this.nbSpaces;
                    this.textInput += "\n \t(Content of file '" + inputFile.getName() + "')"; 
                    reader.close();
                }
                return printResult(fileInputOption, directInputOption, exportResultOption, nbWords, nbSpaces, nbLines, nbChars, textInput, inputFile);
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public void doCalculationDirectInput(){
        // calculation based on textInput 
        this.nbChars = textInput.length();
        countSpaces();
        this.nbChars -= this.nbSpaces;
        String[] Words = textInput.split(" ");
        this.nbWords = Words.length;
        String[] Lines = textInput.split("\n");
        this.nbLines = Lines.length;        
    }

    public void doCalculationTXTFile() throws IOException{
        // initiate stream and reader
        BufferedReader reader = null;
        FileInputStream fileStream = new FileInputStream(this.inputFile);
        InputStreamReader input = new InputStreamReader(fileStream);
        reader = new BufferedReader(input);
        // calculation based on data
        String data;
        while ((data = reader.readLine()) != null) {
            this.textInput += data;
            this.nbChars += data.length();
            String[] Words = data.split(" ");
            int errCount = 0;
            for (String word : Words){ 
                if (word.isEmpty() | word.isBlank() | word.equals(" ")) {
                    errCount++;
                }
            }
            this.nbWords += Words.length - errCount;
            this.nbLines++;
        }
        countSpaces();
        this.nbChars -= this.nbSpaces;
        this.textInput += "\n \t(Content of file '" + inputFile.getName() + "')"; 
        reader.close();
    }

    public void doCalculationWordFile(){
        String[] lines = this.textInput.split("\n");
        for (String line : lines){
            this.textInput += line;
            //nbChars += line.length();
            String[] Words = line.split(" ");
            this.nbWords += Words.length;
            this.nbLines++;
            for (String word : Words){
                this.nbChars += word.length();
            }
        } 
        countSpaces();
    }

    public void doCalculationPDFFile(){
        String[] lines = textInput.split("\n");
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
        countSpaces();
        // pdf specific correction
        nbChars -= nbLines;
    }

    public void countSpaces() {
        for (char c : textInput.toCharArray()) {
            if (c == ' ') { // Optional: additional check for tabs ('\t') or new lines ('\n')  
                 this.nbSpaces++;
            }
        }
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
    public int getNbChars(){return nbChars; }
    public int getNbSpaces(){return nbSpaces; }
    public int getNbWords(){return nbWords; }
    public int getNbLines(){return nbLines; }

    // setter methods
    public void setNbChars(int nbChars) {this.nbChars = nbChars; }
    public void setNbSpaces(int nbSpaces){ this.nbSpaces = nbSpaces; }
    public void setNbWords(int nbWords){ this.nbWords = nbWords; }
    public void setNbLines(int nbLines){ this.nbLines = nbLines; }

    // main method
    public static void main(String[] args) { 
        int exitCode = new CommandLine(new TextAnalysis()).execute(args);
        System.exit(exitCode);
    }
}
