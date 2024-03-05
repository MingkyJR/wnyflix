package com.wny.wnyflix.user.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
public class EsTestController {

//    @GetMapping("/post")
//    public String test(Model model) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
//
//        EsClient esClient = new EsClient();
//        ElasticsearchClient client = esClient.client();
//
//
//        Product product = new Product("bk-1", "City bike", 123.0);
//
//        IndexResponse response = client.index(i -> i
//                .index("products")
//                .id(product.getSku())
//                .document(product)
//        );
//        model.addAttribute("response", response.toString());
//        log.info("response : " + response.toString());
//        return "home";
//    }

//    @GetMapping("/get")
//    public String get(Model model) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
//        EsClient esClient = new EsClient();
//        ElasticsearchClient client = esClient.client();
//
//        GetResponse<Product> response = client.get(g -> g
//                        .index("products")
//                        .id("bk-1"),
//                Product.class
//        );
//
//        if (response.found()) {
//            Product product = response.source();
//            log.info("Product name " + product.getName());
//            model.addAttribute("product", product);
//        } else {
//            log.info ("Product not found");
//        }
//
//        return "home";
//    }


}
