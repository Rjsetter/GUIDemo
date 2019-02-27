package cn.yq;

import java.util.List;

public class testSpit {
    public static void main(String []args){
        String jiraNum = "CDSSX787,SVCXVX90,SDFSFS90,SDFSF00";
        String []s = jiraNum.split(",");
        for(int i=0;i<s.length;i++){
            System.out.println(s[i]);
        }
    }
}
