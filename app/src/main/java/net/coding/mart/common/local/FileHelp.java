package net.coding.mart.common.local;

import net.coding.mart.R;

import java.io.Serializable;

/**
 * Created by chenchao on 2016/12/7.
 * 帮助管理本地文件
 */
public class FileHelp implements Serializable {

    private static final long serialVersionUID = 6580808605439769802L;
    private static String DIV = "|#|";
    private static String DIV_NO_REG = "\\|\\#\\|";
    public String name = "";
    public int rewardId;
    public String rewardName = "";
    public String urlName = "";
    public String suffix = "";
    public boolean checked;

    public FileHelp() {
    }

    public FileHelp(String localFileName) {
        String[] s = localFileName.split(DIV_NO_REG);
        if (s.length == 4) {
            rewardId = Integer.valueOf(s[0]);
            rewardName = s[1];
            urlName = s[2];

            int pointIndex = s[3].lastIndexOf(".");
            if (pointIndex != -1) {
                name = s[3].substring(0, pointIndex);
                suffix = s[3].substring(pointIndex + 1);
            } else {
                name = s[3];
                suffix = "";
            }
        }
    }

    public FileHelp(int rewardId, String rewardName, String name, String url) {
        this.rewardId = rewardId;
        this.rewardName = rewardName;
        this.name = name;

        int lastDivide = url.lastIndexOf("/");
        if (lastDivide != -1) {
            String urlNameContainerSuffix = url.substring(lastDivide + 1);
            int posintIndex = urlNameContainerSuffix.lastIndexOf(".");
            if (posintIndex != -1) {
                urlName = urlNameContainerSuffix.substring(0, posintIndex);
                suffix = urlNameContainerSuffix.substring(posintIndex + 1);
            }
        }
    }

    public boolean isEmpty() {
        return rewardId == 0;
    }

    public String getLocalFileName() {
        return String.format("%s%s%s%s%s%s%s.%s", rewardId, DIV, rewardName, DIV, urlName, DIV, name, suffix);
    }

    public int getTypeImage() {
        return R.mipmap.ic_file_docx;
    }

    public String getNameContainSuffix() {
        return name + "." + suffix;
    }

    public int getCheckImage() {
        return checked ? R.mipmap.local_file_check : R.mipmap.local_file_check_not;
    }
}
