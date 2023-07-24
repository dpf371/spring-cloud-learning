package com.example.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class English {
    public static final String value = "ajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm," +
            "fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuisafy" +
            "iuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufsadf" +
            "hgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb," +
            "mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjsuay" +
            "fusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndjvyu" +
            "xzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqkjrn" +
            "hwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfjsks" +
            "lfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyq" +
            "wiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfhgqi" +
            "ourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfsakf" +
            "kjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwui" +
            "rfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksdhfk" +
            "sdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfkjj" +
            "skfjlksjflkasjlfajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm" +
            ",fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuisaf" +
            "yiuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufsad" +
            "fhgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb" +
            ",mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjsua" +
            "yfusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndjvy" +
            "uxzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqkjr" +
            "nhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfjsk" +
            "slfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqty" +
            "qwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfhgq" +
            "iourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfsak" +
            "fkjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwu" +
            "irfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksdhf" +
            "ksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfkj" +
            "jskfjlksjflkasjlfajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayunn" +
            "m,fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuisa" +
            "fyiuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufsa" +
            "dfhgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xnz" +
            "b,mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjsu" +
            "ayfusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndjv" +
            "yuxzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqkj" +
            "rnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfjs" +
            "kslfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqt" +
            "yqwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfhg" +
            "qiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfsa" +
            "kfkjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqw" +
            "uirfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksdh" +
            "fksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfk" +
            "jjskfjlksjflkasjlfajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayun" +
            "nm,fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuis" +
            "afyiuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufs" +
            "adfhgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xn" +
            "zb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjs" +
            "uayfusijwkerhuiyrwqbm,vnxvhjaskjfhgqiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndj" +
            "vyuxzvkjrhqiuwysdafskjngjksufiasfsakfkjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqk" +
            "jrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhqwuirfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfj" +
            "skslfjklsdjljflksjfklsahgklshgjksdhfksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruq" +
            "tyqwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlfajkdsfuiwqrwqnjsuayfusijwkerhuiyrwqbm,vnxvhjaskjfh" +
            "gqiourwqhkjrvcxbnmzxbfuiwyrhggoayunnm,fsvxzyuifsankwerhuidfysafbgmndjvyuxzvkjrhqiuwysdafskjngjksufiasfs" +
            "akfkjsgpouoishfkjsabgkjghjksahdfuisafyiuwrhekjqwgbrjhhfasifyasiufhwqkjrnhwkjnskjfhuifhsfkjsfjkhwjkeqrhq" +
            "wuirfyiufsufiewhrjkwqrhwuifysadiufsadfhgkjfklsjjjhgsaiuiugyrqhlkakdfjskslfjklsdjljflksjfklsahgklshgjksd" +
            "hfksdfklsjlkfjsghksja,mxcv,mxnbm,xnzb,mzxnvm,xnv,mxz,.vm,xncbquwoiruqtyqwiuytwqiurywruoiwuruqytweghhshfkjjskfjlksjflkasjlf";

    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("gzip", null);
        hashMap.put("deflate", null);
        System.out.println(hashMap.size());
        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.hashCode());
            System.out.println(entry);
        }
    }
}
