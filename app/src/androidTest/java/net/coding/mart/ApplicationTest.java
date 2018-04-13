package net.coding.mart;

import android.app.Application;
import android.test.ApplicationTestCase;

import net.coding.mart.common.local.FileHelp;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);

//        assertEquals(PhoneCountry.createByCountryCode("375", getContext()).country, "Belarus");
//        assertEquals(PhoneCountry.createByCountryCode("+375", getContext()).country, "Belarus");
//        assertEquals(PhoneCountry.createByCountryCode("+726", getContext()).country, "China");

        String url =  "http://coding-net-public-file.qiniudn.com/e2ad9e73-9960-4966-8c93-f508fe314de2.docx";
        FileHelp help = new FileHelp(1, "weixin", "doc1", url);
        assertEquals(help.getNameContainSuffix(), "doc1.docx");
        assertEquals(help.urlName, "e2ad9e73-9960-4966-8c93-f508fe314de2");
    }
}