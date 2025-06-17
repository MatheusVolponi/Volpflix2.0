package br.com.volpflix.service;


import br.com.volpflix.model.Series;
import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;

public class IaConsult {

    private static final Translator translator = new Translator(System.getenv("DEEPL_APIKEY"));

    public static String getTranslate(String text) {
        try {
            String originalText = text;
            TextResult result = translator.translateText(originalText,null,"pt-BR");
            return result.getText();
        } catch (Exception e) {
            System.out.println("Erro na tradução: " + e.getMessage());
            return text;
        }
    }
}
