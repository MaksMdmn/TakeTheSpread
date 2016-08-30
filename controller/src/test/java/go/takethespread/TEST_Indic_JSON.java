package go.takethespread;

import com.fasterxml.jackson.core.JsonProcessingException;
import go.takethespread.util.ParseJsonUtil;

public class TEST_Indic_JSON {
    public static void main(String[] args) {
        try {
            System.out.println(ParseJsonUtil.indicatorsToJson());
            System.out.println(ParseJsonUtil.priceDataToJson(false));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
