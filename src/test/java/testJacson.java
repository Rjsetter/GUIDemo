import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Iterator;

public class testJacson {
    //解析json字符串进行反序列化
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // Jackson提供一个树节点被称为"JsonNode",ObjectMapper提供方法来读json作为树的JsonNode根节点
        JsonNode node = mapper.readTree(new File("D:\\GUIDemo\\src\\test\\java\\country2.json"));
        // 看看根节点的类型
        System.out.println("node JsonNodeType:" + node.getNodeType());
        // 是不是一个容器
        System.out.println("node is container Node ? " + node.isContainerNode());
        // 得到所有node节点的子节点名称
        System.out.println("---------得到所有node节点的子节点名称-------------------------");
        Iterator< String > fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            System.out.print(fieldName + " ");
        }
        System.out.println("\n-----------------------------------------------------");
        // as.Text的作用是有值返回值，无值返回空字符串
        JsonNode country_id = node.get("country_id");
        System.out.println("country_id:" + country_id.asText() + " JsonNodeType:" + country_id.getNodeType());

        JsonNode birthDate = node.get("birthDate");
        System.out.println("birthDate:" + birthDate.asText() + " JsonNodeType:" + birthDate.getNodeType());

        JsonNode nation = node.get("nation");
        System.out.println("nation:" + nation + " JsonNodeType:" + nation.getNodeType());

        JsonNode lakes = node.get("lakes");
        System.out.println("lakes:" + lakes + " JsonNodeType:" + lakes.getNodeType());

        JsonNode provinces = node.get("provinces");
        System.out.println("provinces JsonNodeType:" + provinces.getNodeType());

        boolean flag = true;
        for (JsonNode provinceElements: provinces) {
            //为了避免provinceElements多次打印，用flag控制打印，能体现provinceElements的JsonNodeType就可以了
            if (flag) {
                System.out.println("provinceElements JsonNodeType:" + provinceElements.getNodeType());
                System.out.println("provinceElements is container node? " + provinceElements.isContainerNode());
                flag = false;
            }
            Iterator < String > provinceElementFields = provinceElements.fieldNames();
            while (provinceElementFields.hasNext()) {
                String fieldName = (String) provinceElementFields.next();
                String province;
                if ("population".equals(fieldName)) {
                    province = fieldName + ":" + provinceElements.get(fieldName).asInt();
                } else {
                    province = fieldName + ":" + provinceElements.get(fieldName).asText();
                }
                System.out.println(province);
            }
        }
    }

}
