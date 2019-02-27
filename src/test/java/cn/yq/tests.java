package cn.yq;

public class tests {
    public static void main(String[] args) {
        String ss = "\u4f17\u5b89\u79d1\u6280";
        System.out.println(ss+"--");
        System.out.println(toUnicode("数据未录入"));
        System.out.println("{\"{pre} claim\": [{\"db_middleware\": \"tddl\", \"is_multi_db\": \"Y\", \"db_id\": 2, \"db_company\": \"ä¼—å®‰ä¿\\u009Dé™©\", \"db_type\": \"MySQL\", \"db_name\": \"claim\", \"hosts\": \"{\\\"claim_00\\\": \\\"rdsfbi6rqfbi6rq\\\",\\\"claim_01\\\": \\\"rdsfbi6rqfbi6rq\\\"}\", \"db_cnt\": 2}]}");
    }
    public static String toUnicode(String s)
    {
        String as[] = new String[s.length()];
        String s1 = "";
        for (int i = 0; i < s.length(); i++)
        {
            as[i] = Integer.toHexString(s.charAt(i) & 0xffff);
            s1 = s1 + as[i]+"\t";
        }
        return s1;
    }
}
