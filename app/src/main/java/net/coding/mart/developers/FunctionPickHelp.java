package net.coding.mart.developers;

import net.coding.mart.json.developer.Quotation;

import java.util.List;

/**
 * Created by chenchao on 16/7/1.
 */
public class FunctionPickHelp {

    public static void addPlatform(List<Quotation> list, Quotation platform) {
        for (Quotation item : list) {
            if (platform == list) {
                return;
            }
        }

        list.add(platform);


    }

}
