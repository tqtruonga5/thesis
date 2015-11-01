package vn.edu.hcmut.emrre.core.preprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jvntextpro.JVnTextPro;

public class ProcessVNText implements ProcessText {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessVNText.class);
    private static ProcessVNText processVNText;
    private JVnTextPro jVnTextPro;

    public static ProcessVNText getInstance() {
        if (processVNText == null) {
            processVNText = new ProcessVNText();
        }
        return processVNText;
    }

    private ProcessVNText() {
        initJVNTextPro();
    }

    @Override
    public String wordSegment(String text) {
        return jVnTextPro.wordSegment(text);
    }

    @Override
    public String[] wordsSegment(String text) {
        return null;
    }

    @Override
    public String posTag(String text) {
        String result = "";
        try {
            result = jVnTextPro.posTagging(text).split("/")[1];
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public String[] posTags(String text) {

        return jVnTextPro.posTagging(text).split(" ");
    }

    public void initJVNTextPro() {
        String modelsPath = "D:\\Qtruong\\GIAO TRINH\\Luan Van Tot Nghiep\\vn tokenizer tool\\JVnTextPro-v.2.0-all\\JVnTextPro-v.2.0\\models";
        jVnTextPro = new JVnTextPro();
        jVnTextPro.initSenSegmenter(modelsPath + "\\jvnsensegmenter");
        jVnTextPro.initSenTokenization();
        jVnTextPro.initPosTagger(modelsPath + "\\jvnpostag\\maxent");
        jVnTextPro.initSegmenter(modelsPath + "\\jvnsegmenter");
        // String str =
        // "Cháu ho , khò_khè đã 10 ngày_nay , đã được khám và điều_trị viêm mũi họng , phế_quản với Zinnat 7 ngày . Hiện_tại còn ho , "
        // + "khò_khè - > KHám Tiền Sử Bệnh Viêm phế_quản ";
    }

    public static void main(String[] args) {
        ProcessText textProcessor = ProcessVNText.getInstance();
        String text = "Cháu ho , khò_khè đã 10 ngày_nay , đã được khám và điều_trị viêm mũi họng , phế_quản với Zinnat 7";
        
        System.out.println(textProcessor.posTag("vãi"));
        String[] result = textProcessor.posTags(text);
        for (String string : result) {
            System.out.println(string);
        }

        System.out.println(textProcessor.wordSegment("Bệnh nhân đã thức dậy vãi thế"));
        result = textProcessor.posTags(textProcessor.wordSegment("Bệnh nhân đã thức dậy vãi thế"));
        for (String string : result) {
            System.out.println(string);
        }
    }

}
